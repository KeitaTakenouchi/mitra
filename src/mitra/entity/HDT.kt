package mitra.entity

/**
 * Hierarchical Data Tree
 */
sealed class HDT(val tag: String) {
    var parent: HDNode? = null
    val id: Int

    init {
        id = IdCounter
        IdCounter++
    }

    companion object {
        var IdCounter = 0
    }

    abstract fun traverse(callback: (HDT) -> Unit)

    abstract fun dumpTree(): String
}

class HDNode(tag: Tag, vararg val children: HDT) : HDT(tag) {

    init {
        // register the parent of children
        for (c in children) {
            c.parent = this
        }
    }

    fun children(tag: Tag): List<HDT> {
        return children.filter { it.tag == tag }.toList()
    }

    fun pchild(tag: Tag, pos: Int): HDT? {
        val cs = children(tag)
        if (pos > cs.lastIndex) {
            return null
        }
        return cs[pos]
    }

    override fun traverse(callback: (HDT) -> Unit) {
        callback(this)
        for (c in children) {
            c.traverse(callback)
        }
    }

    override fun dumpTree(): String {
        val ret = StringBuilder()
        ret.append("[$id]<$tag>\n")

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

class HDLeaf(tag: Tag, val data: Data) : HDT(tag) {

    override fun traverse(callback: (HDT) -> Unit) {
        callback(this)
    }

    override fun dumpTree(): String {
        return "[$id]<$tag>$data"
    }
}

typealias Tag = String
typealias Data = String
