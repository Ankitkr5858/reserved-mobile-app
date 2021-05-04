package com.articles.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.articles.app.Adapters.PostsViewPager;
import com.articles.app.Models.PostInfo;
import com.articles.app.interfaces.Display_interface;
import com.articles.app.ui.UpdateActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContentListDetails extends Fragment {
    private static final String TAG = "here" ;
    @BindView(R.id.content_v_p) ViewPager viewPager;

    private PostsViewPager mPostsViewpager;
    ArrayList<PostInfo> postInfo;
    Display_interface display_interface;
    private int startingPosition;


//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            display_interface = (Display_interface) context;

        }catch (ClassCastException e){
            Log.d(TAG, "onAttach: " + e.getMessage());

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.content_details_fragment, container, false);
        ButterKnife.bind(this, view);

        mPostsViewpager = new PostsViewPager(getFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, postInfo);
        viewPager.setAdapter(mPostsViewpager);
        viewPager.setCurrentItem(startingPosition);

        // Bottom Navigation!
        BottomNavigationView bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        // Set Home Screen as selected
        bottomNavigationView.setSelectedItemId(R.id.navigation_posts);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_posts:
                        return true;

                    case R.id.navigation_explore:
                        startActivity(new Intent(getContext(), ExploreScreen.class));
                        setAllowEnterTransitionOverlap(true);
                        return true;

                    case R.id.navigation_update:
                        startActivity(new Intent(getContext(), UpdateActivity.class));
                        setAllowEnterTransitionOverlap(true);
                        return true;

                    case R.id.navigation_upload:
                        startActivity(new Intent(getContext(), UploadContent.class));
                        setAllowEnterTransitionOverlap(true);
                        return true;
                }

                return false;
            }
        });
        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        postInfo = Parcels.unwrap(getArguments().getParcelable("articles"));
        startingPosition = getArguments().getInt("position", 0) ;

    }


}
