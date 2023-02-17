package com.example.deceiver.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deceiver.Activities.MainActivity;
import com.example.deceiver.Activities.MainPageActivity;
import com.example.deceiver.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogInFragment extends Fragment {

    private View objectSignInFragment;
    private Button logInBtn;
    private EditText mailEt,passEt;
    private FirebaseAuth mAuth;
    private TextView logInToSignUpTxt,logInToRPTxt;

    private void attachComponents(){
        logInBtn=objectSignInFragment.findViewById(R.id.btnLogIn);
        mailEt=objectSignInFragment.findViewById(R.id.etMailLogIn);
        passEt=objectSignInFragment.findViewById(R.id.etPassLogIn);
        logInToSignUpTxt=objectSignInFragment.findViewById(R.id.txtSignUpLogIn);
        logInToRPTxt=objectSignInFragment.findViewById(R.id.logInToRPTxt);

        mAuth=FirebaseAuth.getInstance();

        logInToRPTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgotPasswordFragment forgotPasswordFragment=new ForgotPasswordFragment();
                FragmentManager manager=getFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.frameLayoutMain,forgotPasswordFragment,forgotPasswordFragment.getTag())
                        .commit();
            }
        });

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logInUser();
            }
        });

        logInToSignUpTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpFragment signUpFragment=new SignUpFragment();
                FragmentManager manager=getFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.frameLayoutMain,signUpFragment,signUpFragment.getTag())
                        .commit();
            }
        });
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LogInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LogInPage.
     */
    // TODO: Rename and change types and number of parameters
    public static LogInFragment newInstance(String param1, String param2) {
        LogInFragment fragment = new LogInFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    private void logInUser(){
        try{
            if(!mailEt.getText().toString().isEmpty()&&!passEt.getText().toString().isEmpty()){
                if(mAuth!=null){
                    mAuth.signInWithEmailAndPassword(mailEt.getText().toString(),passEt.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Toast.makeText(getContext(), "User signed in successfully.", Toast.LENGTH_SHORT).show();
                                    Intent mainPageActivityIntent = new Intent(getContext(), MainPageActivity.class);
                                    startActivity(mainPageActivityIntent);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
            else{
                Toast.makeText(getContext(), "Missing fields identified.", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        objectSignInFragment=inflater.inflate(R.layout.fragment_log_in_page,container,false);
        attachComponents();

        return objectSignInFragment;
    }

}