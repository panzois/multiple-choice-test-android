package org.pzois.uniwa.android.multiplechoicetest;

public final class AppConstants {

    private AppConstants() {}

    // Intent extras (FIXED KEYS)
    public static final String EXTRA_USERNAME = "extra_username";
    public static final String EXTRA_SCORE = "extra_score";
    public static final String EXTRA_TOTAL = "extra_total";
    public static final String EXTRA_TIMESTAMP = "extra_timestamp";

    // Quiz settings (Integrator can tune)
    public static final int QUESTIONS_PER_TEST = 10;
    public static final long TOTAL_TIME = 5 * 60 * 1000L; // 5 λεπτά
}