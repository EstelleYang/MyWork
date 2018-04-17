package com.lzjs.uappoint.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lzjs.uappoint.R;
import com.lzjs.uappoint.bean.UserInfo;
import com.lzjs.uappoint.util.Contants;
import com.lzjs.uappoint.util.ExitApplication;
import com.lzjs.uappoint.util.SharedUtils;
import com.lzjs.uappoint.util.StringUtils;

public class LoginActivity extends Activity implements View.OnClickListener{

    private static final String TAG ="LoginActivity";
    private TextView tv_userRegi;
    private EditText loginphone;
    private EditText loginpwd;
    private TextView tv_findPwd;
    private TextView tv_login;
    private TextView center_title_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ExitApplication.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);
        initView();
    }

    private void initView() {
        center_title_text=(TextView)findViewById(R.id.center_title_text);
        center_title_text.setText(R.string.login_activity_tital);
        tv_userRegi= (TextView) findViewById(R.id.tv_userRegi);
        loginphone= (EditText) findViewById(R.id.loginphone);
        loginpwd= (EditText) findViewById(R.id.loginpwd);
        tv_findPwd= (TextView) findViewById(R.id.tv_findPwd);
        tv_login= (TextView) findViewById(R.id.tv_login);
        tv_findPwd.setOnClickListener(this);
        tv_userRegi.setOnClickListener(this);

    }

    /**
     * 登录
     */
    public void getUserLoginBtnClick(View view){
        String mobile=loginphone.getText().toString();
        String pwd=loginpwd.getText().toString();
        if (!StringUtils.isEmpty(mobile)&&!StringUtils.isEmpty(pwd)){
            RequestParams params = new RequestParams();
            params.addQueryStringParameter("loginid", mobile);
            params.addQueryStringParameter("userpwd", pwd);
            HttpUtils http = new HttpUtils();
            http.send(HttpRequest.HttpMethod.POST,
                    Contants.USER_LOGIN,
                    params,
                    new RequestCallBack<String>() {

                        @Override
                        public void onStart() {
                            tv_login.setText("登录中...");

                        }

                        @Override
                        public void onLoading(long total, long current, boolean isUploading) {

                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            try {
                                Log.i(TAG, "onSuccess: " + responseInfo.result);
                                JSONObject object = JSON.parseObject(responseInfo.result);
                                if (object.getString("result").equals("200")) {
                                    JSONObject response = object.getJSONObject("response");
                                    JSONObject jsondata = response.getJSONObject("data");
                                    String signature=jsondata.getString("signature");
                                    UserInfo user=JSON.parseObject(jsondata.getString("responseData"),UserInfo.class);
                                    if (user!=null && !TextUtils.isEmpty(signature)){
                                        SharedUtils.putLoginUserId(LoginActivity.this,signature);
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        LoginActivity.this.finish();
                                    }else {
                                        tv_login.setText("登录");
                                        Toast.makeText(LoginActivity.this, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
                                    }

                                }else{
                                    tv_login.setText("登录");
                                    Toast.makeText(LoginActivity.this, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
                                }

                            }catch (Exception e){
                                Toast.makeText(LoginActivity.this, "登录失败，系统错误！", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(HttpException error, String msg) {
                            Toast.makeText(LoginActivity.this, "网络错误，请检查网络！", Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            Toast.makeText(LoginActivity.this, "手机号码或密码不能为空", Toast.LENGTH_SHORT).show();

        }


    }

    /**
     * Called when a view has been clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /**
             * 注册
             */
            case R.id.tv_userRegi:
                startActivity(new Intent(getBaseContext(),RegisterActivity.class));
                break;
            /**
             * 找回密码
             */
            case R.id.tv_findPwd:
                startActivity(new Intent(getBaseContext(),FindPassWordActivity.class));
                break;
            case R.id.title1_back:
                this.finish();
                break;
        }


    }
}
