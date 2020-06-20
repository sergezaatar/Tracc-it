package com.example.tracc_it;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class Diet_Dialog extends AppCompatDialogFragment {
    private EditText editTextMeal;
    private EditText editTextCal;
    private EditText editTextNotes;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
        builder.setView(view)
                .setTitle("Submit")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener(){
                    @Override
                     public void onClick(DialogInterface dialogInterface, int i){

        }
    });
        editTextCal = view.findViewById(R.id.edit_calories);
        editTextMeal = view.findViewById(R.id.edit_meal);
        editTextNotes = view.findViewById(R.id.edit_notes);
    return builder.create();
    }
}
