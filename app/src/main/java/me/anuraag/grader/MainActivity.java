package me.anuraag.grader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;


public class MainActivity extends FragmentActivity {
    private View mCustomView;
    private TextView mTitleTextView,addNewText;
    private ImageView addNew;
    private JSONObject mygrade;
    private GradesAdapter gradesAdapter;
    private Intent itemIntent;
    private EditText newName;
    private AlertDialog.Builder myDeleteDialog;
    private Dialog editDialog;
    private TextView addClassCovering;
    private SharedPreferences sharedPreferences,sharedPref2;
    private ListView mygrades;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private static final int NUM_PAGES = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        SharedPreferences.Editor mypref = getSharedPreferences("grades.xml",MODE_PRIVATE).edit();
//        mypref.clear();
//        mypref.apply();
        sharedPref2 = getSharedPreferences("first.xml",MODE_PRIVATE);
//        sharedPref2.edit().remove("firstTime").apply();;
        if(sharedPref2.getBoolean("firstTime",true)) {
            startActivity(new Intent(this, ScreenSlider.class));
            SharedPreferences.Editor mypref = sharedPref2.edit();
            mypref.putBoolean("firstTime",false);
            mypref.apply();
        }
        android.app.ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        itemIntent = new Intent(getApplicationContext(),GradesActivity.class);
        mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        addNewText = (TextView) mCustomView.findViewById(R.id.addnew);
        addNew = (ImageView)mCustomView.findViewById(R.id.imageView1);
        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CreateClass.class));
            }
        });
        addNew.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    addClassCovering.setBackground(new ColorDrawable(0xFFc0392b));
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    addClassCovering.setBackgroundColor(Color.TRANSPARENT);
                }
                return false;
            }
        });
        addNewText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CreateClass.class));

            }

        });
        addNewText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    addClassCovering.setBackground(new ColorDrawable(0xFFc0392b));
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    addClassCovering.setBackgroundColor(Color.TRANSPARENT);
                }
                return false;
            }
        });
        addClassCovering = (TextView)mCustomView.findViewById(R.id.addClassCovering);

        addClassCovering.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CreateClass.class));
            }
        });
        addClassCovering.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    addClassCovering.setBackground(new ColorDrawable(0xFFc0392b));
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    addClassCovering.setBackgroundColor(Color.TRANSPARENT);
                }
                return false;
            }
        });
        populateListView();



        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
    }
    public void populateListView(){
         sharedPreferences = getSharedPreferences("grades.xml",MODE_PRIVATE);
        Set<String> grades = sharedPreferences.getStringSet("Grades", new HashSet<String>());
        Log.i("My Class Set",grades.toString());
        String named = " ";
        boolean weighted = false;
        JSONArray gradeList;
        String average = " ";
        if(!grades.isEmpty()){
            ArrayList<GradesObject> list = new ArrayList<GradesObject>();
            String[] gradesArray = grades.toArray(new String[grades.size()]);
            for(int x = 0; x < grades.size(); x++){
                try{
                     mygrade = new JSONObject(gradesArray[x]);
                     named = mygrade.getString("Name");
                     weighted = mygrade.getBoolean("Weighted");

                }catch (JSONException j){
                    Log.i(j.toString(),j.toString());
                }

                try{
                        average = mygrade.getString("average") + "%";
                        Log.i("Something","is happening");

                }catch (JSONException j){
                    Log.i(j.toString(),j.toString());
                    average = "N/A";

                }
                try{
                    gradeList = mygrade.getJSONArray("gradelist");
                    List<SingleGrade> mylist = new ArrayList<SingleGrade>();
                    for(int i = 0; i < gradeList.length(); i++){
                        SingleGrade singleGrade = new SingleGrade();
                        singleGrade.setName(gradeList.getJSONObject(i).getString("name"));
                        singleGrade.setPointsRight(Integer.parseInt(gradeList.getJSONObject(i).getString("pointsright")));
                        singleGrade.setTotalPoints(Integer.parseInt(gradeList.getJSONObject(i).getString("totalpoints")));
                        singleGrade.setPercentage(singleGrade.getPercentage());
                        mylist.add(singleGrade);
                    }
                }catch (JSONException j){
                    Log.i(j.toString(),j.toString());
                }
                GradesObject myobj = new GradesObject(named);
                myobj.setGradeAverage(average);
                myobj.setWeighted(weighted);
                myobj.setGradeAverage(average);
                list.add(myobj);
            }
            gradesAdapter = new GradesAdapter(this, list);
            mygrades = (ListView) findViewById(R.id.listView);
            mygrades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    GradesObject gradesObject = gradesAdapter.getItem(position);
                    itemIntent.putExtra("ClassGrade",gradesObject);
                    startActivity(itemIntent);
                }
            });
            mygrades.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    GradesObject gradesObject = gradesAdapter.getItem(position);
                    final String myClassName = gradesObject.getClassName();
                     myDeleteDialog = new AlertDialog.Builder(MainActivity.this);
                    myDeleteDialog.setTitle("What do you want to do?");
                    myDeleteDialog.setMessage("Do you want to edit or delete the class?");
                    //TODO Edit the Class Name (Make sure to look at the grades list for that class too)
                    //TODO Edit the grade item name
                    myDeleteDialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Set<String> myClassSet = sharedPreferences.getStringSet("Grades", new HashSet<String>());
                            Set<String> myClassGrades = sharedPreferences.getStringSet(myClassName, new HashSet<String>());
                            SharedPreferences.Editor myEditor = sharedPreferences.edit();
                            myEditor.remove(myClassName);
                            String[] myClassArray = myClassSet.toArray(new String[myClassSet.size()]);
                            String[] myTempClassArray = myClassArray;
                            ArrayList<String> classArrayList = new ArrayList<String>(Arrays.asList(myClassArray));
                            Log.i("Before removal", myClassSet.toString());
                            for (String a : myTempClassArray) {
                                try {
                                    JSONObject myClassJson = new JSONObject(a);
                                    if (myClassJson.getString("Name").equals(myClassName)) {
                                        classArrayList.remove(a);

                                    }
                                } catch (JSONException j) {
                                    Log.i(j.toString(), j.toString());
                                }
                            }
                            myEditor.remove("Grades");
                            myEditor.putStringSet("Grades", new HashSet<String>(classArrayList));
                            myEditor.apply();
                            Log.i("After removal", classArrayList.toString());
                            populateListView();
                        }
                    });
                    myDeleteDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    myDeleteDialog.setPositiveButton("Edit Class", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            editDialog = new Dialog(MainActivity.this);
                            editDialog.setContentView(R.layout.edit_dialog_view);
                            editDialog.setTitle("Edit Class Name");
                            editDialog.show();
                            Log.i("WTF", "Is going on");
                            newName = (EditText) editDialog.findViewById(R.id.newText);

                            Button submitButton = (Button) editDialog.findViewById(R.id.button);
                            submitButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!newName.getText().toString().isEmpty()) {
                                        String newClassName = newName.getText().toString();
                                        Set<String> myClassSet = sharedPreferences.getStringSet("Grades", new HashSet<String>());
                                        Set<String> myClassGrades = sharedPreferences.getStringSet(myClassName, new HashSet<String>());
                                        SharedPreferences.Editor myEditor = sharedPreferences.edit();
                                        Set<String> myTempGradesSet = sharedPreferences.getStringSet(myClassName, new HashSet<String>());
                                        myEditor.remove(myClassName);
                                        myEditor.putStringSet(newClassName, myTempGradesSet);
                                        String[] myClassArray = myClassSet.toArray(new String[myClassSet.size()]);
                                        String[] myTempClassArray = myClassArray;
                                        ArrayList<String> classArrayList = new ArrayList<String>(Arrays.asList(myClassArray));
                                        Log.i("Before removal", myClassSet.toString());
                                        for (String a : myTempClassArray) {
                                            try {
                                                JSONObject myClassJson = new JSONObject(a);
                                                if (myClassJson.getString("Name").equals(myClassName)) {
                                                    classArrayList.remove(a);
                                                    myClassJson.remove("Name");
                                                    myClassJson.put("Name", newClassName);
                                                    classArrayList.add(myClassJson.toString());
                                                }
                                            } catch (JSONException j) {
                                                Log.i(j.toString(), j.toString());
                                            }
                                        }
                                        myEditor.remove("Grades");
                                        myEditor.putStringSet("Grades", new HashSet<String>(classArrayList));
                                        myEditor.apply();
                                        Log.i("After removal", classArrayList.toString());
                                        populateListView();
                                        editDialog.dismiss();

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Please make sure you enter your input",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        }
                    });
                    myDeleteDialog.show();
                    Log.i("Something","Is going on");
                    populateListView();

                    return true;
                }
            });
            mygrades.setAdapter(gradesAdapter);
        }else{
            mygrades = (ListView) findViewById(R.id.listView);
            mygrades.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public static class GradesAdapter extends ArrayAdapter<GradesObject> {
        private TextView grade,name;
        public GradesAdapter(Context context, ArrayList<GradesObject> notifs) {
            super(context, 0, notifs);
        }

        @Override
        public View getView(int position, View rootView, ViewGroup parent) {
            // Get the data item for this position
            GradesObject notif = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (rootView == null) {
                rootView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_item, parent, false);
            }
            grade = (TextView)rootView.findViewById(R.id.gradeAverage);
            name = (TextView)rootView.findViewById(R.id.title);
            if(notif.getClassName().length() > 25){
                name.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
            }
            name.setText(notif.getClassName());
            grade.setText(notif.getAverage());
            return rootView;
        }


    }

    }



