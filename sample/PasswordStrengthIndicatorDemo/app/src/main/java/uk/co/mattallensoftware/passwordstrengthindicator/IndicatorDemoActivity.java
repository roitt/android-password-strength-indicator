package uk.co.mattallensoftware.passwordstrengthindicator;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;


public class IndicatorDemoActivity extends Activity implements RadioButton.OnCheckedChangeListener {

    private EditText mPasswordField;
    private PasswordStrength mPasswordStrength;

    private RadioButton mRadioWeak, mRadioMedium, mRadioStrong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicator_demo);

        mPasswordField = (EditText) findViewById(R.id.password_field);
        mPasswordStrength = (PasswordStrength) findViewById(R.id.strength);

        mRadioWeak = (RadioButton) findViewById(R.id.radio_weak);
        mRadioWeak.setOnCheckedChangeListener(this);
        mRadioMedium = (RadioButton) findViewById(R.id.radio_medium);
        mRadioMedium.setOnCheckedChangeListener(this);
        mRadioStrong = (RadioButton) findViewById(R.id.radio_strong);
        mRadioStrong.setOnCheckedChangeListener(this);

        // Check the right radio button for the current strength
        int strength = mPasswordStrength.getStrengthRequirement();
        switch (strength) {
            case PasswordStrength.STRENGTH_WEAK:
                mRadioWeak.setChecked(true);
                break;

            case PasswordStrength.STRENGTH_MEDIUM:
                mRadioMedium.setChecked(true);
                break;

            case PasswordStrength.STRENGTH_STRONG:
                mRadioStrong.setChecked(true);
                break;

            default:
                break;
        }

        // Listen for text change events
        mPasswordField.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPasswordStrength.setPassword(mPasswordField.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.radio_weak:
                    mPasswordStrength.setStrengthRequirement(PasswordStrength.STRENGTH_WEAK);
                    break;

                case R.id.radio_medium:
                    mPasswordStrength.setStrengthRequirement(PasswordStrength.STRENGTH_MEDIUM);
                    break;

                case R.id.radio_strong:
                    mPasswordStrength.setStrengthRequirement(PasswordStrength.STRENGTH_STRONG);
                    break;

                default:
                    break;
            }
        }
    }
}
