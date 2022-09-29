package com.r42914lg.arkados.yatodo.utils

import android.app.Application
import android.content.Context
import androidx.test.annotation.UiThreadTest
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.r42914lg.arkados.yatodo.YaTodoApp
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Before
import org.junit.Test

class PersistAttrManagerTest {
    private lateinit var ctx: Context
    private lateinit var persistAttrManager: PersistAttrManager

    @Before
    fun setUp() {
        ctx = getApplicationContext<YaTodoApp>()
        persistAttrManager = PersistAttrManager(ctx as (Application))
    }

    @Test @UiThreadTest
    fun saveAndReadThemeValueDark() {
        persistAttrManager.saveUiTheme(Theme.DARK)
        assertThat(persistAttrManager.uiTheme.value, equalTo(Theme.DARK))
    }
}