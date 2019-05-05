package com.ojambrina.ifisio.UI.login;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ojambrina.ifisio.R;
import com.ojambrina.ifisio.entities.Proffesional;
import com.ojambrina.ifisio.utils.AppPreferences;
import com.ojambrina.ifisio.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {

    //ButterKnife
    @BindView(R.id.edit_name)
    EditText editName;
    @BindView(R.id.edit_surname)
    EditText editSurname;
    @BindView(R.id.edit_identity_number)
    EditText editIdentityNumber;
    @BindView(R.id.edit_email)
    EditText editEmail;
    @BindView(R.id.edit_phone)
    EditText editPhone;
    @BindView(R.id.edit_password)
    EditText editPassword;
    @BindView(R.id.edit_password_repeat)
    EditText editPasswordRepeat;
    @BindView(R.id.layout_register)
    LinearLayout layoutRegister;
    @BindView(R.id.have_account)
    TextView haveAccount;

    //Declarations
    Context context;
    AppPreferences appPreferences;
    Utils utils;
    Proffesional proffesional;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Map<String, Proffesional> proffesionals;
    String username, name, surname, identityNumber, phone, email, password;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        context = this;
        appPreferences = new AppPreferences();
        utils = new Utils();

        setFirebase();
        listeners();
    }

    private void setFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("profesionales");
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void listeners() {
        layoutRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getProffesional();

                if (validateName(editName) &&
                        validateSurname(editSurname) &&
                        validateIdentityNumber(editIdentityNumber) &&
                        validateEmail(editEmail) &&
                        validatePhone(editPhone) &&
                        validatePassword(editPassword) &&
                        validatePasswordRepeat(editPasswordRepeat)) {

                    dialog = utils.showProgressDialog(context, "Creando usuario");
                    dialog.show();

                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        dialog.dismiss();
                                        appPreferences.setEmail(email);

                                        Log.d("REGISTRO", "createUserWithEmail:onComplete:" + task.isSuccessful());
                                        firebaseFirestore.collection("profesionales").document(username).set(proffesionals);
                                        FirebaseAuth.getInstance().signOut();

                                        Intent intent = new Intent(context, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        dialog.dismiss();
                                        Toast.makeText(context, "Refistro fallido." + task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getProffesional() {
        proffesional = new Proffesional();

        name = editName.getText().toString().trim();
        surname = editSurname.getText().toString().trim();
        identityNumber = editIdentityNumber.getText().toString().trim();
        phone = editPhone.getText().toString().trim();
        email = editEmail.getText().toString().trim();

        username = name + " " + surname;

        proffesional.setName(name);
        proffesional.setSurname(surname);
        proffesional.setIdentityNumber(identityNumber);
        proffesional.setPhone(phone);
        proffesional.setEmail(email);

        proffesionals = new HashMap<>();
        proffesionals.put(username, proffesional);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    //VALIDATIONS
    private boolean validateName(EditText editName) {
        name = editName.getText().toString().trim();
        if (editName.length() > 0) {
            return true;
        } else {
            editName.setError("El campo nombre no puede estar vacío");
            return false;
        }
    }

    private boolean validateSurname(EditText editSurname) {
        surname = editSurname.getText().toString().trim();
        if (surname.length() > 0) {
            return true;
        } else {
            editSurname.setError("El campo apellidos no puede estar vacío");
            return false;
        }
    }

    private boolean validateIdentityNumber(EditText editIdentityNumber) {
        identityNumber = editIdentityNumber.getText().toString().trim();
        Pattern pattern = Pattern.compile("(\\d{1,8})([TRWAGMYFPDXBNJZSQVHLCKEtrwagmyfpdxbnjzsqvhlcke])");
        Matcher matcher = pattern.matcher(identityNumber);

        if (matcher.matches()) {
            String letter = matcher.group(2);
            String patternLetters = "TRWAGMYFPDXBNJZSQVHLCKE";
            int index = Integer.parseInt(matcher.group(1));
            index = index % 23;
            String reference = patternLetters.substring(index, index + 1);
            if (reference.equalsIgnoreCase(letter)) {
                return true;
            } else {
                editIdentityNumber.setError("Letra del DNI incorrecta");
                return false;
            }
        } else {
            editIdentityNumber.setError("DNI no válido");
            return false;
        }
    }

    private boolean validatePhone(EditText editPhone) {
        phone = editPhone.getText().toString().trim();
        if (phone.length() > 0) {
            if (android.util.Patterns.PHONE.matcher(phone).matches()) {
                return true;
            } else {
                editPhone.setError("Número de teléfono no válido");
                return false;
            }
        } else {
            editPhone.setError("El campo teléfono no puede estar vacío");
            return false;
        }
    }

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

    private boolean validatePasswordRepeat(EditText editPasswordRepeat) {
        password = editPassword.getText().toString().trim();
        String passwordRepeat = editPasswordRepeat.getText().toString().trim();

        if (passwordRepeat.length() >= 8) {
            if (passwordRepeat.equals(password)) {
                return true;
            } else {
                editPasswordRepeat.setError("Las contraseñas deben coincidir");
                return false;
            }
        } else {
            editPasswordRepeat.setError("Al menos 8 caracteres");
            return false;
        }
    }
}
