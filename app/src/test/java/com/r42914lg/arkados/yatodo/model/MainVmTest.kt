package com.r42914lg.arkados.yatodo.model

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.r42914lg.arkados.yatodo.repository.IRepo
import com.r42914lg.arkados.yatodo.ui.MainActivity
import com.r42914lg.arkados.yatodo.utils.NetworkTracker
import com.r42914lg.arkados.yatodo.utils.UserManager
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo

import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController

@RunWith(RobolectricTestRunner::class)
class MainVmTest {

    private lateinit var controller: ActivityController<MainActivity>
    private lateinit var app: Application
    private lateinit var testListFull: MutableList<TodoItem>
    private lateinit var mockUserManager: UserManager
    private lateinit var mockNetworkTracker: NetworkTracker

    @Before
    fun setUp() {
        controller = Robolectric.buildActivity(MainActivity::class.java)
        app = controller.get().application

        mockUserManager = Mockito.mock(UserManager::class.java)
        Mockito.`when`(mockUserManager.userName).thenReturn(MutableLiveData<String>("John Deacon"))

        mockNetworkTracker = Mockito.mock(NetworkTracker::class.java)
        Mockito.`when`(mockNetworkTracker.isOnline).thenReturn(MutableLiveData<Boolean>(true))

        testListFull = mutableListOf(
            TodoItem(done = true),
            TodoItem(done = true),
            TodoItem(done = true),
            TodoItem(done = true),
            TodoItem(done = true),
            TodoItem(done = true),
            TodoItem(done = false),
            TodoItem(done = false),
            TodoItem(done = false),
            TodoItem(done = false),
            TodoItem(done = false),
            TodoItem(done = false),
        )
    }

    @Test
    fun handleSyncRequest_ShowCompleted_NetworkTrue_UserNameExist_HTTP_200() = runBlocking {
        // Given
        val mockRepo = Mockito.mock(IRepo::class.java)
        Mockito.`when`(mockRepo.getTodoList()).thenReturn(testListFull)
        Mockito.`when`(mockRepo.syncAll(true)).thenReturn(200)

        val mainVm = MainVm(app, mockRepo, mockUserManager, mockNetworkTracker)
        mainVm.setShowCompleted(true)

        // When
        mainVm.handleSyncRequest(false)

        // Then
        assertThat(mainVm.todoItems.value!!.size, equalTo(12))
        assertThat(mainVm.eventSigninRefreshRequest.value, equalTo(false))
        assertThat(mainVm.syncRequestInProgress.value, equalTo(false))
    }

    @Test
    fun handleSyncRequest_ShowCompletedNot_NetworkTrue_UserNameExist_HTTP_200() = runBlocking {
        // Given
        val mockRepo = Mockito.mock(IRepo::class.java)
        Mockito.`when`(mockRepo.getTodoList()).thenReturn(testListFull)
        Mockito.`when`(mockRepo.syncAll(true)).thenReturn(200)

        val mainVm = MainVm(app, mockRepo, mockUserManager, mockNetworkTracker)
        mainVm.setShowCompleted(false)

        // When
        mainVm.handleSyncRequest(false)

        // Then
        assertThat(mainVm.todoItems.value!!.size, equalTo(6))
        assertThat(mainVm.eventSigninRefreshRequest.value, equalTo(false))
        assertThat(mainVm.syncRequestInProgress.value, equalTo(false))
    }

    @Test
    fun handleSyncRequest_ShowCompletedNot_NetworkTrue_UserNameExist_HTTP_401() = runBlocking {
        // Given
        val mockRepo = Mockito.mock(IRepo::class.java)
        Mockito.`when`(mockRepo.getTodoList()).thenReturn(testListFull)
        Mockito.`when`(mockRepo.syncAll(true)).thenReturn(401)

        val mainVm = MainVm(app, mockRepo, mockUserManager, mockNetworkTracker)
        mainVm.setShowCompleted(false)

        // When
        mainVm.handleSyncRequest(false)

        // Then
        assertThat(mainVm.eventSigninRefreshRequest.value, equalTo(true))
        assertThat(mainVm.syncRequestInProgress.value, equalTo(true))
    }

    @Test
    fun handleSyncRequest_ShowCompletedNot_NetworkTrue_UserNameExist_HTTP_OTHER() = runBlocking {
        // Given
        val mockRepo = Mockito.mock(IRepo::class.java)
        Mockito.`when`(mockRepo.getTodoList()).thenReturn(testListFull)
        Mockito.`when`(mockRepo.syncAll(true)).thenReturn(999)

        val mainVm = MainVm(app, mockRepo, mockUserManager, mockNetworkTracker)
        mainVm.setShowCompleted(false)

        // When
        mainVm.handleSyncRequest(false)

        // Then
        assertThat(mainVm.eventSigninRefreshRequest.value, equalTo(false))
        assertThat(mainVm.syncRequestInProgress.value, equalTo(false))
    }

    @Test
    fun handleLogoutRequest_checkItemListBecomesEmptyAndSignOutEventSet()
    = runBlocking {
        // Given
        val mockRepo = Mockito.mock(IRepo::class.java)
        Mockito.`when`(mockRepo.getTodoList()).thenReturn(mutableListOf<TodoItem>())

        val mainVm = MainVm(app, mockRepo, mockUserManager, mockNetworkTracker)

        // When
        mainVm.handleLoginOrLogoutClick()

        // Then
        assertThat(mainVm.todoItems.value!!.size, equalTo(0))
        assertThat(mainVm.evenLogoutRequest.value, equalTo(true))
    }

    @Test
    fun handleLoginWhenConnected_checkSigninEventSet() = runBlocking {
        // Given
        val mockRepo = Mockito.mock(IRepo::class.java)
        Mockito.`when`(mockRepo.getTodoList()).thenReturn(mutableListOf<TodoItem>())

        val mockUserManagerNotSigned = Mockito.mock(UserManager::class.java)
        Mockito.`when`(mockUserManagerNotSigned.userName).thenReturn(MutableLiveData<String>())

        val mainVm = MainVm(app, mockRepo, mockUserManagerNotSigned, mockNetworkTracker)

        // When
        mainVm.handleLoginOrLogoutClick()

        // Then
        assertThat(mainVm.eventLoginRequest.value, equalTo(true))
    }

    @Test
    fun handleLoginWhenNotConnected_checkToastEvent() = runBlocking {
        // Given
        val mockRepo = Mockito.mock(IRepo::class.java)
        Mockito.`when`(mockRepo.getTodoList()).thenReturn(mutableListOf<TodoItem>())

        val mockUserManagerNotSigned = Mockito.mock(UserManager::class.java)
        Mockito.`when`(mockUserManagerNotSigned.userName).thenReturn(MutableLiveData<String>())

        val mockNetworkTrackerNotConnected = Mockito.mock(NetworkTracker::class.java)
        Mockito.`when`(mockNetworkTrackerNotConnected.isOnline)
            .thenReturn(MutableLiveData<Boolean>(false))

        val mainVm = MainVm(app, mockRepo, mockUserManagerNotSigned, mockNetworkTrackerNotConnected)

        // When
        mainVm.handleLoginOrLogoutClick()

        // Then
        assertThat(mainVm.toastUi.value, equalTo("Offline mode"))
    }
}