package me.anuraag.grader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;


public class GradesActivity extends Activity {
    private View mCustomView;
    private TextView mTitleTextView,addNewText,myAddCovering,myBackCovering,myPredictCovering,predictTotal,gradeWanted;
    private ImageView addNew;
    private ImageView back;
    private EditText newName,newTotal,newCorrect;
    private SingleGradeAdapter singleGradeAdapter;
    private SingleGrade singleGrade;
    private Button createClass,predictButton;
    private TextView ograde;
    private AlertDialog.Builder myDeleteDialog;
    private Dialog myDialog;
    private int runningCorrect,runningTotal;
    private Dialog addGrade,predictDialog;
    private String singleGradeName;
    private SharedPreferences sharedPreferences;
    private EditText assignmentName,pointsCorrect,totalPoints;
    private JSONObject mygrade;
    private ListView mygrades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);
        android.app.ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        mCustomView = mInflater.inflate(R.layout.actionbar_three, null);
        mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText("Add a new class");
        addNew = (ImageView)mCustomView.findViewById(R.id.imageView1);
//        addNew.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                createGrade();
////                startActivity(new Intent(getApplicationContext(),CreateClass.class));
//            }
//        });
        myPredictCovering = (TextView)mCustomView.findViewById(R.id.predictCovering);
        myPredictCovering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                predictDialog = new Dialog(GradesActivity.this);
                predictDialog.setTitle("Predict required score");
                predictDialog.setContentView(R.layout.predict_dialog);
                predictTotal = (EditText)predictDialog.findViewById(R.id.pointscorrect);
                gradeWanted = (EditText)predictDialog.findViewById(R.id.assignment);
                predictButton = (Button)predictDialog.findViewById(R.id.button);
                predictDialog.show();

                predictButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!gradeWanted.getText().toString().isEmpty() && !predictTotal.getText().toString().isEmpty() && isNumericDouble(predictTotal.getText().toString().trim().replaceAll("\\s+","")) && isNumericDouble(gradeWanted.getText().toString().trim().replaceAll("\\s+", ""))){
                            double predictTotalInt = Double.parseDouble(predictTotal.getText().toString());
                            double predictTotalGrade = Double.parseDouble(gradeWanted.getText().toString());
                            if(predictTotalInt > 0 && predictTotalGrade>=0) {
                                double numberNeeded = 0;
                                double gradeWantedDouble = Double.parseDouble(gradeWanted.getText().toString().trim().replaceAll("\\s+", ""));
                                int predictedTotalInt = Integer.parseInt(predictTotal.getText().toString());
                                numberNeeded = (gradeWantedDouble / 100) * (runningTotal + predictedTotalInt) - (runningCorrect);
                                DecimalFormat f = new DecimalFormat("##.00");

                                SingleGrade tempgrade = new SingleGrade();
                                tempgrade.setPointsRight((int) numberNeeded);
                                tempgrade.setTotalPoints(predictedTotalInt);
                                Toast.makeText(getApplicationContext(), "The points you would need would be:  " + f.format(numberNeeded) + ". The percentage you would need on the test would be a: " + tempgrade.getPercentage(),
                                        Toast.LENGTH_LONG).show();
                                predictDialog.dismiss();
                            }else{
                                if(predictTotalGrade < 0){
                                    Toast.makeText(getApplicationContext(), "Nice try but you can't have a negative grade :) .",
                                            Toast.LENGTH_LONG).show();
                                }
                                else if(predictTotalInt<=0){
                                    Toast.makeText(getApplicationContext(), "Nice try but you can't have an assignment which is worth less than or equal to 0 points :) .",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Please fill out all the fields properly",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });
        myPredictCovering.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    myPredictCovering.setBackground(new ColorDrawable(0xFFc0392b));
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    myPredictCovering.setBackgroundColor(Color.TRANSPARENT);
                }
                return false;
            }
        });
        myBackCovering = (TextView)mCustomView.findViewById(R.id.backCovering);
        myBackCovering.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    myBackCovering.setBackground(new ColorDrawable(0xFFc0392b));
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    myBackCovering.setBackgroundColor(Color.TRANSPARENT);
                }
                return false;
            }
        });
        myBackCovering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        myAddCovering = (TextView)mCustomView.findViewById(R.id.addCovering);
        myAddCovering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("On CLick", "Works");
                createGrade();
            }
        });
        myAddCovering.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    myAddCovering.setBackground(new ColorDrawable(0xFFc0392b));
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    myAddCovering.setBackgroundColor(Color.TRANSPARENT);
                }

                return false;
            }
        });
        back = (ImageView)mCustomView.findViewById(R.id.back);
