package mitra.dsl

import mitra.entity.HDLeaf
import mitra.entity.HDNode
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class NodeExtractorTest {

    @Test
    fun testNode01() {
        val l1 = HDLeaf("", "")

        val env = Env()
        env.targetNode = l1
        val node = TargetNode.eval(env)

        assertEquals(env.targetNode, node)
    }

    @Test
    fun testNodeParent01() {
        val l1 = HDLeaf("", "")
        val l2 = HDLeaf("", "")
        val l3 = HDLeaf("", "")
        val p1 = HDNode("", l1, l2, l3)

        val env = Env()
        run {
            env.targetNode = l1
            val node = NodeParent(TargetNode).eval(env)
            assertEquals(p1, node)
        }
        run {
            env.targetNode = l2
            val node = NodeParent(TargetNode).eval(env)
            assertEquals(p1, node)
        }
        run {
            env.targetNode = l3
            val node = NodeParent(TargetNode).eval(env)
            assertEquals(p1, node)
        }
        run {
            env.targetNode = p1
            val node = NodeParent(TargetNode).eval(env)
            assertNull(node)
        }
    }

    @Test
    fun testNodeChild01() {
        val l1 = HDLeaf("TAG", "foo")
        val l2 = HDLeaf("TAG", "foo")
        val l3 = HDLeaf("TAG", "foo")
        val p1 = HDNode("PARENT", l1, l2, l3)

        val env = Env()
        env.targetNode = p1

        run {
            val node = NodeChild(TargetNode, "TAG", 0).eval(env)
            assertEquals(l1, node)
        }
        run {
            val node = NodeChild(TargetNode, "TAG", 1).eval(env)
            assertEquals(l2, node)
        }
        run {
            val node = NodeChild(TargetNode, "TAG", 2).eval(env)
            assertEquals(l3, node)
        }
        run {
            val node = NodeChild(TargetNode, "TAG", 10).eval(env)
            assertNull(node)
        }
    }

    @Test
    fun testNodeChild02() {
        val l1 = HDLeaf("TAG", "foo")
        val l2 = HDLeaf("_", "foo")
        val l3 = HDLeaf("TAG", "foo")
        val p1 = HDNode("PARENT", l1, l2, l3)

        val env = Env()
        env.targetNode = p1

        run {
            val node = NodeChild(TargetNode, "TAG", 0).eval(env)
            assertEquals(l1, node)
        }
        run {
            val node = NodeChild(TargetNode, "TAG", 1).eval(env)
            assertEquals(l3, node)
        }
        run {
            val node = NodeChild(TargetNode, "TAG", 2).eval(env)
            assertNull(node)
        }
    }

}