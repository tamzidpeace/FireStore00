package com.example.arafat.firestore00;


import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";

    private EditText titleEditText, descriptionEditText;
    private TextView loadNotesTextView;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference noteRef = db.collection("Notebook").document("My First Note");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        titleEditText = findViewById(R.id.title_edit_text);
        descriptionEditText = findViewById(R.id.description_edit_text);
        loadNotesTextView = findViewById(R.id.load_notes_text_view);
    }

    @Override
    protected void onStart() {
        super.onStart();

        noteRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                if (e != null) {
                    Toast.makeText(MainActivity.this, "error while loading", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onEvent: " + e.toString());
                }

                if (documentSnapshot.exists()) {

                    Notes notes = documentSnapshot.toObject(Notes.class);

                    String title = notes.getTitle();
                    String description = notes.getDescription();
                    loadNotesTextView.setText("Title: " + title + "\n" + "Description: " + description);
                } else {
                    loadNotesTextView.setText("");
                }

            }
        });
    }


    public void saveNote(View view) {

        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        //Toast.makeText(this, title + " " + description, Toast.LENGTH_SHORT).show();

        /*Map<String, Object> note = new HashMap<>();
        note.put(KEY_TITLE, title);
        note.put(KEY_DESCRIPTION, description);*/

        Notes note = new Notes(title, description);


        db.collection("Notebook").document("My First Note").set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    public void loadNotes(View view) {


        noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()) {

                            Notes notes = documentSnapshot.toObject(Notes.class);

                            String title = notes.getTitle();
                            String description = notes.getDescription();
                            loadNotesTextView.setText("Title: " + title + "\n" + "Description: " + description);

                        } else {
                            Toast.makeText(MainActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error: " + e, Toast.LENGTH_SHORT).show();

                    }
                });
    }


    public void updateDescription(View view) {

        String updateDesc = descriptionEditText.getText().toString().trim();

        Map<String, Object> desc = new HashMap<>();

        Notes notes = new Notes();
        desc.put(KEY_DESCRIPTION, updateDesc);
        noteRef.set(desc, SetOptions.merge());
    }

    public void deleteDescription(View view) {
        noteRef.update(KEY_DESCRIPTION, FieldValue.delete());
    }

    public void deleteNote(View view) {
        noteRef.delete();
    }
}
