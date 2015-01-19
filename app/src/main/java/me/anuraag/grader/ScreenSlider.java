package me.anuraag.grader;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.ImageView;


public class ScreenSlider extends FragmentActivity {
    private ViewPager mPager;
    private static final int NUM_PAGES = 8;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slider);
        try {
            getActionBar().hide();
        }catch(NullPointerException n){

        }
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_screen_slider, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }
    public static class ScreenSlidePageFragment extends android.support.v4.app.Fragment {
        private int position;
        public static ScreenSlidePageFragment newInstance(int position) {

            ScreenSlidePageFragment f = new ScreenSlidePageFragment();
            Bundle b = new Bundle();
            b.putInt("position", position);

            f.setArguments(b);

            return f;
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(
                    R.layout.viewpager_layout, container, false);
            ImageView myImage = (ImageView)rootView.findViewById(R.id.myImage);
            Button myButton = (Button)rootView.findViewById(R.id.textView);
            if(getArguments().getInt("position") == 1){
                myImage.setImageDrawable(getResources().getDrawable(R.drawable.s4));
                myButton.setText("Add new classes");

            }
            if(getArguments().getInt("position") == 2){
                myImage.setImageDrawable(getResources().getDrawable(R.drawable.s1));
                myButton.setText("View your grades for the class");

            }
            if(getArguments().getInt("position") == 3){
                myImage.setImageDrawable(getResources().getDrawable(R.drawable.s2));
                myButton.setText("Add new grades");

            }
            if(getArguments().getInt("position") == 4){
                myImage.setImageDrawable(getResources().getDrawable(R.drawable.s3));
                myButton.setText("Predict your scores");


            }
            if(getArguments().getInt("position") == 5){
                myImage.setImageDrawable(getResources().getDrawable(R.drawable.s6));
                myButton.setText("Press and hold to edit a class");

            }
            if(getArguments().getInt("position") == 6){
                myImage.setImageDrawable(getResources().getDrawable(R.drawable.s7));
                myButton.setText("Press and hold to edit a grade");


            }
            if(getArguments().getInt("position") == 7){
                myImage.setImageDrawable(getResources().getDrawable(R.drawable.s5));
                myButton.setText("Welcome to Gradr");
                myButton.setEnabled(true);
                myButton.setBackground(getResources().getDrawable(R.drawable.outline));
                myButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(),MainActivity.class));
                    }
                });

            }

            if(getArguments().getInt("position") == 0){
                myImage.setImageDrawable(getResources().getDrawable(R.drawable.s5));
                myButton.setText("Welcome to Gradr");
            }
            return rootView;
        }
    }
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            ScreenSlidePageFragment myfrag = ScreenSlidePageFragment.newInstance(position);
            return myfrag;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}
