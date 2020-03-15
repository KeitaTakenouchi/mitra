package mitra.entity

import org.junit.jupiter.api.Test

internal class HDTreeTest {

    @Test
    fun dumpTree01() {
        val tree = HDNode("User",
                HDNode("Person",
                        HDLeaf("id", "a"),
                        HDLeaf("name", "Alice"),
                        HDNode("Friendship",
                                HDNode("Friend",
                                        HDLeaf("fid", "2"),
                                        HDLeaf("years", "3")
                                ),
                                HDNode("Friend",
                                        HDLeaf("fid", "4"),
                                        HDLeaf("years", "4")
                                )
                        )
                )
        )
        println(tree.dumpTree())
    }
}