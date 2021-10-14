package ync.ysc3232.pictionary_sabotage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class LoginPage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText etEmail;
    EditText etPass;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);
        mAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.setEmail);
        etPass = findViewById(R.id.setPassword);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadMain();
    }

    //credit to SO user user14245642 for the great sanity checks
    private boolean validateEmail(){

        String email = etEmail.getText().toString().trim();

        if (email.isEmpty()){
            etEmail.setError("Field can't be empty");
        } else if (!VALID_EMAIL_ADDRESS_REGEX.matcher(email).find()){
            etEmail.setError("Please enter a valid email");
        } else {
            etEmail.setError(null);
            return true;
        }
        return false;
    }

    private boolean validatePassword(){
        String password = etPass.getText().toString().trim();
        if (password.isEmpty()){
            etPass.setError("Field can't be empty");
        } else if (password.length() < 8){
            etPass.setError("Password must have at least 8 characters");
        } else if (password.equals("password") || password.equals("12345678")){
            etPass.setError("are u for real?");
        } else {
            return true;
        }
        return false;
    }

    public void loginButtonEvent(View v) {
        if (!validateEmail() || !validatePassword()){
            Toast.makeText(LoginPage.this, "Cannot Create Account", Toast.LENGTH_SHORT).show();
            return;
        }
        loginToFirebase(etEmail.getText().toString().trim(), etPass.getText().toString().trim());
    }

    public void loginToFirebase(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginPage.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(), "Successful Login.",
                                    Toast.LENGTH_SHORT).show();
                            loadMain();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Failed Login.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }

    public void loadMain() {
        FirebaseUser currUser = mAuth.getCurrentUser();
        //updateUI(currUser);
        if (currUser != null) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.putExtra("email", currUser.getEmail());
            i.putExtra("uid", currUser.getUid());
            startActivity(i);
        }
    }

}