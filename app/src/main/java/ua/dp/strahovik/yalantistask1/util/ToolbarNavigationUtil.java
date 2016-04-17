package ua.dp.strahovik.yalantistask1.util;


import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;

public class ToolbarNavigationUtil {

    public static View getToolbarNavigationIcon (Toolbar toolbar){
        //check if contentDescription previously was set
        boolean hadContentDescription = TextUtils.isEmpty(toolbar.getNavigationContentDescription());
        String contentDescription = !hadContentDescription ? toolbar.getNavigationContentDescription()
                .toString() : "navigationIcon" ;
        toolbar.setNavigationContentDescription(contentDescription);
        ArrayList<View> potentialViews = new ArrayList< View >();
        //find the view based on it's content description, set programatically or with android:contentDescription
        toolbar.findViewsWithText(potentialViews, contentDescription, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        //Nav icon is always instantiated at this point because calling setNavigationContentDescription ensures its existence
        View navIcon = null ;
        if (potentialViews.size() > 0 ){
            navIcon = potentialViews.get( 0 ); //navigation icon is ImageButton
        }
        //Clear content description if not previously present
        if (hadContentDescription)
            toolbar.setNavigationContentDescription(null);
        return navIcon;
    }
}
