package org.pzois.uniwa.android.multiplechoicetest;
import org.pzois.uniwa.android.multiplechoicetest.R;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class QuestionBank extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "QuizDatabase.db";
    private static final int DATABASE_VERSION = 13; // Ανέβασε το σε 10 για να πάρει τις 15 ερωτήσεις

    public QuestionBank(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE questions (id INTEGER PRIMARY KEY AUTOINCREMENT, text TEXT, image_id INTEGER, opt1 TEXT, opt2 TEXT, opt3 TEXT, opt4 TEXT, opt5 TEXT, correct INTEGER)");
        db.execSQL("CREATE TABLE results (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, score INTEGER, date TEXT)");
        insertInitialQuestions(db);
    }

        private void insertInitialQuestions(SQLiteDatabase db) {
            addQuestion(db, "Πότε ξεκίνησε η Ελληνική Επανάσταση;", R.drawable.q_greek_revolution, "1821", "1940", "1453", "1204", "1830", 0);
            addQuestion(db, "Ποια είναι η πρωτεύουσα της Ελλάδας;", R.drawable.q_greek_capital, "Θεσσαλονίκη", "Αθήνα", "Πάτρα", "Λάρισα", "Ηράκλειο", 1);
            addQuestion(db, "Ποιος ζωγράφος εικονίζεται στην εικόνα;", R.drawable.q_van_gogh, "Πικάσο", "Βαν Γκογκ", "Νταλί", "Μονέ", "Ρέμπραντ", 1);
            addQuestion(db, "Ποιο είναι το χημικό σύμβολο του νερού;", R.drawable.q_water, "CO2", "O2", "H2O", "NaCl", "CH4", 2);
            addQuestion(db, "Ποιος πλανήτης ονομάζεται 'Κόκκινος Πλανήτης';", R.drawable.q_mars, "Αφροδίτη", "Δίας", "Άρης", "Κρόνος", "Ερμής", 2);
            addQuestion(db, "Πόσες πλευρές έχει ένα κανονικό εξάγωνο;", R.drawable.q_hexagon, "5", "6", "7", "8", "4", 1);
            addQuestion(db, "Ποια εταιρεία δημιούργησε το λειτουργικό Android;", R.drawable.q_android, "Apple", "Microsoft", "Google", "Nokia", "Samsung", 2);
            addQuestion(db, "Ποιο όργανο του σώματος αντλεί το αίμα;", R.drawable.q_human_organs, "Πνεύμονας", "Καρδιά", "Συκώτι", "Στομάχι", "Νεφρός", 1);
            addQuestion(db, "Ποιος ήταν ο βασιλιάς των θεών στον Όλυμπο;", R.drawable.q_zeus, "Ποσειδώνας", "Άρης", "Δίας", "Απόλλωνας", "Ήφαιστος", 2);
            addQuestion(db, "Ποιος διατύπωσε τη θεωρία της σχετικότητας;", R.drawable.q_emc, "Νεύτωνας", "Αϊνστάιν", "Γαλιλαίος", "Τέσλα", "Κιουρί", 1);
            addQuestion(db, "Σε ποια πόλη έγιναν οι πρώτοι σύγχρονοι Ολυμπιακοί Αγώνες;", R.drawable.q_olympic_games, "Παρίσι", "Λονδίνο", "Αθήνα", "Ρώμη", "Βερολίνο", 2);
            addQuestion(db, "Ποιος έγραψε το έπος 'Οδύσσεια';", R.drawable.q_omirus, "Όμηρος", "Ησίοδος", "Σοφοκλής", "Ευριπίδης", "Αριστοφάνης", 0);
            addQuestion(db, "Ποιο είναι το μεγαλύτερο θηλαστικό στον κόσμο;", R.drawable.q_mammals, "Ελέφαντας", "Γαλάζια Φάλαινα", "Καμηλοπάρδαλη", "Λιοντάρι", "Καρχαρίας", 1);
            addQuestion(db, "Ποιο είναι το άθροισμα των γωνιών ενός τριγώνου;", R.drawable.q_triangle, "90", "180", "270", "360", "120", 1);
            addQuestion(db, "Πού βρίσκεται ο Λευκός Πύργος;", R.drawable.q_thesaloniki, "Αθήνα", "Θεσσαλονίκη", "Ναύπλιο", "Χανιά", "Βόλος", 1);
        }

    private void addQuestion(SQLiteDatabase db, String txt, int img, String o1, String o2, String o3, String o4, String o5, int target) {
        ContentValues v = new ContentValues();
        v.put("text", txt);
        v.put("image_id", img);
        v.put("opt1", o1); v.put("opt2", o2); v.put("opt3", o3); v.put("opt4", o4); v.put("opt5", o5);
        v.put("correct", target);
        db.insert("questions", null, v);
    }

    public List<Question> getRandomQuestions(int count) {
        List<Question> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM questions ORDER BY RANDOM() LIMIT " + count, null);
        if (c != null && c.moveToFirst()) {
            do {
                try {
                    String text = c.getString(c.getColumnIndexOrThrow("text"));
                    int imgId = c.getInt(c.getColumnIndexOrThrow("image_id"));
                    int correct = c.getInt(c.getColumnIndexOrThrow("correct"));
                    List<String> opts = new ArrayList<>();
                    opts.add(c.getString(c.getColumnIndexOrThrow("opt1")));
                    opts.add(c.getString(c.getColumnIndexOrThrow("opt2")));
                    opts.add(c.getString(c.getColumnIndexOrThrow("opt3")));
                    opts.add(c.getString(c.getColumnIndexOrThrow("opt4")));
                    opts.add(c.getString(c.getColumnIndexOrThrow("opt5")));
                    list.add(new Question(text, opts, correct, imgId));
                } catch (Exception e) {
                    android.util.Log.e("SQLiteError", "Error reading: " + e.getMessage());
                }
            } while (c.moveToNext());
            c.close();
        }
        return list;
    }

    public void saveScore(String name, int score) {
        String currentTime = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault()).format(new java.util.Date());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("username", name);
        v.put("score", score);
        v.put("date", currentTime);
        db.insert("results", null, v);
    }

    public android.database.Cursor getAllScores() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM results ORDER BY id DESC", null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old, int newV) {
        db.execSQL("DROP TABLE IF EXISTS questions");
        db.execSQL("DROP TABLE IF EXISTS results");
        onCreate(db);
    }
}