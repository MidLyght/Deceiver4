package com.example.deceiver.Fragments;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.deceiver.Activities.MainPageActivity;
import com.example.deceiver.Activities.StandardGameActivity;
import com.example.deceiver.Enums.Phase;
import com.example.deceiver.FirebaseServices;
import com.example.deceiver.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StandardGameDawnLogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StandardGameDawnLogFragment extends Fragment {

    private TextView dawnLog;
    private FirebaseServices fbs;
    private ImageView nextPhase;
    public StandardGameActivity sga;
    View objectStandardGameDawnLogFragment;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog deceiverDialog,villageDialog;
    private TextView decDays,decDawns,decNights,vilDays,vilDawns,vilNights;
    private Button decRestart,decMenu,vilRestart,vilMenu;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StandardGameDawnLogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameDawnLogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StandardGameDawnLogFragment newInstance(String param1, String param2) {
        StandardGameDawnLogFragment fragment = new StandardGameDawnLogFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        objectStandardGameDawnLogFragment=inflater.inflate(R.layout.fragment_game_dawn_log,container,false);

        sga=(StandardGameActivity) getActivity();
        fbs=FirebaseServices.getInstance();

        dawnLog=objectStandardGameDawnLogFragment.findViewById(R.id.textView28);
        nextPhase=objectStandardGameDawnLogFragment.findViewById(R.id.imgGameDawnLogNextPhase);

        dawnLog.setText(sga.dawnLog);
        nextPhase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!sga.deceiver.isAlive()&&!sga.traitor.isAlive()){
                    DocumentReference newUserRe=fbs.getFire().collection("users").document(fbs.getAuth().getCurrentUser().getEmail());

                    newUserRe.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String username=documentSnapshot.getString("Username");
                            String password=documentSnapshot.getString("Password");
                            double wins=documentSnapshot.getDouble("Wins");
                            double losses=documentSnapshot.getDouble("Losses");
                            double gamesplayed=documentSnapshot.getDouble("GamesPlayed");

                            Map<String,Object> user=new HashMap<>();
                            user.put("Username",username);
                            user.put("Password",password);
                            user.put("Wins",wins+1);
                            user.put("Losses",losses);
                            user.put("GamesPlayed",gamesplayed+1);

                            newUserRe.set(user);
                        }
                    });

                    createVillageWinPopup();
                }

                sga.deceiverCount=2;

                if(!sga.deceiver.isAlive()&&sga.traitor.isAlive()||sga.deceiver.isAlive()&&!sga.traitor.isAlive())
                    sga.deceiverCount=1;

                sga.villagerCount=0;

                if(sga.witch.isAlive())
                    sga.villagerCount++;
                if(sga.farmer1.isAlive())
                    sga.villagerCount++;
                if(sga.farmer2.isAlive())
                    sga.villagerCount++;
                if(sga.blacksmith.isAlive())
                    sga.villagerCount++;
                if(sga.seer.isAlive())
                    sga.villagerCount++;
                if(sga.guard.isAlive())
                    sga.villagerCount++;

                if(sga.villagerCount<=sga.deceiverCount){
                    DocumentReference newUserRe=fbs.getFire().collection("users").document(fbs.getAuth().getCurrentUser().getEmail());

                    newUserRe.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String username=documentSnapshot.getString("Username");
                            String password=documentSnapshot.getString("Password");
                            double wins=documentSnapshot.getDouble("Wins");
                            double losses=documentSnapshot.getDouble("Losses");
                            double gamesplayed=documentSnapshot.getDouble("GamesPlayed");

                            Map<String,Object> user=new HashMap<>();
                            user.put("Username",username);
                            user.put("Password",password);
                            user.put("Wins",wins);
                            user.put("Losses",losses+1);
                            user.put("GamesPlayed",gamesplayed+1);

                            newUserRe.set(user);
                        }
                    });
                    createDeceiverWinPopup();
                }

                StandardGameNightFragment standardGameNightFragment=new StandardGameNightFragment();
                FragmentManager manager=getFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.frameLayoutGame,standardGameNightFragment,standardGameNightFragment.getTag())
                        .commit();
            }
        });

        return objectStandardGameDawnLogFragment;
    }
    public void createDeceiverWinPopup(){
        dialogBuilder=new AlertDialog.Builder(getContext())
                .setTitle("Defeat")
                .setMessage("You have failed your duty as the village's detective")
                .setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent i1=new Intent(getContext(),StandardGameActivity.class);
                        startActivity(i1);
                    }
                })
                .setNegativeButton("Main Menu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent i2=new Intent(getContext(),MainPageActivity.class);
                    }
                });

        deceiverDialog=dialogBuilder.create();
        deceiverDialog.show();
    }

    public void createVillageWinPopup(){
        dialogBuilder=new AlertDialog.Builder(getContext())
                .setTitle("Victory")
                .setMessage("You have figured out the identities of the deceivers and done your duty as the village's detective")
                .setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent i1=new Intent(getContext(),StandardGameActivity.class);
                        startActivity(i1);
                    }
                })
                .setNegativeButton("Main Menu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent i2=new Intent(getContext(),MainPageActivity.class);
                    }
                });

        villageDialog=dialogBuilder.create();
        villageDialog.show();
    }
}