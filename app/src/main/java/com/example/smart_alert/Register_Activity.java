package com.example.smart_alert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register_Activity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button register;
    private SQLiteDatabase DB;
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapterItems;
    private String roleType="Citizen";
    private FirebaseAuth auth;
    private DatabaseReference mDatabaseRef;
    private String firebaseRealtimeDatabaseInstance= "https://smart-alert-f5055-default-rtdb.europe-west1.firebasedatabase.app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         Resources res = getResources();
         String[] roles = res.getStringArray(R.array.roles_array);
        setContentView(R.layout.activity_register);

        //link with view
        email= findViewById(R.id.email);
        password= findViewById(R.id.password);
        register= findViewById(R.id.register);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView3);
        auth = FirebaseAuth.getInstance();

        // Create realtime database instance and get "UserRoles" branch
        mDatabaseRef = FirebaseDatabase.getInstance(firebaseRealtimeDatabaseInstance).getReference("UserRoles");
        //Create sqlLite
        DB = openOrCreateDatabase("Userdata.db", MODE_PRIVATE, null);
        DB.execSQL("Create table if not exists Userdetails(" +
                "email TEXT primary key," +
                "subscribed TEXT)");
        //dropdown creation
        adapterItems = new ArrayAdapter<String>(this,R.layout.list_item,roles);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                roleType = item;
                Toast.makeText(Register_Activity.this,"Item: "+ item,Toast.LENGTH_SHORT).show();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                // if email and password are valid ,the registerUser method will be invoked
                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(Register_Activity.this, "Empty Credentials", Toast.LENGTH_SHORT).show();
                }else if(txt_password.length() < 4){
                    Toast.makeText(Register_Activity.this, "Password must contain at least 5 characters", Toast.LENGTH_SHORT).show();
                }else{
                    registerUser(txt_email,txt_password,roleType);
                }
            }
        });


    }



    // User gets registered in firebase, and firebase reports if the registration was successful
    private void registerUser(String email, String password, String role) {
        User user = new User(email,role);
        String uploadID = mDatabaseRef.push().getKey();
        mDatabaseRef.child(uploadID).setValue(user);
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    DbHelper dbHelper = new DbHelper(DB);
                    dbHelper.insertToLocalDB(email,"false");
                    Toast.makeText(Register_Activity.this, "Your registration was successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Register_Activity.this, Start_Activity.class));
                    finish(); //when the user clicks the "back" button he will not be able to return to this activity
                }else{
                    Toast.makeText(Register_Activity.this, "Your registration has failed", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}