package org.pzois.uniwa.android.multiplechoicetest;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.Locale;

public class TestActivity extends AppCompatActivity {

    private String username;

    private QuizEngine engine;
    private QuestionBank db;

    private TextView tvQuestion, tvTimer, tvStep;
    private ImageView ivQuestion;
    private ProgressBar pbTimer;

    private MaterialButton btBack, btNext;
    private MaterialButton btChoice1, btChoice2, btChoice3, btChoice4, btChoice5;

    private Question currentQuestion;
    private int selectedAnswer = -1;

    private CountDownTimer countDownTimer;
    private MediaPlayer bgPlayer;
    private MediaPlayer finishPlayer;

    // ===== ΣΥΝΟΛΙΚΟΣ ΧΡΟΝΟΣ TEST =====
    private static final long TOTAL_TIME = 5 * 60 * 1000; // 5 λεπτά

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

        // ===== USERNAME =====
        Intent intent = getIntent();
        username = intent.getStringExtra(AppConstants.EXTRA_USERNAME);

        if (username == null || username.trim().isEmpty()) {
            Log.e("TestActivity", "Username missing");
            finish();
            return;
        }

        // ===== BIND UI =====
        tvTimer    = findViewById(R.id.TvTimer);
        tvStep     = findViewById(R.id.TvStep);
        tvQuestion = findViewById(R.id.TvQuestion);
        ivQuestion = findViewById(R.id.IvQuestion);
        pbTimer    = findViewById(R.id.PbTimer);

        btBack = findViewById(R.id.BtBack);
        btNext = findViewById(R.id.BtNext);

        btChoice1 = findViewById(R.id.BtChoice1);
        btChoice2 = findViewById(R.id.BtChoice2);
        btChoice3 = findViewById(R.id.BtChoice3);
        btChoice4 = findViewById(R.id.BtChoice4);
        btChoice5 = findViewById(R.id.BtChoice5);

        // ===== DB & ENGINE =====
        db = new QuestionBank(this);
        List<Question> questions = db.getRandomQuestions(10);
        engine = new QuizEngine(username, questions);

        // ===== CHOICE LISTENERS =====
        btChoice1.setOnClickListener(v -> selectAnswer(0, btChoice1));
        btChoice2.setOnClickListener(v -> selectAnswer(1, btChoice2));
        btChoice3.setOnClickListener(v -> selectAnswer(2, btChoice3));
        btChoice4.setOnClickListener(v -> selectAnswer(3, btChoice4));
        btChoice5.setOnClickListener(v -> selectAnswer(4, btChoice5));

        // ===== NEXT =====
        btNext.setOnClickListener(v -> {
            if (selectedAnswer == -1) {
                Toast.makeText(this, "Διάλεξε απάντηση", Toast.LENGTH_SHORT).show();
                return;
            }

            engine.submitAnswer(selectedAnswer);

            if (engine.nextQuestion()) {
                loadQuestion();
            } else {
                // 🔔 Play finish sound
                if (finishPlayer != null) {
                    finishPlayer.start();
                }

                // ⏳ Μικρή καθυστέρηση για να ακουστεί ο ήχος
                new android.os.Handler().postDelayed(this::goToResult, 400);
                }
        });

        // ===== BACK (μόνο αν δεν έχει απαντηθεί) =====
        btBack.setOnClickListener(v -> {
            if (selectedAnswer != -1) {
                Toast.makeText(this,
                        "Δεν μπορείς να γυρίσεις πίσω αφού απαντήσεις",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (engine.previousQuestion()) {
                loadQuestion();
            }
        });

        // ===== BACKGROUND MUSIC =====
        bgPlayer = MediaPlayer.create(this, R.raw.quiz_bg);
        bgPlayer.setLooping(true);
        bgPlayer.setVolume(0.15f, 0.15f);
        bgPlayer.start();

        // ===== finish test sound =====
        finishPlayer = MediaPlayer.create(this, R.raw.finish_sound);

        // ===== LOAD FIRST QUESTION =====
        loadQuestion();

        // ===== START GLOBAL TIMER =====
        startGlobalTimer();
    }

    private void loadQuestion() {
        currentQuestion = engine.getCurrentQuestion();

        if (currentQuestion == null) {
            goToResult();
            return;
        }

        // Step (dynamic)
        tvStep.setText(String.format(
                Locale.getDefault(),
                "Ερώτηση %d/%d",
                engine.getCurrentNumber(),
                engine.getTotalQuestions()
        ));

        tvQuestion.setText(currentQuestion.getText());

        List<String> opts = currentQuestion.getOptions();
        btChoice1.setText(opts.get(0));
        btChoice2.setText(opts.get(1));
        btChoice3.setText(opts.get(2));
        btChoice4.setText(opts.get(3));
        btChoice5.setText(opts.get(4));

        if (currentQuestion.getImageResId() != 0) {
            ivQuestion.setVisibility(ImageView.VISIBLE);
            ivQuestion.setImageResource(currentQuestion.getImageResId());
        } else {
            ivQuestion.setVisibility(ImageView.GONE);
        }

        selectedAnswer = -1;
        clearSelection();

        // ===== epomeno allazei se telos =====
        if (engine.isLastQuestion()) {
            btNext.setText("ΤΕΛΟΣ");
        } else {
            btNext.setText("ΕΠΟΜΕΝΟ");
        }
    }

    // ===== GLOBAL TIMER =====
    private void startGlobalTimer() {
        pbTimer.setMax((int) TOTAL_TIME);
        pbTimer.setProgress((int) TOTAL_TIME);

        countDownTimer = new CountDownTimer(TOTAL_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                pbTimer.setProgress((int) millisUntilFinished);

                long sec = millisUntilFinished / 1000;
                long min = sec / 60;
                long s   = sec % 60;

                tvTimer.setText(String.format(
                        Locale.getDefault(),
                        "⏱ %02d:%02d", min, s
                ));
            }

            @Override
            public void onFinish() {
                Toast.makeText(TestActivity.this,
                        "Τέλος χρόνου!", Toast.LENGTH_LONG).show();
                goToResult();
            }
        }.start();
    }

    private void selectAnswer(int index, MaterialButton selected) {
        selectedAnswer = index;
        clearSelection();
        selected.setSelected(true);
    }

    private void clearSelection() {
        btChoice1.setSelected(false);
        btChoice2.setSelected(false);
        btChoice3.setSelected(false);
        btChoice4.setSelected(false);
        btChoice5.setSelected(false);
    }

    private void goToResult() {
        if (countDownTimer != null) countDownTimer.cancel();

        db.saveScore(username, engine.getScore());

        Intent i = new Intent(this, ResultActivity.class);
        i.putExtra(AppConstants.EXTRA_USERNAME, username);
        i.putExtra(AppConstants.EXTRA_SCORE, engine.getScore());
        i.putExtra(AppConstants.EXTRA_TOTAL, engine.getTotalQuestions());
        startActivity(i);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (bgPlayer != null && bgPlayer.isPlaying()) bgPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bgPlayer != null) bgPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bgPlayer != null) {
            bgPlayer.release();
            bgPlayer = null;
        }

        if (finishPlayer != null) {
            finishPlayer.release();
            finishPlayer = null;
        }
    }
}