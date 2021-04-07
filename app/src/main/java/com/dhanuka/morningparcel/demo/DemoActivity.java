package com.dhanuka.morningparcel.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.dhanuka.morningparcel.R;
/*import com.google.android.gms.common.api.Stkoatus;
import com.google.android.gms.identity.intents.model.UserAddress;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.CardInfo;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.stripe.android.model.Token;*/

public class DemoActivity extends AppCompatActivity {
//    PaymentsClient paymentsClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
    /*    paymentsClient = Wallet.getPaymentsClient(this,
                new Wallet.WalletOptions.Builder()
                        .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                        .build());


        isReadyToPay();*/


    }

    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 53;

/*
    private void payWithGoogle() {
        AutoResolveHelper.resolveTask(
                paymentsClient.loadPaymentData(createPaymentDataRequest()),
                this,
                LOAD_PAYMENT_DATA_REQUEST_CODE
        );
    }
*/

/*
    @NonNull
    private IsReadyToPayRequest createIsReadyToPayRequest() throws JSONException {
        final JSONArray allowedAuthMethods = new JSONArray();
        allowedAuthMethods.put("PAN_ONLY");
        allowedAuthMethods.put("CRYPTOGRAM_3DS");

        final JSONArray allowedCardNetworks = new JSONArray();
        allowedCardNetworks.put("AMEX");
        allowedCardNetworks.put("DISCOVER");
        allowedCardNetworks.put("JCB");
        allowedCardNetworks.put("MASTERCARD");
        allowedCardNetworks.put("VISA");

        final JSONObject isReadyToPayRequestJson = new JSONObject();
        isReadyToPayRequestJson.put("allowedAuthMethods", allowedAuthMethods);
        isReadyToPayRequestJson.put("allowedCardNetworks", allowedCardNetworks);

        return IsReadyToPayRequest.fromJson(isReadyToPayRequestJson.toString());
    }

    private void isReadyToPay() {
        try {
            final IsReadyToPayRequest request = createIsReadyToPayRequest();
            paymentsClient.isReadyToPay(request)
                    .addOnCompleteListener(
                            new OnCompleteListener<Boolean>() {
                                public void onComplete(Task<Boolean> task) {
                                    try {
                                        if (task.isSuccessful()) {
                                            // show Google Pay as payment option
                                            payWithGoogle();
                                        } else {
                                            // hide Google Pay as payment option
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                    );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @NonNull
    private PaymentDataRequest createPaymentDataRequest() {
        final JSONObject tokenizationSpec =
                new GooglePayConfig().getTokenizationSpecification();
        final JSONObject cardPaymentMethod = new JSONObject();
        try {
            cardPaymentMethod.put("type", "CARD")
                    .put(
                            "parameters",
                            new JSONObject()
                                    .put("allowedAuthMethods", new JSONArray()
                                            .put("PAN_ONLY")
                                            .put("CRYPTOGRAM_3DS"))
                                    .put("allowedCardNetworks",
                                            new JSONArray()
                                                    .put("AMEX")
                                                    .put("DISCOVER")
                                                    .put("JCB")
                                                    .put("MASTERCARD")
                                                    .put("VISA"))

                                    // require billing address
                                    .put("billingAddressRequired", true)
                                    .put(
                                            "billingAddressParameters",
                                            new JSONObject()
                                                    // require full billing address
                                                    .put("format", "FULL")

                                                    // require phone number
                                                    .put("phoneNumberRequired", true)
                                    )
                    )
                    .put("tokenizationSpecification", tokenizationSpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // create PaymentDataRequest
        final JSONObject paymentDataRequest = new JSONObject();
        try {
            paymentDataRequest.put("apiVersion", 2)
                    .put("apiVersionMinor", 0)
                    .put("allowedPaymentMethods",
                            new JSONArray().put(cardPaymentMethod))
                    .put("transactionInfo", new JSONObject()
                            .put("totalPrice", "10.00")
                            .put("totalPriceStatus", "FINAL")
                            .put("currencyCode", "USD")
                    )
                    .put("merchantInfo", new JSONObject()
                            .put("merchantName", "Dhanuka Electrotech Pvt.ltd."))

                    // require email address
                    .put("emailRequired", true)
            ;
        } catch (Exception e) {
        }
        return PaymentDataRequest.fromJson(paymentDataRequest.toString());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOAD_PAYMENT_DATA_REQUEST_CODE: {
                switch (resultCode) {
                    case Activity.RESULT_OK: {
                        PaymentData paymentData = PaymentData.getFromIntent(data);
                        // You can get some data on the user's card, such as the
                        // brand and last 4 digits
                        CardInfo info = paymentData.getCardInfo();
                        // You can also pull the user address from the
                        // PaymentData object.
                        UserAddress address = paymentData.getShippingAddress();
                        // This is the raw JSON string version of your Stripe token.
                        String rawToken = paymentData.getPaymentMethodToken()
                                .getToken();
                        Log.e("rawTokenrawToken", rawToken);
                        // Now that you have a Stripe token object,
                        // charge that by using the id
*/
/*
                        Token stripeToken = Token.fromString(rawToken);
                        if (stripeToken != null) {
                            // This chargeToken function is a call to your own
                            // server, which should then connect to Stripe's
                            // API to finish the charge.
                            chargeToken(stripeToken.getId());
                        }
*//*

                        break;
                    }
                    case Activity.RESULT_CANCELED: {
                        break;
                    }
                    case AutoResolveHelper.RESULT_ERROR: {
                        // Log the status for debugging
                        // Generally there is no need to show an error to
                        // the user as the Google Payment API will do that
                        final Status status =
                                AutoResolveHelper.getStatusFromIntent(data);
                        break;
                    }
                    default: {
                        // Do nothing.
                    }
                }
                break;
            }
            default: {
                // Handle any other startActivityForResult calls you may have made.
            }
        }
    }
*/


}
