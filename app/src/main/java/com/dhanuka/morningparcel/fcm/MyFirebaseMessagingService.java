package com.dhanuka.morningparcel.fcm;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;


import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.activity.HomeActivity;
import com.dhanuka.morningparcel.activity.OrderHistoryActivity;

import com.dhanuka.morningparcel.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.Random;

 /*
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
*/

/**
 * NOTE: There can only be one service in each app that receives FCM messages. If multiple
 * are declared in the Manifest then the first one will be chosen.
 * <p>
 * In order to make this Java sample functional, you must remove the following from the Kotlin messaging
 * service in the AndroidManifest.xml:
 * <p>
 * <intent-filter>
 * <action android:name="com.google.firebase.MESSAGING_EVENT" />
 * </intent-filter>
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    // private NotificationUtils notificationUtils;


    private static final String TAG = "FirebaseMessageService";
    Bitmap bitmap, bitmap1;
    private TextToSpeech tts;
    Context mcontext;
    String notification_type="";
    String CHANNEL_ID;

    PendingIntent pendingIntent;
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    Preferencehelper prefsOld;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        prefsOld = new Preferencehelper(getApplicationContext());
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData().toString());
        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        String message = remoteMessage.getData().get("title");
        String message1 = remoteMessage.getData().get("message1");
        if(remoteMessage.getData().get("notification_type")!=null)
        {
            notification_type = remoteMessage.getData().get("notification_type");
        }
        else {

            notification_type="";
        }

        //imageUri will contain URL of the image to be displayed with Notification
        String iconUri = remoteMessage.getData().get("icon");
        String imageUri = remoteMessage.getData().get("image");

        //If the key AnotherActivity has  value as True then when the user taps on notification, in the app AnotherActivity will be opened.
        //If the key AnotherActivity has  value as False then when the user taps on notification, in the app MainActivity2 will be opened.
        String TrueOrFlase = remoteMessage.getData().get("AnotherActivity");

        //To get a Bitmap image from the URL received


        //iconUri="http://mmthinkbiz.com/images/main.png";
        iconUri = "http://mmgeotracker.com/images/logo1.png";
        bitmap1 = getBitmapfromUrl(iconUri);
        try {
            bitmap = getBitmapfromUrl(imageUri);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if (message.contains(";User")) {
            message = message.replace(";User", "");
            if (prefsOld.getPrefsUsercategory().equalsIgnoreCase("1057")) {
                sendNotification(message, message1, bitmap, bitmap1, TrueOrFlase);

            } else {
                sendNotification(message, message1, bitmap, bitmap1, TrueOrFlase);


            }
        } else if (message.contains(";Retailer")) {
            message = message.replace(";Retailer", "");
            if (prefsOld.getPrefsUsercategory().equalsIgnoreCase("1058")) {
                sendNotification(message, message1, bitmap, bitmap1, TrueOrFlase);

            } else {
                sendNotification(message, message1, bitmap, bitmap1, TrueOrFlase);


            }
        }/*else{
            sendNotification(message,message1,bitmap,bitmap1, TrueOrFlase);

        }*/

    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }
    // [END on_new_token]

    /**
     * Schedule async work using WorkManager.
     */


    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        SharedPreferences prefsToken = getSharedPreferences("SAFE_O_BUDDY_TOKEN",
                MODE_PRIVATE);
        SharedPreferences.Editor mEditorprefsToken = prefsToken.edit();

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        mEditorprefsToken.putString("token", token);
        editor.commit();
        mEditorprefsToken.commit();


        Log.e("REGISTER_TOKENN", token);
        // TODO: Implement this method to send token to your app server.
    }


    private void sendNotification(String messageBody, String mMessage, Bitmap image, Bitmap imageicon, String TrueOrFalse) {

        mcontext = this.mcontext;

        int NOTIFICATION_ID = 234;
        mcontext = getBaseContext();

        NotificationManager notificationManagero = (NotificationManager) mcontext.getSystemService(Context.NOTIFICATION_SERVICE);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (notification_type.equalsIgnoreCase("Promotional"))
        {

            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT|PendingIntent.FLAG_CANCEL_CURRENT);

        }
        else
        {
            Intent intent = new Intent(this, OrderHistoryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT|PendingIntent.FLAG_CANCEL_CURRENT);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            Random r = new Random();
            final int i3 = r.nextInt(80 - 65) + 65;
            CHANNEL_ID = "my_channel_01";
            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            NotificationCompat.BigTextStyle inboxStyle = new NotificationCompat.BigTextStyle();

            notificationManagero.createNotificationChannel(mChannel);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(mcontext, CHANNEL_ID);


            if (bitmap != null) {

                builder
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(messageBody)
                        .setContentText(mMessage)
                        .setSound(defaultSoundUri)

                        .setContentIntent(pendingIntent)

                        .setStyle(
                                new NotificationCompat.BigPictureStyle()
                                        .bigPicture(bitmap)
                                        .bigLargeIcon(null)
                        ).setLargeIcon(bitmap);
                notificationManagero.notify(i3, builder.build());

            } else {
                int icon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? R.drawable.app_logo : R.mipmap.ic_launcher;

                builder.setContentTitle(messageBody)
                        .setSound(defaultSoundUri)
                        .setSmallIcon(icon)
                        .setContentIntent(pendingIntent)


                        //    .setStyle(new NotificationCompat.BigTextStyle().bigText(messagesum))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(Html.fromHtml(mMessage)))
                        .setContentText(mMessage);
                notificationManagero.notify(i3, builder.build());

            }


        } else {
            //added by basit on 19-04-2017
            int icon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? R.drawable.app_logo : R.mipmap.ic_launcher;
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);


            if (bitmap != null) {

                notificationBuilder
                        .setSmallIcon(icon)// img at top
                        .setContentTitle(messageBody)
                        .setContentIntent(pendingIntent)

                        .setContentText(mMessage) //"Welcome to Dhanuka"
                        //    .setStyle(new NotificationCompat.BigTextStyle().bigText(messagesum))/*Notification with Image*/
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(Html.fromHtml(messageBody)))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setStyle(
                                new NotificationCompat.BigPictureStyle()
                                        .bigPicture(bitmap)
                                        .bigLargeIcon(null)
                        ).setLargeIcon(bitmap);


                notificationBuilder.setLargeIcon(imageicon)/*Notification icon image*/

                ;
