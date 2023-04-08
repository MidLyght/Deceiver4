package com.example.deceiver.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.deceiver.DataClasses.GameModel;
import com.example.deceiver.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameHistoryFragment extends Fragment {

    View gameHistoryFragmentObject;
    private FirebaseFirestore fbfs;
    private FirebaseAuth fba;
    private FirestoreRecyclerAdapter adapter;
    private RecyclerView gameHistoryRecyclerView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GameHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameHistoryFragment newInstance(String param1, String param2) {
        GameHistoryFragment fragment = new GameHistoryFragment();
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

        fbfs= FirebaseFirestore.getInstance();
        fba= FirebaseAuth.getInstance();
        gameHistoryRecyclerView=gameHistoryFragmentObject.findViewById(R.id.gameHistoryRecyclerView);

        Query query=fbfs.collection("users").document(fba.getCurrentUser().getEmail()).collection("gamehistory");

        FirestoreRecyclerOptions<GameModel> options=new FirestoreRecyclerOptions.Builder<GameModel>()
                .setQuery(query,GameModel.class)
                .build();

        adapter= new FirestoreRecyclerAdapter<GameModel, GameHistoryFragment.GameViewHolder>(options) {
            @NonNull
            @Override
            public GameHistoryFragment.GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.gameitem,parent,false);
                return new GameHistoryFragment.GameViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull GameHistoryFragment.GameViewHolder holder, int position, @NonNull GameModel model) {
                holder.gameResult.setText(model.getResult());
                holder.gameDate.setText(model.getDate());
                holder.gameDays.setText(model.getDate()+" days");
                holder.gameDawns.setText(model.getDawns()+ " dawns");
                holder.gameNights.setText(model.getNights()+" nights");
                holder.gameBodyCount.setText(model.getBodyCount()+" deaths");
            }
        };

        gameHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        gameHistoryRecyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        gameHistoryFragmentObject=inflater.inflate(R.layout.fragment_game_history, container, false);
        return gameHistoryFragmentObject;
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

    @Override
    public void onStop(){
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart(){
        super.onStart();
        adapter.startListening();
    }
}