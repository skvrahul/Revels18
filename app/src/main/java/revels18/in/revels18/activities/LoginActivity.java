package revels18.in.revels18.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import revels18.in.revels18.R;
import revels18.in.revels18.models.registration.LoginResponse;
import revels18.in.revels18.network.RegistrationClient;
import revels18.in.revels18.utilities.NetworkUtils;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText email = (EditText)findViewById(R.id.username_edit_text);
        final EditText password = (EditText)findViewById(R.id.password_edit_text);

        Button loginButton = (Button)findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
                    showAlert("Please enter email and password!", 0);
                    return;
                }
                if (!NetworkUtils.isInternetConnected(LoginActivity.this)){
                    showAlert("Please connect to the internet and try again!", 1);
                    return;
                }
                final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
                dialog.setMessage("Logging in... please wait!");
                dialog.setCancelable(false);
                dialog.show();

                RequestBody body =  RequestBody.create(MediaType.parse("text/plain"), "email="+email.getText().toString()+"&password="+password.getText().toString());
                Call<LoginResponse> call = RegistrationClient.getLoginInterface(LoginActivity.this).attemptLogin(body);
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        String message = "";
                        int error = 0;
                        if (response != null && response.body() != null) {
                            switch(response.body().getPayload().getCode()){
                                case 1: message = "Email or password not specified!";
                                        error = 1;
                                        break;
                                case 2: message = "Could not connect to database!";
                                        error = 1;
                                        break;
                                case 3: message = "Login successful! However, we recommend setting a new password before you continue.";
                                        error = 2;
                                        setLoggedIn();
                                        break;
                                case 4: message = "Login successful!";
                                        error = 2;
                                        setLoggedIn();
                                        break;
                                case 5: message = "Incorrect email or password! Please try again.";
                                        error = 1;
                                        break;
                            }
                            dialog.dismiss();
                            showAlert(message, error);
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Log.d("Failure", t.getMessage());
                        dialog.dismiss();
                        showAlert("Could not connect to server! Please check your internet connect or try again later.", 1);
                    }
                });
            }
        });
    }

    private void setLoggedIn() {

    }

    public void showAlert(String message, final int error){
        String result[] = {"", "Error", "Success"};
        int icon[] = {0, R.drawable.ic_error, R.drawable.ic_success};
        new AlertDialog.Builder(LoginActivity.this).setTitle(result[error]).setMessage(message)
                .setIcon(icon[error])
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (error == 2){
                            Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
                            startActivity(intent);
                        }
                    }
                }).show();
    }
}
