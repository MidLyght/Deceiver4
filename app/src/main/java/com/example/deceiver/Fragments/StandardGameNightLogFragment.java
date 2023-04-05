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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StandardGameNightLogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StandardGameNightLogFragment extends Fragment {

    public View objectStandardGameNightLogFragment;
    private FirebaseServices fbs;
    private TextView nightLog;
    private ImageView nextPhase;
    public StandardGameActivity sga;

    private AlertDialog.Builder dialogBuilderDec,dialogBuilderVil;
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

    public StandardGameNightLogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameNightLogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StandardGameNightLogFragment newInstance(String param1, String param2) {
        StandardGameNightLogFragment fragment = new StandardGameNightLogFragment();
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
        objectStandardGameNightLogFragment=inflater.inflate(R.layout.fragment_game_night_log,container,false);

        sga=(StandardGameActivity) getActivity();
        fbs=FirebaseServices.getInstance();

        nightLog=objectStandardGameNightLogFragment.findViewById(R.id.textView29);
        nextPhase=objectStandardGameNightLogFragment.findViewById(R.id.imgGameNightLogNextPhase);

        nightLog.setText(sga.nightLog);
        nextPhase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    StandardGameDayFragment standardGameDayFragment = new StandardGameDayFragment();
                    FragmentManager manager = getFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.frameLayoutGame, standardGameDayFragment, standardGameDayFragment.getTag())
                            .disallowAddToBackStack()
                            .commit();
                }
        });

        checkGame();

        return objectStandardGameNightLogFragment;
    }

    private void checkGame(){
        StandardGameActivity sga=(StandardGameActivity) getActivity();

        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.HOUR,+3); // Because the Israeli timezone is GMT+3
        Date date1= cal.getTime();
        String datestring = DateFormat.getInstance().format(date1); // Game date

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
                    double zeroes=documentSnapshot.getDouble("GamesPlayed")+1;
                    String formattedDate=documentSnapshot.getString("CreationDate");
                    int zerocount=0;
                    while((((int)zeroes/10))!=0){
                        zeroes=zeroes/10.0;
                        zerocount++;
                    }
                    String gameDoc=Integer.toString(zerocount)+Double.toString(gamesplayed+1);

                    Map<String,Object> user=new HashMap<>();
                    user.put("Username",username);
                    user.put("Password",password);
                    user.put("Wins",wins+1);
                    user.put("Losses",losses);
                    user.put("GamesPlayed",gamesplayed+1);
                    user.put("CreationDate",formattedDate);

                    DocumentReference newUserGameRe= newUserRe.collection("gamehistory").document(gameDoc);

                    int bodyCount=0;

                    if(!sga.deceiver.isAlive())
                        bodyCount++;
                    if(!sga.traitor.isAlive())
                        bodyCount++;
                    if(!sga.witch.isAlive())
                        bodyCount++;
                    if(!sga.farmer1.isAlive())
                        bodyCount++;
                    if(!sga.farmer2.isAlive())
                        bodyCount++;
                    if(!sga.blacksmith.isAlive())
                        bodyCount++;
                    if(!sga.seer.isAlive())
                        bodyCount++;
                    if(!sga.guard.isAlive())
                        bodyCount++;

                    Map<String,Object> game=new HashMap<>();
                    game.put("Dawns",sga.dawnCount);
                    game.put("Days",sga.dayCount);
                    game.put("Nights",sga.nightCount);
                    game.put("BodyCount",bodyCount);
                    game.put("Result","Victory");
                    game.put("Date",datestring);

                    newUserRe.set(user);
                    newUserGameRe.set(game);
                }
            });

            createVillageWinPopup();
            return;
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
                    String formattedDate=documentSnapshot.getString("CreationDate");
                    double zeroes=documentSnapshot.getDouble("GamesPlayed")+1;
                    int zerocount=0;
                    while((((int)zeroes/10))!=0){
                        zeroes=zeroes/10.0;
                        zerocount++;
                    }
                    String gameDoc=Integer.toString(zerocount)+Double.toString(gamesplayed+1);

                    Map<String,Object> user=new HashMap<>();
                    user.put("Username",username);
                    user.put("Password",password);
                    user.put("Wins",wins);
                    user.put("Losses",losses+1);
                    user.put("GamesPlayed",gamesplayed+1);
                    user.put("CreationDate",formattedDate);

                    DocumentReference newUserGameRe= newUserRe.collection("gamehistory").document(gameDoc);

                    int bodyCount=0;

                    if(!sga.deceiver.isAlive())
                        bodyCount++;
                    if(!sga.traitor.isAlive())
                        bodyCount++;
                    if(!sga.witch.isAlive())
                        bodyCount++;
                    if(!sga.farmer1.isAlive())
                        bodyCount++;
                    if(!sga.farmer2.isAlive())
                        bodyCount++;
                    if(!sga.blacksmith.isAlive())
                        bodyCount++;
                    if(!sga.seer.isAlive())
                        bodyCount++;
                    if(!sga.guard.isAlive())
                        bodyCount++;

                    Map<String,Object> game=new HashMap<>();
                    game.put("Dawns",sga.dawnCount);
                    game.put("Days",sga.dayCount);
                    game.put("Nights",sga.nightCount);
                    game.put("BodyCount",bodyCount);
                    game.put("Result","Defeat");
                    game.put("Date",datestring);

                    newUserRe.set(user);
                    newUserGameRe.set(game);
                }
            });
            createDeceiverWinPopup();
        }
    }

    public void createDeceiverWinPopup(){
        dialogBuilderDec=new AlertDialog.Builder(getContext())
                .setTitle("Defeat")
                .setCancelable(false)
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
                        startActivity(i2);
                    }
                });

        deceiverDialog=dialogBuilderDec.create();
        deceiverDialog.show();
    }

    public void createVillageWinPopup(){
        dialogBuilderVil=new AlertDialog.Builder(getContext())
                .setTitle("Victory")
                .setCancelable(false)
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
                        startActivity(i2);
                    }
                });

        villageDialog=dialogBuilderVil.create();
        villageDialog.show();
    }
}