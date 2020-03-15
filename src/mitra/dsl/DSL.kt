package mitra.dsl

import mitra.entity.HDLeaf
import mitra.entity.HDNode
import mitra.entity.HDT
import mitra.entity.Tag

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

class NodeParent(val node: NodeExtractor) : NodeExtractor() {

    override fun eval(env: Env): HDT? {
        return node.eval(env)?.parent
    }

}

class NodeChild(val node: NodeExtractor, val tag: Tag, val pos: Int) : NodeExtractor() {

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
