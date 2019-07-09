package and.bfop.kftc.com.useorgsampleapprenewal.layout.authnewweb;

import android.bfop.kftc.com.useorgsampleapprenewal.R;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
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
import java.util.LinkedHashMap;
import java.util.Map;

import and.bfop.kftc.com.useorgsampleapprenewal.eventbus.ActionBarChangeEvent;
import and.bfop.kftc.com.useorgsampleapprenewal.eventbus.FragmentInitEvent;
import and.bfop.kftc.com.useorgsampleapprenewal.layout.MainFragment;
import and.bfop.kftc.com.useorgsampleapprenewal.layout.Mainlogin;
import and.bfop.kftc.com.useorgsampleapprenewal.layout.common.BaseFragment;
import and.bfop.kftc.com.useorgsampleapprenewal.layout.common.BaseWebAuthInterface;
import and.bfop.kftc.com.useorgsampleapprenewal.layout.common.BaseWebFragment;
import and.bfop.kftc.com.useorgsampleapprenewal.restclient.RetrofitCustomAdapter;
import and.bfop.kftc.com.useorgsampleapprenewal.util.Constants;
import and.bfop.kftc.com.useorgsampleapprenewal.util.FragmentUtil;
import and.bfop.kftc.com.useorgsampleapprenewal.util.StringUtil;
import and.bfop.kftc.com.useorgsampleapprenewal.util.WebViewUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 사용자인증 개선버전에서 공통적으로 사용하는 WebView Fragment
 */
public class AuthNewWebCommonWebViewFragment extends BaseWebFragment implements BaseWebAuthInterface {

    private WebView webView;
    private static String authcode;
    private static String scope;
    private static String invokerType;
    private static String access_token;
    private static String user_seq_no;
    private Context context;
    //===================================== Fragment Lifecycle Callbacks - start =====================================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_webview_common, container, false);
        super.initBaseFragment(view); // BaseFragment 초기화 수행

        // Fragment 초기화 이벤트를 EventBus를 통해서 post (액션바 햄버거메뉴와 뒤로가기 화살표버튼을 상호 교체하기 위해서 수행)
        EventBus.getDefault().post(new FragmentInitEvent(this.getClass(), true));

        Bundle args = this.getArguments();

        // 액션바 타이틀 교체
        EventBus.getDefault().post(new ActionBarChangeEvent(args.getString(Constants.ACTIONBAR_TITLE)));

        // Bundle 파라미터로 받은 url 풀셋
        String urlToLoad = StringUtil.defaultString(args.getString("urlToLoad"));

        // Bundle 파라미터로 받은 header Map
        Map<String, String> headerMap = (Map<String, String>)args.getSerializable("headerMap");
        if(headerMap != null){
            // 사용자이름 필드를 url encoding 한다 (G/W에서 디코딩 해 주는 설정 있음)
            headerMap.put("Kftc-Bfop-UserName", StringUtil.urlEncode(headerMap.get("Kftc-Bfop-UserName")));
        }

        // WebView로 url 호출
        WebViewUtil.loadUrlOnWebView(view, this, urlToLoad, headerMap);

        context = container.getContext();

        return view;
    }

    @Override
    public void onResume() {

//        MessageUtil.showToast(BeanUtil.getClassName(this)+".onResume() called!");
        super.onResume();

        // WebViewFragment 를 참조한 코드
        if(webView != null){ webView.onResume(); }
    }

    @Override
    public void onPause() {

//        MessageUtil.showToast(BeanUtil.getClassName(this)+".onPause() called!");
        super.onPause();

        // WebViewFragment 를 참조한 코드
        if(webView != null){ webView.onPause(); }
    }

    @Override
    public void onDestroy() {

//        MessageUtil.showToast(BeanUtil.getClassName(this)+".onDestroy() called!");
        super.onDestroy();

        // WebViewFragment 를 참조한 코드
        if(webView != null){
            webView.destroy();
            webView = null;
        }
    }
    //===================================== Fragment Lifecycle Callbacks - end =======================================

    @Override
    public void onAuthCodeResponse(Map<String, Object> pMap) {

        // TokenRequestFragment 페이지에서의 뒤로가기 분기를 위해서 추가
        pMap.put("invokerType", "WEB_AUTH_NEW");

        authcode = StringUtil.defaultString(pMap.get("authcode"));
        scope = StringUtil.urlDecode(StringUtil.defaultString(pMap.get("scope"))); // 다중스코프에서 space가 +기호가 되지 않도록 URLDecode 처리
        invokerType = StringUtil.defaultString(pMap.get("invokerType"));

//        Log.d("피맵", " "+authcode+" : "+scope+" : "+ invokerType);

        // token 발급 요청 Fragment로 이동한다.
//        BaseFragment tokenRequestFragment = FragmentUtil.newFragment(TokenRequestFragment.class);
//        BeanUtil.putAllMapToBundle(tokenRequestFragment.getArguments(), pMap); // webview resposne 에서 추출한 authcode 등을 TokenRequestFragment 에 넣어준다.
//        FragmentUtil.replaceFragment(tokenRequestFragment);

        String redirectUriKey = (StringUtil.isNotBlank(invokerType) && "APP".equals(invokerType)) ? "APP_CALLBACK_URL" : "WEB_CALLBACK_URL";

        Map params = new LinkedHashMap<>();
        params.put("code", authcode);
        params.put("client_id", StringUtil.getPropStringForEnv("APP_KEY"));
        params.put("client_secret", StringUtil.getPropStringForEnv("APP_SECRET"));
        params.put("redirect_uri", StringUtil.getPropStringForEnv(redirectUriKey));
        params.put("grant_type", "authorization_code");

        Call<Map> call = RetrofitCustomAdapter.getInstance().token(params);

        call.enqueue(new Callback<Map>() {
            @Override
            public void onResponse(Call<Map> call, Response<Map> response) {
                Log.d("피맵",""+response.body());
                access_token=response.body().get("access_token").toString();
                user_seq_no=response.body().get("user_seq_no").toString();
                Log.d("피맵",""+access_token+" "+user_seq_no);
                Log.d("피맵","시작");
                new getToken().execute("http://192.168.70.29:3000/tokenInsert");//AsyncTask 시작시킴
            }

            @Override
            public void onFailure(Call<Map> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }

    public class getToken extends AsyncTask<String, String, String> {
        private URL url;

        @Override
        protected String doInBackground(String... urls) {
            try {
                Log.d("피맵","들어옴");
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("user_id", Mainlogin.userId);
                jsonObject.accumulate("access_token", access_token);
                jsonObject.accumulate("user_seq_no", user_seq_no);
                Log.d("피맵","JSON입력끝");
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

                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌

                    Log.d("피맵","서버로 보냄");

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();

                    String line = "";

                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    return "성공";
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
                Toast.makeText(context, "사용자 계좌 등록 성공!", Toast.LENGTH_SHORT).show();

                BaseFragment mainFragment = FragmentUtil.newFragment(MainFragment.class);
                FragmentUtil.replaceFragment(mainFragment);
            }
            else if(result.equals("실패")){
                Toast.makeText(context, "사용자 계좌 등록 실패!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    /**
     * 뒤로가기 버튼을 눌렀을 때의 동작
     */
    @Override
    public void onBackPressedForFragment() {

        FragmentUtil.replaceNewFragment(AuthNewWebMenuFragment.class);
    }
}
