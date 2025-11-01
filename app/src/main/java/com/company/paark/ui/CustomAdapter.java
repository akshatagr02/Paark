package com.company.paark.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Build;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.paark.Data;
import com.company.paark.R;
import com.company.paark.profileData;
import com.company.paark.ui.dashboard.DashboardFragment;
import com.company.paark.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

//    private Map<String,String[]> localDataSet;
    private ArrayList<String>keys;
    private ArrayList<String[]> values;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView,sdate,edate,code,price,vtype;
        private final CheckBox checkBox;
        Dialog dialog;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = (TextView) view.findViewById(R.id.vno);
            sdate = (TextView) view.findViewById(R.id.sdate);
            edate = (TextView) view.findViewById(R.id.edate);
            code = (TextView) view.findViewById(R.id.code);
            price = (TextView) view.findViewById(R.id.price);
            vtype = (TextView) view.findViewById(R.id.vtype);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);
            dialog = new Dialog(view.getContext());
            dialog.setContentView(R.layout.dialogbox);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);


        }

        public TextView getTextView() {
            return textView;
        }
        public TextView getPrice(){
            return price;
        }public TextView getSdate(){
            return sdate;
        }public TextView getEdate(){
            return edate;
        }public TextView getCode(){
            return code;
        }public TextView getVtype() {
            return vtype;
        }public CheckBox getCheckBox() {
            return checkBox;
        }

        public Dialog getDialog() {
            return dialog;
        }


    }

    /**
     * Initialize the dataset of the Adapter
     *
//     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView
     */
    public CustomAdapter(ArrayList<String> keys,ArrayList<String[]> values) {
        this.keys=keys;
        this.values=values;
    }
    private FirebaseAuth mAuth;
    private DatabaseReference mPostRefrence;
    private int lateT,lateF;
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter, viewGroup, false);
        mAuth = FirebaseAuth.getInstance();
        mPostRefrence = FirebaseDatabase.getInstance().getReference();
        mPostRefrence.child("Pricing").child(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    profileData post= task.getResult().getValue(profileData.class);
                    lateF =Integer.parseInt(post.latefeeT) ;
                    lateT =Integer.parseInt(post.latefeeF) ;




                }
            }
        });

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element System.out.println(n+"  "+k[1])

//        localDataSet.forEach((n,k)->{
//            Log.d("on", "onBindViewHolder: "+n);
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

            viewHolder.getCode().setText(keys.get(position));
//        Log.d("value", "value: "+values.get(position)[1]);
            viewHolder.getTextView().setText(this.values.get(position)[0]+" "+this.values.get(position)[1]+" "+values.get(position)[2]+" "+this.values.get(position)[3]);
            viewHolder.getSdate().setText(this.values.get(position)[4]);
            viewHolder.getEdate().setText(this.values.get(position)[5]);
            viewHolder.getPrice().setText(this.values.get(position)[6]+" /-");
            viewHolder.getVtype().setText(this.values.get(position)[7]);
            viewHolder.getCheckBox().setChecked(Boolean.valueOf(values.get(position)[8]));

            if (Boolean.parseBoolean(values.get(position)[8])){
            viewHolder.getCheckBox().setEnabled(false);

            }else {
                viewHolder.getCheckBox().setEnabled(true);
                viewHolder.getCheckBox().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



//                boolean isWithdrawan =(viewHolder.getCheckBox().isChecked());
                        Data user = new Data(values.get(position)[0],values.get(position)[1],values.get(position)[2],values.get(position)[3],values.get(position)[4],values.get(position)[5],values.get(position)[6],values.get(position)[7],ischecked(viewHolder),values.get(position)[8]);
                        Map<String, Object> uc = new HashMap<>();
                        Map<String, Object> late = new HashMap<>();
                        uc.put("isWithdrawan",ischecked(viewHolder));

                        TextView text = viewHolder.getDialog().findViewById(R.id.Dialogtext);
                        DashboardFragment homeFragment = new DashboardFragment();
                        Long hrs =0L;
                        if (!Objects.equals(values.get(position)[5], "No time")){

                            hrs = Duration.between(LocalDateTime.parse(values.get(position)[5].replace(" ","T")),LocalDateTime.now()).toHours();



                            long latefee = 0;


                            if (ischecked(viewHolder)){
                                viewHolder.getDialog().show();
                                if (values.get(position)[7].equals("Four Wheeler")){
                                    latefee =hrs*lateT;

                                }else{
                                    latefee = hrs*lateF;


                                }


                                if (hrs<0 || latefee<0){
                                    latefee = 0;
                                    hrs = 0L;
                                    text.setText("No Late Fee");
                                }else {
                                    text.setText("Late Hours is "+hrs+" Hours"+" Late Fee is "+latefee +"/-");

                                }
                            }

                            viewHolder.getDialog().findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    uc.put("Latehrs:",0);

                                    viewHolder.getDialog().dismiss();

                                }
                            });
                            long finalLatefee = latefee;
                            Long finalHrs = hrs;
                            viewHolder.getDialog().findViewById(R.id.Accept).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    late.put("Late hrs:", finalHrs);
                                    late.put("LateFee", finalLatefee);

                                    viewHolder.getDialog().dismiss();
                                    mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child(String.valueOf(keys.get(position))).updateChildren(late);

                                }
                            });
                        }
                        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child(String.valueOf(keys.get(position))).updateChildren(uc);



                        Log.d("isWithdrawan", "onClick: "+ischecked(viewHolder));

                    }
                });
            }

//        Log.d("iswith", "onBindViewHolder: "+Boolean.valueOf(values.get(position)[8]));
//        });




    }
    public Boolean ischecked(ViewHolder viewHolder){
        return  viewHolder.getCheckBox().isChecked();
    }
    public void filterList(Map<String,String[]> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        this.keys = new ArrayList<String>(filterlist.keySet());
        this.values = new ArrayList<String[]>(filterlist.values());



        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return keys.size();
    }

}