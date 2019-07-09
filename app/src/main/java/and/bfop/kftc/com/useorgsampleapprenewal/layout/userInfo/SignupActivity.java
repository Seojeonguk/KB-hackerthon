package and.bfop.kftc.com.useorgsampleapprenewal.layout.userInfo;

import android.bfop.kftc.com.useorgsampleapprenewal.R;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.net.URL;

import and.bfop.kftc.com.useorgsampleapprenewal.layout.SildingActivity;

import org.json.JSONObject;

import android.os.AsyncTask;

import android.bfop.kftc.com.useorgsampleapprenewal.R;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.Response;
import com.squareup.picasso.Downloader;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import and.bfop.kftc.com.useorgsampleapprenewal.App;
import and.bfop.kftc.com.useorgsampleapprenewal.eventbus.FragmentInitEvent;
import and.bfop.kftc.com.useorgsampleapprenewal.layout.common.BaseFragment;
import and.bfop.kftc.com.useorgsampleapprenewal.restclient.RetrofitCustomAdapter;
import and.bfop.kftc.com.useorgsampleapprenewal.restclient.RetrofitUtil;
import and.bfop.kftc.com.useorgsampleapprenewal.util.Constants;
import and.bfop.kftc.com.useorgsampleapprenewal.util.FragmentUtil;
import and.bfop.kftc.com.useorgsampleapprenewal.util.StringUtil;
import butterknife.OnClick;

public class SignupActivity extends AppCompatActivity {

    private EditText nameText;
    private EditText idText;
    private EditText passwordText;
    private EditText password_chk;
    private EditText phoneText;
    private String userName;
    private String userId;
    private String userPassword;
    private String userPassword_chk;
    private String userPhone;
    private Button userid_chk;
    private Button signupBtn;
    private AlertDialog dialog;
    private boolean validate = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        nameText = (EditText) findViewById(R.id.name);
        idText = (EditText) findViewById(R.id.user_id);
        passwordText = (EditText) findViewById(R.id.password);
        password_chk = (EditText) findViewById(R.id.pwd_chk);
        phoneText = (EditText) findViewById(R.id.mobileNO);
        userid_chk = (Button) findViewById(R.id.id_chk);
        signupBtn = (Button) findViewById(R.id.btnRegister);


        userid_chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = idText.getText().toString();
                if (validate) {
                    return;
                }
                if (userID.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                    dialog = builder.setMessage("아이디가 빈칸입니다..")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = nameText.getText().toString();
                userId = idText.getText().toString();
                userPassword = passwordText.getText().toString();
                userPassword_chk = password_chk.getText().toString();
                userPhone = phoneText.getText().toString();

                if (userName.equals("") || userId.equals("") || userPassword.equals("") || userPhone.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                    dialog = builder.setMessage("빈 칸 없이 입력해주세요.")
                            .setPositiveButton("확인", null)
                            .create();

                    dialog.show();
                    return;
                } else if(!userPassword.equals(userPassword_chk)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                    dialog = builder.setMessage("비밀번호가 서로 다릅니다.... 다시 확인해주세요")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
                else{
                    new JSONTask().execute("http://192.168.43.21:3000/user");
                }
            }
        });
    }

    public class JSONTask extends AsyncTask<String, String, String> {
        private URL url;

        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject jsonObject = new JSONObject();
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                userName = nameText.getText().toString();
                userId = idText.getText().toString();
                userPassword = passwordText.getText().toString();
                userPhone = phoneText.getText().toString();
                jsonObject.accumulate("userName", userName);
                jsonObject.accumulate("userId", userId);
                jsonObject.accumulate("userPassword", userPassword);
                jsonObject.accumulate("userPhone", userPhone);

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {
                    url = new URL(urls[0]);
                    //연결을 함
                    con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");//POST방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송

                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                    con.connect();

                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();

                    //버퍼를 생성하고 넣음0
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                    try {
                        if (reader != null) {
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
