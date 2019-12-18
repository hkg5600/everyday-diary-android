package com.example.everyday_diary.base

import android.util.Log
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


    fun addDisposable(disposable: Single<*>, observer: DisposableSingleObserver<Any>) {
        compositeDisposable.add(
            disposable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(observer)
        )
    }

    fun addRoomDisposable(disposable: Completable, msg: String) {
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

    fun getMsgObserver() = MsgDisposableSingleObserver()
    fun getDataObserver() = DataDisposableSingleObserver()

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
        t as retrofit2.Response<Response<String>>
        if (t.isSuccessful) {
            if (checkSuccess(t.body()?.status!!)) message.value = t.body()?.message!!
            else setError(t.body()?.message!!)
        } else checkError(t.code())
    }

    private fun filterDataFromResponse(t: Any) {
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
    fun insertToken(token: Token) = addRoomDisposable(tokenUtil?.insertToken(token)!!, "tokenData")

    fun getToken() = compositeDisposable.add(
        tokenUtil?.getToken()?.subscribeOn(Schedulers.io())?.observeOn(
            AndroidSchedulers.mainThread()
        )?.subscribeWith(TokenRoomDisposableSingleObserver())!!
    )

    fun refreshToken() {
        addDisposable(tokenUtil?.refreshToken(TokenObject.refreshToken!!)!!, getTokenObserver())
    }

    private fun getTokenObserver() = TokenDisposableSingleObserver()

    inner class TokenRoomDisposableSingleObserver : DisposableSingleObserver<Token>() {
        override fun onSuccess(t: Token) {
            TokenObject.token = t.token
            TokenObject.refreshToken = t.refreshToken
            tokenSuccess.call()
        }

        override fun onError(e: Throwable) {
            tokenError.value = e.message
        }
    }

    inner class TokenDisposableSingleObserver : DisposableSingleObserver<Any>() {

        override fun onSuccess(t: Any) {
            t as retrofit2.Response<Response<TokenResponse>>
            if (t.isSuccessful) {
                when (t.body()?.status) {
                    200 -> {
                        TokenObject.token = t.body()?.data?.token
                        insertToken(Token(1, TokenObject.token!!, TokenObject.refreshToken!!))
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
            networkError.call()
        }

    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
