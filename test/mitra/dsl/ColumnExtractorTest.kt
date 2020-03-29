package mitra.dsl

import mitra.entity.HDLeaf
import mitra.entity.HDNode
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

internal class ColumnExtractorTest {

    @Test
    fun testTargetNodes01() {
        val l1 = HDLeaf("tag", "")
        val l2 = HDLeaf("tag", "")
        val l3 = HDLeaf("tag", "")
        val root = HDNode("tag", l1, l2, l3)

        val env = Env()
        env.rootNode = root

        val result = RootNode.eval(env)
        assertEquals(1, result.size)
        assertSame(root, result[0])
    }

    @Test
    fun testChildrenOf01() {
        val l1 = HDLeaf("tag", "")
        val l2 = HDLeaf("tag", "")
        val l3 = HDLeaf("tag", "")
        val root = HDNode("tag", l1, l2, l3)

        val env = Env()
        env.rootNode = root

        val tree = ChildrenOf(RootNode, "tag")
        val result = tree.eval(env)
        assertEquals(3, result.size)
        assertSame(l1, result[0])
        assertSame(l2, result[1])
        assertSame(l3, result[2])
    }

    @Test
    fun testChildrenOf02() {
        val l1 = HDLeaf("tag", "")
        val l2 = HDLeaf("tag", "")
        val l3 = HDLeaf("tag", "")

        val n1 = HDNode("tag", l1, l2)
        val n2 = HDNode("tag", l3)

        val root = HDNode("tag", n1, n2)

        val env = Env()
        env.rootNode = root

        val tree =
                ChildrenOf(
                        ChildrenOf(
                                RootNode
                                , "tag")
                        , "tag")

        val result = tree.eval(env)
        assertEquals(3, result.size)
        assertSame(l1, result[0])
        assertSame(l2, result[1])
        assertSame(l3, result[2])
    }

    @Test
    fun testPChildrenOf01() {
        val l1 = HDLeaf("tag", "")
        val l2 = HDLeaf("tag", "")
        val l3 = HDLeaf("tag", "")

        val root = HDNode("tag", l1, l2, l3)

        val env = Env()
        env.rootNode = root

        val tree = PChildrenOf(
                RootNode
                , "tag"
                , 1
        )

        val result = tree.eval(env)
        assertEquals(1, result.size)
        assertSame(l2, result[0])
    }

    @Test
    fun testPChildrenOf02() {
        val l1 = HDLeaf("tag", "")
        val l2 = HDLeaf("tag", "")
        val l3 = HDLeaf("tag", "")

        val n1 = HDNode("tag", l1, l2)
        val n2 = HDNode("tag", l3)

        val root = HDNode("tag", n1, n2)

        val env = Env()
        env.rootNode = root

        val tree = PChildrenOf(
                ChildrenOf(
                        RootNode
                        , "tag"
                )
                , "tag"
                , 0
        )

        val result = tree.eval(env)
        assertEquals(2, result.size)
        assertSame(l1, result[0])
        assertSame(l3, result[1])
    }

    @Test
    fun testDescendants01() {
        val l1 = HDLeaf("tag", "")
        val l2 = HDLeaf("tag", "")
        val l3 = HDLeaf("tag", "")

        val n1 = HDNode("tag", l1, l2)
        val n2 = HDNode("tag", l3)

        val root = HDNode("tag", n1, n2)

        val env = Env()
        env.rootNode = root

        val tree = DescendantsOf(
                RootNode,
                "tag"
        )

        val result = tree.eval(env)
        assertEquals(5, result.size)
        assertSame(n1, result[0])
        assertSame(l1, result[1])
        assertSame(l2, result[2])
        assertSame(n2, result[3])
        assertSame(l3, result[4])
    }

    @Test
    fun testDescendants02() {
        val l1 = HDLeaf("leaf", "")
        val l2 = HDLeaf("leaf", "")
        val l3 = HDLeaf("leaf", "")

        val n1 = HDNode("non-leaf", l1, l2)
        val n2 = HDNode("non-leaf", l3)

        val root = HDNode("root", n1, n2)

        val env = Env()
        env.rootNode = root

        run {
            val tree = DescendantsOf(
                    RootNode,
                    "non-leaf"
            )

            val result = tree.eval(env)
            assertEquals(2, result.size)
            assertSame(n1, result[0])
            assertSame(n2, result[1])
        }

        run {
            val tree = DescendantsOf(
                    RootNode,
                    "leaf"
            )

            val result = tree.eval(env)
            assertEquals(3, result.size)
            assertSame(l1, result[0])
            assertSame(l2, result[1])
            assertSame(l3, result[2])
        }
    }

}