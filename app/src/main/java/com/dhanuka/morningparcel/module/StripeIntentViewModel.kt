package com.dhanuka.morningparcel.module

import android.app.Application
import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.dhanuka.morningparcel.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException

internal class StripeIntentViewModel(
    application: Application
) : AndroidViewModel(application) {
    val inProgress = MutableLiveData<Boolean>()
    val status = MutableLiveData<String>()

    private val context = application.applicationContext
    private val backendApi = BackendApiFactory(context).create()
    private val compositeSubscription = CompositeDisposable()

    fun createPaymentIntent(
        country: String,
        source: String,
        callback: (JSONObject) -> Unit
    ) {
        makeBackendRequest(
            backendApi::createPaymentIntent,
            R.string.creating_payment_intent,
            R.string.payment_intent_status,
            country,
            source,
            callback
        )
    }

    fun createSetupIntent(


        country: String,
        source: String,
        callback: (JSONObject) -> Unit
    ) {

        Log.e("@@dsada","break")
        makeBackendRequest(
            backendApi::createSetupIntent,
            R.string.creating_setup_intent,
            R.string.setup_intent_status,
            country,
            source,
            callback
        )
    }

    private fun makeBackendRequest(
        apiMethod: (MutableMap<String, Any>) -> Observable<ResponseBody>,
        @StringRes creating: Int,
        @StringRes result: Int,
        country: String,
        source: String,
        callback: (JSONObject) -> Unit
    ) {

        compositeSubscription.add(
            apiMethod(
                mutableMapOf(
                    "country" to country,
                    "amount" to 1.00,
                    "cust_id" to "cus_HJ1CkRMUM6HqG7",
                    "source" to callback,
                    "currency" to "usd"
                )
            )
                .doOnSubscribe {
                    inProgress.postValue(true)
                    status.postValue(context.getString(creating))
                }
                .map {
                    JSONObject(it.string())
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        status.postValue(status.value + "\n\n" +
                            context.getString(result,
                                it.getString("status")))
                        callback(it)
                    },
                    {
                        val errorMessage =
                            (it as? HttpException)?.response()?.errorBody()?.string()
                                ?: it.message
                        status.postValue(status.value + "\n\n$errorMessage")
                        inProgress.postValue(false)
                    }
                ))
    }

    override fun onCleared() {
        super.onCleared()
        compositeSubscription.dispose()
    }
}
