package com.articles.app.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.articles.app.Adapters.CustomAdapter;
import com.articles.app.ContentListDetails;
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

import org.parceler.Parcels;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ContentListFragment extends Fragment implements Display_interface {

    Display_interface display_interface;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser mUser;
    private String user_id;

    RecyclerView recyclerView;
    DatabaseReference database;
    CustomAdapter myAdapter;
    ArrayList<PostInfo> list;

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
        database = FirebaseDatabase.getInstance().getReference("Users").child("all_articles");
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.content_list_fragment, container, false);

        list = new ArrayList<>();

        recyclerView = view.findViewById(R.id.rec);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        new Thread(new Runnable() {
            @Override
            public void run() {
                getData();
            }

        }).start();
        myAdapter = new CustomAdapter(getContext(),list, this);

        return view;
    }

    private void getData() {

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            PostInfo postInfo = dataSnapshot.getValue(PostInfo.class);
                            list.add(postInfo);
                        }
                    }).start();


                }
                myAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(myAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onVersionItemClicked(int position) {
        ArrayList<PostInfo> mpost = list;
        Bundle args = new Bundle();

        args.putParcelable("articles", Parcels.wrap(mpost));
        args.putInt("position", position);


        ContentListDetails mFrag = new ContentListDetails();
        mFrag.setArguments(args);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.second_container, mFrag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();



    }
}