//.setContentIntent(pendingIntent) // this code was removed on 07/14 as it can open another page
                mcontext = getBaseContext();

                NotificationManager notificationManager = (NotificationManager) mcontext.getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(4546 /* ID of notification */, notificationBuilder.build());
                showsound(messageBody);
            } else {


                notificationBuilder.setSmallIcon(icon)// img at top
                        .setLargeIcon(imageicon)/*Notification icon image*/
                        .setSmallIcon(icon)// img at top
                        .setContentIntent(pendingIntent)

                        .setContentTitle(mMessage)
                        //    .setStyle(new NotificationCompat.BigTextStyle().bigText(messagesum))/*Notification with Image*/
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(Html.fromHtml(messageBody)))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentText(messageBody) //"Welcome to Dhanuka"
                        //    .setStyle(new NotificationCompat.BigTextStyle().bigText(messagesum))/*Notification with Image*/
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(Html.fromHtml(messageBody)))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                ;
//.setContentIntent(pendingIntent) // this code was removed on 07/14 as it can open another page
                mcontext = getBaseContext();

                NotificationManager notificationManager = (NotificationManager) mcontext.getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(4546 /* ID of notification */, notificationBuilder.build());
                showsound(messageBody);

            }
        }


    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.DONUT)
    public void showsound(String messageBody) {
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.UK);
                }
            }
        });
        tts.speak(messageBody, TextToSpeech.QUEUE_FLUSH, null);
        //tts.speak("Welcome "  + detaillist.get(j).getEmployeeName().toString() , TextToSpeech.QUEUE_FLUSH, null);
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}