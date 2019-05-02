package com.ojambrina.ifisio.UI.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.ojambrina.ifisio.R;
import com.ojambrina.ifisio.UI.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_email)
    EditText loginEmail;
    @BindView(R.id.login_password)
    EditText loginPassword;
    @BindView(R.id.remember_checkbox)
    CheckBox rememberCheckbox;
    @BindView(R.id.layout_login)
    LinearLayout layoutLogin;
    @BindView(R.id.login_no_account)
    TextView loginNoAccount;

    Context context;
    FirebaseAuth firebaseAuth;


    //TODO añadir login email-password en firebase y una vez obtenido el login lanzar HomeActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        context = this;
        firebaseAuth = FirebaseAuth.getInstance();

        listeners();
    }

    private void listeners() {
        layoutLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEmail.getText().toString().trim();
                String password = loginPassword.getText().toString().trim();

                if (!email.equals("") && !password.equals("")) {
                    firebaseAuth.signInWithEmailAndPassword(email, password);
                    Intent intent = new Intent(context, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    if (loginEmail.getText().toString().trim().isEmpty()) {
                        loginEmail.setError("No puede estar vacío");
                    }
                    if (loginPassword.getText().toString().trim().isEmpty()) {
                        loginPassword.setError("No puede estar vacío");
                    }
                }

            }
        });

        loginNoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}

