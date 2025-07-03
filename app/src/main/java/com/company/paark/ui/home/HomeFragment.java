package com.company.paark.ui.home;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.company.paark.Data;
import com.company.paark.MainActivity;
import com.company.paark.R;
import com.company.paark.databinding.FragmentHomeBinding;
import com.company.paark.ui.CustomAdapter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    CustomAdapter customAdapter;
    private  ArrayList<String> filtred  = new ArrayList<>();
    public static  Boolean isToday = false;
    private FirebaseAuth mAuth;
    public static void setIsToday(Boolean isToday) {
        HomeFragment.isToday = isToday;
    }

    public static Boolean getIsToday() {
        return isToday;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ChipGroup chipGroup = binding.chipGroup;
        filtred.clear();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        HashMap<String,String[]> map = new HashMap();
        mAuth = FirebaseAuth.getInstance();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
//                Toast.makeText(getContext(), "Changed", Toast.LENGTH_SHORT).show();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                Data d = ds.getValue(Data.class);
                String[] da= {d.state,d.district,d.alpha,d.num,d.iddate.replace("T"," "),d.fddate.replace("T"," "),d.fare,d.vtype, String.valueOf(d.isWithdrawan),d.phoneno};
                    Log.d("hwith", "onDataChange: "+String.valueOf(d.isWithdrawan));
                map.put(ds.getKey(),da);


                }
                ArrayList<String> keys = new ArrayList<String>(map.keySet());
                ArrayList<String[]> values = new ArrayList<String[]>(map.values());
                RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
                 customAdapter = new CustomAdapter(keys,values);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(customAdapter);
                map.forEach((n,k)->System.out.println(n+"  "+k[1]));


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("err", "loadPost:onCancelled", databaseError.toException());
            }
        };



       mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(postListener);






        binding.chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.chip.isChecked()){
                setIsToday(true);
                Search(map,filtred,getIsToday());
                }else{
                    setIsToday(false);
                Search(map,filtred,getIsToday());

                }
            }
        });
        binding.chip2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.chip2.isChecked()){
                filtred.add("true");
             Search(map,filtred,getIsToday());

                }else{
                    filtred.remove("true");
                    Search(map,filtred,getIsToday());
                }
            }
        });
   binding.chip3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.chip3.isChecked()){
                filtred.add("false");
             Search(map,filtred,getIsToday());

                }else{
                    filtred.remove("false");
                    Search(map,filtred,getIsToday());
                }
            }
        });
   binding.chip4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.chip4.isChecked()){
                filtred.add("Two Wheeler");
             Search(map,filtred,getIsToday());

                }else{
                    filtred.remove("Two Wheeler");
                    Search(map,filtred,getIsToday());
                }
            }
        });
   binding.chip5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.chip5.isChecked()){
                filtred.add("Four Wheeler");
             Search(map,filtred,getIsToday());

                }else{
                    filtred.remove("Four Wheeler");
                    Search(map,filtred,getIsToday());
                }
            }
        });


        SearchView searchView = root.findViewById(R.id.search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Map<String,String[]> filtred  = new HashMap<>();

                Log.d("fil", "Change: "+filtred);
                map.forEach((n,k)->{
                    Log.d("each", "onQueryTextChange: "+n+" "+k);
                    if (n.contains(newText.toLowerCase())) {
                        filtred.put(n,map.get(n));
                        customAdapter.filterList(filtred);
                    }else{

                        for (String i : k){
                            Log.d("else", "onQueryTextChange: "+i);
                            if (i.toLowerCase().contains(newText.toLowerCase())){
                                filtred.put(n,k);
                                customAdapter.filterList(filtred);
                            }
                        }
                    }
                    Log.d("filter", "onQueryTextChange: "+filtred);

                });

                return false;
            }
        });



        return root;
    }
public  void Search(HashMap<String,String[]> map,ArrayList<String> filtred,Boolean isToday){
    Map<String,String[]> today  = new HashMap<>();

if (isToday){
    map.forEach((n,k)->{
        for (String i : k){
            Log.d("else", "onQueryTextChange: "+i);
            if (i.toLowerCase().contains(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))){
                today.put(n,k);
                customAdapter.filterList(today);
            }
        }
    });
    Map filteredMap =today.entrySet()
            .stream()
            .filter(entry -> {
                Log.d("Searchmap", "Search: "+(Arrays.asList(entry.getValue())));
                return new HashSet<>(Arrays.asList(entry.getValue())).containsAll(filtred);
            })
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    Log.d("filtering", "Search: "+Arrays.asList(filtred));
    customAdapter.filterList(filteredMap);
}else {
    Map filteredMap = map.entrySet()
            .stream()
            .filter(entry -> {
                Log.d("Searchmap", "Search: "+(Arrays.asList(entry.getValue())));
                return new HashSet<>(Arrays.asList(entry.getValue())).containsAll(filtred);
            })
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    Log.d("filtering", "Search: "+Arrays.asList(filtred));
    customAdapter.filterList(filteredMap);
}








}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}