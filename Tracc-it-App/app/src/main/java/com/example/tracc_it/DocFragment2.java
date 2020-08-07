package com.example.tracc_it;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class DocFragment2 extends Fragment {


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /*
        setContentView(R.layout.fragment_mod_doc);
        //doctorListEditText = findViewById(R.id.doctorList);

        // Inflate the layout for this fragment

        //get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.doctorList);
        //create a list of items for the spinner.
        String[] items = new String[]{"1", "2", "three"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        // Inflate the layout for this fragment*/
        return inflater.inflate(R.layout.fragment_mod_doc, container, false);

    }

}
