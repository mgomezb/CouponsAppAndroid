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
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mgomez.cuponesmemoria.R;
import com.mgomez.cuponesmemoria.adapters.AutoCompleteAdapter;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;


/**
 * Created by mgomezacid on 27-05-14.
 */
public class FloatLabeledAutoComplete extends LinearLayout {

    private String hint;

    private TextView hintTextView;
    private AutoCompleteTextView editTextAutoComplete;

    public FloatLabeledAutoComplete(Context context) {
        super(context);
        initialize();
    }

    public FloatLabeledAutoComplete(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttributes(attrs);
        initialize();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public FloatLabeledAutoComplete(Context context, AttributeSet attrs, int defStyle) {
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

        View view = LayoutInflater.from(getContext()).inflate(R.layout.float_labeled_autocomplete, this);

        hintTextView = (TextView) view.findViewById(R.id.FloatLabeledAutoCompleteHint);
        editTextAutoComplete = (AutoCompleteTextView) view.findViewById(R.id.FloatLabeledAutoCompleteAutoComplete);

        if (hint != null) {
            setHint(hint);
        }

        hintTextView.setVisibility(View.INVISIBLE);
        editTextAutoComplete.addTextChangedListener(onTextChanged);
        editTextAutoComplete.setOnFocusChangeListener(onFocusChanged);
    }

    private void setTextSizeEditText(int textSize) {
        editTextAutoComplete.setTextSize(textSize);
    }

    private void setTextSizeHint(int hintTextSize) {
        hintTextView.setTextSize(hintTextSize);
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

    private View.OnFocusChangeListener onFocusChanged = new View.OnFocusChangeListener() {
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
        editTextAutoComplete.setTextColor(getResources().getColor(R.color.tutorial_title));
        editTextAutoComplete.setHintTextColor(getResources().getColor(R.color.background_coupon_image));
        editTextAutoComplete.setHint(hint);
        hintTextView.setText(hint);
    }

    public void setColor(int colorText, int colorHint){
        editTextAutoComplete.setTextColor(colorText);
        hintTextView.setTextColor(colorHint);
    }

    public Editable getText() {
        return editTextAutoComplete.getText();
    }

    public void setText(CharSequence text) {
        editTextAutoComplete.setText(text);
    }

    public void setInputType(TransformationMethod method){
        editTextAutoComplete.setTransformationMethod(method);
    }

    public void setInputType(int inputType){
        editTextAutoComplete.setInputType(inputType);
    }

    public void setInputTypePassword(){
        editTextAutoComplete.setEllipsize(TextUtils.TruncateAt.END);
        editTextAutoComplete.setSingleLine();
        editTextAutoComplete.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    public void setSingleLine(boolean singleLine){
        editTextAutoComplete.setSingleLine(singleLine);
    }

    public void setEditableFalse(){
        editTextAutoComplete.setKeyListener(null);
    }

    public void setOnClick(View.OnFocusChangeListener focusChangeListener){
        this.editTextAutoComplete.setOnFocusChangeListener(focusChangeListener);
    }

    public AutoCompleteTextView autoCompleteTextView(){
        return editTextAutoComplete;
    }

    public void setError(String error){
        editTextAutoComplete.setError(error);
    }

    public void setAdapter(AutoCompleteAdapter adapter){
        editTextAutoComplete.setAdapter(adapter);
    }
}