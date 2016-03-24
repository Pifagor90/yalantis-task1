/*
Copyright info
*/


package ua.dp.strahovik.yalantistask1.listeners;


import android.content.Intent; //[Comment] Please optimize import
import android.view.View;
import android.app.Activity;



public class ExitOnClickListener implements View.OnClickListener { //[Comment] OnClickListener should be internal

    private Activity mActivity; //[Comment] Bad idea. Use context instead of activity

    public ExitOnClickListener(Activity activity){
        this.mActivity = activity;
    } //[Comment] without this

    @Override
    public void onClick(View v) {
        mActivity.finish();
    }
}

