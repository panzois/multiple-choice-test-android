package com.example.multiplechoicetest;

import java.util.ArrayList; // ΕΞΩ από την κλάση
import java.util.List;      // ΕΞΩ από την κλάση

public class Question {
    private String text;
    private List<String> options;
    private int correctOptionIndex;
    private int imageResId;

    // Constructor
    public Question(String text, List<String> options, int correctOptionIndex, int imageResId) {
        this.text = text;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
        this.imageResId = imageResId;
    }

    // Getters
    public String getText() { return text; }
    public List<String> getOptions() { return options; }
    public int getCorrectOptionIndex() { return correctOptionIndex; }
    public int getImageResId() { return imageResId; }
}
