package com.company.paark.ui.dashboard;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.company.paark.Data;
import com.company.paark.MainActivity;
import com.company.paark.Profile;
import com.company.paark.R;
import com.company.paark.databinding.FragmentDashboardBinding;
import com.company.paark.profileData;
import com.company.paark.ui.CameraDetection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public static boolean ishours = false;
    public boolean isCaptured = false;
    EditText state;
    EditText district;
    EditText alpha;
    EditText number;
    private FirebaseAuth mAuth;
    private DatabaseReference mPostRefrence;
    Dictionary<Integer,int[]> type= new Hashtable<>();
    public boolean isCaptured() {
        return isCaptured;
    }

    public void setisCaptured(boolean isCaptured) {
        this.isCaptured = isCaptured;
    }
    private int[] Fare = new int[1];
    CameraDetection cameraDetection;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        cameraDetection = new CameraDetection();
        EditText time = root.findViewById(R.id.itimem);
        EditText date = root.findViewById(R.id.idatem);

         state = root.findViewById(R.id.statem);
         district = root.findViewById(R.id.districtm);
         alpha = root.findViewById(R.id.alpham);
         number = root.findViewById(R.id.numberm);



       mPostRefrence = FirebaseDatabase.getInstance().getReference();

        EditText ftime = root.findViewById(R.id.ftimem);
        EditText fdate = root.findViewById(R.id.fdatem);
        Button calculate = root.findViewById(R.id.calculate);
        Button book = root.findViewById(R.id.Book);
        TextView fare = root.findViewById(R.id.fare);
        Spinner spinner = root.findViewById(R.id.spinner);
        RadioGroup radioGroup = root.findViewById(R.id.radioGroup);
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        int year = mcurrentTime.get(Calendar.YEAR);
        int month = mcurrentTime.get(Calendar.MONTH);
        int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);
        EditText phoneno = binding.phonenom;
        mAuth = FirebaseAuth.getInstance();


        ArrayList<String> categories = new ArrayList<String>();
        categories.add("20 min");
        categories.add("30 min");
        categories.add("45 min");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,categories);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);



        Log.d("date", "onCreateView1: "+hour+":"+minute+" "+year +  " "+month+":"+day);



        time.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")) );
        date.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        fdate.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog =new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        try {
                            Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse((dayOfMonth+"-" +(month+1)+ "-" +year));
                            String newstring = new SimpleDateFormat("dd-MM-yyyy").format(date1);
                            date.setText(newstring);
                            /// set the text
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }

                    }
                },year,month,day);
             datePickerDialog.setTitle("Select Date");
            datePickerDialog.show();
            }

        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        try {
                        Date date = new SimpleDateFormat("HH:mm").parse((selectedHour+":"+selectedMinute));
                        String newstring = new SimpleDateFormat("HH:mm").format(date);
                        time.setText(newstring);

                        }catch (Exception e){
                            System.out.println(e);
                        }

                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


        fdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog =new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        fdate.setText(dayOfMonth+"-" +month+ "-" +year);
                        try {
                            Date date = new SimpleDateFormat("dd-MM-yyyy").parse((dayOfMonth+"-" +(month+1)+ "-" +year));
                            String newstring = new SimpleDateFormat("dd-MM-yyyy").format(date);
                            fdate.setText(newstring);
                            /// set the text
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },year,month,day);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show();
            }

        });
        ftime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        try {
                            Date date = new SimpleDateFormat("HH:mm").parse((selectedHour+":"+selectedMinute));
                            String newstring = new SimpleDateFormat("HH:mm").format(date);
                            ftime.setText(newstring);

                        }catch (Exception e){
                            System.out.println(e);
                        }

                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(), categories.get(position), Toast.LENGTH_SHORT).show();

                ftime.setText((LocalDateTime.now().plusMinutes(Long.parseLong(spinner.getSelectedItem().toString().split(" ")[0]))).format(DateTimeFormatter.ofPattern("HH:mm")));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), String.valueOf(radioGroup.getCheckedRadioButtonId()), Toast.LENGTH_SHORT).show();
//                ThreadLocalRandom.current().ints(0,100).distinct().limit(5).forEach(System.out::println);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {

