package com.ojambrina.ifisio.UI.login;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ojambrina.ifisio.R;
import com.ojambrina.ifisio.UI.HomeActivity;
import com.ojambrina.ifisio.utils.AppPreferences;
import com.ojambrina.ifisio.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.lang.String.format;

public class LoginActivity extends AppCompatActivity {

    //Butterknife
    @BindView(R.id.edit_email)
    EditText editEmail;
    @BindView(R.id.login_password)
    EditText editPassword;
    @BindView(R.id.forgot_password)
    TextView forgotPassword;
    @BindView(R.id.checkbox_remember)
    CheckBox checkBoxRemember;
    @BindView(R.id.layout_login)
    LinearLayout layoutLogin;
    @BindView(R.id.login_no_account)
    TextView loginNoAccount;

    //Declarations
    Context context;
    AppPreferences appPreferences;
    Utils utils;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String email, password;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        context = this;
        appPreferences = new AppPreferences();
        utils = new Utils();
        firebaseAuth = FirebaseAuth.getInstance();

        if (appPreferences.getEmail() != null) {
            editEmail.setText(appPreferences.getEmail());
        }

        listeners();
    }

    private void listeners() {
        layoutLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateEmail(editEmail) && validatePassword(editPassword)) {
                    if (checkBoxRemember.isChecked()) {
                        firebaseUser = firebaseAuth.getCurrentUser();
                    }

                    dialog = utils.showProgressDialog(context, "Iniciando sesión");
                    dialog.show();

                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        appPreferences.setEmail(email);

                                        dialog.dismiss();
                                        Log.d("FIREBASE LOGIN", "signInWithEmail:success");
                                        Intent intent = new Intent(context, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        dialog.dismiss();
                                        Log.w("FIREBASE LOGIN", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(context, "Los datos de inicio de sesión son incorrectos.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.openDialog(context, R.layout.dialog_forgot_password);
            }
        });

        loginNoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //Validations
    private boolean validateEmail(EditText editEmail) {
        email = editEmail.getText().toString().trim();
        if (email.length() > 0) {
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                return true;
            } else {
                editEmail.setError("Email no válido");
                return false;
            }
        } else {
            editEmail.setError("El campo email no puede estar vacío");
            return false;
        }
    }

    private boolean validatePassword(EditText editPassword) {
        password = editPassword.getText().toString().trim();
        if (password.length() >= 8) {
            return true;
        } else {
            editPassword.setError("Al menos 8 caracteres");
            return false;
        }
    }
}