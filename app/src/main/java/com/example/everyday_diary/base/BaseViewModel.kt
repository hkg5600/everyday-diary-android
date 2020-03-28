package com.example.everyday_diary.base

import androidx.lifecycle.ViewModel
import com.example.everyday_diary.room.model.Token
import com.example.everyday_diary.utils.SingleLiveEvent
import com.example.everyday_diary.utils.TokenUtil
import com.example.everyday_diary.network.response.Response
import com.example.everyday_diary.network.response.TokenResponse
import com.example.everyday_diary.utils.TokenObject
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

abstract class BaseViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val tokenUtil = TokenUtil.getInstance()

    //success
    val tokenSuccess: SingleLiveEvent<Any> = SingleLiveEvent()
    val roomSuccess: SingleLiveEvent<String> = SingleLiveEvent()

    //error
    val tokenError: SingleLiveEvent<String> = SingleLiveEvent()
    val roomError: SingleLiveEvent<String> = SingleLiveEvent()
    val error: SingleLiveEvent<String> = SingleLiveEvent()
    val networkError: SingleLiveEvent<Any> = SingleLiveEvent()

    //data
    val message: SingleLiveEvent<String> = SingleLiveEvent()
    val data: SingleLiveEvent<Any> = SingleLiveEvent()
    val tokenChanged: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val loading: SingleLiveEvent<Boolean> = SingleLiveEvent()

    fun addDisposable(disposable: Single<*>, observer: DisposableSingleObserver<Any>) {
        loading.value = true
        compositeDisposable.add(
            disposable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(observer)
        )
    }

    fun addRoomDisposable(disposable: Completable, msg: String) {
        loading.value = true
        compositeDisposable.add(
            disposable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    roomSuccess.value = msg
                },
                {
                    roomError.call()
                }
            )
        )
    }

    fun getDataObserver() = DataDisposableSingleObserver()
    fun getMsgObserver() = MsgDisposableSingleObserver()

    inner class MsgDisposableSingleObserver : DisposableSingleObserver<Any>() {

        override fun onSuccess(t: Any) = filterMsgFromResponse(t)

        override fun onError(e: Throwable) {
            networkError.call()
        }

    }

    inner class DataDisposableSingleObserver : DisposableSingleObserver<Any>() {

        override fun onSuccess(t: Any) = filterDataFromResponse(t)

        override fun onError(e: Throwable) {
            networkError.call()
        }
    }

    private fun filterMsgFromResponse(t: Any) {
        loading.value = false
        t as retrofit2.Response<Response<String>>
        if (t.isSuccessful) {
            if (checkSuccess(t.body()?.status!!)) message.value = t.body()?.message!!
            else setError(t.body()?.message!!)
        } else checkError(t.code())
    }

    private fun filterDataFromResponse(t: Any) {
        loading.value = false
        t as retrofit2.Response<Response<*>>
        if (t.isSuccessful) {
            if (checkSuccess(t.body()?.status!!)) data.value = t.body()?.data!!
            else setError(t.body()?.message!!)
        } else checkError(t.code())
    }

    private fun checkSuccess(status: Int) = status == 200

    private fun checkError(code: Int) {
        if (code == 401) refreshToken()
        else setError("Server")
    }

    private fun setError(message: String) {
        error.value = message
    }

    //Token
    fun insertTokenToRoom(token: Token) {
        loading.value = true
        addRoomDisposable(tokenUtil?.insertToken(token)!!, "tokenData")
    }

    fun deleteTokenFromRoom() {
        loading.value = true
        addRoomDisposable(tokenUtil?.deleteToken()!!, "logout")
    }

    fun getTokenFromRoom() {
        loading.value = true
        compositeDisposable.add(
            tokenUtil?.getToken()?.subscribeOn(Schedulers.io())?.observeOn(
                AndroidSchedulers.mainThread()
            )?.subscribeWith(TokenRoomDisposableSingleObserver())!!
        )
    }

    fun refreshToken() {
        loading.value = true
        addDisposable(tokenUtil?.refreshToken(TokenObject.refreshToken!!)!!, getRefreshTokenObserver())
    }

    private fun getRefreshTokenObserver() = RefreshTokenDisposableSingleObserver()

    inner class TokenRoomDisposableSingleObserver : DisposableSingleObserver<Token>() {
        override fun onSuccess(t: Token) {
            loading.value = false
            TokenObject.token = t.token
            TokenObject.refreshToken = t.refreshToken
            tokenSuccess.call()
        }

        override fun onError(e: Throwable) {
            loading.value = false
            tokenError.value = e.message
        }
    }

    inner class RefreshTokenDisposableSingleObserver : DisposableSingleObserver<Any>() {

        override fun onSuccess(t: Any) {
            loading.value = false
            t as retrofit2.Response<Response<TokenResponse>>
            if (t.isSuccessful) {
                when (t.body()?.status) {
                    200 -> {
                        TokenObject.token = t.body()?.data?.token
                        insertTokenToRoom(Token(1, TokenObject.token!!, TokenObject.refreshToken!!))
                        tokenChanged.value = true
                    }
                    400 -> {
                        tokenChanged.value = false
                    }
                    401 -> {
                        tokenChanged.value = false
                    }
                }
            } else
                setError("Server")
        }

        override fun onError(e: Throwable) {
            loading.value = false
            networkError.call()
        }

    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
