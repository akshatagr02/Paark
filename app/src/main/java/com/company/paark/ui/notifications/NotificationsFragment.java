package com.company.paark.ui.notifications;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.company.paark.Data;
import com.company.paark.databinding.FragmentNotificationsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Date;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private FirebaseAuth mAuth;
    private static int  finalfare = 0, yfare =0 , prevfare=0;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mAuth = FirebaseAuth.getInstance();
        final TextView textView = binding.tearnings;
        final TextView yfaret = binding.yearningst;
        final TextView prefaret = binding.preverarnignt;

        DatabaseReference mDatabaseref = FirebaseDatabase.getInstance().getReference();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
            finalfare =0;
            yfare = 0;
            prevfare=0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Data d = ds.getValue(Data.class);
                    Log.d("yo", "onDataChange: "+LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                   if (d.iddate.split("T")[0].equals(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))){
                       finalfare +=Integer.valueOf(d.fare);
                       Log.d("aaj", "aaj: "+d.fare);
                       Log.d("aaj", "final: "+finalfare);
                   }
                   if (d.iddate.split("T")[0].equals(LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))){

                       yfare  +=Integer.valueOf(d.fare);

                   }else if(!d.iddate.split("T")[0].equals(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))){
                       prevfare +=Integer.valueOf(d.fare);
                   }


                }
                yfaret.setText(NumberFormat.getNumberInstance().format(yfare));
                prefaret.setText(NumberFormat.getNumberInstance().format(prevfare));
                ValueAnimator animator = ValueAnimator.ofInt(0, finalfare); //0 is min number, 600 is max number
                animator.setDuration(800); //Duration is in milliseconds
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int i = Integer.parseInt(animation.getAnimatedValue().toString());
                        String out = NumberFormat.getNumberInstance().format(i);
                        textView.setText(out);
                    }
                });
                animator.start();
                // ..


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("error", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabaseref.child("users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(postListener);
        Log.d("final", "final "+finalfare);







        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}