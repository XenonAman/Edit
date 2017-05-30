package com.example.amank.edit;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.jar.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{

    String[] items = new String[] { "On Due date","1 day prior", "2 day prior", "3 day prior","4 day prior","5 day prior","6 day prior","1 week prior" };
    EditText dd,url1;
    EditText dt,amt;
    ImageButton btn,but;
    public static final int REQUEST_CAPTURE= 1;
    ImageView img1;
    Button saved;  //for bottom button
    Button savekar; // for menu bar button

    int year_x,month_x,day_x;
    static final int DILOG_ID = 0;

    public  static  DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);     //soft keybord shift

        showDialogOnImageButtonClickListener();

        final Calendar tarik = Calendar.getInstance();
        year_x =  tarik.get(Calendar.YEAR);
        month_x = tarik.get(Calendar.MONTH);
        day_x = tarik.get(Calendar.DAY_OF_MONTH);

        //-----------Initiate--------//

        dd = (EditText) findViewById(R.id.duedatesecE);
        but =(ImageButton)findViewById(R.id.daba);
        img1 = (ImageView)findViewById(R.id.imgdikha);
        saved = (Button)findViewById(R.id.savedata);
        url1 = (EditText)findViewById(R.id.urldala);

        databaseHelper = new DatabaseHelper(this);
        process();


        if(!hasCamers()){
            but.setEnabled(false);
        }

        //-----Spinner ka Code------------//

        Spinner dynamicSpinner = (Spinner)findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        dynamicSpinner.setAdapter(adapter);

      dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              Log.v("item", (String) parent.getItemAtPosition(position));

          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {

          }
      });
      //--------Spinner ka Code khatam----------//

    }       //--------Idher OnCreate method khatam-------------//

    //--------Camera API ka CODE------------//

    public boolean hasCamers()
    {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }
    public void showDialogOnImageButtonClickListener()
    {
        btn = (ImageButton)findViewById(R.id.cal);
        btn.setOnClickListener(
          new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  showDialog(DILOG_ID);
              }
          }
        ) ;
    }

    public void dabaya(View view) {
        Toast.makeText(this, "Camera chalu hone wala hai", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(i,REQUEST_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == REQUEST_CAPTURE) {
                Bundle extras = data.getExtras();
                Log.i("1st", "working");
                Bitmap photo = (Bitmap) extras.get("data");
                Log.i("2nd", "working");
                if (photo != null) {
                    img1.setImageBitmap(photo);
                    Log.i("3rd", "working");
                } else {
                    Toast.makeText(this, "Something Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Try Again!", Toast.LENGTH_SHORT).show();
        }
    }
    //--------Camera API ka CODE khatam------------//

    //--------Date Picker ka CODE------------//


    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == DILOG_ID)
            return new DatePickerDialog(this,dpickerListner,year_x,month_x,day_x);

        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x=year;
            month_x= month + 1;
            day_x=dayOfMonth;

            Toast.makeText(MainActivity.this,"Ye Date tune dala "+day_x +"/"+ month_x +"/" + year_x,Toast.LENGTH_LONG).show();

            dd.setText(day_x +"/"+ month_x +"/" + year_x);
        }
    };
    //--------Date Picker ka CODE khatam------------//


    //--------Save Button ka CODE------------//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.savey,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void savekar(MenuItem item) {

    }
    //--------Save Button ka CODE khatam------------//


    //--------URL Button ka CODE------------//

    public void webkholdia(View view)
    {
        Intent browserIntent  = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+url1.getText()));
        startActivity(browserIntent);
    }

    //----------SAVE Button ka CODE--------------//


    public void process() {
        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    databaseHelper.insertData(dt.getText().toString(),amt.getText().toString(),ImageToByte(img1));
                    Toast.makeText(getApplicationContext(), "Your DATA Save", Toast.LENGTH_SHORT).show();

                }
                catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Lag Gaye", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private byte[] ImageToByte(ImageView img1) {
        Bitmap bitmap = ((BitmapDrawable)img1.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


}   //---ye main activity ka bracket-------//

