package com.example.FirstApp2;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.URI;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onStart(){
        super.onStart();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getBaseContext(),gso);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        findViewById(R.id.sign_out_button).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.user_info);
        linearLayout.removeAllViews();

        if(user != null){
            String email = user.getEmail();
            TextView textView = new TextView(this);
            textView.setText("Email: " + email);
            linearLayout.addView(textView);
        }else if(account != null){
            String personName = account.getDisplayName();
            String email = account.getEmail();
            Uri photo = account.getPhotoUrl();

            ImageView imageView =new ImageView(this);
            imageView.setImageURI(photo);
            linearLayout.addView(imageView);

            TextView textView = new TextView(this);
            textView.setText("Email: " + email);
            textView.setTextSize(25);
            textView.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1f));
            linearLayout.addView(textView);

            TextView textView2 = new TextView(this);
            textView2.setText("Name: " + personName);
            textView2.setTextSize(25);
            textView.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1f));
            linearLayout.addView(textView2);
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.sign_out_button:
                signOut();
                break;
        }
    }
    private void signOut(){
        if( mAuth.getCurrentUser() != null) {
            mAuth.signOut();
            Intent intent = new Intent(this,SignupActivity.class);
            startActivity(intent);
        }else{
            mGoogleSignInClient.signOut().addOnCompleteListener(ProfileActivity.this,
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mAuth.signOut(); //signOut Firebase
                            Toast.makeText(ProfileActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ProfileActivity.this,SignupActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });

        }
    }
}
