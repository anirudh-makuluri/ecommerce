package com.example.oops;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oops.Prevalent.Prevalent;
import com.example.oops.model.Users;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class MainActivity extends AppCompatActivity {

    private Button joinNowButton, loginButton, googlebutton;
    private LoginButton facebookbutton;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private CallbackManager mCallbackManager;
    private FirebaseAuth mFirebaseAuth;
    private AccessTokenTracker accessTokenTracker;
    private FirebaseAuth.AuthStateListener authStateListener;
    private TextView textView;

    //@Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser user= mAuth.getCurrentUser();
//        if(user!=null)
//        {
//            Intent intent=new Intent (getApplicationContext(),HomeActivity.class);
//            startActivity(intent);
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        Paper.init(this);
        createrequest();
        joinNowButton = (Button) findViewById(R.id.main_join_now_btn);
        loginButton = (Button) findViewById(R.id.main_login_btn);
        googlebutton = (Button) findViewById(R.id.google_sign_in);
        facebookbutton = findViewById(R.id.faceboook_sign_in);
        facebookbutton.setReadPermissions("email","public_profile");
        textView=findViewById(R.id.app_slogan);
        loadingBar = new ProgressDialog(this);
        mFirebaseAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        facebookbutton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    updateUI(user);
                } else {
                    updateUI(null);
                }
            }
        };
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    mFirebaseAuth.signOut();
                }
            }
        };


        googlebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class
                );
                startActivity(intent);
            }
        });

        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class
                );
                startActivity(intent);
            }
        });

        String UserNameKey = Paper.book().read(Prevalent.UserNameKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);
        if (UserNameKey != null && UserPasswordKey != null) {
            if (!TextUtils.isEmpty(UserNameKey) && !TextUtils.isEmpty(UserPasswordKey)) {
                AllowAccess(UserNameKey, UserPasswordKey);
                loadingBar.setTitle("Already Logged in");
                loadingBar.setMessage("Please wait, while we are checking for creds");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }

    }

    private void handleFacebookToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mFirebaseAuth.signInWithCredential(credential).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            System.out.println("Entering via fb");
                            Toast.makeText(MainActivity.this, "entering via fb", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class
                            );
                            startActivity(intent);
                            updateUI(user);
                        } else {
                            Toast.makeText(MainActivity.this, "login failed", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });

    }

    //@Override
    protected void OnActivityResult(int requestcode, int resultcode, @Nullable Intent data) {
        mCallbackManager.onActivityResult(requestcode, resultcode, data);
        super.onActivityResult(requestcode, resultcode, data);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            textView.setText("logged in");
            Intent intent = new Intent(MainActivity.this, HomeActivity.class
            );
            startActivity(intent);
        }
    }

    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(authStateListener);
    }

    protected void onStop()
    {
        super.onStop();
        if(authStateListener!=null)
        {
            mFirebaseAuth.removeAuthStateListener(authStateListener);
        }
    }


    private void AllowAccess(final String name,final String password) {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Accounts").child(name).exists())
                {
                    Users usersdata=snapshot.child("Accounts").child(name).getValue(Users.class);
                    if(usersdata.getName().equals(name) )
                    {
                        if(usersdata.getPassword().equals(password))
                        {
                            Toast.makeText(MainActivity.this,"Getting you in",Toast.LENGTH_LONG).show();
                            loadingBar.dismiss();
                            Intent intent = new Intent(MainActivity.this,HomeActivity.class
                            );
                            startActivity(intent);
                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this,"Wrong password",Toast.LENGTH_LONG).show();

                        }

                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this,"No account with this phone number",Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private  void createrequest()
    {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                //Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent=new Intent (getApplicationContext(),HomeActivity.class);
                            startActivity(intent);
                            //updateUI(user);
                        } else {
                            Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();
                            // If sign in fails, display a message to the user.
                           // Log.w(TAG, "signInWithCredential:failure", task.getException());
                           // updateUI(null);
                        }
                    }
                });
    }


}