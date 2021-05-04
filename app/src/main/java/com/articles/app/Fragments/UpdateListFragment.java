package com.articles.app.Fragments;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.articles.app.Adapters.CustomAdapter;
import com.articles.app.Adapters.FirebaseAdapter;
import com.articles.app.Models.PostInfo;
import com.articles.app.R;
import com.articles.app.interfaces.Display_interface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


public class UpdateListFragment extends Fragment implements Display_interface {
    private static final String TAG = "Update Fragment";
    Display_interface display_interface;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser mUser;
    private String user_id;


    private ArrayList<PostInfo> mList_;
    private DatabaseReference databaseReference;
    private CustomAdapter myAdapter;
    private RecyclerView mRecyclerView;


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        firebaseAuth = FirebaseAuth.getInstance();

        mUser = firebaseAuth.getCurrentUser();

        if (mUser != null){
            user_id = mUser.getUid();
            Log.d(TAG, "onAttach: " + user_id);
            mUser.reload();
        }

        try{
            display_interface = (Display_interface) context;

        }catch (ClassCastException e){
            Log.d(TAG, "onAttach: " + e.getMessage());

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_list_layout, container, false);

        mList_ = new ArrayList<>();

        mRecyclerView = view.findViewById(R.id.recyclerView_list);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

//        ListView listView =
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String str = ((TextView)view).getText().toString();
//                display_interface.onVersionItemClicked(str);
//            }
//        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                getData();
            }

        }).start();
        myAdapter = new CustomAdapter(getContext(),mList_, this);

        return view;

    }

    private void getData() {
        databaseReference.child(user_id).child("content").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "onDataChange: " + snapshot.getChildren().toString());

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    PostInfo postInfo = dataSnapshot.getValue(PostInfo.class);

                    mList_.add(postInfo);
                }

                myAdapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(myAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "UpdateFragment" + error.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

    @Override
    public void onVersionItemClicked(int position) {
        PostInfo mpost = mList_.get(position);
        Log.d(TAG, "onVersionItemClicked: " + mpost.getAuthorEmail());
        Bundle args = new Bundle();
        args.putParcelable("the_article", Parcels.wrap(mpost));

        FragmentDetails mFrag = new FragmentDetails();
        mFrag.setArguments(args);

        FragmentTransaction mTransaction = getFragmentManager().beginTransaction();
        mTransaction.replace(R.id.fragment_container_view_tag, mFrag);
        mTransaction.addToBackStack(null);
        mTransaction.commit();





    }
}
