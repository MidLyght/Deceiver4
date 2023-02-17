package com.example.deceiver.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.deceiver.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IntroductionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IntroductionFragment extends Fragment {

    View objectIntroductionFragment;
    private ImageView introToTutorialImg,introToRolesImg;
    private TextView introToMainPageImg;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public IntroductionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IntroductionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IntroductionFragment newInstance(String param1, String param2) {
        IntroductionFragment fragment = new IntroductionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void attachComponents(){
        introToMainPageImg=objectIntroductionFragment.findViewById(R.id.introToMainPageTxt);
        introToRolesImg=objectIntroductionFragment.findViewById(R.id.introToRolesImg);
        introToTutorialImg=objectIntroductionFragment.findViewById(R.id.introToTutorialImg);

        introToMainPageImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainPageFragment mainPageFragment=new MainPageFragment();
                FragmentManager manager=getFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.frameLayoutMainPage,mainPageFragment,mainPageFragment.getTag())
                        .commit();
            }
        });

        introToRolesImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RolesFragment rolesFragment=new RolesFragment();
                FragmentManager manager=getFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.frameLayoutMainPage,rolesFragment,rolesFragment.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });

        introToTutorialImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HowToPlayFragment howToPlayFragment=new HowToPlayFragment();
                FragmentManager manager=getFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.frameLayoutMainPage,howToPlayFragment,howToPlayFragment.getTag())
                        .addToBackStack(null)
                        .commit();
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
        objectIntroductionFragment=inflater.inflate(R.layout.fragment_introduction,container,false);
        attachComponents();

        return objectIntroductionFragment;
    }
}