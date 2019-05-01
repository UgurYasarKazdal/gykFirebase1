package com.uykazdal.gezginapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import static android.content.ContentValues.TAG;

public class LoginActivity extends BaseActivity {
    private static final int RC_SIGN_IN = 1;
    private EditText mEdt_userName;
    private EditText mEdt_password;
    private Button mBtn_signIn;
    private Button mBtn_signUp;
    private TextView mTv_error;
    private FirebaseAuth mAuth;

    private String mError;
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEdt_userName = findViewById(R.id.edt_login_userName);
        mEdt_password = findViewById(R.id.edt_login_password);
        mBtn_signIn = findViewById(R.id.btn_login_signIn);
        mBtn_signUp = findViewById(R.id.btn_login_signUp);
        mTv_error = findViewById(R.id.tv_login_error);

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        doGoogleLogin(account);

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });

        findViewById(R.id.btn_login_googleSignOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutAll();
            }
        });


        mAuth = FirebaseAuth.getInstance();

        if (getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
        }

        mBtn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkLogin()) {
                    showDialog("Giriş Yapılıyor","GezginApp");
                  //  signIn();
                } else {
                    mTv_error.setVisibility(View.VISIBLE);
                    mTv_error.setText(mError);
                }
            }
        });

        mBtn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkLogin()) { // true ise
                    createAccount();
                } else { //false ise
                    mTv_error.setVisibility(View.VISIBLE);
                    mTv_error.setText(mError);
                }
            }
        });


    }

    private void doGoogleLogin(GoogleSignInAccount account) {
        if (account != null) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void signOutAll() {
        if (mAuth != null) {
            mAuth.signOut();
        }
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            doGoogleLogin(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            doGoogleLogin(null);
        }
    }

    private boolean checkLogin() {
        boolean isUserNameNonNull = !getUserName().equals("");
        boolean isPasswordNonNull = !getPassword().equals("");

        if (!isUserNameNonNull) {
            mError = "Lütfen userName alanını boş bırakmayın.";
            mEdt_userName.requestFocus();
            return false;
        }

        if (!isPasswordNonNull) {
            mError = "Lütfen password alanını boş bırakmayın.";
            mEdt_password.requestFocus();
            return false;
        }

        if (!getUserName().contains("@")) {
            mError = "Lütfen userName alanın @ kullanın...";
            mEdt_userName.requestFocus();
            return false;
        }

        return true;
    }

    private void createAccount() {
        mAuth.createUserWithEmailAndPassword(getUserName(), getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            setErrorText(task.getException().getMessage().toString());
                        }

                        doLogin(task, getCurrentUser());

                        // ...
                    }
                });
    }

    private void doLogin(Task<AuthResult> task, FirebaseUser currentUser) {
        if (task.isSuccessful()) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void signIn() {
        mAuth.signInWithEmailAndPassword(getUserName(), getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            // updateUI(user);
                            dismissDiloag();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            setErrorText(task.getException().getMessage().toString());
                        }

                        doLogin(task, getCurrentUser());

                        // ...
                    }
                });
    }

    private String getUserName() {
        return mEdt_userName.getText().toString().trim();
    }

    private String getPassword() {
        return mEdt_password.getText().toString().trim();
    }

    private void setErrorText(String _error) {
        mTv_error.setVisibility(View.VISIBLE);
        mTv_error.setText(_error);


    }

    private FirebaseUser getCurrentUser() {
        FirebaseUser user = mAuth.getCurrentUser();
        return user;
    }
}
