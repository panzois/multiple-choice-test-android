package org.pzois.uniwa.android.multiplechoicetest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
<<<<<<< HEAD
=======
import android.view.View;
import android.widget.Button;
>>>>>>> 31c098850a955c9504180c383704a106e2b81c94
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import com.example.multiplechoicetest.Question;
import com.example.multiplechoicetest.QuestionBank;
import com.example.multiplechoicetest.QuizEngine;

import java.util.List;

public class TestActivity extends AppCompatActivity {

    private String username;
    private QuizEngine engine;
    private QuestionBank db;
<<<<<<< HEAD
=======

    private TextView tvTimer, tvQuestion;
    private ImageView ivQuestion;
    private Button[] choiceButtons;
    private Button btNext;
>>>>>>> 31c098850a955c9504180c383704a106e2b81c94

    private TextView tvQuestion;
    private ImageView ivQuestion;
    private MaterialButton btNext;

    private MaterialButton btChoice1, btChoice2, btChoice3, btChoice4, btChoice5;

    private Question currentQuestion;
    private int selectedAnswer = -1;

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

        // --- Username ---
        Intent intent = getIntent();
        username = intent.getStringExtra(AppConstants.EXTRA_USERNAME);

        if (username == null || username.trim().isEmpty()) {
            Log.e("TestActivity", "Username missing");
            finish();
            return;
        }

        // --- Bind UI ---
        tvQuestion = findViewById(R.id.TvQuestion);
        ivQuestion = findViewById(R.id.IvQuestion);

        btChoice1 = findViewById(R.id.BtChoice1);
        btChoice2 = findViewById(R.id.BtChoice2);
        btChoice3 = findViewById(R.id.BtChoice3);
        btChoice4 = findViewById(R.id.BtChoice4);
        btChoice5 = findViewById(R.id.BtChoice5);
        btNext    = findViewById(R.id.BtNext);

<<<<<<< HEAD
        // --- Init DB & Engine ---
        db = new QuestionBank(this);
        List<Question> questions = db.getRandomQuestions(10); // π.χ. 10 ερωτήσεις
        engine = new QuizEngine(username, questions);

        // --- Choice listeners ---
        btChoice1.setOnClickListener(v -> selectAnswer(0, btChoice1));
        btChoice2.setOnClickListener(v -> selectAnswer(1, btChoice2));
        btChoice3.setOnClickListener(v -> selectAnswer(2, btChoice3));
        btChoice4.setOnClickListener(v -> selectAnswer(3, btChoice4));
        btChoice5.setOnClickListener(v -> selectAnswer(4, btChoice5));

        // --- Next ---
        btNext.setOnClickListener(v -> {
            if (selectedAnswer == -1) {
                Toast.makeText(this, "Διάλεξε απάντηση", Toast.LENGTH_SHORT).show();
                return;
            }

            engine.submitAnswer(selectedAnswer);

            if (engine.nextQuestion()) {
                loadQuestion();
            } else {
                goToResult();
            }
        });

        // Load first question
        loadQuestion();
=======
    private void goToResult(int score, int total) {
        Intent i = new Intent(TestActivity.this, ResultActivity.class);
        i.putExtra(AppConstants.EXTRA_USERNAME, username);
        i.putExtra(AppConstants.EXTRA_SCORE, score);
        i.putExtra(AppConstants.EXTRA_TOTAL, total);
        startActivity(i);
        finish();
>>>>>>> 31c098850a955c9504180c383704a106e2b81c94
    }

    private void loadQuestion() {
        currentQuestion = engine.getCurrentQuestion();

        if (currentQuestion == null) {
            goToResult();
            return;
        }

        tvQuestion.setText(currentQuestion.getText());

        List<String> opts = currentQuestion.getOptions();
        btChoice1.setText(opts.get(0));
        btChoice2.setText(opts.get(1));
        btChoice3.setText(opts.get(2));
        btChoice4.setText(opts.get(3));
        btChoice5.setText(opts.get(4));

        // Image handling
        if (currentQuestion.getImageResId() != 0) {
            ivQuestion.setImageResource(currentQuestion.getImageResId());
            ivQuestion.setVisibility(ImageView.VISIBLE);
        } else {
            ivQuestion.setVisibility(ImageView.GONE);
        }

        selectedAnswer = -1;
        clearSelection();
    }

    private void selectAnswer(int index, MaterialButton selected) {
        selectedAnswer = index;
        clearSelection();
        selected.setSelected(true);
    }
<<<<<<< HEAD

    private void clearSelection() {
        btChoice1.setSelected(false);
        btChoice2.setSelected(false);
        btChoice3.setSelected(false);
        btChoice4.setSelected(false);
        btChoice5.setSelected(false);
    }

    private void goToResult() {
        db.saveScore(username, engine.getScore());

        Intent i = new Intent(this, ResultActivity.class);
        i.putExtra(AppConstants.EXTRA_USERNAME, username);
        i.putExtra(AppConstants.EXTRA_SCORE, engine.getScore());
        i.putExtra(AppConstants.EXTRA_TOTAL, engine.getTotalQuestions());
        startActivity(i);
        finish();
    }
}
=======
}
>>>>>>> 31c098850a955c9504180c383704a106e2b81c94
