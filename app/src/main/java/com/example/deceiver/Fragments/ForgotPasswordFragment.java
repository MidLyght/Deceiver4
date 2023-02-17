package com.example.deceiver.Fragments;

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

import com.example.deceiver.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForgotPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForgotPasswordFragment extends Fragment {

    private View objectForgotPasswordFragment;
    private Button resetPassBtn;
    private EditText emailEt;
    private TextView RPtoLoginTxt;
    private FirebaseAuth mAuth;

    private void resetPassword(){
        try{
            if(!emailEt.getText().toString().isEmpty())
                mAuth.sendPasswordResetEmail(emailEt.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(), "Password reset email has been sent.", Toast.LENGTH_SHORT).show();
                                LogInFragment logInFragment=new LogInFragment();
                                FragmentManager manager=getFragmentManager();
                                manager.beginTransaction()
                                        .replace(R.id.frameLayoutMain,logInFragment,logInFragment.getTag())
                                        .commit();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Password reset failed.", Toast.LENGTH_SHORT).show();
                            }
                        });
            else{
                Toast.makeText(getContext(), "Missing fields identified.", Toast.LENGTH_SHORT).show();
            }

        }
        catch(Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void attachComponents(){
        resetPassBtn=objectForgotPasswordFragment.findViewById(R.id.resetPasswordBtn);
        emailEt=objectForgotPasswordFragment.findViewById(R.id.emailEtRP);
        RPtoLoginTxt=objectForgotPasswordFragment.findViewById(R.id.RPtoLoginTxt);
        mAuth=FirebaseAuth.getInstance();

        resetPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });

        RPtoLoginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogInFragment logInFragment=new LogInFragment();
                FragmentManager manager=getFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.frameLayoutMain,logInFragment,logInFragment.getTag())
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

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ForgotPasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForgotPasswordFragment newInstance(String param1, String param2) {
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
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
        objectForgotPasswordFragment=inflater.inflate(R.layout.fragment_forgot_password,container,false);
        attachComponents();

        return objectForgotPasswordFragment;
    }
}