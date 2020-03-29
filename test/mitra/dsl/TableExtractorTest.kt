package mitra.dsl

import mitra.entity.HDLeaf
import mitra.entity.HDNode
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

internal class TableExtractorTest {

    @Test
    fun testColumn01() {
        val l1 = HDLeaf("tag", "")
        val l2 = HDLeaf("tag", "")
        val l3 = HDLeaf("tag", "")
        val root = HDNode("tag", l1, l2, l3)

        val env = Env()
        env.rootNode = root

        val column = Column(
                ChildrenOf(TargetNodes, "tag")
        )
        val table = column.eval(env)

        assertEquals(3, table.records.size)
        assertEquals(1, table.records[0].item.size)

        assertSame(l1, table.records[0].item[0])
        assertSame(l2, table.records[1].item[0])
        assertSame(l3, table.records[2].item[0])
    }

    @Test
    fun testProduct01() {
        val l1 = HDLeaf("tag", "")
        val l2 = HDLeaf("tag", "")
        val l3 = HDLeaf("tag", "")

        val n1 = HDNode("node", l1, l2)
        val n2 = HDNode("node", l3)

        val root = HDNode("tag", n1, n2)

        val env = Env()
        env.rootNode = root

        val column = Product(
                Column(DescendantsOf(TargetNodes, "tag")),
                Column(DescendantsOf(TargetNodes, "node"))
        )
        val table = column.eval(env)

        assertEquals(6, table.records.size)
        assertEquals(2, table.records[0].item.size)

        assertSame(l1, table.records[0].item[0]); assertSame(n1, table.records[0].item[1])
        assertSame(l1, table.records[1].item[0]); assertSame(n2, table.records[1].item[1])

        assertSame(l2, table.records[2].item[0]); assertSame(n1, table.records[2].item[1])
        assertSame(l2, table.records[3].item[0]); assertSame(n2, table.records[3].item[1])

        assertSame(l3, table.records[4].item[0]); assertSame(n1, table.records[4].item[1])
        assertSame(l3, table.records[5].item[0]); assertSame(n2, table.records[5].item[1])
    }

}