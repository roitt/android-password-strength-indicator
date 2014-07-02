package uk.co.mattallensoftware.passwordstrengthindicator;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


public class IndicatorDemoActivity extends Activity {

    private static final String TAG = IndicatorDemoActivity.class.getSimpleName();

    private EditText mPasswordField;
    private PasswordStrength mPasswordStrength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicator_demo);

        mPasswordField = (EditText) findViewById(R.id.password_field);
        mPasswordStrength = (PasswordStrength) findViewById(R.id.strength);

        // Listen for text change events
        mPasswordField.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    mPasswordStrength.setPassword(mPasswordField.getText().toString());
                } catch (NullPointerException e){
                    // Do nuttin'
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
