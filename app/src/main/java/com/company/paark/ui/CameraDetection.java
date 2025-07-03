package com.company.paark.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.company.paark.ui.dashboard.DashboardFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

import java.util.List;

public class CameraDetection {
    public  String txt;

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public void CameraDetectionSys(Context context, Bitmap bitmap,EditText state,EditText district,EditText alpha, EditText num){




            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
            FirebaseVisionTextDetector detector = FirebaseVision.getInstance().getVisionTextDetector();
            detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                @Override
                public void onSuccess(FirebaseVisionText firebaseVisionText) {
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                    List<FirebaseVisionText.Block> blocks = firebaseVisionText.getBlocks();
                    if (blocks.size() == 0) {

                        Toast.makeText(context, "No Text ", Toast.LENGTH_LONG).show();

                    }

                    for (FirebaseVisionText.Block block : firebaseVisionText.getBlocks()) {

                        String txt = block.getText();
                    setTxt(txt);
                    Log.d("text", "onSuccess: "+getTxt());
                    SetEditables(state,district,alpha,num,context);

                   new DashboardFragment().setisCaptured(true);

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(context, "Fail to detect the text from image", Toast.LENGTH_SHORT).show();
                }
            });


        }
        public void SetEditables(EditText state,EditText district,EditText alpha, EditText num,Context context){
                StringBuilder code= new StringBuilder();
            if (getTxt().contains("\n")){
                for(String element:getTxt().split("\n")){
                    if (element.length()==5){
                       code.append(element);

                        Log.d("ele", "Milgya: "+element);


                    }else {
                        Log.d("ele", "Chodgya: "+element);
                    }


                }

            }else {
                for(String element:getTxt().split(" ")){
                    if (element.length()==4||element.length() == 2){
                        code.append(element);
                        Log.d("ele", "Milgya: "+element);
                    }else {
                        Log.d("ele", "Chodgya: "+element);
                    }
                }

            }
            try {
                state.setText(code.substring(0,2));
                district.setText(code.substring(2,4));
                alpha.setText(code.substring(4,6));
                num.setText(code.substring(6,10));
            }catch (Exception e){
                Toast.makeText(context, "Error in Image Fill Manually", Toast.LENGTH_SHORT).show();
            }
        }
    }

