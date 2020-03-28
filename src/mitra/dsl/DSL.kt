package mitra.dsl

import mitra.entity.*


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
            is HDNode -> {
                val cs = n.children(tag)
                if (pos > cs.lastIndex) {
                    return null
                }
                return cs[pos]
            }
        }
    }

}
