package mitra.dsl

import mitra.entity.HDLeaf
import mitra.entity.HDNode
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

internal class ProgramTest {

    @Test
    fun testProgram01() {
        val l1 = HDLeaf("tag", "08")
        val l2 = HDLeaf("tag", "12")
        val l3 = HDLeaf("tag", "17")
        val l4 = HDLeaf("tag", "10")

        val n1 = HDNode("node", l1, l2)
        val n2 = HDNode("node", l3, l4)

        val root = HDNode("tag", n1, n2)

        val program = Program(
                Column(DescendantsOf(RootNode, "tag")),
                ComparisonConst(
                        NodePointer(TargetNode, 0)
                        , BinOp.GE
                        , "10"
                )
        )

        val table = program.eval(root)
        assertEquals(3, table.records.size)
        assertSame(l2, table.records[0].item[0])
        assertSame(l3, table.records[1].item[0])
        assertSame(l4, table.records[2].item[0])
    }

    @Test
    fun testProgram02() {
        val l1 = HDLeaf("tag", "08")
        val l2 = HDLeaf("tag", "12")
        val l3 = HDLeaf("tag", "17")
        val l4 = HDLeaf("tag", "10")

        val n1 = HDNode("node", l1, l2)
        val n2 = HDNode("node", l3, l4)

        val root = HDNode("root", n1, n2)

        val program = Program(
                Product(
                        Column(DescendantsOf(RootNode, "node")),
                        Column(DescendantsOf(RootNode, "tag"))
                ),
                AND(
                        ComparisonConst(
                                NodePointer(TargetNode, 1), BinOp.GE, "10"
                        ),
                        ComparisonNodes(
                                NodePointer(TargetNode, 0)
                                , BinOp.EQ
                                , NodePointer(ParentOf(TargetNode), 1)
                        )
                )
        )

        val table = program.eval(root)
        assertEquals(3, table.records.size)
        assertSame(n1, table.records[0].item[0]);assertSame(l2, table.records[0].item[1])
        assertSame(n2, table.records[1].item[0]);assertSame(l3, table.records[1].item[1])
        assertSame(n2, table.records[2].item[0]);assertSame(l4, table.records[2].item[1])
    }

}