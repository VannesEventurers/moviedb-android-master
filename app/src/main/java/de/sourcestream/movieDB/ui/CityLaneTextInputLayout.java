package de.sourcestream.movieDB.ui;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;

public class CityLaneTextInputLayout extends TextInputLayout {

    public CityLaneTextInputLayout(Context context) {
        super(context);
    }

    public CityLaneTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setError(CharSequence error) {
        super.setError(error);
        requestFocus();
    }
}
