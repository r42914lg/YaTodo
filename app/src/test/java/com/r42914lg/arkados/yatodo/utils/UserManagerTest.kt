package com.r42914lg.arkados.yatodo.utils

import com.google.firebase.auth.FirebaseUser
import com.r42914lg.arkados.yatodo.ui.MainActivity
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController

@RunWith(RobolectricTestRunner::class)
class UserManagerTest {

    private lateinit var controller: ActivityController<MainActivity>
    private lateinit var activity: MainActivity
    private lateinit var userManager: UserManager

    @Before
    fun setUp() {
        controller = Robolectric.buildActivity(MainActivity::class.java)
        activity = controller.get()
        userManager = UserManager(activity.application)
    }

    @Test
    fun saveAuthTokenAndCheckValuesMatch() {
        val mockFirebaseUser = Mockito.mock(FirebaseUser::class.java)
        Mockito.`when`(mockFirebaseUser.displayName).thenReturn("John Deacon")

        userManager.saveAuthToken( mockFirebaseUser, "token_test_value_here_123456786877397")

        assertThat(userManager.token, equalTo("token_test_value_here_123456786877397"))
        assertThat(userManager.user, sameInstance(mockFirebaseUser))
        assertThat(userManager.userName.value, equalTo("John Deacon"))
    }

    @Test
    fun clearAuthTokenAndCheckValuesNull() {
        userManager.clearAuthToken()

        assertThat(userManager.token, Is(nullValue()))
        assertThat(userManager.user, Is(nullValue()))
        assertThat(userManager.userName.value, Is(nullValue()))
    }
}