//                    Log.d("dic", "onClick: "+type.get(R.id.FourWheeler)[1]);

                        LocalDateTime dateTime1 = LocalDateTime.parse((date.getText().toString() +" "+time.getText().toString()) , DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
                        LocalDateTime dateTime2 = LocalDateTime.parse((fdate.getText().toString() +" "+ftime.getText().toString()) , DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
                        long val = Calculator(dateTime1,dateTime2);

                        if (ishours){


                        fare.setText("Total Hours: "+String.valueOf(((val<0)?"Time Not Possible":val))+" and Total Fare: "+((val<0)?0:val)*Fare(radioGroup.getCheckedRadioButtonId(),3)+" /-");
                        Log.d("dif", "onClick: "+String.valueOf(val));
                        }else {

                            fare.setText("Total Minutes: "+spinner.getSelectedItem().toString()+" and Total Fare: "+Fare(radioGroup.getCheckedRadioButtonId(),spinner.getSelectedItemPosition()));

//                            database(state.getText().toString(),district.getText().toString(),alpha.getText().toString(),number.getText().toString(),LocalDateTime.now(),LocalDateTime.now().plusMinutes(Long.parseLong(spinner.getSelectedItem().toString().split(" ")[0])));
//                            Toast.makeText(getContext(),String.valueOf(ishours), Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number.getText().toString().isEmpty() ){
                    number.setError("Enter the Number");
                } else if ( phoneno.getText().toString().isEmpty()) {
                    phoneno.setError("Phone Number Required");
                } else if(phoneno.getText().toString().length() !=10){
                    phoneno.setError("Enter Valid Number");
                }else {



                double rand = Math.floor((1000000+Math.random()*10000000));
                String code = Double.valueOf(rand).toString().replace(".0","");
                LocalDateTime dateTime1 = LocalDateTime.parse((date.getText().toString() +" "+time.getText().toString()) , DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
                LocalDateTime dateTime2 = LocalDateTime.parse((fdate.getText().toString() +" "+ftime.getText().toString()) , DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
                long val = Calculator(dateTime1,dateTime2);
                Long Fare1 = (ishours?val*Fare(radioGroup.getCheckedRadioButtonId(),3):Fare(radioGroup.getCheckedRadioButtonId(),spinner.getSelectedItemPosition()));
                ArrayList<String> message = new ArrayList<>();
                message.add("You Have Parked Your Vehical for "+val+(ishours?" Hours ":" Minutes")+" from "+dateTime1+" to "+dateTime2 + " vehical number  " +state.getText().toString() +" "+district.getText().toString() +" "+alpha.getText().toString()+" "+number.getText().toString());
                message.add(" Paid Rs. "+ Fare1);
                message.add(" Your Parking code is "+ code +"\n DO NOT SHARE THE CODE WITH ANYONE \n PLZ CHECK THAT LAST 4 DIGITS OF THE VEHICAL NUMBER IS CORRECTLY WRITTEN");

//                String msg  ="You Have Parked Your Vehical for "+val+(ishours?" Hours ":" Minutes")+" from "+dateTime1+" to "+dateTime2 + " vehical number  " +state.getText().toString() +" "+district.getText().toString() +" "+alpha.getText().toString()+" "+number.getText().toString()+" Paid Rs. "+ (ishours?val*Fare(radioGroup.getCheckedRadioButtonId(),3):Fare(radioGroup.getCheckedRadioButtonId(),spinner.getSelectedItemPosition()))+" Your Parking code is "+ code +"\n DO NOT SHARE THE CODE WITH ANYONE \n CHECK THAT LAST 4 DIGITS OF THE VEHICAL NUMBER IS CORRECTLY WRITTEN";
                Log.d("msg", "msg: "+message);
                try {
                SmsManager smsManager = SmsManager.getDefault();

                smsManager.sendMultipartTextMessage(phoneno.getText().toString(),null,message,null,null);
                Toast.makeText(getContext(), "Message sent", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Error in Sending the Text", Toast.LENGTH_SHORT).show();
                }

                database(state.getText().toString(),district.getText().toString(),alpha.getText().toString(),number.getText().toString(),dateTime1.toString(),dateTime2.toString(),code,Fare1.toString(),(radioGroup.getCheckedRadioButtonId()==R.id.TwoWheeler?"Two Wheeler":"Four Wheeler"),false,phoneno.getText().toString());
            }
            }
        });
        Button capture = root.findViewById(R.id.cap);
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, 1);

            }
        });


        return root;
    }

    private int Fare(int id, int postion){





        return  Fare[0] = type.get(id)[postion];
    }

    @Override
    public void onStart() {
        super.onStart();
        mPostRefrence.child("Pricing").child(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    profileData post= task.getResult().getValue(profileData.class);
                    int[] two = {Integer.parseInt(post.T20),Integer.parseInt(post.T30),Integer.parseInt(post.T45),Integer.parseInt(post.Tperh)};
                    int[] four = {Integer.parseInt(post.f20),Integer.parseInt(post.f30),Integer.parseInt(post.f45),Integer.parseInt(post.fperh)};
                    type.put(R.id.TwoWheeler, two);
                    type.put(R.id.FourWheeler, four);



                    Log.d("finfare", "onDataChange: "+Fare[0]);
                    Log.d("firebase", String.valueOf(task.getResult().getValue(profileData.class)));
                }
            }
        });
    }

    public long Calculator(LocalDateTime dateTime1, LocalDateTime dateTime2){
//        LocalDateTime dateTime1 = LocalDateTime.parse((idate[2]+"-"+idate[1]+"-"+idate[0]+" "+ihour+":"+imin), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        LocalDateTime dateTime2 = LocalDateTime.parse((fdate[2]+"-"+fdate[1]+"-"+fdate[0]+" "+fhour+":"+fmin), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        Log.d("date",idate+" "+itime );
        Long hrs = Duration.between(dateTime1,dateTime2).toHours();
        Long min = Duration.between(dateTime1,dateTime2).toMinutes();
        //Toast.makeText(getContext(), String.valueOf(hrs), Toast.LENGTH_SHORT).show();
        if (hrs ==0){
            ishours = false;
        return min ;

        }else {
            ishours = true;
            return hrs;
        }
    }
    public void database(String state,String district,String alpha,String num,String iddate,String fddate,String code,String fare,String vtype,Boolean isWithdrawan,String phoneno){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

//        Toast.makeText(getContext(), code, Toast.LENGTH_SHORT).show();
        Data user = new Data(state,district,alpha,num,iddate,fddate,fare,vtype,isWithdrawan,phoneno);

        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child(code).setValue(user);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode ==  -1) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

//            Toast.makeText(getContext(), "clicked", Toast.LENGTH_SHORT).show();
            cameraDetection.CameraDetectionSys(getContext(), imageBitmap,state,district,alpha,number);

//            cameraDetection.SetEditables(state,district,alpha,number);
            // below line is to set the
            // image bitmap to our image.
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}