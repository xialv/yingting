package com.yitingche.demo.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.yitingche.demo.R;

public class ClearEditText extends FrameLayout implements View.OnClickListener, TextWatcher, View.OnFocusChangeListener {

    private EditText mEditText;
    private View mClearButton;
    public ClearEditText(Context context) {
        super(context);
        initViews(context);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(context);
    }

    private void initViews(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(R.layout.clear_edit_text, null);
        mEditText = (EditText) root.findViewById(R.id.edit_text);
        mClearButton = root.findViewById(R.id.clear_btn);

        mEditText.addTextChangedListener(this);
        mEditText.setOnFocusChangeListener(this);
        mClearButton.setOnClickListener(this);

        LayoutParams lp = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER_VERTICAL;
        addView(root, lp);

        displayClearButton(false);
    }

    @Override
    public void onClick(View v) {
        if (mClearButton == v) {
            mEditText.getText().clear();
            displayClearButton(false);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        refreshClearButtonState();
    }

    @Override
    public void afterTextChanged(Editable s) {
        refreshClearButtonState();
        if(mListener != null){
            mListener.onTextChanged(s);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    private void refreshClearButtonState() {
        String text = mEditText.getText().toString();
        boolean hasFocus = mEditText.isFocused();
        if (text != null && !text.isEmpty()) {
            displayClearButton(true);
        } else {
            displayClearButton(false);
        }
    }

    public void displayClearButton(boolean show) {
        if (show) {
            if (mClearButton.getVisibility() != View.VISIBLE)
                mClearButton.setVisibility(View.VISIBLE);
        } else if (mClearButton.getVisibility() != View.INVISIBLE) {
            mClearButton.setVisibility(View.INVISIBLE);
        }
    }
    private TextChangeListener mListener = null;
    public void setTextChangeListener(TextChangeListener listener){
        mListener = listener;
    }
    public interface TextChangeListener{
        public void onTextChanged(Editable s);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
        mEditText.setOnClickListener(l);
    }
}