package com.yitingche.demo.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.yitingche.demo.R;

public class ClearableEditText extends EditText
{

    private Drawable mDrawable;

    private boolean mPressed;

    private boolean mShown;

    public ClearableEditText(Context context) {
        this(context, null, 0);
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        intiView();
    }

    private void intiView(){
        Drawable left = getResources().getDrawable(R.drawable.search_icon);
        left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());
        mDrawable = getResources().getDrawable(R.drawable.icon_clear);
        mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(), mDrawable.getMinimumHeight());
        this.setCompoundDrawables(left, null, mDrawable, null);

        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mShown = s.length() > 0;
                refreshDrawableState();
            }
        });
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (!mShown) {
            mergeDrawableStates(drawableState, EMPTY_STATE_SET);
        }
        return drawableState;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        if (mDrawable != null) {
            int[] myDrawableState = getDrawableState();

            // Set the state of the Drawable
            mDrawable.setState(myDrawableState);

            invalidate();
        }
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == mDrawable;
    }

    @Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (mDrawable != null) {
            mDrawable.jumpToCurrentState();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean buttonTouched = false;
        if (mShown) {
            if ((this.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL)) {
                if (event.getX() < mDrawable.getIntrinsicWidth() + getPaddingLeft()) {
                    buttonTouched = true;
                }
            } else {
                if (event.getX() > getWidth() - getPaddingRight() - mDrawable.getIntrinsicWidth()) {
                    buttonTouched = true;
                }
            }
        }
        if (buttonTouched) {
            onButtonTouchEvent(event);
            return true;
        }

        return super.dispatchTouchEvent(event);
    }

    private void onButtonTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (isEnabled() && mPressed) {
                    onClearButtonClick();
                }
                break;

            case MotionEvent.ACTION_DOWN:
                if (isEnabled() && mShown) {
                    mPressed = true;
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                if (mPressed) {
                    mPressed = false;
                }
        }
    }

    private void onClearButtonClick() {
        setText("");
    }
}