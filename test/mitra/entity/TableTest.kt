package mitra.entity

import org.junit.jupiter.api.Test

internal class TableTest {

    @Test
    fun table01() {
        val table = Table(
                Record(HDLeaf("tag", "d1"), HDLeaf("tag", "d2")),
                Record(HDLeaf("tag", "d1"), HDLeaf("tag", "d2")),
                Record(HDLeaf("tag", "d1"), HDLeaf("tag", "d2"))
        )
        println(table.dumpTable())
    }

}