package com.rezofy.utils;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.freshdesk.hotline.Hotline;
import com.rezofy.R;

import java.util.Calendar;

/**
 * Created by anuj on 30/9/16.
 */
public class FloatingButtonMove {

    private Context mContext;
    float dx, dy;
    int lastAction;
    private static final int MAX_CLICK_DURATION = 200;
    public static long startClickTime;

    public FloatingButtonMove(Context context){
        mContext = context;

    }

    public int dragView(View v, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                dx = v.getX() - event.getRawX();
                dy = v.getY() - event.getRawY();
                lastAction = MotionEvent.ACTION_DOWN;
                startClickTime = Calendar.getInstance().getTimeInMillis();
                break;
            case MotionEvent.ACTION_MOVE:
//                v.setY(event.getRawY() + dy);
//                v.setX(event.getRawX() + dx);

                v.setY(event.getRawY() - v.getHeight() * 85 / 100);
                v.setX(event.getRawX() - v.getWidth() * 55 / 100 );

                lastAction = MotionEvent.ACTION_MOVE;
                break;
            case MotionEvent.ACTION_UP:
                if (lastAction == MotionEvent.ACTION_DOWN) {

                    long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
                    if(clickDuration < MAX_CLICK_DURATION) {
                        //click event has occurred
                       // Toast.makeText(mContext, "Clicked!", Toast.LENGTH_SHORT).show();
                        if(Boolean.parseBoolean(mContext.getString(R.string.isHotLineIntegrated))) {
                            Hotline.showConversations(mContext);
                        } else {
                            Toast.makeText(mContext, "Coming Soon!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;

            default:
                lastAction = -1;
                return lastAction;
        }
        return lastAction;
    }
}