package com.example.FirstApp2;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "EmailPasssword";
    private EditText EmailField;
    private EditText PasswordField;
    private SignInButton mSignInButton;
    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    @Override
    public void onStart(){
        super.onStart();
        //Check if user is already signedIn
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null) {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            updateUI(account);
        }
        if(currentUser != null) {
            updateUI(currentUser);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        EmailField = findViewById(R.id.field_email);
        PasswordField = findViewById(R.id.field_password);
        mSignInButton = findViewById(R.id.google_sign_in_button);
        mSignInButton.setSize(SignInButton.SIZE_WIDE);

        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.email_sign_up_button).setOnClickListener(this);
        findViewById(R.id.google_sign_in_button).setOnClickListener(this);

        //Check online connectivity
        if(isOnline()){
            //configure google sign-in
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        }else{
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this)
                    .setTitle("No Internet Connection")
                    .setMessage("Check your internet Connectivity")
                    .setIcon(android.R.drawable.ic_dialog_alert);
            alertDialog.show();
        }

        //myGoogleAPIClient = GoogleSignIn.getClient(this.gso);
        mAuth = FirebaseAuth.getInstance();
    }

    public boolean isOnline(){
        ConnectivityManager cm =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
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
        }else if(i == R.id.google_sign_in_button){
            // sign in using google account
            googleSignIn();
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

    private void googleSignIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    //get user info after user signs in
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask){
        try{
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        } catch (ApiException e) {
            Log.w("SignUpActivity","signIn Failed "+ e.getStatusCode());
            updateUI((GoogleSignInAccount) null);
        }
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
    private void updateUI(GoogleSignInAccount account) {
        if(account != null){
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
