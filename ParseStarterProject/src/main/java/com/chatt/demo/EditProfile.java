package com.chatt.demo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.starter.R;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by QUYNGO on 1/9/2016.
 */
public class EditProfile extends CustomActivity {
    EditText editName , editGender , editTel , editEmail , editAdd;
    Button btnSave , btnCancel;
    String gender[] = {"Male" , "Female" };
   // ImageButton imgBtn;
    String imgDecodableString ,val;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        editName = (EditText) findViewById(R.id.editText);
        //editGender = (EditText) findViewById(R.id.editText2);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>
                (
                        this,
                        android.R.layout.simple_spinner_item,
                        gender
                );
        adapter.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);
        //Thiết lập adapter cho Spinner
        spinner.setAdapter(adapter);
        //thiết lập sự kiện chọn phần tử cho Spinner
        spinner.setOnItemSelectedListener(new MyProcessEvent());

        editTel = (EditText) findViewById(R.id.editText3);
        editEmail = (EditText) findViewById(R.id.editText4);
        editAdd = (EditText) findViewById(R.id.editText5);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancel = (Button) findViewById(R.id.btnCancel);
       // imgBtn = (ImageButton) findViewById(R.id.imageView1);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Profile");
        query.whereEqualTo("Name", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + objects.size() + " scores");
                    for (ParseObject object : objects) {
                        editName.setText(object.getString("Name"));
                       // editGender.setText(object.getString("Gender"));
                        editTel.setText(object.getNumber("Telephone").toString());
                        editEmail.setText(ParseUser.getCurrentUser().getEmail().toString());
                        editAdd.setText(object.getString("Address"));
                    }


                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }

        });

        btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
//                                R.drawable.user_chat1);
//                        // Convert it to byte
//                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                        // Compress image to lower quality scale 1 - 100
//                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                        byte[] image = stream.toByteArray();
//
//                        // Create the ParseFile
//                        ParseFile file = new ParseFile("user_chat1.png", image);
//                        // Upload the image into Parse Cloud
//                        file.saveInBackground();
                        ParseObject object1 = new ParseObject("Profile");
                        String name = editName.getText().toString();
                        //String gender = editGender.getText().toString();
                        object1.put("Name", name);
                        object1.put("Gender", gender.toString() );
                        object1.put("Telephone", Integer.parseInt(editTel.getText().toString()));
                        object1.put("Email", editEmail.getText().toString());
                        object1.put("Address", editAdd.getText().toString());
                        //object1.put("Avatar" , file);
                        object1.saveEventually(new SaveCallback() {

                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    //c.setStatus(Conversation.STATUS_SENT);
                                    setResult(RESULT_OK);
                                    Toast.makeText(EditProfile.this, "Information is updated successfully!", Toast.LENGTH_LONG).show();
                                    finish();

                                } else
                                    Toast.makeText(EditProfile.this, "Error :" + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                                // c.setStatus(Conversation.STATUS_FAILED);
                                //adp.notifyDataSetChanged();
                            }
                        });

                    }
                });


//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ParseQuery<ParseObject> query = ParseQuery.getQuery("Profile");
//                query.whereEqualTo("Name", ParseUser.getCurrentUser().getUsername());
//                query.findInBackground(new FindCallback<ParseObject>() {
//                    @Override
//                    public void done(List<ParseObject> nameList, ParseException e) {
//                        if (e == null) {
//                            for (ParseObject nameObj : nameList) {
//                                nameObj.put("Name", editName.getText().toString());
//                                nameObj.put("Gender", editGender.getText().toString());
//                                nameObj.put("Telephone", editEmail.getText().toString());
//                                nameObj.put("Email", editEmail.getText().toString());
//                                nameObj.put("Address", editAdd.getText().toString());
//                                nameObj.saveInBackground();
//                            }
//                        } else {
//                            Log.d("Post retrieval", "Error: " + e.getMessage());
//                        }
//                    }
//                });
//
//            }
//        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }


    );


    }
    public class MyProcessEvent implements android.widget.AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0,
                                   View arg1,
                                   int arg2,
                                   long arg3) {
            //arg2 là phần tử được chọn trong data source
            //selection.setText(arr[arg2]);
            val += gender[arg2] ;
        }
        //Nếu không chọn gì cả
        public void onNothingSelected(AdapterView<?> arg0) {
            //selection.setText("");
            val = "";
        }
    }

//    public void loadImagefromGallery(View view) {
//        // Create intent to Open Image applications like Gallery, Google Photos
//        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        // Start the Intent
//        startActivityForResult(galleryIntent, 2);
//    }
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == 2 && resultCode == RESULT_OK && null != data)
//        {
//            Uri selectedImage = data.getData();
//            String[] filePathColumn = { MediaStore.Images.Media.DATA };
//
//            // Get the cursor
//            Cursor cursor = getContentResolver().query(selectedImage,
//                    filePathColumn, null, null, null);
//            // Move to first row
//            cursor.moveToFirst();
//
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            imgDecodableString = cursor.getString(columnIndex);
//            cursor.close();
//            // Set the Image in ImageView after decoding the String
//            imgBtn.setImageBitmap(BitmapFactory
//                    .decodeFile(imgDecodableString));
//
//        } else {
//            Toast.makeText(this, "You haven't picked Image",
//                    Toast.LENGTH_LONG).show();
//        }
//    }

}
