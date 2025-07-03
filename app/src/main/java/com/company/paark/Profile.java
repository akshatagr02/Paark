package com.company.paark;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.company.paark.ui.Login.MainLogin;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Stack;


public class Profile extends Fragment {
    private FirebaseAuth mAuth;
    private DatabaseReference mPostRefrence;
    private String uid;
    private static  profileData post;
    private EditText T20,T30,T45,Tperh,F20,F30,F45,Fperh,Tlate,Flate;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        TextView textView = view.findViewById(R.id.user);
         T20 = view.findViewById(R.id.p20T);
         T30 = view.findViewById(R.id.p30T);
         T45 = view.findViewById(R.id.p45T);
         Tperh = view.findViewById(R.id.perhrT);
         Tlate = view.findViewById(R.id.latefeeT);

        F20 = view.findViewById(R.id.p20f);
         F30 = view.findViewById(R.id.p30f);
         F45 = view.findViewById(R.id.p45f);
         Fperh = view.findViewById(R.id.perhrf);
         Flate =view.findViewById(R.id.latefeef);
        mPostRefrence = FirebaseDatabase.getInstance().getReference();


        textView.setText("Welcome: " + mAuth.getCurrentUser().getEmail().split("@")[0]);
         uid = mAuth.getCurrentUser().getUid();
        Button signout = view.findViewById(R.id.signout);
        Button save = view.findViewById(R.id.Save);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getContext(), MainLogin.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        Pricing();


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> uc = new HashMap<>();
                uc.put("T20", T20.getText().toString());
                uc.put("T30", T30.getText().toString());
                uc.put("T45", T45.getText().toString());
                uc.put("Tperh", Tperh.getText().toString());
                uc.put("f20", F20.getText().toString());
                uc.put("f30", F30.getText().toString());
                uc.put("f45", F45.getText().toString());
                uc.put("fperh", Fperh.getText().toString());
                uc.put("latefeeT", Tlate.getText().toString());
                uc.put("latefeeF", Flate.getText().toString());
                mPostRefrence.child("Pricing").child(uid).updateChildren(uc);
            }
        });


    }

    public void Pricing() {

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                 post= dataSnapshot.getValue(profileData.class);


    if(dataSnapshot.exists()){

                Settext();
    }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.w("profile", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mPostRefrence.child("Pricing").child(uid).addValueEventListener(postListener);

    }
    private void Settext(){
        T20.setText(post.T20);
        T30.setText(post.T30);
        T45.setText(post.T45);
        Tperh.setText(post.Tperh);
        Tlate.setText(post.Tperh);
        Tlate.setText(post.latefeeT);

        F20.setText(post.f20);
        F30.setText(post.f30);
        F45.setText(post.f45);
        Fperh.setText(post.fperh);
        Flate.setText(post.latefeeF);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}