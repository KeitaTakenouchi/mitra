package mitra.entity

/**
 * Hierarchical Data Tree
 */
sealed class HDT(val tag: String) {
    abstract fun dumpTree(): String
}

open class HDNode(tag: Tag, vararg val children: HDT) : HDT(tag) {
    override fun dumpTree(): String {
        val ret = StringBuilder()
        ret.append("<$tag>\n")

        for (c in children.dropLast(1)) {
            ret.append("├──" + c.dumpTree().replace("\n", "\n│  ") + "\n")
        }
        
        run {
            val c = children.last()
            ret.append("└──" + c.dumpTree().replace("\n", "\n   "))
        }

        return ret.toString()
    }
}

class HDTLeaf(tag: Tag, val data: Data) : HDT(tag) {
    override fun dumpTree(): String {
        return "<$tag>$data"
    }
}

typealias Tag = String
typealias Data = String
