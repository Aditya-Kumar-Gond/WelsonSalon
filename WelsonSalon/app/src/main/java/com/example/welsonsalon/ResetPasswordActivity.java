package com.example.welsonsalon;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
   TextInputLayout User_email;
    Button forgot_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        User_email = findViewById(R.id.user_Reg_Email);
        forgot_btn = findViewById(R.id.get_mail_user);

        forgot_btn.setOnClickListener(v -> {
            if(User_email.getEditText().getText().toString().isEmpty()){
                User_email.setError("Please Provide your email");
            }else {
                auth.sendPasswordResetEmail(User_email.getEditText().getText().toString())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                Toast.makeText(ResetPasswordActivity.this, "Please Check your mail", Toast.LENGTH_SHORT).show();
                                Dialog dialog = new Dialog(this);
                                dialog.setContentView(R.layout.custom_dialog_success);
                                dialog.show();

                                TextView dialog_txt =dialog.findViewById(R.id.dialog_text);
                                TextView dialog_txt2 =dialog.findViewById(R.id.dialog_text2);
                                ImageView dialog_close = dialog.findViewById(R.id.CloseDialog);
                                Button dialog_btn = dialog.findViewById(R.id.dialog_button1);

                                dialog_txt2.setText("Please check your mail!");
                                dialog_txt.setText("We have sent an email to reset your password");
                                dialog_close.setOnClickListener(v1 -> dialog.dismiss());
                                dialog_btn.setOnClickListener(v1 -> {
                                    dialog.dismiss();
                                    startActivity(new Intent(ResetPasswordActivity.this,LoginPage.class));
                                    finish();
                                });
                            }else{
                                Toast.makeText(ResetPasswordActivity.this, "Please Provide your correct Mail!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}