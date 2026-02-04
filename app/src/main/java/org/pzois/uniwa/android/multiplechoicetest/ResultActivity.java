package org.pzois.uniwa.android.multiplechoicetest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DateFormat;
import java.util.Date;

public class ResultActivity extends AppCompatActivity {

    private TextView tvScore;
    private Button btRestart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvScore = findViewById(R.id.TvScore);
        btRestart = findViewById(R.id.BtRestart);

        // Get extras using AppConstants
        Intent i = getIntent();
        String username = i.getStringExtra(AppConstants.EXTRA_USERNAME);
        int score = i.getIntExtra(AppConstants.EXTRA_SCORE, 0);
        int total = i.getIntExtra(AppConstants.EXTRA_TOTAL, 0);
        long timestamp = i.getLongExtra(AppConstants.EXTRA_TIMESTAMP, System.currentTimeMillis());

        String dateStr = DateFormat.getDateTimeInstance().format(new Date(timestamp));

        String resultText =
                "Χρήστης: " + (username == null ? "-" : username) + "\n" +
                        "Σκορ: " + score + " / " + total + "\n" +
                        "Ώρα: " + dateStr;

        tvScore.setText(resultText);

        btRestart.setOnClickListener(v -> {
            Intent restart = new Intent(ResultActivity.this, LoginActivity.class);
            // καθαρίζει το back stack ώστε να μη γυρνάει πίσω στο Test
            restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(restart);
            finish();
        });
    }
}