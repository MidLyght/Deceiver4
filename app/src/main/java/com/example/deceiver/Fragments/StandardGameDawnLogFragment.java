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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.deceiver.Activities.MainPageActivity;
import com.example.deceiver.Activities.StandardGameActivity;
import com.example.deceiver.Enums.Phase;
import com.example.deceiver.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StandardGameDawnLogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StandardGameDawnLogFragment extends Fragment {

    private TextView dawnLog;
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
        dawnLog=objectStandardGameDawnLogFragment.findViewById(R.id.textView28);
        nextPhase=objectStandardGameDawnLogFragment.findViewById(R.id.imgGameDawnLogNextPhase);

        dawnLog.setText(sga.dawnLog);
        nextPhase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!sga.deceiver.isAlive()&&!sga.traitor.isAlive()){
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
        StandardGameActivity sga=(StandardGameActivity)getActivity();
        dialogBuilder=new AlertDialog.Builder(getContext());
        final View contactPopupView=getLayoutInflater().inflate(R.layout.deceiverwinpopup,null);

        decRestart=contactPopupView.findViewById(R.id.decRestart);
        decMenu=contactPopupView.findViewById(R.id.decMenu);
        decDays=contactPopupView.findViewById(R.id.decDays);
        decDays.setText(sga.dayCount+" days");
        decDawns=contactPopupView.findViewById(R.id.decDawns);
        decDawns.setText(sga.dawnCount+" dawns");
        decNights=contactPopupView.findViewById(R.id.decNights);
        decNights.setText(sga.nightCount+" nights");

        decRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),StandardGameActivity.class));
            }
        });

        decMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainPageActivity.class));
            }
        });

        dialogBuilder.setView(contactPopupView);
        deceiverDialog=dialogBuilder.create();
        deceiverDialog.show();
    }

    public void createVillageWinPopup(){
        StandardGameActivity sga=(StandardGameActivity)getActivity();
        dialogBuilder=new AlertDialog.Builder(getContext());
        final View contactPopupView=getLayoutInflater().inflate(R.layout.villagewinpopup,null);

        vilRestart=contactPopupView.findViewById(R.id.vilRestart);
        vilMenu=contactPopupView.findViewById(R.id.vilMenu);
        vilDays=contactPopupView.findViewById(R.id.vilDays);
        vilDays.setText(sga.dayCount+" days");
        vilDawns=contactPopupView.findViewById(R.id.vilDawns);
        vilDawns.setText(sga.dawnCount+" dawns");
        vilNights=contactPopupView.findViewById(R.id.vilNights);
        vilNights.setText(sga.nightCount+" nights");

        vilRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),StandardGameActivity.class));
            }
        });

        vilMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainPageActivity.class));
            }
        });

        dialogBuilder.setView(contactPopupView);
        villageDialog=dialogBuilder.create();
        villageDialog.show();
    }
}