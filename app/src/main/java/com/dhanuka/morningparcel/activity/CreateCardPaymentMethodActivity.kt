package com.dhanuka.morningparcel.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import butterknife.ButterKnife
import com.dhanuka.morningparcel.activity.KeyboardController
import com.dhanuka.morningparcel.R
import com.dhanuka.morningparcel.SnackbarController
import com.dhanuka.morningparcel.StripeFactory


import com.stripe.android.Stripe
import com.stripe.android.model.PaymentMethod
import com.stripe.android.view.CardValidCallback
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_create_card_payment_method.*

class CreateCardPaymentMethodActivity : AppCompatActivity() {
    //  val coordinator: CoordinatorLayout
    private val compositeDisposable = CompositeDisposable()

    private val stripe: Stripe by lazy {
        StripeFactory(this).create()
    }
    private val snackbarController: SnackbarController by lazy {
        SnackbarController(coordinator)
    }
    private val keyboardController: KeyboardController by lazy {
        KeyboardController(this)
    }
//val payment_methods:RecyclerView=findViewById(R.id.payment_methods) as RecyclerView;

    var price="";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_card_payment_method_new)
        ButterKnife.bind(this);
        price=intent.getStringExtra("price")
        /*  payment_methods.setHasFixedSize(false)
          payment_methods.layoutManager = LinearLayoutManager(this)
          payment_methods.adapter = adapter
  */
        card_multiline_widget.setShouldShowPostalCode(false);
        card_multiline_widget.setCardValidCallback(object : CardValidCallback {
            override fun onInputChanged(
                    isValid: Boolean,
                    invalidFields: Set<CardValidCallback.Fields>
            ) {
                // added as an example - no-op
            }
        })

        create_button.setOnClickListener {
            keyboardController.hide()
            createPaymentMethod()
        }
    }

    private fun createPaymentMethod() {
        val paymentMethodCreateParams =
                card_multiline_widget.paymentMethodCreateParams ?: return

        // Note: using this style of Observable creation results in us having a method that
        // will not be called until we subscribe to it.
        val createPaymentMethodObservable = Observable.fromCallable {
            stripe.createPaymentMethodSynchronous(paymentMethodCreateParams)
        }

        compositeDisposable.add(createPaymentMethodObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onCreatePaymentMethodStart() }
                .doOnComplete { onCreatePaymentMethodCompleted() }
                .subscribe(
                        { onCreatedPaymentMethod(it) },
                        { showSnackbar(it.localizedMessage.orEmpty()) }
                )
        )
    }
    private fun showSnackbar(message: String) {
        snackbarController.show(message)
    }

    private fun onCreatePaymentMethodStart() {
        progress_bar.visibility = View.VISIBLE
        create_button.isEnabled = false
    }

    private fun onCreatePaymentMethodCompleted() {
        progress_bar.visibility = View.INVISIBLE
        create_button.isEnabled = true
    }

    private fun onCreatedPaymentMethod(paymentMethod: PaymentMethod?) {
        val PREFS_FILENAME = "payment_prfs"
        val prefs = this.getSharedPreferences(PREFS_FILENAME, 0)
        val editor = prefs!!.edit()

        if (paymentMethod != null) {
            editor.putString("isOK", "1")
            editor.putString("mid", paymentMethod.id)
            editor.apply()

            Log.e("paymentMethod12343",paymentMethod.id)
            /*   Intent:resultIntent = new Intent();
              resultIntent.putExtra("name", name_edit_text.getText().toString());
              resultIntent.putExtra("number", name_edit_text1.getText().toString());
              resultIntent.putExtra("mPosition", mPosition);
              resultIntent.putExtra("strId", strRoasterId);
              setResult(Activity.RESULT_OK, resultIntent);
              finish();*/
            //   setResult(Activity.RESULT_OK, Intent().putExtras(viewModel.paymentResult.toBundle()))
            finish()

            //    adapter.paymentMethods.add(0, paymentMethod)
            //  adapter.notifyItemInserted(0)
        } else {
            editor.putString("isOK", "0")
            editor.putString("mid", "0")
            editor.apply()
            showSnackbar("Created null PaymentMethod")

        }
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }




}
