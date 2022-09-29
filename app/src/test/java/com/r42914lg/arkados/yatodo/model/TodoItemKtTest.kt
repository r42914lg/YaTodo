package com.r42914lg.arkados.yatodo.model

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.junit.Before
import org.junit.Test

internal class TodoItemKtTestFilterAndCountCompleted {

    private lateinit var input: List<TodoItem>

    @Before
    fun prepareTodoItems() {
        input = listOf(
            TodoItem(done = true),
            TodoItem(done = false),
            TodoItem(done = true),
            TodoItem(done = false),
            TodoItem(done = true),
            TodoItem(done = false),
            TodoItem(done = false),
        )
    }

    @Test
    fun checkCountCompletedCalculation() {
        val result = input.countCompleted()
        assertThat(result, equalTo(3))
    }

    @Test
    fun checkListSizeAfterFilterNonCompletedOnly() {
        val result = input.filterNonCompletedOnly()
        assertThat(result, hasSize(4))
    }
}