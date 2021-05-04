package com.articles.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.articles.app.Models.PostInfo;
import com.articles.app.signup.LoginActivity;
import com.articles.app.ui.UpdateActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class    UploadContent extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Upload Class";
    private PostInfo mPostInfo;
//    private  String SarahIvyWairimu = "jannelle";
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.titleEditText) EditText content_title;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.contentEditText)
    EditText content_text;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.authorEditText) EditText author_name;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.emailEditText) EditText author_email;
//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.DateEditText) EditText date_text;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.authorUploadPhoto) TextView author_photo;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.contentImage) TextView content_image;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.mButton)
    Button mButton;


    private EditText mAuthorImageUrl,mContentTitle, mAuthorName,mAuthorEmail,mContent;
    private String user_id;

    private TextView mContentImage, mAuthorImage;

    private Uri AuthorImagePathUri, ContentImagePathUri;

    private StorageReference authorReference, contentReference;
    private DatabaseReference databaseReference;
    private final int AUTHOR_REQUEST_CODE = 101;
    private final int CONTENT_REQUEST_CODE = 102;
    private StorageTask author_task, content_task;
    private ProgressDialog mProgress;


    private Bitmap bitmap;

    private String author_image_url, content_image_url;

    @Override
    protected void onStart() {
        super.onStart();
        mUser = mAuth.getCurrentUser();
        if (mUser != null){
            mUser.reload();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();

        mProgress = new ProgressDialog(UploadContent.this);

        authorReference = FirebaseStorage.getInstance().getReference("authors");
        contentReference = FirebaseStorage.getInstance().getReference("content");

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");



        mContentTitle = content_title;
        mContent = content_text;
        mAuthorEmail = author_email;
        mAuthorImage = author_photo;
        mAuthorName = author_name;
        mContentImage = content_image;

        mContentImage.setOnClickListener(this);
        mAuthorImage.setOnClickListener(this);

        mButton.setOnClickListener(this);


        // Bottom Navigation!
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        // Set Home Screen as selected
        bottomNavigationView.setSelectedItemId(R.id.navigation_upload);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_posts:
                        startActivity(new Intent(getApplicationContext(), ContentList.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_explore:
                        startActivity(new Intent(getApplicationContext(), ExploreScreen.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navigation_update:
                        startActivity(new Intent(getApplicationContext(), UpdateActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navigation_upload:
                        return true;
                }

                return false;
            }
        });
    }

    // Logout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(UploadContent.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v == mAuthorImage){

            Intent photoIntent = new Intent(Intent.ACTION_PICK);
            photoIntent.setType("image/*");
            startActivityForResult(photoIntent, AUTHOR_REQUEST_CODE);

        }

        if (v == mContentImage ){
            Intent photoIntent = new Intent(Intent.ACTION_GET_CONTENT);
            photoIntent.setType("image/*");
            startActivityForResult(photoIntent, CONTENT_REQUEST_CODE);
        }

        if (v == mButton){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mProgress.setMessage("Uploading");
                    mProgress.show();
                    addUserToDatabase();
                    addDataToSharedDatabase();

                }
            }, 2500);
        }


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == AUTHOR_REQUEST_CODE  && data != null && data.getData() != null){
            AuthorImagePathUri = data.getData();
            System.out.println(AuthorImagePathUri);
            if (author_task != null && author_task.isInProgress()){
                Toast.makeText(UploadContent.this, "Wait", Toast.LENGTH_SHORT).show();
            }else{
                UploadAuthorImage();
            }

        }

        if (resultCode == RESULT_OK && requestCode == CONTENT_REQUEST_CODE && data != null && data.getData() != null ){
            ContentImagePathUri = data.getData();

            if (content_task != null && content_task.isInProgress()){
               Toast.makeText(UploadContent.this, "Hold on man", Toast.LENGTH_SHORT).show();
            }else{
                UploadContentImage();
            }
        }
    }


    public String GetFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        Log.d(TAG, "GetFileExtension: " + mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)));
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void UploadAuthorImage() {
        if (AuthorImagePathUri != null) {
            mProgress.setMessage("Uploading Your Image");
            mProgress.show();
            StorageReference storageReference2 = authorReference.child(System.currentTimeMillis() + "." + GetFileExtension(AuthorImagePathUri));

            try{
                bitmap = MediaStore.Images.Media.getBitmap(UploadContent.this.getContentResolver(), AuthorImagePathUri);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG,20, baos);

            final byte[] data  = baos.toByteArray();

            author_task = storageReference2.putBytes(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference2.getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    mProgress.dismiss();
                                    author_image_url = uri.toString();
                                    mAuthorImage.setText(author_image_url);
                                }
                            });

                        }

                    })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadContent.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            })
            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double task_length = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    mProgress.setProgress((int)task_length);
                }
            });
        }else{
            mProgress.setMessage("Failed");
            mProgress.show();
        }
    }




    private void UploadContentImage() {

        if (ContentImagePathUri != null) {
            mProgress.setMessage("Uploading your content Image");
            mProgress.show();
            StorageReference storageReference3 = contentReference.child(System.currentTimeMillis() + "." + GetFileExtension(ContentImagePathUri));
            Log.d(TAG, "UploadImage: " + storageReference3);

            try{
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), ContentImagePathUri);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.PNG,20, baos);

            final byte[] data  = baos.toByteArray();


            content_task = storageReference3.putBytes(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(UploadContent.this, "Success", Toast.LENGTH_SHORT).show();

                            storageReference3.getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            mProgress.dismiss();
                                            content_image_url = uri.toString();
                                            mContentImage.setText(content_image_url);
                                        }
                                    });
                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadContent.this, e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });
        }else{
            mProgress.setMessage("content error");
            mProgress.show();
        }

    }

    public void addUserToDatabase(){
        String title = mContentTitle.getText().toString().trim();
        String content = mContent.getText().toString().trim();
        String name = mAuthorName.getText().toString().trim();
        String email = mAuthorEmail.getText().toString();
        String authorImageUrl = author_image_url;
        String content_url = content_image_url;

        PostInfo postInfo = new PostInfo(title, content, name, email, authorImageUrl, content_url);
        Log.d(TAG, "addUserToDatabase: " + authorImageUrl + content_url);

        String content_key = databaseReference.push().getKey();
        databaseReference.child(user_id).child("content").child(content_key).setValue(postInfo)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    mProgress.dismiss();
                    Toast.makeText(UploadContent.this, "Successfully added article", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(UploadContent.this, "There was a problem loading the data", Toast.LENGTH_LONG).show();
                }

            }
        });


    }


    public void addDataToSharedDatabase(){
        ProgressDialog mProgress = new ProgressDialog(UploadContent.this);
        mProgress.setMessage("Wait ...");
        mProgress.show();

        String title = mContentTitle.getText().toString().trim();
        String content = mContent.getText().toString().trim();
        String name = mAuthorName.getText().toString().trim();
        String email = mAuthorEmail.getText().toString();
        String authorImageUrl = author_image_url;
        String content_url = content_image_url;

        PostInfo postInfo = new PostInfo(title, content, name, email, authorImageUrl, content_url);

        String uploadId = databaseReference.push().getKey();
        //haha
        assert uploadId != null;
        databaseReference.child("all_articles").child(uploadId).setValue(postInfo)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mProgress.setMessage("Ok.. Done!!");
                            mProgress.dismiss();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(UploadContent.this, ContentList.class);
                                    startActivity(intent);
                                }
                            }, 2000);
                        }else{
                            Toast.makeText(UploadContent.this, "There was a problem loading the data", Toast.LENGTH_LONG).show();
                        }

                    }
                });


    }

    }
