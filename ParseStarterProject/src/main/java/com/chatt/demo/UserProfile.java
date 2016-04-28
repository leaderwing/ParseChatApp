package com.chatt.demo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.starter.R;

import java.util.List;

/**
 * Created by QUYNGO on 1/8/2016.
 */
public class UserProfile extends CustomActivity {
    TextView u_name , gender , email , phone ,address;
    Button btnOk;
    String  imgDecodableString;
    public static boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        u_name = (TextView) findViewById(R.id.user_name);
        gender = (TextView) findViewById(R.id.gender);
        email = (TextView) findViewById(R.id.email);
        phone = (TextView) findViewById(R.id.tele);
        address = (TextView) findViewById(R.id.addr);
        btnOk = (Button) findViewById(R.id.btnEdit);
        //btnEdit.setVisibility(View.INVISIBLE);
        Intent getI = getIntent();
        Bundle pack = getI.getBundleExtra(Const.EXTRA_DATA);
        final String data = pack.getString("User");
        final String mail = pack.getString("Email");
        //setResult(RESULT_OK);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Profile");
        query.whereEqualTo("Name", data);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    // flag = true;
                    Log.d("score", "Retrieved " + objects.size() + " scores");
                    for (ParseObject object : objects) {
                        //imageButton.set
                        u_name.setText(object.getString("Name"));
                        gender.setText(object.getString("Gender"));
                        email.setText(mail);
                        phone.setText(object.getNumber("Telephone").toString());
                        address.setText(object.getString("Address"));
                    }
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                    //btnEdit.setVisibility(View.VISIBLE);
//                    btnEdit.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            //if(data == ParseUser.getCurrentUser().getUsername()) {
//                            Toast.makeText(UserProfile.this, "Edit profile", Toast.LENGTH_SHORT).show();
//                            Intent i = new Intent(UserProfile.this, EditProfile.class);
//                            startActivityForResult(i, 1000);
//                        }
//
//                    });
                }

                // }
                else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });


       ParseQuery<ParseObject>query1 = ParseQuery.getQuery("Profile");
        query1.whereNotEqualTo("Name", ParseUser.getCurrentUser().getUsername());
        query1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + objects.size() + " scores");
                    for (ParseObject object : objects) {
                        u_name.setText(data);
                        gender.setText("Null");
                        email.setText(mail);
                        phone.setText("Null");
                        address.setText("Null");
                    }
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                    //btnEdit.setVisibility(View.VISIBLE);
//                    btnEdit.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            //if(data == ParseUser.getCurrentUser().getUsername()) {
//                            Toast.makeText(UserProfile.this, "Edit profile", Toast.LENGTH_SHORT).show();
//                            Intent i = new Intent(UserProfile.this, EditProfile.class);
//                            startActivityForResult(i, 1000);
//                        }
//
//                    });
                }

                // }
                else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

//        if(u_name.getText().toString() == ParseUser.getCurrentUser().getUsername())
//            flag = true;


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK)
        {
           finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu , menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId())
        {
            case R.id.chat:
                startActivity(new Intent(UserProfile.this,
                        Chat.class).putExtra(
                        Const.EXTRA_DATA, u_name.getText().toString()));
                break;
            case R.id.add_friend:
                Toast.makeText(this , u_name.getText().toString() + "is already added to your friends" , Toast.LENGTH_LONG ).show();
                break;

            }
        return true;
    }
}
