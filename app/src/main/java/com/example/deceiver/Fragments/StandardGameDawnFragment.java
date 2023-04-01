package com.example.deceiver.Fragments;

import static android.content.ContentValues.TAG;
import static com.example.deceiver.Enums.StandardRole.Blacksmith;
import static com.example.deceiver.Enums.StandardRole.Deceiver;
import static com.example.deceiver.Enums.StandardRole.Farmer;
import static com.example.deceiver.Enums.StandardRole.Guard;
import static com.example.deceiver.Enums.StandardRole.Seer;
import static com.example.deceiver.Enums.StandardRole.Traitor;
import static com.example.deceiver.Enums.StandardRole.Witch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.example.deceiver.Enums.StandardTeam;
import com.example.deceiver.FirebaseServices;
import com.example.deceiver.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StandardGameDawnFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StandardGameDawnFragment extends Fragment {

    View objectStandardGameDawnFragment;
    private StandardCharacter deceiver,traitor,farmer1,farmer2,witch,blacksmith,seer,guard;
    private ImageView c1,c2,c3,c4,c5,c6,c7,c8,c1dead,c2dead,c3dead,c4dead,c5dead,c6dead,c7dead,c8dead,c1role,c2role,c3role,c4role,c5role,c6role,c7role,c8role,nextPhase;
    private TextView dayNum;
    private ArrayList<StandardCharacter> order;
    private AlertDialog.Builder dialogBuilderDec,dialogBuilderVil;
    private AlertDialog deceiverDialog,villageDialog;
    private TextView decDays,decDawns,decNights,vilDays,vilDawns,vilNights;
    private Button decRestart,decMenu,vilRestart,vilMenu;
    private FirebaseServices fbs;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StandardGameDawnFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameDawnFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StandardGameDawnFragment newInstance(String param1, String param2) {
        StandardGameDawnFragment fragment = new StandardGameDawnFragment();
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
        StandardGameActivity sga=(StandardGameActivity) getActivity();

        objectStandardGameDawnFragment=inflater.inflate(R.layout.fragment_game_dawn,container,false);
        if(sga.dawnCount==1)
            createCharacters();
        attachComponents();
        dawnPowers();
        checkGame();

        return objectStandardGameDawnFragment;
    }

    private void checkGame(){
        StandardGameActivity sga=(StandardGameActivity) getActivity();

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
    }

    private void dawnPowers(){
        StandardGameActivity sga=(StandardGameActivity) getActivity();

        if(guard.isAlive()){
            Random random=new Random();
            int val=random.nextInt(8);
            while(order.get(val).isAlive()==false||order.get(val)==guard){
                val=random.nextInt(8);
            }
            order.get(val).setProtected(true);
            sga.dawnLog+="The guard has chosen to protect character "+(val+1)+".\n";
        }

        if(witch.isAlive()&&witch.isCanVivify()){
            Random random=new Random();
            int val=random.nextInt(8);
            while(order.get(val).isAlive()==false){
                val=random.nextInt(8);
            }
            order.get(val).setVivified(true);
            witch.setCanVivify(false);
        }

        if(seer.isAlive() && sga.dawnCount%3==0){
            Random random=new Random();
            int val=random.nextInt(8);
            while(!order.get(val).isAlive()||order.get(val)==seer||order.get(val).isExposed()){
                val=random.nextInt(8);
            }
            order.get(val).setExposed(true);
            sga.dawnLog+="The seer has revealed the identity of character "+(val+1)+"!\n";
        }

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

    public void attachComponents(){
        StandardGameActivity sga=(StandardGameActivity) getActivity();
        fbs=FirebaseServices.getInstance();

        deceiver=sga.deceiver;
        traitor=sga.traitor;
        witch=sga.witch;
        farmer1=sga.farmer1;
        farmer2=sga.farmer2;
        blacksmith=sga.blacksmith;
        seer=sga.seer;
        guard=sga.guard;

        order=sga.order;

        c1=objectStandardGameDawnFragment.findViewById(R.id.imgGameDawnChar1);
        c2=objectStandardGameDawnFragment.findViewById(R.id.imgGameDawnChar2);
        c3=objectStandardGameDawnFragment.findViewById(R.id.imgGameDawnChar3);
        c4=objectStandardGameDawnFragment.findViewById(R.id.imgGameDawnChar4);
        c5=objectStandardGameDawnFragment.findViewById(R.id.imgGameDawnChar5);
        c6=objectStandardGameDawnFragment.findViewById(R.id.imgGameDawnChar6);
        c7=objectStandardGameDawnFragment.findViewById(R.id.imgGameDawnChar7);
        c8=objectStandardGameDawnFragment.findViewById(R.id.imgGameDawnChar8);

        c1dead=objectStandardGameDawnFragment.findViewById(R.id.imgGameDawnChar1Dead);
        c2dead=objectStandardGameDawnFragment.findViewById(R.id.imgGameDawnChar2Dead);
        c3dead=objectStandardGameDawnFragment.findViewById(R.id.imgGameDawnChar3Dead);
        c4dead=objectStandardGameDawnFragment.findViewById(R.id.imgGameDawnChar4Dead);
        c5dead=objectStandardGameDawnFragment.findViewById(R.id.imgGameDawnChar5Dead);
        c6dead=objectStandardGameDawnFragment.findViewById(R.id.imgGameDawnChar6Dead);
        c7dead=objectStandardGameDawnFragment.findViewById(R.id.imgGameDawnChar7Dead);
        c8dead=objectStandardGameDawnFragment.findViewById(R.id.imgGameDawnChar8Dead);

        c1role=objectStandardGameDawnFragment.findViewById(R.id.imgGameDawnChar1Role);
        c2role=objectStandardGameDawnFragment.findViewById(R.id.imgGameDawnChar2Role);
        c3role=objectStandardGameDawnFragment.findViewById(R.id.imgGameDawnChar3Role);
        c4role=objectStandardGameDawnFragment.findViewById(R.id.imgGameDawnChar4Role);
        c5role=objectStandardGameDawnFragment.findViewById(R.id.imgGameDawnChar5Role);
        c6role=objectStandardGameDawnFragment.findViewById(R.id.imgGameDawnChar6Role);
        c7role=objectStandardGameDawnFragment.findViewById(R.id.imgGameDawnChar7Role);
        c8role=objectStandardGameDawnFragment.findViewById(R.id.imgGameDawnChar8Role);

        dayNum=objectStandardGameDawnFragment.findViewById(R.id.txtGameDawnDayNum);
        dayNum.setText("Day "+sga.dayCount);

        nextPhase=objectStandardGameDawnFragment.findViewById(R.id.imgGameDawnNextPhase);

        nextPhase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sga.dawnCount++;
                StandardGameDawnLogFragment standardGameDawnLogFragment=new StandardGameDawnLogFragment();
                FragmentManager manager=getFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.frameLayoutGame,standardGameDawnLogFragment,standardGameDawnLogFragment.getTag())
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

        if(order.get(0).getRole()==StandardRole.Deceiver)
            c1role.setImageResource(R.drawable.eye);
        if(order.get(0).getRole()==StandardRole.Traitor)
            c1role.setImageResource(R.drawable.traitoricon);
        if(order.get(0).getRole()==StandardRole.Witch)
            c1role.setImageResource(R.drawable.witchiconrole);
        if(order.get(0).getRole()==StandardRole.Blacksmith)
            c1role.setImageResource(R.drawable.blacksmithicon);
        if(order.get(0).getRole()==StandardRole.Farmer)
            c1role.setImageResource(R.drawable.farmericon);
        if(order.get(0).getRole()==StandardRole.Seer)
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
        if(order.get(1).getRole()==StandardRole.Seer)
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
        if(order.get(2).getRole()==StandardRole.Seer)
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
        if(order.get(3).getRole()==StandardRole.Seer)
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
        if(order.get(4).getRole()==StandardRole.Seer)
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
        if(order.get(5).getRole()==StandardRole.Seer)
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
        if(order.get(6).getRole()==StandardRole.Seer)
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
        if(order.get(7).getRole()==StandardRole.Seer)
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
    }

    private void createCharacters(){
        StandardGameActivity sga=(StandardGameActivity) getActivity();

        deceiver=sga.deceiver;
        traitor=sga.traitor;
        witch=sga.witch;
        blacksmith=sga.blacksmith;
        farmer1=sga.farmer1;
        farmer2=sga.farmer2;
        seer=sga.seer;
        guard=sga.guard;

        order=sga.order;
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