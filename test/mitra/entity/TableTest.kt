package mitra.entity

import org.junit.jupiter.api.Test

internal class TableTest {

    @Test
    fun table01() {
        val table = Table(
                Record("foo", "1"),
                Record("bar", "8"),
                Record("baz", "9"),
                Record("aaa", "3"),
                Record("bbb", "4"),
                Record("ccc", "1")
        )
        println(table.dumpTable())
    }

}