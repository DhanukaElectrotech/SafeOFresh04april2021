package com.dhanuka.morningparcel.service

import android.content.Context
import android.util.Log
import androidx.annotation.Size
import com.dhanuka.morningparcel.module.BackendApiFactory
import com.stripe.android.EphemeralKeyProvider
import com.stripe.android.EphemeralKeyUpdateListener
import com.dhanuka.morningparcel.Helper.Preferencehelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import java.io.IOException

/**
 * An implementation of [EphemeralKeyProvider] that can be used to generate
 * ephemeral keys on the backend.
 */
internal class ExampleEphemeralKeyProvider constructor(
    backendUrl: String,
    mCtx: Context
 ) : EphemeralKeyProvider {
  //  var prefs: Preferencehelper? = null
    var mCtx: Context? = null
   /*  constructor(context: Context) : this("http://webtecnoworld.com/stripeNew/")
  */  constructor(context: Context) : this("https://Vehicletrack.membocool.com?",context)

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val backendApi: BackendApi = BackendApiFactory(backendUrl).create()
    private val prefs: Preferencehelper = Preferencehelper(mCtx)

    override fun createEphemeralKey(
        @Size(min = 4) apiVersion: String,
        keyUpdateListener: EphemeralKeyUpdateListener
    ) {
    //  prefs = Preferencehelper(mCtx);
        compositeDisposable.add(
            backendApi.createEphemeralKey(hashMapOf(
                "api_version" to apiVersion,
                "paycat" to prefs!!.prefsPaymentoption,
                "cust_id" to prefs!!.prefsCustId/*"cus_HJ1CkRMUM6HqG7"*/
                ))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ responseBody ->
                    try {
                        val json_contact: JSONObject = JSONObject(responseBody.string())
                        Log.e("MYNEWPA", json_contact.toString())
                        var jsonarray_info:String= json_contact.getString("e_key")

                        val ephemeralKeyJson = responseBody.string()
                        keyUpdateListener.onKeyUpdate(jsonarray_info)
                    } catch (e: IOException) {
                        keyUpdateListener
                            .onKeyUpdateFailure(0, e.message ?: "")
                    }
                }, {
                    Log.e("StripeExample", "Exception in ExampleEphemeralKeyProvider", it)
                })
        )
    }
}
