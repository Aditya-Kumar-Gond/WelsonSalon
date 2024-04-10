package com.example.welsonsalon;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterPage extends AppCompatActivity {
    // Variables
    ImageView image;
    TextView logoText,sloganText;
    TextInputLayout regName,regUsername,regEmail,regPhoneNo,regPassword;
    Button regBtn,regToLoginBtn;
    
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference ref = FirebaseDatabase.getInstance("https://welson-salon-backend-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        image = findViewById(R.id.logo_image2);
        logoText = findViewById(R.id.welcome);
        sloganText = findViewById(R.id.jrny);

        regName = findViewById(R.id.regfullname);
        regUsername = findViewById(R.id.username);
        regEmail = findViewById(R.id.email);
        regPhoneNo = findViewById(R.id.phoneNo);
        regPassword = findViewById(R.id.reg_password);
        regBtn  = findViewById(R.id.register_btn);
        regToLoginBtn = findViewById(R.id.Already_have_an_account);

        regToLoginBtn.setOnClickListener(view -> {
            goBackToLogin(regToLoginBtn);
        });

        regBtn.setOnClickListener(view -> {

            if(validateName(regName) && validateEmail(regEmail) && validatePassword(regPassword) && validatePhoneNo(regPhoneNo) && validateUsername(regUsername)){
                registerUser();

            }else{
                validateUsername(regUsername);
                validatePhoneNo(regPhoneNo);
                validatePassword(regPassword);
                validateEmail(regEmail);
                validateName(regName);
            }

        });
    }


    public void goBackToLogin(View view) {
        Intent intent =new Intent(RegisterPage.this, LoginPage.class);

        Pair[] pairs = new Pair[7];
        pairs[0]= new Pair<View,String>(image,"logo_salon");
        pairs[1]= new Pair<View,String>(logoText,"logo_head");
        pairs[6]= new Pair<View,String>(sloganText,"login_continue");
        pairs[6]= new Pair<View,String>(regName,"user_name");
        pairs[2]= new Pair<View,String>(regUsername,"trans_username");
        pairs[2]= new Pair<View,String>(regEmail,"trans_email");
        pairs[2]= new Pair<View,String>(regPhoneNo,"trans_Phone");
        pairs[3]= new Pair<View,String>(regPassword,"button_password");
        pairs[4]= new Pair<View,String>(regBtn,"button_login");
        pairs[5]= new Pair<View,String>(regToLoginBtn,"user_signup");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterPage.this,pairs);
        startActivity(intent,options.toBundle());
    }

    private Boolean validateName(TextInputLayout regName){
            String val = regName.getEditText().getText().toString();
             if(val.isEmpty()){
                regName.setError("Field cannot be empty");
                return false;
              }
             else
             {
              regName.setError(null);
             regName.setErrorEnabled(false);
               return true;
             }
    }
    private Boolean validateUsername(TextInputLayout regName){
        String val = regName.getEditText().getText().toString();
//        String noWhiteSpace = "(=/\\s+$)";
        if(val.isEmpty()){
            regName.setError("Feild cannot be empty");
            return false;
        }else if (val.length()>=15){
            regName.setError("Username is to long");
            return false;
        }
        else
        {
            regName.setError(null);
            return true;
        }

    }
    private Boolean validateEmail(TextInputLayout regName){
        String val = regName.getEditText().getText().toString();

        if(val.isEmpty()){
            regName.setError("Field cannot be empty");
            return false;
        }
        else
        {
            regName.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }

    }
    private Boolean validatePhoneNo(TextInputLayout regName){
        String val = regName.getEditText().getText().toString();

        if(val.isEmpty()){
            regName.setError("Feild cannot be empty");
            return false;
        }
        else
        {
            regName.setError(null);
            return true;
        }

    }
    private Boolean validatePassword(TextInputLayout regName){
        String val = regName.getEditText().getText().toString();

        if(val.isEmpty()){
            regName.setError("Feild cannot be empty");
            return false;
        }
        else
        {
            regName.setError(null);
            return true;
        }

    }



    // Save data in Firebase on button click
    public void registerUser(){
        //Get all the values in String
        String Firstname = regName.getEditText().getText().toString();
        String Lastname = regUsername.getEditText().getText().toString();
        String email = regEmail.getEditText().getText().toString();
        String phoneNo = regPhoneNo.getEditText().getText().toString();
        String password = regPassword.getEditText().getText().toString();
        

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {

            if(task.isSuccessful()){

                FirebaseUser firebaseUser = auth.getCurrentUser();
                if (firebaseUser != null) {
                    firebaseUser.sendEmailVerification().addOnCompleteListener(task1 -> {
                        if(task.isSuccessful()){
                            Toast.makeText(this, "Verification mail has been sent!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                String UserId = auth.getCurrentUser().getUid();

                HashMap<String,Object> user = new HashMap<>();
                user.put("FirstName",Firstname);
                user.put("id",UserId);
                user.put("Email",email);
                user.put("PhoneNo",phoneNo);
                user.put("UserImage",UserId);
                user.put("LastName",Lastname);
                user.put("Password",password);

                ref.child(UserId).setValue(user);


                Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.custom_dialog);
                Button dialog_btn = dialog.findViewById(R.id.dialog_button);
                TextView dialog_txt = dialog.findViewById(R.id.dialog_text);
                dialog_txt.setText("We have sent a verification mail please comfirm it to continue.");
                ImageView dialog_close = dialog.findViewById(R.id.CloseDialog);
                dialog_close.setOnClickListener(v -> {
                        dialog.dismiss();
                    startActivity(new Intent(RegisterPage.this,LoginPage.class));
                    finish();
                });
                dialog.show();
                dialog_btn.setOnClickListener(v -> {
                    dialog.dismiss();
                    startActivity(new Intent(RegisterPage.this,LoginPage.class));
                    finish();
                });
            }else
            {
                regEmail.setError("User already Exist!");
            }
        });
}

}