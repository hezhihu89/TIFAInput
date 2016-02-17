package net.siysoft.tifa.tifainput;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mDefault, mMD5;
    private TIFAEditText myEditText1, myEditText2;
    private Button mGetButton1, mGetButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDefault = (TextView) findViewById(R.id.default_tv);
        mGetButton1 = (Button) findViewById(R.id.get_num);
        mGetButton2 = (Button) findViewById(R.id.get_num2);
        mMD5 = (TextView) findViewById(R.id.md5_tv);
        myEditText1 = (TIFAEditText) findViewById(R.id.input_ed);
        myEditText1.setHideString(true);
        myEditText2 = (TIFAEditText) findViewById(R.id.input_ed1);
        myEditText2.setInputType(TIFAKeyboradUtil.InputType.TYPE_KEY_NUM);

        mGetButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myEditText1.getText().toString().length() != 0) {
                    mDefault.setText(myEditText1.getDefaultText());
                    mMD5.setText(myEditText1.getMD5());
                }
            }
        });
        mGetButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myEditText2.getText().toString().length() != 0) {
                    mDefault.setText(myEditText2.getDefaultText());
                    mMD5.setText(myEditText2.getMD5());
                    Log.d("TAG", myEditText2.getDefaultText());
                }
            }
        });
    }
}