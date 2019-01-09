package com.example.arafat.firestore00;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private EditText titleEditText, descriptionEditText, priorityEditText;
    private TextView loadNotesTextView;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference noteRef = db.collection("Notebook").document("My First Note");
    private CollectionReference notebookRef = db.collection("Notebook");
    private DocumentSnapshot lastResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        titleEditText = findViewById(R.id.title_edit_text);
        descriptionEditText = findViewById(R.id.description_edit_text);
        priorityEditText = findViewById(R.id.priority_edit_text);
        loadNotesTextView = findViewById(R.id.load_notes_text_view);
    }

   /* @Override
    protected void onStart() {
        super.onStart();

        notebookRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {

                String data = "";

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                    Notes notes = documentSnapshot.toObject(Notes.class);
                    notes.setDocumentID(documentSnapshot.getId());

                    String documentID = notes.getDocumentID();
                    String title = notes.getTitle();
                    String desc = notes.getDescription();
                    int priority = notes.getPriority();

                    data = data + ("DocumentID: " + documentID + "\nTitle: " + title + "\nDescription: " + desc +
                            "\nPriority: " + priority + "\n\n");

                }

                loadNotesTextView.setText(data);

            }
        });
    }*/


    public void saveNote(View view) {

        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        if (priorityEditText.length() == 0) {
            priorityEditText.setText("0");
        }

        int priority = Integer.parseInt(priorityEditText.getText().toString());

        Notes note = new Notes(title, description, priority);


        notebookRef.add(note);

    }


    public void loadNotes(View view) {
        Query query;

        if (lastResult == null) {
            query = notebookRef.orderBy("priority")
                    .limit(3);
        } else {
            query = notebookRef.orderBy("priority")
                    .startAfter(lastResult)
                    .limit(3);
        }
        query
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                            Notes notes = documentSnapshot.toObject(Notes.class);
                            notes.setDocumentID(documentSnapshot.getId());

                            String documentID = notes.getDocumentID();
                            String title = notes.getTitle();
                            String desc = notes.getDescription();
                            int priority = notes.getPriority();

                            data = data + ("DocumentID: " + documentID + "\nTitle: " + title + "\nDescription: " + desc +
                                    "\nPriority: " + priority + "\n\n");

                        }

                        if (queryDocumentSnapshots.size() > 0) {
                            data += "____________\n\n";
                            loadNotesTextView.append(data);

                            lastResult = queryDocumentSnapshots.getDocuments()
                                    .get(queryDocumentSnapshots.size() - 1);

                        }

                    }
                });
    }


    // this three method don't work now. because I remove these functionality
   /* public void updateDescription(View view) {

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
    }*/
}
