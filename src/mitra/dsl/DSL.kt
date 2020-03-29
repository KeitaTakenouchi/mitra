package mitra.dsl

import mitra.entity.*

sealed class ColumnExtractor {
    abstract fun eval(env: Env): List<HDT>
}

object TargetNodes : ColumnExtractor() {
    override fun eval(env: Env): List<HDT> {
        return listOf(env.rootNode!!)
    }
}

class ChildrenOf(val extractor: ColumnExtractor, val tag: Tag) : ColumnExtractor() {

    override fun eval(env: Env): List<HDT> {
        val nodes = extractor.eval(env)
        return nodes
                .filterIsInstance<HDNode>()
                .flatMap { it.children(tag) }
    }

}

class PChildrenOf(val extractor: ColumnExtractor, val tag: Tag, val pos: Int) : ColumnExtractor() {
    override fun eval(env: Env): List<HDT> {
        val nodes = extractor.eval(env)
        return nodes
                .filterIsInstance<HDNode>()
                .mapNotNull { it.pchild(tag, pos) }
    }
}

class DescendantsOf(val extractor: ColumnExtractor, val tag: Tag) : ColumnExtractor() {
    override fun eval(env: Env): List<HDT> {
        val nodes = extractor.eval(env)

        val ret = mutableListOf<HDT>()
        for (n in nodes) {
            n.traverse {
                // ignore itself
                if (it == n) return@traverse
                if (it.tag == tag) {
                    ret.add(it)
                }
            }
        }
        return ret
    }
}

sealed class Predicate {
    abstract fun eval(env: Env): Boolean
}

class ComparisonConst(val left: NodePointer, val binOp: BinOp, val right: Data) : Predicate() {
    override fun eval(env: Env): Boolean {
        val node = left.eval(env) ?: return false

        val left = when (node) {
            is HDNode -> return false
            is HDLeaf -> node.data
        }

        return binOp.eval(left, right)
    }
}

class ComparisonNodes(val left: NodePointer, val binOp: BinOp, val right: NodePointer) : Predicate() {
    override fun eval(env: Env): Boolean {
        val nodeL = left.eval(env) ?: return false
        val nodeR = right.eval(env) ?: return false

        return when (nodeL) {
            is HDLeaf -> {
                when (nodeR) {
                    is HDLeaf -> (binOp.eval(nodeL.data, nodeR.data))
                    is HDNode -> false // leaf <-> node
                }
            }
            is HDNode -> {
                when (nodeR) {
                    is HDLeaf -> false // node <-> leaf
                    is HDNode -> (nodeL === nodeR)
                }
            }
        }
    }
}

class AND(val left: Predicate, val right: Predicate) : Predicate() {
    override fun eval(env: Env): Boolean = (left.eval(env) and right.eval(env))
}

class OR(val left: Predicate, val right: Predicate) : Predicate() {
    override fun eval(env: Env): Boolean = (left.eval(env) or right.eval(env))
}

class NOT(val pred: Predicate) : Predicate() {
    override fun eval(env: Env): Boolean = (!pred.eval(env))
}

class NodePointer(val extractor: NodeExtractor, val index: Int) {
    fun eval(env: Env): HDT? {
        env.targetNode = env.targetRecord!!.item[index]
        val node = extractor.eval(env)
        env.targetNode = null
        return node
    }
}

enum class BinOp {
    EQ {
        override fun eval(left: Data, right: Data): Boolean = (left == right)
    },
    NEQ {
        override fun eval(left: Data, right: Data): Boolean = (left != right)
    },
    LT {
        override fun eval(left: Data, right: Data): Boolean = (left < right)
    },
    LE {
        override fun eval(left: Data, right: Data): Boolean = (left <= right)
    },
    GT {
        override fun eval(left: Data, right: Data): Boolean = (left > right)
    },
    GE {
        override fun eval(left: Data, right: Data): Boolean = (left >= right)
    };

    abstract fun eval(left: Data, right: Data): Boolean
}

sealed class NodeExtractor {
    abstract fun eval(env: Env): HDT?
}

object TargetNode : NodeExtractor() {

    override fun eval(env: Env): HDT? {
        val n = env.targetNode
        require(n != null)
        return n
    }

}

class ParentOf(val node: NodeExtractor) : NodeExtractor() {

    override fun eval(env: Env): HDT? {
        return node.eval(env)?.parent
    }

}

class ChildOf(val node: NodeExtractor, val tag: Tag, val pos: Int) : NodeExtractor() {

    override fun eval(env: Env): HDT? {
        return when (val n = node.eval(env)) {
            null -> null
            is HDLeaf -> null
            is HDNode -> n.pchild(tag, pos)
        }
    }

}
