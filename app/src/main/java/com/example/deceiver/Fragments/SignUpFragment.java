package com.example.deceiver.Fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deceiver.FirebaseServices;
import com.example.deceiver.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    View objectSignUpFragment;
    private Button signUpBtn;
    private EditText mailEt,usernameEt,passEt,confirmPassEt;
    private FirebaseServices fbs;
    private TextView signUpToLogInTxt;
    private boolean worked;


    private void attachComponents(){
        try{
                signUpBtn=objectSignUpFragment.findViewById(R.id.btnSignUp);
                mailEt=objectSignUpFragment.findViewById(R.id.etEmailSignUp);
                usernameEt=objectSignUpFragment.findViewById(R.id.etUsernameSignUp);
                passEt=objectSignUpFragment.findViewById(R.id.etPassSignUp);
                confirmPassEt=objectSignUpFragment.findViewById(R.id.etPassConfirmSignUp);
                signUpToLogInTxt=objectSignUpFragment.findViewById(R.id.txtSignUpToLogin);
                fbs=FirebaseServices.getInstance();

                signUpBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        createUser();
                    }
                });
                signUpToLogInTxt.setOnClickListener(new View.OnClickListener() {
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
        catch(Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void createUser(){
        try{
            if(!mailEt.getText().toString().isEmpty()&&!passEt.getText().toString().isEmpty()&&!confirmPassEt.getText().toString().isEmpty()){
                if(passEt.getText().toString().equals(confirmPassEt.getText().toString())){
                    fbs.getAuth().createUserWithEmailAndPassword(mailEt.getText().toString(),passEt.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    worked=true;
                                    Toast.makeText(getContext(), "Account created.", Toast.LENGTH_SHORT).show();
                                    if(fbs.getAuth().getCurrentUser()!=null){
                                        fbs.getAuth().signOut();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(worked==true){
                                        addUserToFirestore();
                                        worked=false;
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(getContext(), "Passwords do not match.", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(getContext(), "Missing fields identified.", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void addUserToFirestore() {
        DocumentReference newUserRef=fbs.getFire().collection("users").document(usernameEt.getText().toString());

        Map<String,Object> user=new HashMap<>();
        user.put("Email",mailEt.getText().toString());
        user.put("Password",passEt.getText().toString());

        newUserRef.set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        objectSignUpFragment=inflater.inflate(R.layout.fragment_sign_up,container,false);
        attachComponents();

        return objectSignUpFragment;
    }
}