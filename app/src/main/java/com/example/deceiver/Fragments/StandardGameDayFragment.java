package com.example.deceiver.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.deceiver.Activities.MainPageActivity;
import com.example.deceiver.Activities.StandardGameActivity;
import com.example.deceiver.DataClasses.StandardCharacter;
import com.example.deceiver.Enums.StandardRole;
import com.example.deceiver.FirebaseServices;
import com.example.deceiver.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StandardGameDayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StandardGameDayFragment extends Fragment {

    private View objectStandardGameDayFragment;
    private FirebaseServices fbs;
    public StandardGameActivity sga;
    private ImageView c1,c2,c3,c4,c5,c6,c7,c8,c1dead,c2dead,c3dead,c4dead,c5dead,c6dead,c7dead,c8dead,c1role,c2role,c3role,c4role,c5role,c6role,c7role,c8role,c1lynch,c2lynch,c3lynch,c4lynch,c5lynch,c6lynch,c7lynch,c8lynch,nextPhase;
    private TextView dayNum;
    ArrayList<StandardCharacter> order;
    private AlertDialog.Builder dialogBuilderDec,dialogBuilderVil;
    private AlertDialog deceiverDialog,villageDialog;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StandardGameDayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameDayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StandardGameDayFragment newInstance(String param1, String param2) {
        StandardGameDayFragment fragment = new StandardGameDayFragment();
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
        objectStandardGameDayFragment=inflater.inflate(R.layout.fragment_game_day,container,false);
        attachComponents();
        checkGame();

        return objectStandardGameDayFragment;
    }

    private void checkGame(){
        StandardGameActivity sga=(StandardGameActivity) getActivity();

        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.HOUR,+3); // Because the Israeli timezone is GMT+3
        Date date1= cal.getTime() ;
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

    private void attachComponents() {
        sga=(StandardGameActivity) getActivity();
        fbs=FirebaseServices.getInstance();
        sga.dayCount++;

        order=sga.order;

        c1=objectStandardGameDayFragment.findViewById(R.id.imgGameDayChar1);
        c2=objectStandardGameDayFragment.findViewById(R.id.imgGameDayChar2);
        c3=objectStandardGameDayFragment.findViewById(R.id.imgGameDayChar3);
        c4=objectStandardGameDayFragment.findViewById(R.id.imgGameDayChar4);
        c5=objectStandardGameDayFragment.findViewById(R.id.imgGameDayChar5);
        c6=objectStandardGameDayFragment.findViewById(R.id.imgGameDayChar6);
        c7=objectStandardGameDayFragment.findViewById(R.id.imgGameDayChar7);
        c8=objectStandardGameDayFragment.findViewById(R.id.imgGameDayChar8);

        c1dead=objectStandardGameDayFragment.findViewById(R.id.imgGameDayChar1Dead);
        c2dead=objectStandardGameDayFragment.findViewById(R.id.imgGameDayChar2Dead);
        c3dead=objectStandardGameDayFragment.findViewById(R.id.imgGameDayChar3Dead);
        c4dead=objectStandardGameDayFragment.findViewById(R.id.imgGameDayChar4Dead);
        c5dead=objectStandardGameDayFragment.findViewById(R.id.imgGameDayChar5Dead);
        c6dead=objectStandardGameDayFragment.findViewById(R.id.imgGameDayChar6Dead);
        c7dead=objectStandardGameDayFragment.findViewById(R.id.imgGameDayChar7Dead);
        c8dead=objectStandardGameDayFragment.findViewById(R.id.imgGameDayChar8Dead);

        c1role=objectStandardGameDayFragment.findViewById(R.id.imgGameDayChar1Role);
        c2role=objectStandardGameDayFragment.findViewById(R.id.imgGameDayChar2Role);
        c3role=objectStandardGameDayFragment.findViewById(R.id.imgGameDayChar3Role);
        c4role=objectStandardGameDayFragment.findViewById(R.id.imgGameDayChar4Role);
        c5role=objectStandardGameDayFragment.findViewById(R.id.imgGameDayChar5Role);
        c6role=objectStandardGameDayFragment.findViewById(R.id.imgGameDayChar6Role);
        c7role=objectStandardGameDayFragment.findViewById(R.id.imgGameDayChar7Role);
        c8role=objectStandardGameDayFragment.findViewById(R.id.imgGameDayChar8Role);

        c1lynch=objectStandardGameDayFragment.findViewById(R.id.imgGameDayChar1Lynch);
        c2lynch=objectStandardGameDayFragment.findViewById(R.id.imgGameDayChar2Lynch);
        c3lynch=objectStandardGameDayFragment.findViewById(R.id.imgGameDayChar3Lynch);
        c4lynch=objectStandardGameDayFragment.findViewById(R.id.imgGameDayChar4Lynch);
        c5lynch=objectStandardGameDayFragment.findViewById(R.id.imgGameDayChar5Lynch);
        c6lynch=objectStandardGameDayFragment.findViewById(R.id.imgGameDayChar6Lynch);
        c7lynch=objectStandardGameDayFragment.findViewById(R.id.imgGameDayChar7Lynch);
        c8lynch=objectStandardGameDayFragment.findViewById(R.id.imgGameDayChar8Lynch);

        dayNum=objectStandardGameDayFragment.findViewById(R.id.txtGameDayDayNum);
        dayNum.setText("Day "+sga.dayCount);

        nextPhase=objectStandardGameDayFragment.findViewById(R.id.imgGameDayNextPhase);

        nextPhase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(c1lynch.getVisibility()==View.VISIBLE){
                    c1lynch.setVisibility(View.INVISIBLE);
                    order.get(0).setAlive(false);
                }
                if(c2lynch.getVisibility()==View.VISIBLE){
                    c2lynch.setVisibility(View.INVISIBLE);
                    order.get(1).setAlive(false);
                }
                if(c3lynch.getVisibility()==View.VISIBLE){
                    c3lynch.setVisibility(View.INVISIBLE);
                    order.get(2).setAlive(false);
                }
                if(c4lynch.getVisibility()==View.VISIBLE){
                    c4lynch.setVisibility(View.INVISIBLE);
                    order.get(3).setAlive(false);
                }
                if(c5lynch.getVisibility()==View.VISIBLE){
                    c5lynch.setVisibility(View.INVISIBLE);
                    order.get(4).setAlive(false);
                }
                if(c6lynch.getVisibility()==View.VISIBLE){
                    c6lynch.setVisibility(View.INVISIBLE);
                    order.get(5).setAlive(false);
                }
                if(c7lynch.getVisibility()==View.VISIBLE){
                    c7lynch.setVisibility(View.INVISIBLE);
                    order.get(6).setAlive(false);
                }
                if(c8lynch.getVisibility()==View.VISIBLE){
                    c8lynch.setVisibility(View.INVISIBLE);
                    order.get(7).setAlive(false);
                }

                sga.order=order;

                StandardGameDawnFragment standardGameDawnFragment=new StandardGameDawnFragment();
                FragmentManager manager=getFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.frameLayoutGame,standardGameDawnFragment,standardGameDawnFragment.getTag())
                        .disallowAddToBackStack()
                        .commit();
            }
        });

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(c1lynch.getVisibility()==View.INVISIBLE) {
                    c1lynch.setVisibility(View.VISIBLE);
                    c2lynch.setVisibility(View.INVISIBLE);
                    c3lynch.setVisibility(View.INVISIBLE);
                    c4lynch.setVisibility(View.INVISIBLE);
                    c5lynch.setVisibility(View.INVISIBLE);
                    c6lynch.setVisibility(View.INVISIBLE);
                    c7lynch.setVisibility(View.INVISIBLE);
                    c8lynch.setVisibility(View.INVISIBLE);
                }
                else
                    c1lynch.setVisibility(View.INVISIBLE);
            }
        });

        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(c2lynch.getVisibility()==View.INVISIBLE) {
                    c2lynch.setVisibility(View.VISIBLE);
                    c1lynch.setVisibility(View.INVISIBLE);
                    c3lynch.setVisibility(View.INVISIBLE);
                    c4lynch.setVisibility(View.INVISIBLE);
                    c5lynch.setVisibility(View.INVISIBLE);
                    c6lynch.setVisibility(View.INVISIBLE);
                    c7lynch.setVisibility(View.INVISIBLE);
                    c8lynch.setVisibility(View.INVISIBLE);
                }
                else
                    c2lynch.setVisibility(View.INVISIBLE);
            }
        });
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(c3lynch.getVisibility()==View.INVISIBLE) {
                    c3lynch.setVisibility(View.VISIBLE);
                    c2lynch.setVisibility(View.INVISIBLE);
                    c1lynch.setVisibility(View.INVISIBLE);
                    c4lynch.setVisibility(View.INVISIBLE);
                    c5lynch.setVisibility(View.INVISIBLE);
                    c6lynch.setVisibility(View.INVISIBLE);
                    c7lynch.setVisibility(View.INVISIBLE);
                    c8lynch.setVisibility(View.INVISIBLE);
                }
                else
                    c3lynch.setVisibility(View.INVISIBLE);
            }
        });
        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(c4lynch.getVisibility()==View.INVISIBLE) {
                    c4lynch.setVisibility(View.VISIBLE);
                    c2lynch.setVisibility(View.INVISIBLE);
                    c3lynch.setVisibility(View.INVISIBLE);
                    c1lynch.setVisibility(View.INVISIBLE);
                    c5lynch.setVisibility(View.INVISIBLE);
                    c6lynch.setVisibility(View.INVISIBLE);
                    c7lynch.setVisibility(View.INVISIBLE);
                    c8lynch.setVisibility(View.INVISIBLE);
                }
                else
                    c4lynch.setVisibility(View.INVISIBLE);
            }
        });
        c5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(c5lynch.getVisibility()==View.INVISIBLE) {
                    c5lynch.setVisibility(View.VISIBLE);
                    c2lynch.setVisibility(View.INVISIBLE);
                    c3lynch.setVisibility(View.INVISIBLE);
                    c4lynch.setVisibility(View.INVISIBLE);
                    c1lynch.setVisibility(View.INVISIBLE);
                    c6lynch.setVisibility(View.INVISIBLE);
                    c7lynch.setVisibility(View.INVISIBLE);
                    c8lynch.setVisibility(View.INVISIBLE);
                }
                else
                    c5lynch.setVisibility(View.INVISIBLE);
            }
        });
        c6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(c6lynch.getVisibility()==View.INVISIBLE) {
                    c6lynch.setVisibility(View.VISIBLE);
                    c2lynch.setVisibility(View.INVISIBLE);
                    c3lynch.setVisibility(View.INVISIBLE);
                    c4lynch.setVisibility(View.INVISIBLE);
                    c5lynch.setVisibility(View.INVISIBLE);
                    c1lynch.setVisibility(View.INVISIBLE);
                    c7lynch.setVisibility(View.INVISIBLE);
                    c8lynch.setVisibility(View.INVISIBLE);
                }
                else
                    c6lynch.setVisibility(View.INVISIBLE);
            }
        });
        c7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(c7lynch.getVisibility()==View.INVISIBLE) {
                    c7lynch.setVisibility(View.VISIBLE);
                    c2lynch.setVisibility(View.INVISIBLE);
                    c3lynch.setVisibility(View.INVISIBLE);
                    c4lynch.setVisibility(View.INVISIBLE);
                    c5lynch.setVisibility(View.INVISIBLE);
                    c6lynch.setVisibility(View.INVISIBLE);
                    c1lynch.setVisibility(View.INVISIBLE);
                    c8lynch.setVisibility(View.INVISIBLE);
                }
                else
                    c7lynch.setVisibility(View.INVISIBLE);
            }
        });
        c8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(c8lynch.getVisibility()==View.INVISIBLE) {
                    c8lynch.setVisibility(View.VISIBLE);
                    c2lynch.setVisibility(View.INVISIBLE);
                    c3lynch.setVisibility(View.INVISIBLE);
                    c4lynch.setVisibility(View.INVISIBLE);
                    c5lynch.setVisibility(View.INVISIBLE);
                    c6lynch.setVisibility(View.INVISIBLE);
                    c7lynch.setVisibility(View.INVISIBLE);
                    c1lynch.setVisibility(View.INVISIBLE);
                }
                else
                    c8lynch.setVisibility(View.INVISIBLE);
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