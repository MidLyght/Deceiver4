package com.example.deceiver.Fragments;

import static android.content.ContentValues.TAG;
import static com.example.deceiver.Enums.StandardRole.Deceiver;
import static com.example.deceiver.Enums.StandardRole.Seer;
import static com.example.deceiver.Enums.StandardRole.Traitor;

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
import com.example.deceiver.DataClasses.StandardCharacter;
import com.example.deceiver.Enums.Phase;
import com.example.deceiver.Enums.StandardRole;
import com.example.deceiver.FirebaseServices;
import com.example.deceiver.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StandardGameNightFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StandardGameNightFragment extends Fragment {

    View objectStandardGameNightFragment;
    private FirebaseServices fbs;
    private StandardCharacter deceiver,traitor,farmer1,farmer2,witch,blacksmith,seer,guard;
    private ImageView c1,c2,c3,c4,c5,c6,c7,c8,c1dead,c2dead,c3dead,c4dead,c5dead,c6dead,c7dead,c8dead,c1role,c2role,c3role,c4role,c5role,c6role,c7role,c8role,nextPhase;
    private TextView dayNum;
    private ArrayList<StandardCharacter> order;
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

    public StandardGameNightFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameNightFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StandardGameNightFragment newInstance(String param1, String param2) {
        StandardGameNightFragment fragment = new StandardGameNightFragment();
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
        objectStandardGameNightFragment=inflater.inflate(R.layout.fragment_game_night,container,false);
        attachComponents();
        nightPowers();
        checkGame();

        return objectStandardGameNightFragment;
    }

    private void checkGame(){
        StandardGameActivity sga=(StandardGameActivity) getActivity();

        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.HOUR,+3); // Because the Israeli timezone is GMT+3
        Date date1= cal.getTime() ;
        String datestring = DateFormat.getInstance().format(date1);

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
                    double zeroes=gamesplayed+1;
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

    private void attachComponents() {
        StandardGameActivity sga=(StandardGameActivity) getActivity();
        fbs=FirebaseServices.getInstance();
        sga.nightCount++;

        deceiver=sga.deceiver;
        traitor=sga.traitor;
        witch=sga.witch;
        farmer1=sga.farmer1;
        farmer2=sga.farmer2;
        blacksmith=sga.blacksmith;
        seer=sga.seer;
        guard=sga.guard;

        order=sga.order;

        c1=objectStandardGameNightFragment.findViewById(R.id.imgGameNightChar1);
        c2=objectStandardGameNightFragment.findViewById(R.id.imgGameNightChar2);
        c3=objectStandardGameNightFragment.findViewById(R.id.imgGameNightChar3);
        c4=objectStandardGameNightFragment.findViewById(R.id.imgGameNightChar4);
        c5=objectStandardGameNightFragment.findViewById(R.id.imgGameNightChar5);
        c6=objectStandardGameNightFragment.findViewById(R.id.imgGameNightChar6);
        c7=objectStandardGameNightFragment.findViewById(R.id.imgGameNightChar7);
        c8=objectStandardGameNightFragment.findViewById(R.id.imgGameNightChar8);

        c1dead=objectStandardGameNightFragment.findViewById(R.id.imgGameNightChar1Dead);
        c2dead=objectStandardGameNightFragment.findViewById(R.id.imgGameNightChar2Dead);
        c3dead=objectStandardGameNightFragment.findViewById(R.id.imgGameNightChar3Dead);
        c4dead=objectStandardGameNightFragment.findViewById(R.id.imgGameNightChar4Dead);
        c5dead=objectStandardGameNightFragment.findViewById(R.id.imgGameNightChar5Dead);
        c6dead=objectStandardGameNightFragment.findViewById(R.id.imgGameNightChar6Dead);
        c7dead=objectStandardGameNightFragment.findViewById(R.id.imgGameNightChar7Dead);
        c8dead=objectStandardGameNightFragment.findViewById(R.id.imgGameNightChar8Dead);

        c1role=objectStandardGameNightFragment.findViewById(R.id.imgGameNightChar1Role);
        c2role=objectStandardGameNightFragment.findViewById(R.id.imgGameNightChar2Role);
        c3role=objectStandardGameNightFragment.findViewById(R.id.imgGameNightChar3Role);
        c4role=objectStandardGameNightFragment.findViewById(R.id.imgGameNightChar4Role);
        c5role=objectStandardGameNightFragment.findViewById(R.id.imgGameNightChar5Role);
        c6role=objectStandardGameNightFragment.findViewById(R.id.imgGameNightChar6Role);
        c7role=objectStandardGameNightFragment.findViewById(R.id.imgGameNightChar7Role);
        c8role=objectStandardGameNightFragment.findViewById(R.id.imgGameNightChar8Role);

        dayNum=objectStandardGameNightFragment.findViewById(R.id.txtGameNightDayNum);
        dayNum.setText("Day "+sga.dayCount);

        nextPhase=objectStandardGameNightFragment.findViewById(R.id.imgGameNightNextPhase);

        nextPhase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deceiver.getWoundCounter() == 2) {
                        deceiver.setWoundCounter(0);
                        deceiver.setHeavilyWounded(false);
                        traitor.setWoundCounter(0);
                        traitor.setHeavilyWounded(false);
                    }
                    if (deceiver.getWoundCounter() == 1) {
                        deceiver.setWoundCounter(2);
                        traitor.setWoundCounter(2);
                    }
                    StandardGameNightLogFragment standardGameNightLogFragment = new StandardGameNightLogFragment();
                    FragmentManager manager = getFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.frameLayoutGame, standardGameNightLogFragment, standardGameNightLogFragment.getTag())
                            .disallowAddToBackStack()
                            .commit();
            }
        });

        if(!order.get(0).isAlive())
            c1dead.setVisibility(View.VISIBLE);
        if(!order.get(1).isAlive())
            c2dead.setVisibility(View.VISIBLE);
        if(!order.get(2).isAlive())
            c3dead.setVisibility(View.VISIBLE);
        if(!order.get(3).isAlive())
            c4dead.setVisibility(View.VISIBLE);
        if(!order.get(4).isAlive())
            c5dead.setVisibility(View.VISIBLE);
        if(!order.get(5).isAlive())
            c6dead.setVisibility(View.VISIBLE);
        if(!order.get(6).isAlive())
            c7dead.setVisibility(View.VISIBLE);
        if(!order.get(7).isAlive())
            c8dead.setVisibility(View.VISIBLE);

        if(order.get(0).getRole()== StandardRole.Deceiver)
            c1role.setImageResource(R.drawable.eye);
        if(order.get(0).getRole()==StandardRole.Traitor)
            c1role.setImageResource(R.drawable.traitoricon);
        if(order.get(0).getRole()==StandardRole.Witch)
            c1role.setImageResource(R.drawable.witchiconrole);
        if(order.get(0).getRole()==StandardRole.Blacksmith)
            c1role.setImageResource(R.drawable.blacksmithicon);
        if(order.get(0).getRole()==StandardRole.Farmer)
            c1role.setImageResource(R.drawable.farmericon);
        if(order.get(0).getRole()== Seer)
            c1role.setImageResource(R.drawable.seericon);
        if(order.get(0).getRole()==StandardRole.Guard)
            c1role.setImageResource(R.drawable.shieldicon);

        if(order.get(1).getRole()==StandardRole.Deceiver)
            c2role.setImageResource(R.drawable.eye);
        if(order.get(1).getRole()==StandardRole.Traitor)
            c2role.setImageResource(R.drawable.traitoricon);
        if(order.get(1).getRole()==StandardRole.Witch)
            c2role.setImageResource(R.drawable.witchiconrole);
        if(order.get(1).getRole()==StandardRole.Blacksmith)
            c2role.setImageResource(R.drawable.blacksmithicon);
        if(order.get(1).getRole()==StandardRole.Farmer)
            c2role.setImageResource(R.drawable.farmericon);
        if(order.get(1).getRole()== Seer)
            c2role.setImageResource(R.drawable.seericon);
        if(order.get(1).getRole()==StandardRole.Guard)
            c2role.setImageResource(R.drawable.shieldicon);

        if(order.get(2).getRole()==StandardRole.Deceiver)
            c3role.setImageResource(R.drawable.eye);
        if(order.get(2).getRole()==StandardRole.Traitor)
            c3role.setImageResource(R.drawable.traitoricon);
        if(order.get(2).getRole()==StandardRole.Witch)
            c3role.setImageResource(R.drawable.witchiconrole);
        if(order.get(2).getRole()==StandardRole.Blacksmith)
            c3role.setImageResource(R.drawable.blacksmithicon);
        if(order.get(2).getRole()==StandardRole.Farmer)
            c3role.setImageResource(R.drawable.farmericon);
        if(order.get(2).getRole()== Seer)
            c3role.setImageResource(R.drawable.seericon);
        if(order.get(2).getRole()==StandardRole.Guard)
            c3role.setImageResource(R.drawable.shieldicon);

        if(order.get(3).getRole()==StandardRole.Deceiver)
            c4role.setImageResource(R.drawable.eye);
        if(order.get(3).getRole()==StandardRole.Traitor)
            c4role.setImageResource(R.drawable.traitoricon);
        if(order.get(3).getRole()==StandardRole.Witch)
            c4role.setImageResource(R.drawable.witchiconrole);
        if(order.get(3).getRole()==StandardRole.Blacksmith)
            c4role.setImageResource(R.drawable.blacksmithicon);
        if(order.get(3).getRole()==StandardRole.Farmer)
            c4role.setImageResource(R.drawable.farmericon);
        if(order.get(3).getRole()== Seer)
            c4role.setImageResource(R.drawable.seericon);
        if(order.get(3).getRole()==StandardRole.Guard)
            c4role.setImageResource(R.drawable.shieldicon);

        if(order.get(4).getRole()==StandardRole.Deceiver)
            c5role.setImageResource(R.drawable.eye);
        if(order.get(4).getRole()==StandardRole.Traitor)
            c5role.setImageResource(R.drawable.traitoricon);
        if(order.get(4).getRole()==StandardRole.Witch)
            c5role.setImageResource(R.drawable.witchiconrole);
        if(order.get(4).getRole()==StandardRole.Blacksmith)
            c5role.setImageResource(R.drawable.blacksmithicon);
        if(order.get(4).getRole()==StandardRole.Farmer)
            c5role.setImageResource(R.drawable.farmericon);
        if(order.get(4).getRole()== Seer)
            c5role.setImageResource(R.drawable.seericon);
        if(order.get(4).getRole()==StandardRole.Guard)
            c5role.setImageResource(R.drawable.shieldicon);

        if(order.get(5).getRole()==StandardRole.Deceiver)
            c6role.setImageResource(R.drawable.eye);
        if(order.get(5).getRole()==StandardRole.Traitor)
            c6role.setImageResource(R.drawable.traitoricon);
        if(order.get(5).getRole()==StandardRole.Witch)
            c6role.setImageResource(R.drawable.witchiconrole);
        if(order.get(5).getRole()==StandardRole.Blacksmith)
            c6role.setImageResource(R.drawable.blacksmithicon);
        if(order.get(5).getRole()==StandardRole.Farmer)
            c6role.setImageResource(R.drawable.farmericon);
        if(order.get(5).getRole()== Seer)
            c6role.setImageResource(R.drawable.seericon);
        if(order.get(5).getRole()==StandardRole.Guard)
            c6role.setImageResource(R.drawable.shieldicon);

        if(order.get(6).getRole()==StandardRole.Deceiver)
            c7role.setImageResource(R.drawable.eye);
        if(order.get(6).getRole()==StandardRole.Traitor)
            c7role.setImageResource(R.drawable.traitoricon);
        if(order.get(6).getRole()==StandardRole.Witch)
            c7role.setImageResource(R.drawable.witchiconrole);
        if(order.get(6).getRole()==StandardRole.Blacksmith)
            c7role.setImageResource(R.drawable.blacksmithicon);
        if(order.get(6).getRole()==StandardRole.Farmer)
            c7role.setImageResource(R.drawable.farmericon);
        if(order.get(6).getRole()== Seer)
            c7role.setImageResource(R.drawable.seericon);
        if(order.get(6).getRole()==StandardRole.Guard)
            c7role.setImageResource(R.drawable.shieldicon);

        if(order.get(7).getRole()==StandardRole.Deceiver)
            c8role.setImageResource(R.drawable.eye);
        if(order.get(7).getRole()==StandardRole.Traitor)
            c8role.setImageResource(R.drawable.traitoricon);
        if(order.get(7).getRole()==StandardRole.Witch)
            c8role.setImageResource(R.drawable.witchiconrole);
        if(order.get(7).getRole()==StandardRole.Blacksmith)
            c8role.setImageResource(R.drawable.blacksmithicon);
        if(order.get(7).getRole()==StandardRole.Farmer)
            c8role.setImageResource(R.drawable.farmericon);
        if(order.get(7).getRole()== Seer)
            c8role.setImageResource(R.drawable.seericon);
        if(order.get(7).getRole()==StandardRole.Guard)
            c8role.setImageResource(R.drawable.shieldicon);

        if(order.get(0).isExposed())
            c1role.setVisibility(View.VISIBLE);
        if(order.get(1).isExposed())
            c2role.setVisibility(View.VISIBLE);
        if(order.get(2).isExposed())
            c3role.setVisibility(View.VISIBLE);
        if(order.get(3).isExposed())
            c4role.setVisibility(View.VISIBLE);
        if(order.get(4).isExposed())
            c5role.setVisibility(View.VISIBLE);
        if(order.get(5).isExposed())
            c6role.setVisibility(View.VISIBLE);
        if(order.get(6).isExposed())
            c7role.setVisibility(View.VISIBLE);
        if(order.get(7).isExposed())
            c8role.setVisibility(View.VISIBLE);

        if(sga.dawnCount==3)
            blacksmith.setHasSword(true);
    }

    private void nightPowers(){
        StandardGameActivity sga=(StandardGameActivity) getActivity();

        if((deceiver.isAlive()&&!deceiver.isHeavilyWounded())||(traitor.isAlive()&&!traitor.isHeavilyWounded()&&!deceiver.isAlive())){
            Random random=new Random();
            int val=random.nextInt(8);
            while(order.get(val).isAlive()==false || order.get(val).getRole()==Deceiver || order.get(val).getRole()==Traitor){
                val=random.nextInt(8);
            }
            if(order.get(val).isHasSword()){
                deceiver.setHeavilyWounded(true);
                traitor.setHeavilyWounded(true);
                deceiver.setWoundCounter(1);
                traitor.setWoundCounter(1);
                blacksmith.setExposed(true);
                sga.nightLog+="The blacksmith has heavily wounded the deceiver!\n";
            }
            else if(order.get(val).isVivified()){
                //
                sga.nightLog+="Character "+(val+1)+" was saved by the witch!\n";
                order.get(val).setVivified(false);
            }
            else if(order.get(val).isProtected()){
                guard.setExposed(true);
                guard.setAlive(false);
                sga.nightLog+="The guard died protecting character "+(val+1)+".\n";
                order.get(val).setProtected(false);
            }
            else{
                order.get(val).setAlive(false);
                sga.nightLog+="Character "+(val+1)+" died.\n";

                if(order.get(val).getRole()==Seer){
                    val= random.nextInt(8);
                    while(!(order.get(val).isAlive())||(order.get(val)==seer)||(order.get(val).isExposed())){
                        val=random.nextInt(8);
                    }
                    order.get(val).setExposed(true);
                    sga.nightLog+="The seer has revealed the identity of character "+(val+1)+" before dying.\n";
                }
            }
        }

        deceiver.setVivified(false);
        traitor.setVivified(false);
        witch.setVivified(false);
        guard.setVivified(false);
        farmer1.setVivified(false);
        farmer2.setVivified(false);
        seer.setVivified(false);
        blacksmith.setVivified(false);

        deceiver.setProtected(false);
        traitor.setProtected(false);
        witch.setProtected(false);
        guard.setProtected(false);
        farmer1.setProtected(false);
        farmer2.setProtected(false);
        seer.setProtected(false);
        blacksmith.setProtected(false);

        sga.deceiver=deceiver;
        sga.traitor=traitor;
        sga.witch=witch;
        sga.farmer1=farmer1;
        sga.farmer2=farmer2;
        sga.blacksmith=blacksmith;
        sga.seer=seer;
        sga.guard=guard;

        sga.order=order;
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