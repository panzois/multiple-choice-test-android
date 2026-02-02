package com.example.multiplechoicetest;

import java.util.List;

public class QuizEngine {
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private String playerName;

    // Ο Constructor παίρνει το όνομα και τη λίστα που θα φέρει ο Role A από την QuestionBank
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
        if (selectedIndex == getCurrentQuestion().getCorrectOptionIndex()) {
            score++;
        }
    }

    // Προχωράει στην επόμενη ερώτηση. Επιστρέφει false αν τελείωσαν οι ερωτήσεις.
    public boolean nextQuestion() {
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            return true;
        }
        return false;
    }

    public int getScore() { return score; }
    public int getTotalQuestions() { return questions.size(); }
    public String getPlayerName() { return playerName; }
    public int getCurrentNumber() { return currentQuestionIndex + 1; }
}