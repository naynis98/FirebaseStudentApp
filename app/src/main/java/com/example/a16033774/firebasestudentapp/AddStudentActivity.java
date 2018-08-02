package com.example.a16033774.firebasestudentapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddStudentActivity extends AppCompatActivity {
    private static final String TAG = "AddStudentActivity";

    private EditText etName, etAge;
    private Button btnAdd;

    // TODO: Task 1 - Declare Firebase variables
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference studentListRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        etName = (EditText)findViewById(R.id.editTextName);
        etAge = (EditText)findViewById(R.id.editTextAge);
        btnAdd = (Button)findViewById(R.id.buttonAdd);

        // TODO: Task 2: Get Firebase database instance and reference
        firebaseDatabase = FirebaseDatabase.getInstance();
        studentListRef = firebaseDatabase.getReference("/studentList");

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Task 3: Add student to database and go back to main screen
                String name = etName.getText().toString();
                int age = Integer.parseInt(etAge.getText().toString());
                Student student = new Student(name, age);

                studentListRef.push().setValue(student);

                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);

            }
        });

    }
}
