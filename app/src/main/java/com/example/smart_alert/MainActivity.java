package com.example.smart_alert;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Button logout;
    private EditText title;
    private String eventType="Fire";
    private String timestamp;
    private String location;
    private Button submit_event;
    private Button back;
    private static final int PICK_IMAGE_REQUEST =1;
    private Button mButtonChooseImage;
    private ProgressBar progressBar;
    private ImageView imageView;
    private Uri mIamgeUri;
    private Boolean imageIsSet = false;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private String firebaseRealtimeDatabaseInstance= "https://smart-alert-f5055-default-rtdb.europe-west1.firebasedatabase.app";
    private StorageTask mUploadTask;

    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapterItems;
    private int ACCESS_FINE_LOCATION_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Resources res = getResources();
        String[] item = res.getStringArray(R.array.natural_events);


        // link fields with xml elements
        logout = findViewById(R.id.logout);
        progressBar = findViewById(R.id.progressBar);
        submit_event = findViewById(R.id.submit_event);
        title =findViewById(R.id.EditText_event_title);
        back = findViewById(R.id.buttonBack);
        mButtonChooseImage = findViewById(R.id.buttonChooseFile);
        imageView = findViewById(R.id.imageView);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView3);

        //dropdown creation
        adapterItems = new ArrayAdapter<String>(this,R.layout.list_item,item);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                eventType = item;
                Toast.makeText(MainActivity.this,"Item: "+ item,Toast.LENGTH_SHORT).show();
            }
        });

        // Connecting with Firebase Storage and Realtime Database
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance(firebaseRealtimeDatabaseInstance).getReference("uploads");

        //set on click listeners

        // logout button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "Logged Out",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, Start_Activity.class));
            }
        });

        //Upload event on firebase
        submit_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* String txt_title = title.getText().toString();
                if(txt_title.isEmpty()){
                    Toast.makeText(MainActivity.this,"Title is empty",Toast.LENGTH_SHORT).show();
                }else{
                    FirebaseDatabase.getInstance(firebaseRealtimeDatabaseInstance).getReference().child("ProgrammingKnowledge").push().child("Title").setValue(txt_title);
                    Toast.makeText(MainActivity.this,"Event Added",Toast.LENGTH_SHORT).show();

                }
                */
                if(mUploadTask !=null && mUploadTask.isInProgress()){
                    Toast.makeText(MainActivity.this,"Upload in progress",Toast.LENGTH_SHORT).show();
                }else{
                    if(title.getText().toString().isEmpty()){
                        Toast.makeText(MainActivity.this,"Title is empty",Toast.LENGTH_SHORT).show();
                    }else{
                        uploadEvent();
                    }

                }


            }
        });
        // Choose an image from the user's phone directory
        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        // Redirect to another activity where the user can see all the events he has uploaded
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,HomePage.class));
            }
        });



    }


    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*"); // we will only see images
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==PICK_IMAGE_REQUEST && resultCode==RESULT_OK
            && data !=null && data.getData() !=null
        ) {
           mIamgeUri = data.getData();

            Picasso.with(this).load(mIamgeUri).resize(200,200).into(imageView);
            imageIsSet =true;
        }
    }
    //gets file extension from an image (Ex. duck.png -> png)
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    /*
     * This method uploads event data on realtime database and the image in firebase's storage
     */
    private void uploadEvent(){
        if(mIamgeUri !=null){
            LocationUtils l= new LocationUtils();
            MyLocationListener myLocationListener = new MyLocationListener(this);
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if(l.checkLocation(MainActivity.this,locationManager)){
                // will make a file named "3456765432345.jpg", so the name will always be unique
                StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                        + "."+ getFileExtension(mIamgeUri));
                mUploadTask= fileReference.putFile(mIamgeUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Handler handler = new Handler();
                                //Delays progress bar being set back to zero for 0.5 seconds, therefore creating a "fully complete progress bar effect"
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setProgress(0);
                                    }
                                },500);
                                Toast.makeText(MainActivity.this, "Upload Successful",Toast.LENGTH_LONG).show();
                                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!urlTask.isSuccessful());
                                Uri downloadUrl = urlTask.getResult();
                                Log.d(TAG, "onSuccess: firebase download url: " + downloadUrl.toString()); //use if testing...don't need this line.
                                //Upload upload = new Upload(mEditTextFileName.getText().toString().trim(),downloadUrl.toString());
                                NaturalEvent naturalEvent = new NaturalEvent(title.getText().toString().trim(),eventType,l.getLocation(MainActivity.this,locationManager),getTimestamp(),downloadUrl.toString());

                                String uploadID = mDatabaseRef.push().getKey();
                                mDatabaseRef.child(uploadID).setValue(naturalEvent);
                                //go back to home activity
                                String email = new Intent().getStringExtra("UserEmail");
                                Intent intent = new Intent(MainActivity.this, HomePage.class);
                                intent.putExtra("UserEmail",email);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                double progress = (100.0 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                                progressBar.setProgress((int)progress);

                            }
                        });
            }else{
                Toast.makeText(this,"No file selected",Toast.LENGTH_SHORT).show();
            }
        }

    }
    private String getTimestamp() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return formatter.format(new Date());
    }
   /* private String getLocation() {

        MyLocationListener myLocationListener = new MyLocationListener(this);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"Enable GPS Permission",Toast.LENGTH_LONG).show();
            return "unknown";
        }else{
           // locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, myLocationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
            return myLocationListener.getLocation();

        }

    }
    private boolean checkLocation(){
        if(getLocation() == "unknown"){
            Toast.makeText(MainActivity.this, "Location not found,try again later",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }*/

}