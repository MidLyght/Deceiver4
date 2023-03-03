package com.example.deceiver.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.deceiver.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StandardGameTutorialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StandardGameTutorialFragment extends Fragment {
    View objectStandardGameTutorialFragment;
    private Button doneBtn;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StandardGameTutorialFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StandardGameTutorialFragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static StandardGameTutorialFragment newInstance(String param1, String param2) {
        StandardGameTutorialFragment fragment = new StandardGameTutorialFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void attachComponents(){
        doneBtn=objectStandardGameTutorialFragment.findViewById(R.id.tutorialDoneBtn);

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntroductionFragment introductionFragment=new IntroductionFragment();
                FragmentManager manager=getFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.frameLayoutMainPage,introductionFragment,introductionFragment.getTag())
                        .commit();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        objectStandardGameTutorialFragment=inflater.inflate(R.layout.fragment_standard_game_tutorial, container, false);
        attachComponents();

        return objectStandardGameTutorialFragment;
    }
}