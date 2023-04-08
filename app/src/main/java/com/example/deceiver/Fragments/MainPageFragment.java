package com.example.deceiver.Fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.deceiver.Activities.GameHistoryActivity;
import com.example.deceiver.Activities.StandardGameActivity;
import com.example.deceiver.DataClasses.GameModel;
import com.example.deceiver.FirebaseServices;
import com.example.deceiver.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainPageFragment extends Fragment {

    View objectMainPageFragment;
    private FirebaseFirestore fbfs;
    private FirebaseAuth fba;
    private FirestoreRecyclerAdapter adapter;
    private Button btnPlay,btnIntro,btnProfile;
    private RecyclerView gameHistoryRecyclerView;
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

        btnIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntroductionFragment introductionFragment=new IntroductionFragment();
                FragmentManager manager=getFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.frameLayoutMainPage,introductionFragment,introductionFragment.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent standardGameActivityIntent = new Intent(getContext(), StandardGameActivity.class);
                startActivity(standardGameActivityIntent);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createProfilePopUp();
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

        showGameHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gameHistoryActivityIntent = new Intent(getContext(), GameHistoryActivity.class);
                startActivity(gameHistoryActivityIntent);
            }
        });

       /* Query query=fbfs.collection("users").document(fba.getCurrentUser().getEmail()).collection("gamehistory");

        FirestoreRecyclerOptions<GameModel> options=new FirestoreRecyclerOptions.Builder<GameModel>()
                .setQuery(query,GameModel.class)
                .build();
        adapter= new FirestoreRecyclerAdapter<GameModel, GameViewHolder>(options) {
            @NonNull
            @Override
            public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.gameitem,parent,false);
                return new GameViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull GameViewHolder holder, int position, @NonNull GameModel model) {
                holder.gameResult.setText(model.getResult());
                holder.gameDate.setText(model.getDate());
                holder.gameDays.setText(model.getDate()+" days");
                holder.gameDawns.setText(model.getDawns()+ " dawns");
                holder.gameNights.setText(model.getNights()+" nights");
                holder.gameBodyCount.setText(model.getBodyCount()+" deaths");
            }
        };*/

        /*gameHistoryRecyclerView.setHasFixedSize(true);
        gameHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        gameHistoryRecyclerView.setAdapter(adapter);*/

        DocumentReference newUserRe=fbfs.collection("users").document(fba.getCurrentUser().getEmail());
        newUserRe.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                usernameProfile.setText(documentSnapshot.getString("Username"));
                dateProfile.setText("Detective since "+documentSnapshot.getString("CreationDate"));
                gamesProfile.setText(documentSnapshot.getLong("GamesPlayed").toString()+" games played");
                winsProfile.setText(documentSnapshot.getLong("Wins").toString()+" games won");
                defeatsProfile.setText(documentSnapshot.getLong("Losses").toString()+" games lost");
            }
        });

        /*if(showGameHistory.isChecked())
            gameHistoryRecyclerView.setVisibility(View.VISIBLE);
        if(!showGameHistory.isChecked())
            gameHistoryRecyclerView.setVisibility(View.INVISIBLE);*/

        dialogBuilder.setView(contactPopUpView);
        dialogBuilder.create().show();
    }

    private class GameViewHolder extends RecyclerView.ViewHolder{

        private TextView gameResult,gameDate,gameDays,gameDawns,gameNights,gameBodyCount;
        public GameViewHolder(@NonNull View itemView){
            super(itemView);

            gameResult=itemView.findViewById(R.id.resultGameItem);
            gameDate=itemView.findViewById(R.id.dateGameItem);
            gameDays=itemView.findViewById(R.id.daysGameItem);
            gameDawns=itemView.findViewById(R.id.dawnsGameItem);
            gameNights=itemView.findViewById(R.id.nightsGameItem);
            gameBodyCount=itemView.findViewById(R.id.bodyCountGameItem);
        }
    }
}