package android.nexd.com.geocaching.ui.activity;

import android.content.Intent;
import android.nexd.com.geocaching.R;
import android.nexd.com.geocaching.persenter.InputTypePresenter;
import android.nexd.com.geocaching.ui.iView.IInputTypeView;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by wangxu on 16/6/15.
 */
public class InputTypeActivity extends BaseActivity<InputTypePresenter> implements IInputTypeView, View.OnClickListener {

    EditText userName;

    EditText telNumber;

    Button submit;

    @Override
    protected int getLayoutResId() {
        return R.layout.input_type;
    }

    @Override
    protected void initPresenter() {
        presenter = new InputTypePresenter(this, this);
        presenter.init();
    }

    @Override
    public void initView() {
        userName = (EditText) findViewById(R.id.user_name);
        telNumber = (EditText) findViewById(R.id.tel_number);
        submit = (Button) findViewById(R.id.submit);
        if (submit != null) {
            submit.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                if (checkContent(userName.getText().toString().trim(), telNumber.getText().toString().trim())) {
                    Intent resultIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("userName", userName.getText().toString().trim());
                    bundle.putString("telNumber", telNumber.getText().toString().trim());
                    resultIntent.putExtras(bundle);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else showToast("输入不正确");
                break;
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private boolean checkContent(String userName, String telNumber) {
        return !TextUtils.isEmpty(userName) && !TextUtils.isEmpty(telNumber);
    }
}
