package com.example.tracc_it;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
public class DietActivity extends AppCompatActivity implements Diet_Dialog.DietDialogListener {
    private TextView submitMeal;
    private TextView numOfCal;
    private TextView optionalNotes;
    private Button buttonSubmit;

   @Override
    protected void onCreate(Bundle saveInstanceState)
   {
       super.onCreate(saveInstanceState);
       setContentView(R.layout.activity_diet);
       submitMeal  = (TextView) findViewById(R.id.Submit_A_Meal);
       numOfCal = (TextView) findViewById(R.id.Calories);
       optionalNotes = (TextView) findViewById(R.id.Notes);
       buttonSubmit = (Button) findViewById(R.id.Diet_Submit_Button);
       buttonSubmit.setOnClickListener( new View.OnClickListener() {
           @Override
           public void onClick(View view)
           {
               openDialog();
           }
       });
   }
   public void openDialog()
   {
        Diet_Dialog dietDialog = new Diet_Dialog();
        dietDialog.show(getSupportFragmentManager(), "Diet_Dialog");
   }

    @Override
    public void applyTexts(String TextMeal, String TextCal, String TextNotes) {
        submitMeal.setText(TextMeal);
        numOfCal.setText((TextCal));
        optionalNotes.setText(TextNotes);
    }
}
