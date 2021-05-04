package com.articles.app.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.articles.app.ContentList;
import com.articles.app.Models.Datum;
import com.articles.app.Models.PostInfo;
import com.articles.app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentDetails extends Fragment implements View.OnClickListener{

    private PostInfo mArticles;
    private String TAG = "hello";
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private EditText mAuthorImageUrl,mContentTitle, mAuthorName,mAuthorEmail,mContent;
    private String user_id;

    private ImageView mContentImage, mAuthorImage;

    private Uri AuthorImagePathUri, ContentImagePathUri;

    private StorageReference authorReference, contentReference;
    private DatabaseReference databaseReference;
    private final int AUTHOR_REQUEST_CODE = 101;
    private final int CONTENT_REQUEST_CODE = 102;
    private StorageTask author_task, content_task;
    private ProgressDialog mProgress;
    private String content_image_url, author_image_url;
    private Bitmap bitmap;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.update_title)
    TextView content_title;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.update_author_image)
    ImageView author_profile;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.update_content_image)
    ImageView content_image;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.update_content) TextView content;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.update_author) TextView article_author;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.update_email) TextView author_email;
    @BindView(R.id.submit_update)
    Button mButton;
    @BindView(R.id.delete_article) Button mDelete;

    // TODO: Rename and change types and number of parameters
    public static FragmentDetails newInstance(Datum articles) {
        FragmentDetails fragment = new FragmentDetails();
        Bundle args = new Bundle();
        args.putParcelable("the_article", Parcels.wrap(articles));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        mUser = mAuth.getCurrentUser();
        if (mUser != null){
            mUser.reload();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;

        mArticles = Parcels.unwrap(getArguments().getParcelable("the_article"));

        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();

        authorReference = FirebaseStorage.getInstance().getReference("authors");
        contentReference = FirebaseStorage.getInstance().getReference("content");

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");


        mProgress= new ProgressDialog(getContext());


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view =  inflater.inflate(R.layout.details_fragment_layout, container, false);
        ButterKnife.bind(this, view);

        content_title.setText(mArticles.getTitle(), TextView.BufferType.EDITABLE);
        article_author.setText(mArticles.getAuthorName(), TextView.BufferType.EDITABLE);
        author_email.setText(mArticles.getAuthorEmail(), TextView.BufferType.EDITABLE);
        content.setText(mArticles.getContent(), TextView.BufferType.EDITABLE);
        Picasso.get().load(mArticles.getAuthorPhotoUrl()).into(author_profile);
        Picasso.get().load(mArticles.getContentImageURL()).into(content_image);



        mContentTitle = (EditText) content_title;
        mContent = (EditText) content;
        mAuthorEmail = (EditText) author_email;
        mAuthorImage = author_profile;
        mAuthorName = (EditText) article_author;
        mContentImage = content_image;

        mContentImage.setOnClickListener(this);
        mAuthorImage.setOnClickListener(this);


        mButton.setOnClickListener(this);
        mDelete.setOnClickListener(this);

        return view;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
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
            if (author_task != null && author_task.isInProgress() || content_task != null && content_task.isInProgress()){
                Toast.makeText(getContext(), "Hold on", Toast.LENGTH_SHORT).show();
            }
            else{
                UploadAuthorImage();
                UploadContentImage();
            }

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

        if (v == mDelete){
            deleteFromAllData();
            deleteFromChildData();
        }
}




    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == AUTHOR_REQUEST_CODE  && data != null && data.getData() != null){
            AuthorImagePathUri = data.getData();
            System.out.println(AuthorImagePathUri);

            // Do something with the bitmap
            mAuthorImage.setImageURI(AuthorImagePathUri);
            // At the end remember to close the cursor or you will end with the RuntimeException!
        }

        if (resultCode == Activity.RESULT_OK && requestCode == CONTENT_REQUEST_CODE && data != null && data.getData() != null ){
            ContentImagePathUri = data.getData();

            // Do something with the bitmap
            mContentImage.setImageURI(ContentImagePathUri);
            // At the end remember to close the cursor or you will end with the RuntimeException!
        }
    }



    public String GetFileExtension(Uri uri){
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        Log.d(TAG, "GetFileExtension: " + mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)));
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void UploadAuthorImage() {
        if (AuthorImagePathUri != null) {
            mProgress.setMessage("Updating Your Image");
            mProgress.show();
            StorageReference storageReference2 = authorReference.child(System.currentTimeMillis() + "." + GetFileExtension(AuthorImagePathUri));

            try{
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), AuthorImagePathUri);

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
                                            author_image_url = uri.toString();
                                            mProgress.dismiss();
                                        }
                                    });

                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                        }
                    });
        }else{
            mProgress.setMessage("Failed");
            mProgress.show();
        }
    }




    private void UploadContentImage() {

        if (ContentImagePathUri != null) {
            mProgress.setMessage("Updating your content Image");
            mProgress.show();
            StorageReference storageReference3 = contentReference.child(System.currentTimeMillis() + "." + GetFileExtension(ContentImagePathUri));
            Log.d(TAG, "UploadImage: " + storageReference3);

            try{
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), ContentImagePathUri);

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

                            storageReference3.getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            content_image_url = uri.toString();
                                            mProgress.dismiss();
                                        }
                                    });
                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });
        }else{
            mProgress.setMessage("content error");
            mProgress.show();
        }

    }


