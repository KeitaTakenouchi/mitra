package mitra.entity

class Table(vararg val records: Record) {
    fun dumpTable(): String {
        val ret = StringBuilder()

        for (r in records) {
            ret.append(r).append("\n")
        }

        return ret.toString()
    }
}

class Record(vararg val item: HDT) {
    override fun toString(): String {
        return "(" + item.joinToString(", ") + ")"
    }
}
