 package com.example.aayush.clean;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

 public class ProfileActivity extends AppCompatActivity {

    private TextView textViewUsername,textViewUserEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (!SharedPrefManager.getInstance(this).isUserLoggedin()){
            finish();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }

        textViewUsername = (TextView)findViewById(R.id.textViewUsername);
        textViewUserEmail= (TextView)findViewById(R.id.textViewUserEmail);

        textViewUsername.setText(SharedPrefManager.getInstance(this).getUsername());
        textViewUserEmail.setText(SharedPrefManager.getInstance(this).getUserEmail());
    }

     @Override
     public boolean onCreateOptionsMenu(Menu menu) {

       getMenuInflater().inflate(R.menu.menu,menu);
         return true;
     }

     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
         switch (item.getItemId()){
             case R.id.menuLogout:
                 SharedPrefManager.getInstance(this).Logout();
                 finish();
                 startActivity(new Intent(this,LoginActivity.class));
                 break;
         }
         return true;
     }
 }
