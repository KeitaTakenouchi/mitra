package mitra.entity

/**
 * Hierarchical Data Tree
 */
sealed class HDT(val tag: String) {
    var parent: HDNode? = null

    abstract fun dumpTree(): String
}

class HDNode(tag: Tag, vararg val children: HDT) : HDT(tag) {

    init {
        // register the parent of children
        for (c in children) {
            c.parent = this
        }
    }

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