//    public void verifyChanges(){
//        if (mArticles.getTitle().equals(content_title.getText().toString())){
//            databaseReference.child(user_id).child("content").child().setValue(postInfo)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if(task.isSuccessful()){
//                                mProgress.dismiss();
//                                Toast.makeText(getContext(), "Successfully Updated", Toast.LENGTH_LONG).show();
//                            }else{
//                                Toast.makeText(getContext(), "There was a problem loading the data", Toast.LENGTH_LONG).show();
//                            }
//
//                        }
//                    });
//
//        }
//    }


    public void addUserToDatabase(){
        String title = mContentTitle.getText().toString().trim();
        String content = mContent.getText().toString().trim();
        String name = mAuthorName.getText().toString().trim();
        String email = mAuthorEmail.getText().toString();



        PostInfo postInfo = new PostInfo(title, content, name, email, content_image_url, author_image_url);
        Log.d(TAG, "addUserToDatabase: " + content_image_url + author_image_url);

        String content_key = databaseReference.push().getKey();
        databaseReference.child(user_id).child("content").child(content_key).setValue(postInfo)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mProgress.dismiss();
                            Toast.makeText(getContext(), "Successfully Updated", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getContext(), "There was a problem loading the data", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }


    public void addDataToSharedDatabase(){
        ProgressDialog mProgress = new ProgressDialog(getContext());
        mProgress.setMessage("Wait ...");
        mProgress.show();

        String title = mContentTitle.getText().toString().trim();
        String content = mContent.getText().toString().trim();
        String name = mAuthorName.getText().toString().trim();
        String email = mAuthorEmail.getText().toString();

        PostInfo postInfo = new PostInfo(title, content, name, email, author_image_url, content_image_url);

        String uploadId = databaseReference.push().getKey();
        //haha
        assert uploadId != null;
        databaseReference.child("all_articles").child(uploadId).setValue(postInfo)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mProgress.setMessage("Ok.. Done!!");

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(getContext(), UpdateListFragment.class);
                                    startActivity(intent);
                                }
                            }, 2000);
                            mProgress.dismiss();
                        }else{
                            Toast.makeText(getContext(), "There was a problem loading the data", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }


    public void deleteFromAllData(){
        Query applesQuery = databaseReference.child("all_articles").orderByChild("title").equalTo(mArticles.getTitle());

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }


    private void deleteFromChildData() {

        Query applesQuery = databaseReference.child(user_id).child("content").orderByChild("title").equalTo(mArticles.getTitle());

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(getContext(), ContentList.class);
                                startActivity(intent);
                            }
                        }
                    });
                    Toast.makeText(getContext(), "success!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }

}
