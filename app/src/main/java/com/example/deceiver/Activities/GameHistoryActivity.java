package com.example.deceiver.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.deceiver.DataClasses.GameModel;
import com.example.deceiver.Fragments.GameHistoryFragment;
import com.example.deceiver.Fragments.MainPageFragment;
import com.example.deceiver.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class GameHistoryActivity extends AppCompatActivity {

    private FirebaseFirestore fbfs;
    private FirebaseAuth fba;
    private FirestoreRecyclerAdapter adapter;
    private RecyclerView gameHistoryRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_history);

        gameHistoryRecyclerView=findViewById(R.id.gameHistoryRecyclerView1);

        fbfs= FirebaseFirestore.getInstance();
        fba= FirebaseAuth.getInstance();

        Query query=fbfs.collection("users").document(fba.getCurrentUser().getEmail()).collection("gamehistory");

        FirestoreRecyclerOptions<GameModel> options=new FirestoreRecyclerOptions.Builder<GameModel>()
                .setQuery(query,GameModel.class)
                .build();
        adapter= new FirestoreRecyclerAdapter<GameModel, GameViewHolder>(options) {
            @NonNull
            @Override
            public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.gameitem,parent,false);
                return new GameViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull GameViewHolder holder, int position, @NonNull GameModel model) {
                holder.gameResult.setText(model.getResult());
                holder.gameDate.setText(model.getDate());
                holder.gameDays.setText(model.getDays()+" days");
                holder.gameDawns.setText(model.getDawns()+ " dawns");
                holder.gameNights.setText(model.getNights()+" nights");
                holder.gameBodyCount.setText(model.getBodyCount()+" deaths");
            }
        };

        gameHistoryRecyclerView.setHasFixedSize(true);
        gameHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        gameHistoryRecyclerView.setAdapter(adapter);
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
    protected void onStop(){
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart(){
        super.onStart();
        adapter.startListening();
    }
}