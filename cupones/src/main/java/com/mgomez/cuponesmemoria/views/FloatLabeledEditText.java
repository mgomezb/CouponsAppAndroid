package com.mgomez.cuponesmemoria.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mgomez.cuponesmemoria.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;


public class FloatLabeledEditText extends LinearLayout {

    private String hint;

    private TextView hintTextView;
    private EditText editText;

    public FloatLabeledEditText(Context context) {
        super(context);
        initialize();
    }

    public FloatLabeledEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttributes(attrs);
        initialize();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public FloatLabeledEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setAttributes(attrs);
        initialize();
    }

    private void setAttributes(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FloatLabeledEditText);
        try {
            hint = a.getString(R.styleable.FloatLabeledEditText_floatingHint);
        } finally {
            a.recycle();
        }
    }

    private void initialize() {
        setOrientation(VERTICAL);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.float_labeled_edit_text, this);

        hintTextView = (TextView) view.findViewById(R.id.FloatLabeledEditTextHint);
        editText = (EditText) view.findViewById(R.id.FloatLabeledEditTextEditText);

        if (hint != null) {
            setHint(hint);
        }

        hintTextView.setVisibility(View.INVISIBLE);
        editText.addTextChangedListener(onTextChanged);
        editText.setOnFocusChangeListener(onFocusChanged);
    }

    private TextWatcher onTextChanged = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            setShowHint(editable.length() != 0);
        }
    };

    private OnFocusChangeListener onFocusChanged = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean gotFocus) {
            if (gotFocus) {
                ObjectAnimator.ofFloat(hintTextView, "alpha", 0.33f, 1f).start();
            } else {
                ObjectAnimator.ofFloat(hintTextView, "alpha", 1f, 0.33f).start();
            }
        }
    };

    private void setShowHint(final boolean show) {
        AnimatorSet animation = null;
        if ((hintTextView.getVisibility() == VISIBLE) && !show) {
            animation = new AnimatorSet();
            ObjectAnimator move = ObjectAnimator.ofFloat(hintTextView, "translationY", 0, hintTextView.getHeight() / 8);
            ObjectAnimator fade = ObjectAnimator.ofFloat(hintTextView, "alpha", 1, 0);
            animation.playTogether(move, fade);
        } else if ((hintTextView.getVisibility() != VISIBLE) && show) {
            animation = new AnimatorSet();
            ObjectAnimator move = ObjectAnimator.ofFloat(hintTextView, "translationY", hintTextView.getHeight() / 8, 0);
            ObjectAnimator fade = ObjectAnimator.ofFloat(hintTextView, "alpha", 0, 1);
            animation.playTogether(move, fade);
        }

        if (animation != null) {
            animation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    hintTextView.setVisibility(VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    hintTextView.setVisibility(show ? VISIBLE : INVISIBLE);
                }
            });
            animation.start();
        }
    }

    private void setHint(String hint) {
        this.hint = hint;
        hintTextView.setTextColor(getResources().getColor(R.color.tutorial_message));
        editText.setTextColor(getResources().getColor(R.color.tutorial_title));
        editText.setHintTextColor(getResources().getColor(R.color.background_coupon_image));
        editText.setHint(hint);
        hintTextView.setText(hint);
    }

    public void setColor(int colorText, int colorHint){
        editText.setTextColor(colorText);
        hintTextView.setTextColor(colorHint);
    }

    public Editable getText() {
        return editText.getText();
    }

    public void setText(CharSequence text) {
        editText.setText(text);
    }

    public void setInputType(TransformationMethod method){
        editText.setTransformationMethod(method);
    }

    public void setInputType(int inputType){
        editText.setInputType(inputType);
    }

    public void setInputTypePassword(){
        editText.setEllipsize(TextUtils.TruncateAt.END);
        editText.setSingleLine();
        editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    public void setSingleLine(boolean singleLine){
        editText.setSingleLine(singleLine);
    }

    public void setEditableFalse(){
        editText.setKeyListener(null);
    }

    public void setOnClick(OnFocusChangeListener focusChangeListener){
        this.editText.setOnFocusChangeListener(focusChangeListener);
    }

    public void setError(String error){
        editText.setError(error);
    }
}
