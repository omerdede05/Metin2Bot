package com.bot.metin2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAccessibility = findViewById(R.id.btnAccessibility);
        Button btnStart = findViewById(R.id.btnStart);
        Button btnStop = findViewById(R.id.btnStop);

        btnAccessibility.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
        });

        btnStart.setOnClickListener(v -> {
            Intent intent = new Intent(this, BotService.class);
            intent.setAction("START");
            startService(intent);
            Toast.makeText(this, "Bot baslatildi!", Toast.LENGTH_SHORT).show();
        });

        btnStop.setOnClickListener(v -> {
            Intent intent = new Intent(this, BotService.class);
            intent.setAction("STOP");
            startService(intent);
            Toast.makeText(this, "Bot durduruldu!", Toast.LENGTH_SHORT).show();
        });
    }
}
