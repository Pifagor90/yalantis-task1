/*
Copyright info
*/


package ua.dp.strahovik.yalantistask1.listeners;


import android.content.Intent;
import android.view.View;
import android.app.Activity;



public class ExitOnClickListener implements View.OnClickListener {

    private Activity mActivity;

    public ExitOnClickListener(Activity activity){
        this.mActivity = activity;
    }

    @Override
    public void onClick(View v) {
        mActivity.finish();
    }
}

