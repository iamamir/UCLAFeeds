package com.netsol.utiliy;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 * Created by kamranw on 6/3/2015.
 */
public class NDSpinner extends Spinner {

    OnItemSelectedListener listener;
    public NDSpinner(Context context)
    { super(context); }

    public NDSpinner(Context context, AttributeSet attrs)
    { super(context, attrs); }

    public NDSpinner(Context context, AttributeSet attrs, int defStyle)
    { super(context, attrs, defStyle); }


//    @Override
//    public void setSelection(int position) {
//
//        boolean sameSelected = position == getSelectedItemPosition();
//        super.setSelection(position);
//        if (sameSelected) {
//            // Spinner does not call the OnItemSelectedListener if the same item is selected, so do it manually now
//            getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
//        }
//        if (listener != null)
//            listener.onItemSelected(null, null, position, 0);
//    }
    @Override public void  setSelection(int position)
    {

        super.setSelection(position);
        if (position == 0) {
            // Spinner does not call the OnItemSelectedListener if the same item is selected, so do it manually now
            getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
        }
    }

    public void setOnItemSelectedEvenIfUnchangedListener(
            OnItemSelectedListener listener) {
        this.listener = listener;
    }

}
