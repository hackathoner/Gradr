package me.anuraag.grader;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashSet;
import java.util.Set;


public class CreateClass extends Activity {
    private View mCustomView;
    private TextView mTitleTextView,addNewText,backCovering;
    private ImageView back;
    private Button createClass;
    private EditText className;
    private TextView uw_w;
    private String jsonstring;
    private CheckBox mybox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my);
        android.app.ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        mCustomView = mInflater.inflate(R.layout.actionbar_two, null);
        mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText("Add a new class");
        back = (ImageView)mCustomView.findViewById(R.id.imageView1);
        createClass = (Button)findViewById(R.id.button);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        backCovering = (TextView)mCustomView.findViewById(R.id.backCovering);
        backCovering.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    backCovering.setBackground(new ColorDrawable(0xFFc0392b));
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    backCovering.setBackgroundColor(Color.TRANSPARENT);
                }
                return false;
            }
        });
        backCovering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                className.setText("");
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        className = (EditText)findViewById(R.id.name);
//        uw_w = (TextView)findViewById(R.id.weighted);
//        mybox = (CheckBox)findViewById(R.id.checkBox);
//        mybox.setChecked(true);
//        mybox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    uw_w.setText("Grades Weighted");
//                }else{
//                    uw_w.setText("Grades Unweighted");
//                }
//            }
//        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                className.setText("");
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        back.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    backCovering.setBackground(new ColorDrawable(0xFFc0392b));
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    backCovering.setBackgroundColor(Color.TRANSPARENT);
                }

                return true;
            }
        });
        createClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(className.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please make sure add a class name",
                            Toast.LENGTH_SHORT).show();

                }else{
                    JSONObject myclass = new JSONObject();
                    try {
                        myclass.put("Name", className.getText().toString());
                        myclass.put("Weighted", false);
                        jsonstring = myclass.toString();
                        Set<String> ss = new HashSet<String>();
                        SharedPreferences sharedPref = getSharedPreferences("grades.xml",MODE_PRIVATE);
                        Set<String> old = sharedPref.getStringSet("Grades",ss);
                        Log.i("Set",old.toString());
                        SharedPreferences.Editor ed = sharedPref.edit();
                        if(old.isEmpty()){
                            Set<String> grades = new HashSet<String>();
                            grades.add(jsonstring);
                            ed.putStringSet("Grades", grades);
                            Log.i("New Set",grades.toString());
                            ed.apply();
                        }else{
                            old.add(jsonstring);
                            ed.remove("Grades");
                            ed.putStringSet("Grades",old);
                            Log.i("New Set",old.toString());
                            ed.apply();

                        }


                    }catch (JSONException j){
                        Log.i(j.toString(), j.toString());
                    }
                    Toast.makeText(getApplicationContext(),"Class created",
                            Toast.LENGTH_LONG).show();

                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_class, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }
}
