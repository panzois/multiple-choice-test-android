package org.pzois.uniwa.android.multiplechoicetest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = "TestActivity";

    private String username;

    // UI
    private TextView tvTimer, tvQuestion;
    private ImageView ivQuestion;
    private Button[] choiceButtons;
    private Button btNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1) get username
        username = getIntent().getStringExtra(AppConstants.EXTRA_USERNAME);
        if (username == null || username.trim().isEmpty()) {
            Log.e(TAG, "Missing username");
            finish();
            return;
        }
        Log.d(TAG, "Username received: " + username);

        // 2) bind UI
        bindViews();
        bindEvents();

        // 3) placeholder (μέχρι να κουμπώσει engine)
        showPlaceholderQuestion();
    }

    private void bindViews() {
        tvTimer = findViewById(R.id.TvTimer);
        tvQuestion = findViewById(R.id.TvQuestion);
        ivQuestion = findViewById(R.id.IvQuestion);

        choiceButtons = new Button[]{
                findViewById(R.id.BtChoice1),
                findViewById(R.id.BtChoice2),
                findViewById(R.id.BtChoice3),
                findViewById(R.id.BtChoice4),
                findViewById(R.id.BtChoice5)
        };

        btNext = findViewById(R.id.BtNext);
    }

    private void bindEvents() {
        for (int i = 0; i < choiceButtons.length; i++) {
            final int index = i;
            choiceButtons[i].setOnClickListener(v -> onAnswerSelected(index));
        }

        btNext.setOnClickListener(v -> onNext());
    }

    private void showPlaceholderQuestion() {
        tvTimer.setText("Timer: --");
        tvQuestion.setText("Ερώτηση placeholder (θα έρθει από Quiz Engine)");

        // προς το παρόν κρύβουμε εικόνα αν δεν έχουμε
        ivQuestion.setImageDrawable(null);
        ivQuestion.setVisibility(ImageView.GONE);

        for (Button b : choiceButtons) {
            b.setText("Option");
            b.setEnabled(true);
            b.setVisibility(Button.VISIBLE);
        }
    }

    private void onAnswerSelected(int choiceIndex) {
        // TODO: εδώ ο George θα κουμπώσει engine.submitAnswer(choiceIndex)
        Toast.makeText(this, "Επέλεξες: " + (choiceIndex + 1), Toast.LENGTH_SHORT).show();
    }

    private void onNext() {
        // TODO: εδώ θα γίνει engine.nextQuestion() ή finish test
        goToResult(0, AppConstants.QUESTIONS_PER_TEST);
    }

    private void goToResult(int score, int total) {
        Intent i = new Intent(TestActivity.this, ResultActivity.class);
        i.putExtra(AppConstants.EXTRA_USERNAME, username);
        i.putExtra(AppConstants.EXTRA_SCORE, score);
        i.putExtra(AppConstants.EXTRA_TOTAL, total);
        i.putExtra(AppConstants.EXTRA_TIMESTAMP, System.currentTimeMillis());
        startActivity(i);
        finish();
    }
}