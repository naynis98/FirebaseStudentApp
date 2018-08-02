package com.example.a16033774.firebasestudentapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView lvStudent;
    private ArrayList<Student> alStudent;
    private ArrayAdapter<Student> aaStudent;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference studentListRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvStudent = (ListView)findViewById(R.id.listViewStudents);
        alStudent = new ArrayList<Student>();
        aaStudent = new ArrayAdapter<Student>(this, android.R.layout.simple_list_item_1, alStudent);
        lvStudent.setAdapter(aaStudent);

        firebaseDatabase = FirebaseDatabase.getInstance();
        studentListRef = firebaseDatabase.getReference("studentList");

        studentListRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i("MainActivity", "onChildAdded()");
                Student student = dataSnapshot.getValue(Student.class);
                if (student != null) {
                    student.setId(dataSnapshot.getKey());

                    alStudent.add(student);
                    aaStudent.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i("MainActivity", "onChildChanged()");
                String selectedId = dataSnapshot.getKey();
                Student student = dataSnapshot.getValue(Student.class);
                if (student != null) {
                    for (int i = 0; i < alStudent.size(); i++) {
                        if (alStudent.get(i).getId().equals(selectedId)) {
                            student.setId(selectedId);
                            alStudent.set(i, student);
                            break;
                        }
                    }
                    aaStudent.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.i("MainActivity", "onChildRemoved()");
                String selectedId = dataSnapshot.getKey();
                for(int i= 0; i < alStudent.size(); i++) {
                    if (alStudent.get(i).getId().equals(selectedId)) {
                        alStudent.remove(i);
                        break;
                    }
                }
                aaStudent.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i("MainActivity", "onChildMoved()");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MainActivity", "Database error occurred", databaseError.toException());
            }
        });

        lvStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Student student = alStudent.get(i);  // Get the selected Student
                Intent intent = new Intent(MainActivity.this, StudentDetailsActivity.class);
                intent.putExtra("Student", student);
                startActivityForResult(intent, 1);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.addStudent) {

            Intent intent = new Intent(getApplicationContext(), AddStudentActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

