package org.pzois.uniwa.android.multiplechoicetest;

import android.content.Intent;
import android.database.Cursor; // Προστέθηκε
import android.os.Bundle;
import android.view.View; // Προστέθηκε
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog; // Προστέθηκε
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DateFormat;
import java.util.Date;

public class ResultActivity extends AppCompatActivity {
    QuestionBank dbHelper;
    private TextView tvScore;
    private Button btRestart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);

        dbHelper = new QuestionBank(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Σύνδεση Κουμπιού Ιστορικού
        Button btnHistory = findViewById(R.id.btnShowHistory);
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHistoryPopUp();
            }
        });

        tvScore = findViewById(R.id.TvScore);
        btRestart = findViewById(R.id.BtRestart);

        // Λήψη δεδομένων
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

        // Κουμπί Επανεκκίνησης
        btRestart.setOnClickListener(v -> {
            Intent restart = new Intent(ResultActivity.this, LoginActivity.class);
            restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(restart);
            finish();
        });

    } // <--- ΕΔΩ ΚΛΕΙΝΕΙ Η ONCREATE!

    // Η μέθοδος τώρα είναι ΕΞΩ από την onCreate, όπως πρέπει
    public void showHistoryPopUp() {
        Cursor cursor = dbHelper.getAllScores();
        StringBuilder builder = new StringBuilder();

        if (cursor != null && cursor.getCount() == 0) {
            builder.append("Δεν υπάρχουν αποθηκευμένα σκορ.");
        } else if (cursor != null) {
            while (cursor.moveToNext()) {
                builder.append("👤 ").append(cursor.getString(1)) // Username
                        .append("\n🏆 Σκορ: ").append(cursor.getInt(2))    // Score
                        .append("\n📅 ").append(cursor.getString(3)) // Date
                        .append("\n------------------\n");
            }
            cursor.close();
        }

        new AlertDialog.Builder(this)
                .setTitle("Ιστορικό Τεστ")
                .setMessage(builder.toString())
                .setPositiveButton("OK", null)
                .show();
    }
} 