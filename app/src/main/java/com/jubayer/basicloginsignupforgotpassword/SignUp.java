package com.jubayer.basicloginsignupforgotpassword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;

public class SignUp extends AppCompatActivity {
    Button haveAccount, reg_btn;
    private EditText regName, username, email, phoneNo, password;
    private FirebaseAuth mAuth;
    ProgressDialog dialog;
    FirebaseDatabase rootNode;
    DatabaseReference reference;


    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        haveAccount = findViewById(R.id.haveAccount);
        reg_btn = findViewById(R.id.reg_btn);
        mAuth = FirebaseAuth.getInstance();

        regName = findViewById(R.id.regName);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        phoneNo = findViewById(R.id.phoneNo);
        password = findViewById(R.id.password);

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, Login.class));
                Toast.makeText(SignUp.this, "Registration are completed.", Toast.LENGTH_SHORT).show();
                registarUser();
            }
        });

        haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
            }
        });
    }

    private void registarUser() {
        String name, userName, Email, phoneNumber, pass;
        name = regName.getText().toString().trim();
        userName = username.getText().toString().trim();
        Email = email.getText().toString().trim();
        phoneNumber = phoneNo.getText().toString().trim();
        pass = password.getText().toString().trim();


        if (name.isEmpty()) {
            regName.setError("Name is required");
            regName.requestFocus();
            return;
        }
        if (userName.isEmpty()) {
            username.setError("User name is required");
            username.requestFocus();
            return;
        }

        if (Email.isEmpty()) {
            email.setError("E-mail is required");
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            email.setError("Please provide valid email!");
            email.requestFocus();
            return;
        }
        if (phoneNumber.isEmpty()) {
            phoneNo.setError("User name is required");
            phoneNo.requestFocus();
            return;
        }

        if (pass.isEmpty()) {
            password.setError("Password is required");
            password.requestFocus();
            return;
        }

        if (pass.length() <= 12) {
            password.setError("Min password length should be 6 character");
            password.requestFocus();
            return;
        }
        dialog = new ProgressDialog(SignUp.this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading....");

        mAuth.createUserWithEmailAndPassword(Email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            UserModel user = new UserModel(name, userName, Email, phoneNumber, pass);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                dialog = new ProgressDialog(SignUp.this);
                                                dialog.setCancelable(false);
                                                dialog.setMessage("Loading....");
                                                Toast.makeText(SignUp.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(SignUp.this, MainActivity.class));
                                                finish();

                                            } else {

                                                dialog = new ProgressDialog(SignUp.this);
                                                dialog.setCancelable(false);
                                                dialog.setMessage("Loading....");
                                                Toast.makeText(SignUp.this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });

                        }
                    }
                });

    }

    private Boolean validateName(){
        String val = regName.getText().toString();

        if (val.isEmpty()){
            regName.setError("Field can not be empty !");
            return  false;
        } else {
            regName.setError(null);
            regName.setEnabled(false);
            return true;
        }
    }
    private Boolean validateUserName(){
        String val = username.getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (val.isEmpty()){
            username.setError("Field can not be empty !");
            return  false;

        } else if (val.length()>=15) {
            username.setError("Username is too long");
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            username.setError("White Spaces are not allowed !");
                return false;
        } else {
            username.setError(null);
            return true;
        }
    }
    private Boolean validateEmail(){
        String val = email.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()){
            email.setError("Field is empty");
            return false;
        } else if (!val.matches(emailPattern)){
            email.setError("Invalid email ID or address");
            return false;
        } else {
            email.setError("null");
            return true;
        }
    }
    private Boolean validatePhoneNo(){
        String val = phoneNo.getText().toString();
        if (val.isEmpty()){
            phoneNo.setError("Field is empty");
            return false;
        } else {
            phoneNo.setError("null");
            return true;
        }
    }
    private Boolean validatePassword(){
        String val = password.getText().toString();
        String passwordVal = "^" +
                "(?=.*[a-zA-Z])" +
                "(?=.*[@#$%^&+=])" +
                "(?=\\S+$)" +
                ".{4,}" +
                "$";

        if (val.isEmpty()) {
           password.setError("Field cannot be Empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            password.setError("Password is too weak");
            return false;
        } else {
            password.setError(null);
            return true;
        }

    }

    // save data in firebase database
    public void registerUser(View view){
       /* DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("UserData");*/

        if (!validateName() | !validateUserName() | !validateEmail() | !validatePhoneNo() | !validatePassword()){
            return;
        }

        String name = regName.getText().toString();
        String userName = username.getText().toString();
        String userEmail = email.getText().toString();
        String phoneNumber = phoneNo.getText().toString();
        String Password = password.getText().toString();
        UserModel userModel = new UserModel(name, userName, userEmail, phoneNumber, Password);
        reference.child(userName).setValue(userModel);
    }
}