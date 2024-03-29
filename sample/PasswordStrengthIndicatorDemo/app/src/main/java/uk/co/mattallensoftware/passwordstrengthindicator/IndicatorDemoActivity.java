package uk.co.mattallensoftware.passwordstrengthindicator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;


public class IndicatorDemoActivity extends Activity implements RadioButton.OnCheckedChangeListener {

    private EditText mPasswordField;
    private PasswordStrengthView mPasswordStrengthView;

    private static final String VIEW_TO_LOAD = "view";

    private RadioButton mRadioWeak, mRadioMedium, mRadioStrong;
    private CheckBox mCheckboxGuides;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getIntent().getIntExtra(VIEW_TO_LOAD, R.layout.activity_indicator_bar_demo));

        mPasswordField = (EditText) findViewById(R.id.password_field);
        mPasswordStrengthView = (PasswordStrengthView) findViewById(R.id.strength);

        mRadioWeak = (RadioButton) findViewById(R.id.radio_weak);
        mRadioWeak.setOnCheckedChangeListener(this);
        mRadioMedium = (RadioButton) findViewById(R.id.radio_medium);
        mRadioMedium.setOnCheckedChangeListener(this);
        mRadioStrong = (RadioButton) findViewById(R.id.radio_strong);
        mRadioStrong.setOnCheckedChangeListener(this);
        mCheckboxGuides = (CheckBox) findViewById(R.id.show_guides);
        mCheckboxGuides.setOnCheckedChangeListener(this);

        // Check the right radio button for the current strength
        int strength = mPasswordStrengthView.getStrengthRequirement();
        switch (strength) {
            case PasswordStrengthView.STRENGTH_WEAK:
                mRadioWeak.setChecked(true);
                break;

            case PasswordStrengthView.STRENGTH_MEDIUM:
                mRadioMedium.setChecked(true);
                break;

            case PasswordStrengthView.STRENGTH_STRONG:
                mRadioStrong.setChecked(true);
                break;

            default:
                break;
        }

        // And set the right state for the checkbox
        mCheckboxGuides.setChecked(mPasswordStrengthView.isShowingGuides());

        // Listen for text change events
        mPasswordField.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPasswordStrengthView.setPassword(mPasswordField.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.radio_weak:
                if (isChecked) mPasswordStrengthView.setStrengthRequirement(PasswordStrengthView.STRENGTH_WEAK);
                break;

            case R.id.radio_medium:
                if (isChecked) mPasswordStrengthView.setStrengthRequirement(PasswordStrengthView.STRENGTH_MEDIUM);
                break;

            case R.id.radio_strong:
                if (isChecked) mPasswordStrengthView.setStrengthRequirement(PasswordStrengthView.STRENGTH_STRONG);
                break;

            case R.id.show_guides:
                mPasswordStrengthView.setShowGuides(isChecked);
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.indicator_demo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if (item.getItemId() == R.id.action_bar_view) {
            Intent i = getIntent().putExtra(VIEW_TO_LOAD, R.layout.activity_indicator_bar_demo);
            finish();
            startActivity(i);
            return true;
        }
        else if (item.getItemId() == R.id.action_round_view) {
            Intent i = getIntent().putExtra(VIEW_TO_LOAD, R.layout.activity_indicator_round_demo);
            finish();
            startActivity(i);
            return true;
        }
        return false;
    }
}
