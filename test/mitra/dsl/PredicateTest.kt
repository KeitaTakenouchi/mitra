package mitra.dsl

import mitra.entity.HDLeaf
import mitra.entity.HDNode
import mitra.entity.Record
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class PredicateTest {

    @Test
    fun testComparisonConst01() {
        val env = Env()
        val l1 = HDLeaf("value", "10")
        env.targetRecord = Record(l1)

        run {
            val pred = ComparisonConst(
                    left = NodePointer(TargetNode, 0)
                    , binOp = BinOp.EQ
                    , right = "10"
            )
            assertEquals(true, pred.eval(env))
        }

    }

    @Test
    fun testComparisonNodes01() {
        val env = Env()
        val l1 = HDLeaf("value", "10")
        val l2 = HDLeaf("value", "10") // has the same value as l1
        val l3 = HDLeaf("value", "10") // has the same value as l1, but a different parent
        HDNode("p", l1, l2)
        HDNode("p", l3)
        env.targetRecord = Record(l1, l2, l3)

        run {
            val pred = ComparisonNodes(
                    left = NodePointer(TargetNode, 0)
                    , binOp = BinOp.EQ
                    , right = NodePointer(TargetNode, 0)
            )
            assertEquals(true, pred.eval(env))
        }

        run {
            val pred = ComparisonNodes(
                    left = NodePointer(TargetNode, 0)
                    , binOp = BinOp.EQ
                    , right = NodePointer(TargetNode, 1)
            )
            assertEquals(true, pred.eval(env))// because the leaves have the same value
        }

        // the common parent
        run {
            val pred = ComparisonNodes(
                    left = NodePointer(ParentOf(TargetNode), 0)
                    , binOp = BinOp.EQ
                    , right = NodePointer(ParentOf(TargetNode), 1)
            )
            assertEquals(true, pred.eval(env))
        }

        // different parents
        run {
            val pred = ComparisonNodes(
                    left = NodePointer(ParentOf(TargetNode), 0)
                    , binOp = BinOp.EQ
                    , right = NodePointer(ParentOf(TargetNode), 2) // l3
            )
            assertEquals(false, pred.eval(env))
        }
    }


}