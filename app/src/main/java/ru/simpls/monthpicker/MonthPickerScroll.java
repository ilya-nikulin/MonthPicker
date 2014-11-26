package ru.simpls.monthpicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by nikulin on 18.08.2014.
 */
public class MonthPickerScroll extends HorizontalScrollView {

    private Runnable scrollerTask;
    private Runnable initialScrollTask;
    private int initialPosition=0;
    private int newCheck = 100;
    /////////////////////////////
    public MonthPickerScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
        scrollerTask = new Runnable() {
            public void run() {
                int newPosition = getScrollY();
                if(initialPosition - newPosition == 0){//has stopped
                    setPosition();
                }else{
                    initialPosition = getScrollY();
                    MonthPickerScroll.this.postDelayed(scrollerTask, newCheck);
                }
            }
        };
    }
    ////////////////////////////////////////
    public void setInitialPosition(int pos){
        final LinearLayout linearLayout = ((LinearLayout) findViewById(R.id.monthPickerLayout));
        int childrenNum = linearLayout.getChildCount();
        for (int i = 0; i < childrenNum; i++) {
            if (i+1==pos) {
                final int month=i;
                initialScrollTask = new Runnable() {
                    public void run() {
                        View v2 = linearLayout.getChildAt(month);
                        int viewLeft = v2.getLeft();
                        int viewWidth = v2.getWidth();
                        scrollBy((viewLeft + viewWidth), 0);
                    }
                };
                postDelayed(initialScrollTask, 0);
                break;
            }
        }
    }
    ////////////////////////////////////////////
    public void setPosition(){
        int center = getScrollX() + getWidth() / 2;
        LinearLayout linearLayout = ((LinearLayout) findViewById(R.id.monthPickerLayout));
        int childrenNum = linearLayout.getChildCount();
        for (int i = 0; i < childrenNum; i++) {
            View v2 = linearLayout.getChildAt(i);
            int viewLeft = v2.getLeft();
            int viewWidth = v2.getWidth();
            if (center >= viewLeft && center <= viewLeft + viewWidth) {
                int selectedMonth = i;
                smoothScrollBy((viewLeft + viewWidth / 2) - center, 0);
                break;
            }
        }
    }
    ////////////////////////////////////////
    public void monthPicked(){
        initialPosition = getScrollY();
        postDelayed(scrollerTask, newCheck);
    }
    //////////////////////////////////////////
    public String getMonthName(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }
    //////////////////////////////////////////
    public void setMonthNames(){
        LinearLayout linearLayout = ((LinearLayout) findViewById(R.id.monthPickerLayout));
        int childrenNum = linearLayout.getChildCount()-1;
        for (int i = 1; i < childrenNum; i++) {
            TextView v2 = (TextView)linearLayout.getChildAt(i);
            v2.setText(getMonthName(i-1));
        }
    }
}