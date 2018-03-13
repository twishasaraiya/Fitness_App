package com.example.FirstApp2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "EmailPasssword";
    private EditText EmailField;
    private EditText PasswordField;

    private GoogleApiClient myGoogleAPIClient;
    private FirebaseAuth mAuth;

    @Override
    public void onStart(){
        super.onStart();
        //Check if user is already signedIn
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        EmailField = findViewById(R.id.field_email);
        PasswordField = findViewById(R.id.field_password);

        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.email_sign_up_button).setOnClickListener(this);
        findViewById(R.id.google_sign_in_button).setOnClickListener(this);

        //configure google sign-in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //myGoogleAPIClient = GoogleSignIn.getClient(this.gso);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if( i == R.id.email_sign_in_button){
            //log in using email password
            signIn(EmailField.getText().toString(),PasswordField.getText().toString());
        }else if( i == R.id.email_sign_up_button){
            //sign up using email and password.ie create new account
            Log.d(TAG,"Signup button clicked");
            createAccount(EmailField.getText().toString(),PasswordField.getText().toString());
        }
    }

    public void createAccount(String email,String password){
        Log.d(TAG,"createAccount:"+email);
        if(!validateForm()){
            return;
        }
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this,new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        if(task.isSuccessful()){
                            Toast.makeText(SignupActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }else{
                            Toast.makeText(SignupActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signIn(String email,String password){
        Log.d(TAG,"signIn:"+email);
        if(!validateForm()){
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        if(task.isSuccessful()){
                            //Sign In Success
                            Toast.makeText(SignupActivity.this, "Signed In Successfully", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }else{
                            Toast.makeText(SignupActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user){
        if(user != null){
            //change activity
            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private boolean validateForm(){
        boolean valid =true;
        String email = EmailField.getText().toString();
        if(TextUtils.isEmpty(email)){
            EmailField.setError("Required");
            valid = false;
        }else{
            PasswordField.setError(null);
        }
        return valid;
    }
}
