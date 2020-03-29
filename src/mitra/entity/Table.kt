package mitra.entity

class Table(vararg val records: Record) {

    fun join(table: Table): Table {
        val recs = mutableListOf<Record>()
        for (r1 in records) {
            for (r2 in table.records) {
                recs.add(Record(*r1.item, *r2.item))
            }
        }
        return Table(*recs.toTypedArray())
    }

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
        return "(" + item.joinToString(", ") { """[${it.id}]<${it.tag}>""" } + ")"
    }

}