//        back.setOnTouchListener( new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });
        mygrades = (ListView)findViewById(R.id.listView);
        GradesObject myobj = (GradesObject)getIntent().getSerializableExtra("ClassGrade");
        mTitleTextView.setText(myobj.getClassName());
        SharedPreferences mypref = getSharedPreferences("Grades.xml",MODE_PRIVATE);

//        Log.i("Issue",myobj.getGradesList().toString());
//        if(!mypref.getStringSet(mTitleTextView.getText().toString(),new HashSet<String>()).isEmpty()){
            populateListView();
//        }else{
//            Toast.makeText(getApplicationContext(), "You don't have any grades yet",
//                    Toast.LENGTH_LONG).show();
//
//        }
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
    }

    public void populateListView(){
        ArrayList<SingleGrade> list = new ArrayList<SingleGrade>();
        SharedPreferences myprefs = getSharedPreferences("grades.xml",MODE_PRIVATE);
        Set<String> mygradesSet = myprefs.getStringSet(mTitleTextView.getText().toString(),new HashSet<String>());
        String[] myGradesArray = mygradesSet.toArray(new String[mygradesSet.size()]);
         runningCorrect = 0;
         runningTotal = 0;
        ograde = (TextView)findViewById(R.id.ograde);
        Log.i("Grades Array",mygradesSet.toString());
        for(int x = 0; x < myGradesArray.length; x++){
            SingleGrade mygrade = new SingleGrade();
            mygrade.setName(myGradesArray[x].substring(0,myGradesArray[x].indexOf(">")));
            mygrade.setPointsRight(Integer.parseInt(myGradesArray[x].substring(myGradesArray[x].indexOf(">") + 1,myGradesArray[x].indexOf("/"))));
            mygrade.setTotalPoints(Integer.parseInt(myGradesArray[x].substring(myGradesArray[x].indexOf("/") + 1,myGradesArray[x].length())));
            runningCorrect+=mygrade.getPointsRight();
            runningTotal+=mygrade.getTotalPoints();
            list.add(mygrade);
            Log.i("issue","issue");
        }
        if(!mygradesSet.isEmpty()) {
            TextView isMissing = (TextView)findViewById(R.id.ifMissing);
            isMissing.setVisibility(View.INVISIBLE);
            sharedPreferences = getSharedPreferences("grades.xml",MODE_PRIVATE);
            Set<String> myClassSet = sharedPreferences.getStringSet("Grades", new HashSet<String>());
            String[] myClassArray = myClassSet.toArray(new String[myClassSet.size()]);
            String[] myTempClassArray = myClassArray;
            ArrayList<String> classArrayList = new ArrayList<String>(Arrays.asList(myClassArray));
            SharedPreferences.Editor myEditor = sharedPreferences.edit();
            DecimalFormat f = new DecimalFormat("##.00");
            SingleGrade mytempgrade = new SingleGrade();
            mytempgrade.setPointsRight(runningCorrect);
            mytempgrade.setTotalPoints(runningTotal);
            double finalGrade =  mytempgrade.getPercentage();

            Log.i("ArrayList Before hand", classArrayList.toString());
            for (String a : myTempClassArray) {
                try {
                    JSONObject myClassJson = new JSONObject(a);
                    if (myClassJson.getString("Name").equals(mTitleTextView.getText().toString())) {
                        classArrayList.remove(a);
                        Log.i("Hello","Is anyone home");
                        try{
                            String s = myClassJson.getString("average");
                            myClassJson.remove("average");
                            myClassJson.put("average",finalGrade);

                        }catch(JSONException j){
                            Log.i(j.toString(),j.toString());
                            myClassJson.put("average",finalGrade);

                        }

                        classArrayList.add(myClassJson.toString());
                        Log.i("Class Array List",classArrayList.toString());
                    }
                } catch (JSONException j) {
                    Log.i(j.toString(), j.toString());
                }

            }
            Log.i("ArrayList After hand", classArrayList.toString());

            myEditor.remove("Grades");
            myEditor.putStringSet("Grades", new HashSet<String>(classArrayList));
            myEditor.apply();
            ograde.setText(finalGrade + "%");
        }else{
            TextView isMissing = (TextView)findViewById(R.id.ifMissing);
            isMissing.setVisibility(View.VISIBLE);
            ograde.setText("N/A");



        }
        Log.i("Something","is happening");
//        SingleGrade singleGrade1 = new SingleGrade();
//        singleGrade1.setName("BC Test 9");
//        singleGrade1.setTotalPoints(50);
//        singleGrade1.setPointsRight(41);
//        list.add(singleGrade1);
        singleGradeAdapter = new SingleGradeAdapter(this, list);
        mygrades.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                SingleGrade singleGrade = singleGradeAdapter.getItem(position);
                 singleGradeName = singleGrade.getName();
                myDeleteDialog = new AlertDialog.Builder(GradesActivity.this);
                myDeleteDialog.setTitle("What do you want to do?");
                myDeleteDialog.setMessage("Do you want to edit or delete this grade?");
                myDeleteDialog.setPositiveButton("Edit grade", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDialog = new Dialog(GradesActivity.this);
                        myDialog.setContentView(R.layout.edit_grade_dialog);
                        myDialog.setTitle("Edit grade");
                        myDialog.show();
                         newName = (EditText)myDialog.findViewById(R.id.assignment);
                         newCorrect = (EditText)myDialog.findViewById(R.id.pointscorrect);
                         newTotal = (EditText)myDialog.findViewById(R.id.totalpoints);
                        Button myButton = (Button)myDialog.findViewById(R.id.button);
                        myButton.setOnClickListener( new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String newNameString = newName.getText().toString();
                                String newCorrectString = newCorrect.getText().toString();
                                String newTotalString = newTotal.getText().toString();
                                if(!newCorrectString.isEmpty() && !newNameString.isEmpty() && !newTotalString.isEmpty()
                                        && isNumeric(newCorrectString) && isNumeric(newTotalString) && Integer.parseInt(newCorrectString) >= 0 && Integer.parseInt(newTotalString) >= 1) {
                                    newNameString = newNameString.trim();
//                                    newNameString = newNameString.replaceAll("\\s+","");
                                    sharedPreferences = getSharedPreferences("grades.xml", MODE_PRIVATE);
                                    Set<String> gradesSet = sharedPreferences.getStringSet(mTitleTextView.getText().toString(), new HashSet<String>());
                                    ArrayList<String> gradesArrayList = new ArrayList<String>(gradesSet);
                                    for (String a : gradesSet) {
                                        if (a.substring(0, a.indexOf(">")).equals(singleGradeName)) {
                                            gradesArrayList.remove(a);
                                            gradesArrayList.add(newNameString + ">" + newCorrectString + "/" + newTotalString);
                                        }
                                    }
                                    SharedPreferences.Editor myEditor = sharedPreferences.edit();
                                    myEditor.remove(mTitleTextView.getText().toString());
                                    myEditor.putStringSet(mTitleTextView.getText().toString(), new HashSet<String>(gradesArrayList));
                                    myEditor.apply();
                                    populateListView();
                                    myDialog.dismiss();
                                }else{
                                    Toast.makeText(getApplicationContext(), "Please fill out all the fields properly",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                myDeleteDialog.setNegativeButton("Delete grade", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sharedPreferences = getSharedPreferences("grades.xml",MODE_PRIVATE);
                        Set<String> gradesSet = sharedPreferences.getStringSet(mTitleTextView.getText().toString(),new HashSet<String>());
                        ArrayList<String> gradesArrayList = new ArrayList<String>(gradesSet);
                        for(String a: gradesSet){
                            if(a.substring(0,a.indexOf(">")).equals(singleGradeName)){
                                gradesArrayList.remove(a);
                            }
                        }
                        SharedPreferences.Editor myEditor = sharedPreferences.edit();
                        myEditor.remove(mTitleTextView.getText().toString());
                        myEditor.putStringSet(mTitleTextView.getText().toString(),new HashSet<String>(gradesArrayList));
                        myEditor.apply();
                        populateListView();
                    }
                });
                myDeleteDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                myDeleteDialog.show();

                return false;
            }
        });
        mygrades.setAdapter(singleGradeAdapter);
        //TODO Create New Grades WITH TYPES IF WEIGHTED
        //TODO Calculate the Grades
        //TODO Edit Classes/Grades

    }
    public void createGrade(){
         addGrade = new Dialog(this);
        addGrade.setTitle("Add a new grade");
        addGrade.setContentView(R.layout.create_grade_layout);
        addGrade.show();
        assignmentName = (EditText)addGrade.findViewById(R.id.assignment);
        pointsCorrect = (EditText)addGrade.findViewById(R.id.pointscorrect);
        totalPoints = (EditText)addGrade.findViewById(R.id.totalpoints);

        Button submit =(Button)addGrade.findViewById(R.id.button);
        submit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalPoints.getText().toString().isEmpty() || pointsCorrect.getText().toString().isEmpty() || assignmentName.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill out all the fields properly",
                            Toast.LENGTH_SHORT).show();

                }else if(!isNumeric(totalPoints.getText().toString()) || !isNumeric(pointsCorrect.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please make sure your point values are numbers",
                            Toast.LENGTH_SHORT).show();

                }else if(Integer.parseInt(totalPoints.getText().toString()) <= 0){
                    Toast.makeText(getApplicationContext(), "Please make sure your total points are not less than or equal to zero",
                            Toast.LENGTH_SHORT).show();
                }else if(Integer.parseInt(pointsCorrect.getText().toString()) < 0){
                    Toast.makeText(getApplicationContext(), "Please make sure your points correct are greater than or equal to zero",
                            Toast.LENGTH_SHORT).show();
                }else {

                    Toast.makeText(getApplicationContext(), "Grade Added",
                            Toast.LENGTH_SHORT).show();
                    String aName = assignmentName.getText().toString();
                    aName = aName.trim();
//                    aName = aName.replaceAll("\\s+","");
                    String pCorrect = pointsCorrect.getText().toString();
                    String tPoints = totalPoints.getText().toString();
                    SharedPreferences sharedPreferences = getSharedPreferences("grades.xml", MODE_PRIVATE);
                    String className = mTitleTextView.getText().toString();
                    Set<String> grades = sharedPreferences.getStringSet(className, new HashSet<String>());
                    if(grades.isEmpty()){
                        Set<String> gradesSet = new TreeSet<String>();
                        gradesSet.add(aName + ">" + pCorrect + "/" + tPoints );
                        SharedPreferences.Editor ed = sharedPreferences.edit();
                        ed.putStringSet(className,gradesSet);
                        ed.apply();
                        Log.i("What","is going on");
                        populateListView();
                    }else{
                        SharedPreferences.Editor ed = sharedPreferences.edit();
                        grades.add(aName + ">" + pCorrect + "/" + tPoints);
                        ed.remove(className);
                        ed.commit();
                        Log.i("Grades Array Added",grades.toString());
                        ed.putStringSet(className,grades);
                        ed.apply();
                        Log.i("It","is going on");

                        populateListView();
                    }
                    addGrade.cancel();
                }

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_grades, menu);
        return true;
    }
    public boolean isNumericDouble(String str) {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }
    public boolean isNumeric(String str) {
        try
        {
            int d = Integer.parseInt(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }
    public static class SingleGradeAdapter extends ArrayAdapter<SingleGrade> {
        private TextView perc,points,name;
        public SingleGradeAdapter(Context context, ArrayList<SingleGrade> notifs) {
            super(context, 0, notifs);
        }

        @Override
        public View getView(int position, View rootView, ViewGroup parent) {
            // Get the data item for this position
            SingleGrade notif = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (rootView == null) {
                rootView = LayoutInflater.from(getContext()).inflate(R.layout.grades_listview_item, parent, false);
            }
            perc = (TextView)rootView.findViewById(R.id.gradeAverage);
            points = (TextView)rootView.findViewById(R.id.points);
            name = (TextView)rootView.findViewById(R.id.title);
            if(notif.getName().length() >= 25){
                name.setText(notif.getName());
                name.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);

            }else{
                name.setText(notif.getName());

            }
            points.setText(notif.getPointsRight() + "/" + notif.getTotalPoints());
            perc.setText(notif.getPercentage() + "%");
            return rootView;
        }


    }
}
