package com.r42914lg.arkados.yatodo.ui

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.r42914lg.arkados.yatodo.FileReader
import com.r42914lg.arkados.yatodo.R
import com.r42914lg.arkados.yatodo.network.CertificateWrapper
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import okhttp3.tls.HandshakeCertificates
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private val mockWebServer = MockWebServer()

    private val serverCertificates = HandshakeCertificates.Builder()
        .heldCertificate(CertificateWrapper.getLocalHostCert())
        .build()

    @Before
    fun setup() {
        mockWebServer.useHttps(serverCertificates.sslSocketFactory(), false)
        mockWebServer.start(8443)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testNumOfItemsInInRecycler_whenListFilteredOrNewItemAdded() {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(200)
                    .setBody(FileReader.readStringFromFile("success_response.json"))
            }
        }

        val scenario = launchActivity<MainActivity>()

        onView(withId(R.id.synchronize)).perform(click())
        onView(withId(R.id.recycler)).check(RecyclerViewItemCountAssertion(6));

        onView(withId(R.id.show_completed)).perform(click())
        onView(withId(R.id.recycler)).check(RecyclerViewItemCountAssertion(3));

        onView(withId(R.id.fab)).perform(click())
        onView(withId(R.id.todo_input_text))
            .perform(typeText("Testing... here is new todo item from espresso"))

        onView(withId(R.id.button_save)).perform(click())
        onView(withId(R.id.recycler)).check(RecyclerViewItemCountAssertion(4));

        onView(withId(R.id.synchronize)).perform(click())
        onView(withId(R.id.show_completed)).perform(click())
        onView(withId(R.id.recycler)).check(RecyclerViewItemCountAssertion(6));

        scenario.close()
    }
}