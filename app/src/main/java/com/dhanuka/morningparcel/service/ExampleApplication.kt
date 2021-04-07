package com.dhanuka.morningparcel

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.dhanuka.morningparcel.service.ExampleEphemeralKeyProvider
import com.facebook.stetho.Stetho
import com.stripe.android.CustomerSession
import com.stripe.android.PaymentConfiguration
import com.dhanuka.morningparcel.Helper.Preferencehelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExampleApplication : MultiDexApplication() {



    override fun onCreate() {

        initiateStripe(this);



        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            Stetho.initializeWithDefaults(this@ExampleApplication)
        }
        getEmpKey(this);

    }
    fun getEmpKey(context:Context) {
        CustomerSession.initCustomerSession(
                context,
                ExampleEphemeralKeyProvider(context),
                false
        )
    }

    fun initiateStripe1() {}

    fun initiateStripe(context:Context) {
        val prefs: Preferencehelper = Preferencehelper(context)
        if (prefs!!.prefsPaymentoption.equals("1", ignoreCase = true)) {
            PaymentConfiguration.init(context, "pk_test_EbCpH358aTH3p8TSFGJf1aCe00VuQ0Q8qB")
        } else {
            PaymentConfiguration.init(context, "pk_live_hr9jWy8lCWINlj6NulW4vPeH00YHulDFlP")
        }

    }

}
