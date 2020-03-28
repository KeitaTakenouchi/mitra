package mitra.dsl

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class BinOpTest {

    @Test
    fun testBinOp01() {
        assertEquals(true, BinOp.EQ.eval("A", "A"))
        assertEquals(false, BinOp.EQ.eval("A", "B"))

        assertEquals(true, BinOp.NEQ.eval("A", "B"))

        assertEquals(true, BinOp.GT.eval("5", "1"))
        assertEquals(false, BinOp.GT.eval("5", "5"))
        assertEquals(false, BinOp.GT.eval("1", "5"))

        assertEquals(true, BinOp.GE.eval("5", "5"))

        assertEquals(true, BinOp.LT.eval("1", "5"))

        assertEquals(true, BinOp.LE.eval("5", "5"))
    }

}
