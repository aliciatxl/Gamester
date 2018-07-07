package com.example.tansuyee.android1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText email, password;
    ProgressBar progressBar;
    boolean isSignUp = true;
    String emailString;
    String passwordString;

    private FirebaseAuth mAuth;

    private void logInUser() {
        emailString = email.getText().toString();
        passwordString = password.getText().toString();
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerUser() {
        emailString = email.getText().toString();
        passwordString = password.getText().toString();
        if (emailString.isEmpty()) {
            email.setError("Email is required");
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            email.setError("Please enter a valid email");
            email.requestFocus();
            return;
        }

        if (passwordString.isEmpty()) {
            password.setError("Password is required");
            password.requestFocus();
            return;
        }

        if (passwordString.length() < 6) {
            password.setError("Minimum length of password should be 6");
            password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(emailString, passwordString)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(MainActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication failed. " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            Log.i("Error", task.getException().getMessage());
                        }

                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.buttonSignUp).setOnClickListener(this);
        findViewById(R.id.textViewLogIn).setOnClickListener(this);
        findViewById(R.id.relativeLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (getCurrentFocus() != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSignUp:
                if (isSignUp) {
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                    //registerUser();
                } else {
                    logInUser();
                }
                break;
            case R.id.textViewLogIn:
                TextView textView = findViewById(R.id.textViewLogIn);
                TextView message = findViewById(R.id.message);
                Button button = findViewById(R.id.buttonSignUp);
                if (isSignUp) {
                    textView.setText(getResources().getString(R.string.sign_up));
                    message.setText(getResources().getString(R.string.no_account));
                    button.setText(getResources().getString(R.string.log_in));
                    isSignUp = false;
                } else {
                    textView.setText(getResources().getString(R.string.log_in));
                    message.setText(getResources().getString(R.string.have_account));
                    button.setText(getResources().getString(R.string.sign_up));
                    isSignUp = true;
                }
        }
    }
}
