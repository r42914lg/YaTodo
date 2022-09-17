package com.r42914lg.arkados.yatodo.model

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseUser
import com.r42914lg.arkados.yatodo.R
import com.r42914lg.arkados.yatodo.log
import com.r42914lg.arkados.yatodo.utils.SharedPrefManager
import com.r42914lg.arkados.yatodo.repository.IRepo
import com.r42914lg.arkados.yatodo.utils.NetworkTracker
import com.r42914lg.arkados.yatodo.utils.Theme
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class MainVm @AssistedInject constructor(
    private val app: Application,
    private val repo: IRepo,
    private val sharedPrefManager: SharedPrefManager,
    networkTracker: NetworkTracker,
) : AndroidViewModel(app), IApiErrorListener {

    @AssistedFactory
    interface Factory {
        fun create(): MainVm
    }

    private lateinit var autoSynJob: Job

    private var _uiTheme = sharedPrefManager.uiTheme
    val uiTheme: LiveData<Theme>
        get() = _uiTheme

    private var _networkStatus = networkTracker.isOnline
    val networkStatus: LiveData<Boolean>
        get() = _networkStatus

    private var _toastToUi = MutableLiveData<String?>()
    fun toastShown() { _toastToUi.value = null }
    val toastUi: LiveData<String?>
        get() = _toastToUi

    val currentUserName: LiveData<String?>
        get() = sharedPrefManager.userName

    val currentUser: FirebaseUser?
        get() = sharedPrefManager.user

    private val _showCompleted = MutableLiveData(true)
    val showCompleted: LiveData<Boolean>
        get() = _showCompleted

    fun setShowCompleted(value: Boolean) {
        _showCompleted.value = value
        prepareList()
    }

    private val _autoRefresh = MutableLiveData(false)

    fun setAutoRefresh(value: Boolean) {
        _autoRefresh.value = value
        if (value)
            autoSynJob = viewModelScope.launch {
                log("setAutoRefresh -> obtaining flow...")
                repo.getFlow().collect {
                    countCompleteAndFilter(it)
                }
            }
        else {
            log("setAutoRefresh -> cancelling flow...")
            autoSynJob.cancel()
        }
    }

    private val _todoItems = MutableLiveData<List<TodoItem>>()
    val todoItems: LiveData<List<TodoItem>>
        get() = _todoItems

    private val _countCompleted = MutableLiveData(todoItems.value?.countCompleted() ?: 0)
    val countCompleted: LiveData<Int>
        get() = _countCompleted

    private var _eventSigninRefreshRequest = MutableLiveData<Boolean>(false)
    val eventSigninRefreshRequest: LiveData<Boolean>
        get() = _eventSigninRefreshRequest

    private var _eventLoginRequest = MutableLiveData<Boolean>(false)
    val eventLoginRequest: LiveData<Boolean>
        get() = _eventLoginRequest

    private var _eventLogoutRequest = MutableLiveData<Boolean>(false)
    val evenLogoutRequest: LiveData<Boolean>
        get() = _eventLogoutRequest

    init {
        repo.setResultListener(this)
        prepareList()
    }

    fun prepareList() {
        log("prepareList")
        viewModelScope.launch {
            countCompleteAndFilter(repo.getTodoList())
        }
    }

    private fun countCompleteAndFilter(fullList: MutableList<TodoItem>) {
        log("countCompleteAndFilter with list --> $fullList")
        _countCompleted.value = fullList.countCompleted()
        _todoItems.value =
            if (showCompleted.value == false) {
                fullList.filterNonCompletedOnly()
            } else
                fullList
    }

    fun onDeleteOrRestore(item: TodoItem, restoreFlag: Boolean) {
        item.deletepending = !restoreFlag
        item.changed = Calendar.getInstance().time
        viewModelScope.launch {
            repo.addOrUpdateTodo(item)
        }
    }

    fun onCompleteChanged(item: TodoItem) {
        item.changed = Calendar.getInstance().time
        viewModelScope.launch {
            repo.addOrUpdateTodo(item)
        }
        _countCompleted.value = todoItems.value?.countCompleted() ?: 0
    }

    fun handleSyncRequest() {
        log("handleSyncRequest")
        if (networkStatus.value == false) {
            _toastToUi.value = app.getString(R.string.network_offline)
            return
        }
        if (currentUserName.value.isNullOrEmpty()) {
            _toastToUi.value = app.getString(R.string.force_signin_message)
            return
        }
        viewModelScope.launch {
            if (repo.syncAll(true) == 200)
                prepareList()
        }
    }

    fun onSignSuccess(firebaseUser: FirebaseUser, token: String) {
        log("onSignSuccess --> ${firebaseUser.displayName} $token")
        _eventLoginRequest.value = false
        sharedPrefManager.saveAuthToken(firebaseUser, token)
    }

    fun onSignRefreshSuccess(token: String) {
        log("onSignRefreshSuccess --> $token")
        _eventLoginRequest.value = false
        sharedPrefManager.saveAuthToken(token)
        handleSyncRequest()
    }

    fun onSignFailure() {
        log("onSignFailure")
        _eventLoginRequest.value = false
    }

    fun onSigninRefreshFailure() {
        log("onSigninRefreshFailure")
        _eventSigninRefreshRequest.value = false
    }

    fun handleLoginOrLogoutClick() {
        if (currentUserName.value == null) {
            if (networkStatus.value == false) {
                _toastToUi.value = app.getString(R.string.network_offline)
                return
            }
            log("handleLoginOrLogoutClick -> initiate signin...")
            _eventLoginRequest.value = true
        }  else {
            log("handleLoginOrLogoutClick -> initiate logout...")
            _toastToUi.value = app.getString(R.string.items_deleted_message)
            _eventLogoutRequest.value = true
            viewModelScope.launch {
                repo.clearAllLocal()
            }
        }
    }

    override fun onCode401() {
        log("onCode401")
        _eventSigninRefreshRequest.value = true
    }

    override fun onOther() {
        log("onOther")
        _toastToUi.value = app.getString(R.string.server_error_message)
    }

    fun onSingOut() {
        log("onSingOut")
        _eventLogoutRequest.value = false
        sharedPrefManager.clearAuthToken()
    }

    fun storeUiTheme(theme: Theme) {
        log("storeUiTheme -> new them selected: $theme")
        sharedPrefManager.saveUiTheme(theme)
    }
}