package org.pzois.uniwa.android.multiplechoicetest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.multiplechoicetest.Question;
import com.example.multiplechoicetest.QuestionBank;
import com.example.multiplechoicetest.QuizEngine;

import java.util.List;

public class TestActivity extends AppCompatActivity {

    private String username;
    private QuizEngine engine;
    private QuestionBank db;

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

        username = getIntent().getStringExtra(AppConstants.EXTRA_USERNAME);

        bindViews();
        bindEvents();

        // Role B Initialization
        db = new QuestionBank(this);
        List<Question> randomQuestions = db.getRandomQuestions(5);
        engine = new QuizEngine(username, randomQuestions);


        updateUI(); // Εμφάνιση 1ης ερώτησης
    }



    private void bindViews() {
        tvTimer = findViewById(R.id.TvTimer);
        tvQuestion = findViewById(R.id.TvQuestion);
        ivQuestion = findViewById(R.id.IvQuestion);
        choiceButtons = new Button[]{
                findViewById(R.id.BtChoice1), findViewById(R.id.BtChoice2),
                findViewById(R.id.BtChoice3), findViewById(R.id.BtChoice4),
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

    private void updateUI() {
        Question current = engine.getCurrentQuestion();
        if (current == null) return;

        tvQuestion.setText(current.getText());

        // ΔΙΑΧΕΙΡΙΣΗ ΕΙΚΟΝΑΣ (Απαίτηση Εκφώνησης)
        if (current.getImageResId() != 0) {
            ivQuestion.setImageResource(current.getImageResId());
            ivQuestion.setVisibility(View.VISIBLE);
        } else {
            ivQuestion.setVisibility(View.GONE);
        }

        List<String> options = current.getOptions();
        for (int i = 0; i < choiceButtons.length; i++) {
            if (i < options.size()) {
                choiceButtons[i].setText(options.get(i));
                choiceButtons[i].setVisibility(View.VISIBLE);
                choiceButtons[i].setEnabled(true);
                choiceButtons[i].setBackgroundColor(Color.parseColor("#6200EE"));
            } else {
                choiceButtons[i].setVisibility(View.GONE);
            }
        }
    }

    private void onAnswerSelected(int choiceIndex) {
        engine.submitAnswer(choiceIndex);

        int correctIndex = engine.getCurrentQuestion().getCorrectOptionIndex();

        for (int i = 0; i < choiceButtons.length; i++) {
            choiceButtons[i].setEnabled(false); // Κλειδώνουμε τα κουμπιά

            if (i == correctIndex) {
                choiceButtons[i].setBackgroundColor(Color.GREEN);
            } else if (i == choiceIndex) {
                choiceButtons[i].setBackgroundColor(Color.RED);
            }
        }
        btNext.setVisibility(View.VISIBLE);
    }

    private void onNext() {
        if (engine.nextQuestion()) {
            updateUI(); // Πάμε στην επόμενη ερώτηση
            btNext.setVisibility(View.INVISIBLE); // Κρύβουμε πάλι το κουμπί Next
        } else {
            finishQuiz(); // Τέλος quiz
        }
    }

    private void finishQuiz() {
        db.saveScore(username, engine.getScore());
        goToResult(engine.getScore(), engine.getTotalQuestions());
    }

    private void goToResult(int score, int total) {
        Intent i = new Intent(TestActivity.this, ResultActivity.class);
        i.putExtra(AppConstants.EXTRA_USERNAME, username);
        i.putExtra(AppConstants.EXTRA_SCORE, score);
        i.putExtra(AppConstants.EXTRA_TOTAL, total);
        startActivity(i);
        finish();
    }
}