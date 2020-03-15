package mitra.entity

import org.junit.jupiter.api.Test

internal class HDTreeTest {

    @Test
    fun dumpTree01() {
        val tree = HDNode("User",
                HDNode("Person",
                        HDTLeaf("id", "a"),
                        HDTLeaf("name", "Alice"),
                        HDNode("Friendship",
                                HDNode("Friend",
                                        HDTLeaf("fid", "2"),
                                        HDTLeaf("years", "3")
                                ),
                                HDNode("Friend",
                                        HDTLeaf("fid", "4"),
                                        HDTLeaf("years", "4")
                                )
                        )
                )
        )
        println(tree.dumpTree())
    }
}