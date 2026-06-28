package com.bot.metin2;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Path;
import android.os.Build;
import android.os.Handler;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

public class BotService extends AccessibilityService {

    private Handler handler = new Handler();
    private boolean running = false;
    private static final String CHANNEL_ID = "bot_channel";

    private int[][] slots = {
        {500, 900},
        {300, 700},
        {700, 800},
        {400, 600},
        {600, 1000}
    };

    private static final int SKILL1_X = 200;
    private static final int SKILL1_Y = 1600;
    private static final int SKILL2_X = 350;
    private static final int SKILL2_Y = 1600;
    private static final int SKILL3_X = 500;
    private static final int SKILL3_Y = 1600;

    private int currentSlot = 0;

    private void showToast(String msg) {
        new Handler(getMainLooper()).post(() ->
            Toast.makeText(BotService.this, msg, Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if ("START".equals(intent.getAction())) {
                running = true;
                createNotificationChannel();
                startForegroundNotification();
                showToast("Bot baslatildi!");
                handler.postDelayed(botLoop, 1000);
            } else if ("STOP".equals(intent.getAction())) {
                running = false;
                handler.removeCallbacksAndMessages(null);
                stopForeground(true);
                showToast("Bot durduruldu!");
            }
        }
        return START_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID, "Bot Service", NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager nm = getSystemService(NotificationManager.class);
            nm.createNotificationChannel(channel);
        }
    }

    private void startForegroundNotification() {
        Notification notification = new Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("Metin2 Bot")
            .setContentText("Bot calisiyor...")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build();
        startForeground(1, notification);
    }

    private Runnable botLoop = new Runnable() {
        @Override
        public void run() {
            if (!running) return;

            int[] slot = slots[currentSlot];
            showToast("Slot " + (currentSlot + 1) + " e gidiliyor");
            tap(slot[0], slot[1]);

            handler.postDelayed(() -> {
                if (!running) return;
                tap(SKILL1_X, SKILL1_Y);
                handler.postDelayed(() -> {
                    if (!running) return;
                    tap(SKILL2_X, SKILL2_Y);
                    handler.postDelayed(() -> {
                        if (!running) return;
                        tap(SKILL3_X, SKILL3_Y);
                        currentSlot = (currentSlot + 1) % slots.length;
                        handler.postDelayed(botLoop, 3000);
                    }, 600);
                }, 600);
            }, 2000);
        }
    };

    private void tap(int x, int y) {
        Path path = new Path();
        path.moveTo(x, y);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        builder.addStroke(
            new GestureDescription.StrokeDescription(path, 0, 100)
        );
        dispatchGesture(builder.build(), null, null);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {}

    @Override
    public void onInterrupt() { running = false; }
}
