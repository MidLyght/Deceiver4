package com.example.deceiver.Activities;

import static com.example.deceiver.Enums.StandardRole.Blacksmith;
import static com.example.deceiver.Enums.StandardRole.Deceiver;
import static com.example.deceiver.Enums.StandardRole.Farmer;
import static com.example.deceiver.Enums.StandardRole.Guard;
import static com.example.deceiver.Enums.StandardRole.Seer;
import static com.example.deceiver.Enums.StandardRole.Traitor;
import static com.example.deceiver.Enums.StandardRole.Witch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.deceiver.DataClasses.StandardCharacter;
import com.example.deceiver.Fragments.LogInFragment;
import com.example.deceiver.Enums.Phase;
import com.example.deceiver.Enums.StandardTeam;
import com.example.deceiver.Fragments.StandardGameDawnFragment;
import com.example.deceiver.Fragments.StandardGameDawnLogFragment;
import com.example.deceiver.Fragments.StandardGameDayFragment;
import com.example.deceiver.Fragments.StandardGameNightFragment;
import com.example.deceiver.Fragments.StandardGameNightLogFragment;
import com.example.deceiver.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class StandardGameActivity extends AppCompatActivity {

    public StandardCharacter deceiver,traitor,farmer1,farmer2,witch,blacksmith,seer,guard;
    public ArrayList<StandardCharacter> order;
    public int dayCount=1,dawnCount=1,nightCount=0;
    public int villagerCount,deceiverCount;
    public String dawnLog="",nightLog="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_game);

        witch=new StandardCharacter();
        deceiver=new StandardCharacter();
        traitor=new StandardCharacter();
        farmer1=new StandardCharacter();
        farmer2=new StandardCharacter();
        guard=new StandardCharacter();
        seer=new StandardCharacter();
        blacksmith=new StandardCharacter();

        deceiver.setRole(Deceiver);
        deceiver.setTeam(StandardTeam.Deceivers);
        traitor.setRole(Traitor);
        traitor.setTeam(StandardTeam.Deceivers);
        witch.setRole(Witch);
        farmer1.setRole(Farmer);
        farmer2.setRole(Farmer);
        guard.setRole(Guard);
        seer.setRole(Seer);
        blacksmith.setRole(Blacksmith);

        order=new ArrayList();
        order.add(deceiver);
        order.add(traitor);
        order.add(witch);
        order.add(farmer1);
        order.add(farmer2);
        order.add(guard);
        order.add(seer);
        order.add(blacksmith);
        Collections.shuffle(order);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayoutGame, new StandardGameDawnFragment());
        ft.commit();
    }
}