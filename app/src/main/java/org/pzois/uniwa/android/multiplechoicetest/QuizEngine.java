package org.pzois.uniwa.android.multiplechoicetest;

import java.util.List;

public class QuizEngine {

    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private String playerName;

    // Constructor
    public QuizEngine(String playerName, List<Question> questions) {
        this.playerName = playerName;
        this.questions = questions;
    }

    public Question getCurrentQuestion() {
        if (currentQuestionIndex < questions.size()) {
            return questions.get(currentQuestionIndex);
        }
        return null;
    }

    // Ελέγχει την απάντηση και αυξάνει το σκορ
    public void submitAnswer(int selectedIndex) {
        if (getCurrentQuestion() != null &&
                selectedIndex == getCurrentQuestion().getCorrectOptionIndex()) {
            score++;
        }
    }

    // Πάει στην επόμενη ερώτηση
    public boolean nextQuestion() {
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            return true;
        }
        return false;
    }

    // Χρήσιμο για το κουμπί "ΤΕΛΟΣ"
    public boolean isLastQuestion() {
        return currentQuestionIndex == questions.size() - 1;
    }

    // ===== Getters =====
    public int getScore() {
        return score;
    }

    public int getTotalQuestions() {
        return questions.size();
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getCurrentNumber() {
        return currentQuestionIndex + 1;
    }
}