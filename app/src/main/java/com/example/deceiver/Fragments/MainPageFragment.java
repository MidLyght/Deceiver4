package com.example.deceiver.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.deceiver.Activities.StandardGameActivity;
import com.example.deceiver.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainPageFragment extends Fragment {

    View objectMainPageFragment;
    private Button btnPlay,btnIntro;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainPageFragment newInstance(String param1, String param2) {
        MainPageFragment fragment = new MainPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void attachComponents(){
        btnPlay=objectMainPageFragment.findViewById(R.id.btnPlayMainPage);
        btnIntro=objectMainPageFragment.findViewById(R.id.btnIntroMainPage);

        btnIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntroductionFragment introductionFragment=new IntroductionFragment();
                FragmentManager manager=getFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.frameLayoutMainPage,introductionFragment,introductionFragment.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent standardGameActivityIntent = new Intent(getContext(), StandardGameActivity.class);
                startActivity(standardGameActivityIntent);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        objectMainPageFragment=inflater.inflate(R.layout.fragment_main_page,container,false);
        attachComponents();

        return objectMainPageFragment;
    }
}