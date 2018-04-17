package com.lzjs.uappoint.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.lzjs.uappoint.myview.CountDownTimerUtils;
import com.lzjs.uappoint.util.Contants;
import com.lzjs.uappoint.util.ExitApplication;
import com.lzjs.uappoint.util.StringUtils;

public class RegisterActivity extends Activity implements Handler.Callback{

    private static final String TAG ="RegisterActivity" ;
    private Button registered_confirm;
    private TextView verification_number_bt;
    private TextView center_title_text;
    private EditText et_phone_number;
    private EditText et_password_rg;
    private EditText idcardno;
    private EditText username;
    private EditText number_yzm;
    private Handler mHandler;
    private ImageView title1_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ExitApplication.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        registered_confirm= (Button) findViewById(R.id.registered_confirm);
        center_title_text=(TextView)findViewById(R.id.center_title_text);
        verification_number_bt= (TextView) findViewById(R.id.verification_number_bt);
        et_phone_number= (EditText) findViewById(R.id.et_phone_number);
        idcardno= (EditText) findViewById(R.id.idcardno);
        username= (EditText) findViewById(R.id.username);
        et_password_rg= (EditText) findViewById(R.id.et_password_rg);
        number_yzm= (EditText) findViewById(R.id.number_yzm);
        title1_back= (ImageView) findViewById(R.id.title1_back);
        mHandler=new Handler(this);

        center_title_text.setText(R.string.register);
        final CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(verification_number_bt, 60000, 1000);
        registered_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();//校验短信验证码
                Log.i(TAG, "onClick: flag"+flag);
            }
        });
        verification_number_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //verification_number_bt.setClickable(false);
                //verification_number_bt.setTextColor(0x88000000);
                mCountDownTimerUtils.start();
                IdentifyingCode();//下发短信验证码
            }
        });
        title1_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.finish();
            }
        });
    }

    /**
     * 注册
     */
    public void regist(){
        String mobile=et_phone_number.getText().toString();
        String pwd=et_password_rg.getText().toString();
        String vcode=number_yzm.getText().toString();
        String name=username.getText().toString();
        String idcard=idcardno.getText().toString();

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("mobile", mobile);
        params.addQueryStringParameter("username", name);
        params.addQueryStringParameter("idcardno", idcard);
        params.addQueryStringParameter("userpwd", pwd);

        //String imei = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId();
        String imei = "123dddd-ddfef-dd";
        params.addQueryStringParameter("imei",imei);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                Contants.REGISTER_CREATE_PASS,
                params,
                new RequestCallBack<String>() {

                    @Override
                    public void onStart() {
                        registered_confirm.setText("注册中...");
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        try {
                            Log.i(TAG, "onSuccess: " + responseInfo.result);
                            JSONObject object = JSON.parseObject(responseInfo.result);
                            JSONObject response = object.getJSONObject("response");
                            if (object.getString("result").equals("200")) {
                                if(response.get("data").equals("success")){
                                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getBaseContext(), LoginActivity.class));
                                }else
                                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();

                            }else{
                                JSONObject reerror = response.getJSONObject("response");
                                Toast.makeText(RegisterActivity.this, "注册失败,"+reerror.getString("errorText"), Toast.LENGTH_SHORT).show();
                            }

                        }catch (Exception e){
                            Toast.makeText(RegisterActivity.this, "注册失败,系统错误！", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(RegisterActivity.this, "系统异常，请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 获取短信验证码
     */
    public void IdentifyingCode(){
        String mobile=et_phone_number.getText().toString();
        if(StringUtils.isMobileNO(mobile)){
            RequestParams params = new RequestParams();
            params.addQueryStringParameter("mobile", mobile);
            HttpUtils http = new HttpUtils();
            http.send(HttpRequest.HttpMethod.POST,
                    Contants.GET_VALIDATE_CODE,
                    params,
                    new RequestCallBack<String>() {

                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onLoading(long total, long current, boolean isUploading) {

                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            Log.i(TAG, "onSuccess: " + responseInfo.result);
                            JSONObject object = JSON.parseObject(responseInfo.result);
                            if (object.getString("result").equals("200")) {
                                /*
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (int i = 60; i >= 0; i--) {
                                            Message message = Message.obtain();
                                            message.arg1 = i;
                                            message.what = 1;
                                            mHandler.sendMessage(message);
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    }
                                }).start();
*/
                                Toast.makeText(RegisterActivity.this,"验证码已下发至手机请注意查收！",Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(HttpException error, String msg) {
                            Toast.makeText(RegisterActivity.this,"网络错误，请检查网络！",Toast.LENGTH_SHORT).show();
                        }
                    });
        }else
            Toast.makeText(RegisterActivity.this, "请输入正确的手机号!", Toast.LENGTH_SHORT).show();
    }

    /**
     * 注册验证
     * @return
     */
    boolean flag=false;
    private void check(){

        String mobile=et_phone_number.getText().toString();
        String smscode=number_yzm.getText().toString();
        if (TextUtils.isEmpty(mobile) ||TextUtils.isEmpty(smscode)){

        }
        else{
            RequestParams params = new RequestParams();
            params.addQueryStringParameter("mobile", mobile);
            params.addQueryStringParameter("smscode", smscode);
            HttpUtils http = new HttpUtils();
            http.send(HttpRequest.HttpMethod.POST,
                    Contants.JUDGMENT_VALIDATE_CODE,
                    params,
                    new RequestCallBack<String>() {

                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onLoading(long total, long current, boolean isUploading) {

                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            Log.i(TAG, "onSuccess: " + responseInfo.result);
                            JSONObject object = JSON.parseObject(responseInfo.result);
                            if (object.getString("result").equals("200")) {
                                JSONObject response = object.getJSONObject("response");
                                //JSONObject jsondata = response.getJSONObject("data");

                                if(response.get("data").equals("success")){
                                    regist();
                                }else{
                                    Toast.makeText(RegisterActivity.this, "手机号或验证码错误！", Toast.LENGTH_SHORT).show();
                                }

                            }

                        }

                        @Override
                        public void onFailure(HttpException error, String msg) {
                            Toast.makeText(RegisterActivity.this,"网络错误，请检查网络！",Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    @Override
    public boolean handleMessage(Message msg) {

        switch (msg.what) {
            case 1:
                if (msg.arg1 == 0) {
                    verification_number_bt.setText("重新获取");
                    verification_number_bt.setClickable(true);
                    verification_number_bt.setTextColor(0xff000000);
                } else {

                    verification_number_bt.setText(msg.arg1 + "秒后重新获取");
                }

                break;
        }
        return true;
    }
}
