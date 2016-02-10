package ameba.com.wherezat.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ameba.com.wherezat.R;
import ameba.com.wherezat.global_classes.Utill_G_S;
import ameba.com.wherezat.services.User_Verification_Service;


public
class CodeVerification extends Activity implements View.OnClickListener
{

    EditText txtCode1, txtCode2, txtCode3, txtCode4;

    Button btnContinue;
    Utill_G_S utill_g_s;
    SharedPreferences sharedpreferencesObj;
    Context con;
    String ApplicationID,DeviceSerialNo;

    @Override
    protected
    void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_verification);

        con=this;

        ApplicationID= getIntent().getStringExtra("ApplicationID");
        DeviceSerialNo= getIntent().getStringExtra("DeviceSerialNo");

        sharedpreferencesObj = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        utill_g_s = new Utill_G_S();
        utill_g_s.ChangeStatusColor(CodeVerification.this, R.color.greybackground);

        txtCode1 = (EditText) findViewById(R.id.txtCode1);
        txtCode2 = (EditText) findViewById(R.id.txtCode2);
        txtCode3 = (EditText) findViewById(R.id.txtCode3);
        txtCode4 = (EditText) findViewById(R.id.txtCode4);

        (btnContinue = (Button) findViewById(R.id.btnContinue)).setOnClickListener(this);

        txtCode1.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                Integer textlength1 = txtCode1.getText().length();

                if (textlength1 >= 1)
                {
                    txtCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }

        });

        txtCode2.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                Integer textlength1 = txtCode2.getText().length();

                if (textlength1 >= 1)
                {
                    txtCode3.requestFocus();
                }
                else
                {
                    txtCode1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        txtCode3.addTextChangedListener(new TextWatcher()
        {
            @Override
            public
            void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public
            void onTextChanged(CharSequence s, int start, int before, int count)
            {
                Integer textlength1 = txtCode3.getText().length();

                if (textlength1 >= 1)
                {
                    txtCode4.requestFocus();
                }
                else
                {
                    txtCode2.requestFocus();
                }
            }

            @Override
            public
            void afterTextChanged(Editable s)
            {

            }
        });



        txtCode4.addTextChangedListener(new TextWatcher()
        {
            @Override
            public
            void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public
            void onTextChanged(CharSequence s, int start, int before, int count)
            {
                Integer textlength1 = txtCode4.getText().length();

                if (textlength1 < 1)
                {
                    txtCode3.requestFocus();
                }
            }

            @Override
            public
            void afterTextChanged(Editable s)
            {

            }
        });


        txtCode4.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                {
                    btnContinue.performClick();
                    return true;
                }
                return false;
            }
        });




    }

    @Override
    public
    void onBackPressed()
    {

    }


    private
    boolean get_Check()
    {
        if (txtCode1.getText().toString().trim().isEmpty())
        {

            return false;
        }
        else if (txtCode2.getText().toString().trim().isEmpty())
        {

            return false;
        }
        else if (txtCode3.getText().toString().trim().isEmpty())
        {
            return false;
        }
        else if (txtCode4.getText().toString().trim().isEmpty())
        {

            return false;
        }
        else
        {
            return true;
        }


    }

    @Override
    public
    void onClick(View v)
    {

        switch (v.getId())
        {
            case R.id.btnContinue:

                if (get_Check())
                {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    String code = txtCode1.getText().toString() + txtCode2.getText().toString() + txtCode3.getText().toString() + txtCode4.getText().toString();

                    new User_Verification_Service(con, sharedpreferencesObj.getString("CustomerId", ""), code,ApplicationID,DeviceSerialNo).execute();
                }
                else
                {
                    Utill_G_S.showToast("Please enter full code.",con);
                }

                break;

            default:
                break;


        }
    }
}