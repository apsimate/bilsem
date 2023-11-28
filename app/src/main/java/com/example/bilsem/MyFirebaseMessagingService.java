package com.example.bilsem;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;



public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);

        // Token'i sunucunuza göndermek için bir API çağrısı yapabilirsiniz.
        sendTokenToServer(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            sendNotification(title, body);
        } else if (remoteMessage.getData().size() > 0) {
            String title = remoteMessage.getData().get("title");
            String content = remoteMessage.getData().get("content");

            // Veritabanındaki değişiklikleri algıladığınızda burada bildirim gönderme işlemini gerçekleştirebilirsiniz.
            sendNotification(title, content);
        }
    }

    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, AnnouncementsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int requestCode = 0;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent,
                PendingIntent.FLAG_IMMUTABLE);

        String channelId = "fcm_default_channel";
        android.net.Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Android Oreo ve sonraki sürümler için bildirim kanalı oluşturulması gerekmektedir.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        int notificationId = 0;
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    // Token'i sunucunuza göndermek için bir API çağrısı örneği
    private void sendTokenToServer(String token) {
        // Burada token'i sunucunuza göndermek için kullanacağınız API çağrısını gerçekleştirebilirsiniz.
        // Örnek olarak Retrofit, OkHttp veya diğer uygun kütüphaneleri kullanabilirsiniz.
        // API çağrısını gerçekleştirecek kodu buraya yazabilirsiniz.
        // Bu örnekte ise sadece loglama yapılması simüle edilmiştir.
        Log.d(TAG, "Token sent to server: " + token);
    }
}