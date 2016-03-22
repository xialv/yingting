package com.yitingche.demo.activity;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.yitingche.demo.R;


/**
 * Created by scenic on 15/9/27.
 */
public class MyBaseActivity extends Activity{

    public boolean isUseCustomActionBar;
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(isUseCustomActionBar){
            final ActionBar actionBar = getActionBar();
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.actionbar);//自定义ActionBar布局
            actionBar.getCustomView().findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCustomBuckPress();
                }
            });
            setCustomActionBarTitle(getTitle() + "");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void setCustomActionBarTitle(String str){
       TextView textView = (TextView) getActionBar().getCustomView().findViewById(R.id.textview_actionbar_title);
        textView.setText(str);
    }

    protected void onCustomBuckPress() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.menu_main, menu);
        return false;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
////            startActivity(new Intent(this,Settings.class));
//            startActivity(new Intent(this,SettingsActivity.class));
//        }

        return false;
    }
}
