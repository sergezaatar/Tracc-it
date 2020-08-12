package com.example.tracc_it;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.text.TextUtils.concat;

public class MedsFragmentAdd extends Fragment {
    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    F I R E    B A S E    V A R I A B L E S
    /**/    private FirebaseAuth mAuth;
    /**/    private FirebaseFirestore database;

    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    U S E R    I N F O R M A T I O N
    /**/    Number medDose, hour, min;
    /**/    String medName, medSignature;

    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    I N P U T    V A R I A B L E S
    /**/     EditText textMedName, textMedDose, textMedSignature;
    /**/     Button medsButton, editButton;
    /**/     TimePicker timePicker1;

    private String TAG;

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Lemmmmm";
            String description = "Jennnn";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Noo", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        createNotificationChannel();

        return inflater.inflate(R.layout.fragment_add_meds, container, false);
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        String am_pm;
        super.onCreate(savedInstanceState);
        textMedDose = view.findViewById(R.id.medDose);
        textMedName = view.findViewById(R.id.medName);
        textMedSignature = view.findViewById(R.id.medSignature);
        medsButton = view.findViewById(R.id.medsButton);
        timePicker1 = view.findViewById(R.id.timePicker1);
        int hour = (timePicker1.getHour())*3600;
        int min = (timePicker1.getMinute())*60;
        Calendar cal = Calendar.getInstance();
        long millis = hour+min;


        String currentTime = new SimpleDateFormat("HH:mm aaa", Locale.getDefault()).format(new Date());



        final Ringtone r = RingtoneManager.getRingtone(view.getContext(),RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
        if(hour > 12) {
            am_pm = "PM";
            hour = hour - 12;
        }
        else
        {
            am_pm="AM";
        }
        String time = hour + ":" + min +" "+ am_pm;




        medsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(view.getContext(), "Your medication has been added!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(view.getContext(), ReminderBroadcast.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(view.getContext(),0,intent,0);
                AlarmManager alarmManager =(AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                long timeatButtonClick = System.currentTimeMillis();
                long tenSecondsinMillsis =  1000 * 10;
                alarmManager.set(AlarmManager.RTC_WAKEUP,millis, pendingIntent);
                addMeds(time);


            }
        });
    }
    private void addMeds(String time)
    {
        // Set up firebase stuff
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        Map<String, Object> Rx = new HashMap<>();
        try {
            Rx.put("Rx Name", textMedName.getText().toString().trim());
            Rx.put("Rx Dose", textMedDose.getText().toString().trim());
            Rx.put("Rx Signature", textMedSignature.getText().toString().trim());
            Rx.put("Rx Time", time );

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }



        database.collection("users").document(mAuth.getCurrentUser().getEmail())
                .collection("Rx").document(textMedName.getText().toString().trim())
                .set(Rx)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");

                        // After successful upload to database the user has added a doctor
                        // Set the home page fragment to be viewed first
                        Class fragmentClass = HomeFragment.class;
                        Fragment fragment = null;
                        try {
                            fragment = (Fragment) fragmentClass.newInstance();
                            // Insert the fragment by replacing any existing fragment
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

    }

}
