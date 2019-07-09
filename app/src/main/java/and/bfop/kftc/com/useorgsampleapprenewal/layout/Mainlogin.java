package and.bfop.kftc.com.useorgsampleapprenewal.layout;

import android.Manifest;
import android.bfop.kftc.com.useorgsampleapprenewal.R;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONObject;

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
import java.util.List;

import and.bfop.kftc.com.useorgsampleapprenewal.layout.userInfo.SignupActivity;

public class Mainlogin extends AppCompatActivity {
    private static EditText Id, Password;
    public static String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainlogin);

        Id = (EditText) findViewById(R.id.main_userId);
        Password = (EditText) findViewById(R.id.main_userPassword);

        Button btnlogin = (Button) findViewById(R.id.btn_login);
        Button signupbtn = (Button) findViewById(R.id.signup);
        Button btn_find = (Button) findViewById(R.id.pass_find);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userId = Id.getText().toString();
                String userPassword = Password.getText().toString();
                new MainLogin().execute("http://192.168.70.29:3000/userLogin", userId, userPassword);
            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mainlogin.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mainlogin.this, password_find.class);
                startActivity(intent);
            }
        });

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(Mainlogin.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(Mainlogin.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }

        };
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.READ_SMS, Manifest.permission.READ_CALL_LOG)
                .check();
    }

    public class MainLogin extends AsyncTask<String, String, String> {
        private URL url;

        @Override
        protected String doInBackground(String... params) {
            try {
                JSONObject jsonObject = new JSONObject();
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.

                jsonObject.accumulate("userId", params[1]);
                jsonObject.accumulate("userPassword", params[2]);

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {
                    url = new URL(params[0]);

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

                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));


                    StringBuffer buffer = new StringBuffer();

                    String line = "";

                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                    if (buffer.toString().equals("성공")) {
                        return buffer.toString();
                    }

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
            return "실패";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("성공")) {
                Toast.makeText(Mainlogin.this, "로그인되었습니다.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Mainlogin.this, MainActivity.class);
                startActivity(intent);
            }
            else if(result.equals("실패")){
                Toast.makeText(Mainlogin.this, "로그인 실패하셨습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}