@file:OptIn(ExperimentalCoroutinesApi::class)

package org.araqnid.kotlin.setawsssocredentials

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.araqnid.kotlin.assertthat.assertThat
import org.araqnid.kotlin.assertthat.equalTo
import kotlin.test.Test

class CommandTest {
    @Test
    fun run_command() = runTest {
        val lines = flowOf("\nabc\ndef\n", "gh", "i\n").extractLines().toList()
        assertThat(
            lines, equalTo(
                listOf(
                    "",
                    "abc",
                    "def",
                    "ghi"
                )
            )
        )
    }
}
