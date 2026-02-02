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
import com.google.android.material.button.MaterialButton;
import android.content.res.ColorStateList;

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

    private MaterialButton BtChoice1, BtChoice2, BtChoice3, BtChoice4, BtChoice5;

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

        Intent intent = getIntent();
        username = intent.getStringExtra(AppConstants.EXTRA_USERNAME);

        if (username == null || username.trim().isEmpty()) {
            Log.e("TestActivity", "Missing username");
            finish();
            return;
        }

        // Bind buttons (IDs μένουν ίδια)
        BtChoice1 = findViewById(R.id.BtChoice1);
        BtChoice2 = findViewById(R.id.BtChoice2);
        BtChoice3 = findViewById(R.id.BtChoice3);
        BtChoice4 = findViewById(R.id.BtChoice4);
        BtChoice5 = findViewById(R.id.BtChoice5);

        // Click listeners: selected state
        BtChoice1.setOnClickListener(v -> setSelectedChoice(BtChoice1, BtChoice1, BtChoice2, BtChoice3, BtChoice4, BtChoice5));
        BtChoice2.setOnClickListener(v -> setSelectedChoice(BtChoice2, BtChoice1, BtChoice2, BtChoice3, BtChoice4, BtChoice5));
        BtChoice3.setOnClickListener(v -> setSelectedChoice(BtChoice3, BtChoice1, BtChoice2, BtChoice3, BtChoice4, BtChoice5));
        BtChoice4.setOnClickListener(v -> setSelectedChoice(BtChoice4, BtChoice1, BtChoice2, BtChoice3, BtChoice4, BtChoice5));
        BtChoice5.setOnClickListener(v -> setSelectedChoice(BtChoice5, BtChoice1, BtChoice2, BtChoice3, BtChoice4, BtChoice5));

    private void goToResult(int score, int total) {
        Intent i = new Intent(TestActivity.this, ResultActivity.class);
        i.putExtra(AppConstants.EXTRA_USERNAME, username);
        i.putExtra(AppConstants.EXTRA_SCORE, score);
        i.putExtra(AppConstants.EXTRA_TOTAL, total);
        startActivity(i);
        finish();
    }

    private void setSelectedChoice(MaterialButton selected, MaterialButton... all) {
        for (MaterialButton b : all) b.setSelected(false);
        selected.setSelected(true);
    }
}
