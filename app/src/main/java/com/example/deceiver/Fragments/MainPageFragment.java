package com.example.deceiver.Fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deceiver.Activities.GameHistoryActivity;
import com.example.deceiver.Activities.MainActivity;
import com.example.deceiver.Activities.StandardGameActivity;
import com.example.deceiver.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainPageFragment extends Fragment {

    View objectMainPageFragment;
    private FirebaseFirestore fbfs;
    private FirebaseAuth fba;
    private Button btnPlay,btnIntro,btnProfile;
    private TextView logOut;
    private TextView usernameProfile,dateProfile,gamesProfile,winsProfile,defeatsProfile;
    private Button showGameHistory;
    private AlertDialog.Builder dialogBuilder;

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
        btnProfile=objectMainPageFragment.findViewById(R.id.btnProfileMainPage);
        logOut=objectMainPageFragment.findViewById(R.id.logOutMainPage);

        btnIntro.setOnClickListener(view -> {
            IntroductionFragment introductionFragment=new IntroductionFragment();
            FragmentManager manager=getFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.frameLayoutMainPage,introductionFragment,introductionFragment.getTag())
                    .addToBackStack(null)
                    .commit();
        });

        btnPlay.setOnClickListener(view -> {
            Intent standardGameActivityIntent = new Intent(getContext(), StandardGameActivity.class);
            startActivity(standardGameActivityIntent);
        });

        btnProfile.setOnClickListener(view ->
                createProfilePopUp());

        logOut.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(getContext(), "User signed out", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(getContext(),MainActivity.class);
            startActivity(i);
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        fbfs=FirebaseFirestore.getInstance();
        fba=FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        objectMainPageFragment=inflater.inflate(R.layout.fragment_main_page,container,false);
        attachComponents();

        return objectMainPageFragment;
    }

    public void createProfilePopUp(){
        dialogBuilder=new AlertDialog.Builder(getContext());
        final View contactPopUpView=getLayoutInflater().inflate(R.layout.profilepopup,null);

        fbfs=FirebaseFirestore.getInstance();
        fba=FirebaseAuth.getInstance();

        usernameProfile=contactPopUpView.findViewById(R.id.usernameProfile);
        dateProfile=contactPopUpView.findViewById(R.id.dateProfile);
        gamesProfile=contactPopUpView.findViewById(R.id.gamesPlayedProfile);
        winsProfile=contactPopUpView.findViewById(R.id.gamesWonProfile);
        defeatsProfile=contactPopUpView.findViewById(R.id.gamesLostProfile);
        showGameHistory=contactPopUpView.findViewById(R.id.gameHistoryProfile);

        showGameHistory.setOnClickListener(view -> {
            Intent gameHistoryActivityIntent = new Intent(getContext(), GameHistoryActivity.class);
            startActivity(gameHistoryActivityIntent);
        });

        DocumentReference newUserRe=fbfs.collection("users").document(fba.getCurrentUser().getEmail());
        newUserRe.get().addOnSuccessListener(documentSnapshot -> {
            usernameProfile.setText(documentSnapshot.getString("Username"));
            dateProfile.setText("Detective since "+documentSnapshot.getString("CreationDate"));
            gamesProfile.setText(documentSnapshot.getLong("GamesPlayed").toString()+" games played");
            winsProfile.setText(documentSnapshot.getLong("Wins").toString()+" games won");
            defeatsProfile.setText(documentSnapshot.getLong("Losses").toString()+" games lost");
        });

        dialogBuilder.setView(contactPopUpView);
        dialogBuilder.create().show();
    }
}