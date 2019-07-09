package and.bfop.kftc.com.useorgsampleapprenewal.layout;

import android.app.Activity;
import android.app.Fragment;
import android.app.Instrumentation;
import android.bfop.kftc.com.useorgsampleapprenewal.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.CallLog;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
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
import java.text.Collator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import and.bfop.kftc.com.useorgsampleapprenewal.SlidingRootNav;
import and.bfop.kftc.com.useorgsampleapprenewal.SlidingRootNavBuilder;
import and.bfop.kftc.com.useorgsampleapprenewal.fragment.CenteredTextFragment;
import and.bfop.kftc.com.useorgsampleapprenewal.menu.DrawerAdapter;
import and.bfop.kftc.com.useorgsampleapprenewal.menu.DrawerItem;
import and.bfop.kftc.com.useorgsampleapprenewal.menu.SimpleItem;
import and.bfop.kftc.com.useorgsampleapprenewal.menu.SpaceItem;
import and.bfop.kftc.com.useorgsampleapprenewal.restclient.RetrofitCustomAdapter;
import and.bfop.kftc.com.useorgsampleapprenewal.util.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static and.bfop.kftc.com.useorgsampleapprenewal.layout.AppPreferences.post_cnt;
import static and.bfop.kftc.com.useorgsampleapprenewal.layout.ReadMmsSms.checkgrade;
import static and.bfop.kftc.com.useorgsampleapprenewal.layout.ReadMmsSms.grade;
import static and.bfop.kftc.com.useorgsampleapprenewal.layout.ReadMmsSms.pay;
import static and.bfop.kftc.com.useorgsampleapprenewal.layout.ReadMmsSms.paycount;
import static and.bfop.kftc.com.useorgsampleapprenewal.layout.ReadMmsSms.payofcharge;

public class SildingActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener, AuthenticationListener {
    class Month {
        int in_money;
        int out_money;
        int deal;

        Month(int in, int out, int deal) {
            this.in_money = in;
            this.out_money = out;
            this.deal = deal;
        }
    }

    static int cnt = 0;
    static int video_cnt = 0;
    static int posting_cnt = 0;
    static int user_like = 0;
    static double following_mod_followed = 0;
    static String fintechusenum = null;
    static String user_token = null;
    static String full_name = null;

    static long like_mod_posting = 0;
    static long comments_mod_posting = 0;

    static TextView back_month;
    static TextView input_data;
    static TextView output_data;
    static TextView Monthago;
    static TextView twoMonthago;
    static TextView threeMonthago;
    static TextView Monthago_in;
    static TextView Monthago_out;
    static TextView twoMonthago_in;
    static TextView twoMonthago_out;
    static TextView threeMonthago_in;
    static TextView threeMonthago_out;
    static TextView Monthago_deal;
    static TextView twoMonthago_deal;
    static TextView threeMonthago_deal;
    static LineChart lineChart;

    static TextView credit_grade;
    static TextView credit_score;
    static TextView menu_score;

    static Handler mHandler;
    static SQLiteDatabase db;

    static SMSHelper smsHelper;
    final static String dbSMS = "sms.db";
    final static String tbl_sms = "smstable";
    final static int dbVer_sms = 1;

    static Context context;

    public static int telecom_score=0;
    public static int call_score=0;
    public static double balance_score = 0;
    public static double save_money = 0;
    public static int bank_score = 0;
    public static long sns_score = 0;
    public static int result_grade = 0;
    public static int result_score = 600;


    int receive, delivery, missed, draft, callcount = 0;
    long receive_time, delivery_time;
    long durationcount = 0;

    public static final String MESSAGE_TYPE_INBOX = "1";
    public static final String MESSAGE_TYPE_SENT = "2";
    public static final String MESSAGE_TYPE_CONVERSATIONS = "3";
    public static final String MESSAGE_TYPE_DRAFT = "5";
    final static private String[] CALL_PROJECTION = {CallLog.Calls.TYPE,
            CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER,
            CallLog.Calls.DATE, CallLog.Calls.DURATION};

    private static final String TAG = "Victor-Manage_Clique";
    private static final int POS_DASHBOARD = 0;
    private static final int POS_ACCOUNT = 1;
    private static final int POS_MESSAGES = 2;
    private static final int POS_CART = 3;
    private static final int POS_LOGOUT = 5;

    private String token = null;
    private AppPreferences appPreferences = null;
    private AuthenticationDialog authenticationDialog = null;
    private Button button = null;
    private TextView button2 = null;

    boolean mbs_check = false;
    private String[] screenTitles;
    private Drawable[] screenIcons;

    private SlidingRootNav slidingRootNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_silding);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;
        mHandler = new Handler(Looper.getMainLooper());
        smsHelper = new SMSHelper(this, dbSMS, null, dbVer_sms);
        db = smsHelper.getWritableDatabase();

        callLog();
        setCallScore(receive, delivery, missed, draft, durationcount);
        TextView call_total = (TextView) findViewById(R.id.call_total);
        call_total.setText("" + callcount);
        TextView incoming = (TextView) findViewById(R.id.incoming);
        incoming.setText("" + receive);
        TextView outgoing = (TextView) findViewById(R.id.outgoing);
        outgoing.setText("" + delivery);
        TextView out = (TextView) findViewById(R.id.out);
        out.setText("" + missed);
        TextView refusal = (TextView) findViewById(R.id.refusal);
        refusal.setText("" + draft);
        TextView incoming_time = (TextView) findViewById(R.id.incoming_time);
        incoming_time.setText(timeToString(receive_time));
        TextView outgoing_time = (TextView) findViewById(R.id.outgoing_time);
        outgoing_time.setText(timeToString(delivery_time));
        ImageView cell_image = (ImageView) findViewById(R.id.cell_image);
        cell_image.setBackground(new ShapeDrawable(new OvalShape()));
        cell_image.setClipToOutline(true);
        ImageView in_image = (ImageView) findViewById(R.id.in_image);
        in_image.setBackground(new ShapeDrawable(new OvalShape()));
        in_image.setClipToOutline(true);
        ImageView out_image = (ImageView) findViewById(R.id.out_image);
        out_image.setBackground(new ShapeDrawable(new OvalShape()));
        out_image.setClipToOutline(true);
        ImageView not_image = (ImageView) findViewById(R.id.not_image);
        not_image.setBackground(new ShapeDrawable(new OvalShape()));
        not_image.setClipToOutline(true);
        ImageView refusal_image = (ImageView) findViewById(R.id.refusal_image);
        refusal_image.setBackground(new ShapeDrawable(new OvalShape()));
        refusal_image.setClipToOutline(true);

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        final FoldingCell fc0 = (FoldingCell) findViewById(R.id.folding_cell0);
        final FoldingCell fc1 = (FoldingCell) findViewById(R.id.folding_cell1);
        final FoldingCell fc2 = (FoldingCell) findViewById(R.id.folding_cell2);
        final FoldingCell fc3 = (FoldingCell) findViewById(R.id.folding_cell3);
        final FoldingCell fc4 = (FoldingCell) findViewById(R.id.folding_cell4);

        TextView my_page_text = (TextView) findViewById(R.id.my_page_text);
        TextView sns_text = (TextView) findViewById(R.id.sns_text);
        TextView telecom_text = (TextView) findViewById(R.id.telecom_text);
        TextView bank_book_text = (TextView) findViewById(R.id.bank_book_text);
        TextView call_list_text = (TextView) findViewById(R.id.call_list_text);

        my_page_text.setPaintFlags(my_page_text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        sns_text.setPaintFlags(sns_text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        telecom_text.setPaintFlags(telecom_text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        bank_book_text.setPaintFlags(bank_book_text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        call_list_text.setPaintFlags(call_list_text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        //계좌내역
        input_data = (TextView) findViewById(R.id.input_text);
        output_data = (TextView) findViewById(R.id.output_text);
        back_month = (TextView) findViewById(R.id.back_month);
        Monthago = (TextView) findViewById(R.id.Monthago);
        twoMonthago = (TextView) findViewById(R.id.twoMonthago);
        threeMonthago = (TextView) findViewById(R.id.threeMonthago);
        Monthago_in = (TextView) findViewById(R.id.Monthago_in);
        Monthago_out = (TextView) findViewById(R.id.Monthago_out);
        twoMonthago_in = (TextView) findViewById(R.id.twoMonthago_in);
        twoMonthago_out = (TextView) findViewById(R.id.twoMonthago_out);
        threeMonthago_in = (TextView) findViewById(R.id.threeMonthago_in);
        threeMonthago_out = (TextView) findViewById(R.id.threeMonthago_out);
        Monthago_deal = (TextView) findViewById(R.id.Monthago_deal);
        twoMonthago_deal = (TextView) findViewById(R.id.twoMonthago_deal);
        threeMonthago_deal = (TextView) findViewById(R.id.threeMonthago_deal);
        credit_grade = (TextView) findViewById(R.id.credit_grade);
        credit_score = (TextView) findViewById(R.id.credit_score);
        menu_score = (TextView) findViewById(R.id.menu_score);

        final TextView pay_per = (TextView) findViewById(R.id.pay_per);
        final TextView pay_list = (TextView) findViewById(R.id.pay_list);
        final TextView pay_count = (TextView) findViewById(R.id.pay_count);
        final TextView positive_pay = (TextView) findViewById(R.id.positive_pay);
        final TextView positive_auto = (TextView) findViewById(R.id.positive_auto);
        final TextView positive_charge = (TextView) findViewById(R.id.positive_charge);

        fc0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (telecom_score == 0) {
                    setTelecomScore(grade, paycount);
                }
                result_score = 600;
                result_score += telecom_score + call_score + sns_score + bank_score;
                if (result_score > 999) result_score=1000;
                credit_score.setText(result_score + "");
                menu_score.setText(String.valueOf(result_score));
                switch (result_score/100) {
                    case 1:credit_grade.setText(9+"");break;
                    case 2:credit_grade.setText(8+"");break;
                    case 3:credit_grade.setText(7+"");break;
                    case 4:credit_grade.setText(6+"");break;
                    case 5:credit_grade.setText(5+"");break;
                    case 6:credit_grade.setText(4+"");break;
                    case 7:credit_grade.setText(3+"");break;
                    case 8:credit_grade.setText(2+"");break;
                    case 9:credit_grade.setText(1+"");
                }
                fc0.toggle(false);
            }
        });
        fc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc1.toggle(false);
            }
        });
        fc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkgrade) {
                    Toast toast = Toast.makeText(getApplicationContext(), "등급을 조회할 수 없습니다.", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    ImageView my_image_grade = (ImageView) findViewById(R.id.my_image_grade);
                    int image_id = getResources().getIdentifier(grade, "drawable", getPackageName());
                    my_image_grade.setImageResource(image_id);
                }
                String taxpay;
                if (pay) taxpay = "Perpect";
                else if (paycount == 2) taxpay = "Great";
                else if (paycount == 1) taxpay = "Good";
                else taxpay = "Bad";
                if (taxpay.equals("Bad")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "자동납부 이력이 없습니다.", Toast.LENGTH_LONG);
                    toast.show();
                    pay_list.setTextColor(Color.parseColor("#FF3333"));
                    pay_list.setText("납부내역을 조회할 수 없습니다.");
//                      positive_pay.setText(taxpay);
                } else {
                    pay_per.setTextColor(Color.parseColor("#FF3333"));
                    pay_per.setText("건");
                    pay_list.setText("의 미납내역이 조회되었습니다.");
                    pay_count.setText(String.valueOf(3 - paycount));
                    positive_pay.setText(taxpay);
                    int my_charge = Integer.parseInt(payofcharge);
                    String my_e_charge = String.format("%,d", my_charge);
                    my_charge *= paycount;
                    positive_auto.setText(my_e_charge + "원");
                    String my_t_charge = String.format("%,d", +my_charge);
                    positive_charge.setText(my_t_charge + "원");
                }
                if (telecom_score == 0)
                    setTelecomScore(grade, paycount);
                fc2.toggle(false);
            }
        });
        fc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JSONTask().execute("http://192.168.70.29:3000/getData");//AsyncTask 시작시킴
                fc3.toggle(false);
            }
        });
        fc4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc4.toggle(false);
            }
        });

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_ACCOUNT).setChecked(true),
                createItemFor(POS_DASHBOARD),
                createItemFor(POS_MESSAGES),
                createItemFor(POS_CART),
                new SpaceItem(48),
                createItemFor(POS_LOGOUT)));
        adapter.setListener(this);

        RecyclerView list = (RecyclerView) findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_DASHBOARD);

        button = (Button) findViewById(R.id.login_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gogogo(v);
            }
        });
        button2 = (TextView) findViewById(R.id.logout_button);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout_logout(v);
            }
        });

        appPreferences = new AppPreferences(this);

        //check already have access token
        token = appPreferences.getString(AppPreferences.TOKEN);
        if (token != null) {
            getUserInfoByAccessToken(token);
        }
        Point point = getScreenSize(this);
        LinearLayout sns_profile = (LinearLayout) findViewById(R.id.insta_profile);
        LinearLayout sns_login = (LinearLayout) findViewById(R.id.insta_login);
        int Status_height1 = getStatusBarHeight1();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(point.x, point.y - Status_height1 * 2);
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(point.x, point.y - Status_height1 * 2);
        sns_profile.setLayoutParams(layoutParams);
        sns_login.setLayoutParams(layoutParams1);
    }

    void setTelecomScore(String m_grade, int m_pay_count) {
        telecom_score = 0;
        if (m_grade.equals("vvip")) telecom_score += 70;
        else if (m_grade.equals("vip")) telecom_score += 60;
        else if (m_grade.equals("gold")) telecom_score += 50;
        else if (m_grade.equals("sliver")) telecom_score += 40;
        else if (m_grade.equals("white")) telecom_score += 30;
        else if (m_grade.equals("normal")) telecom_score += 20;
        switch (m_pay_count) {
            case 3:
                telecom_score += 30;
                break;
            case 2:
                telecom_score += 20;
                break;
            case 1: telecom_score+=10;break;
//            case 0: telecomscore-=10;break;
        }
        if (telecom_score>99) telecom_score = 100;
    }

    void setCallScore(int re, int de, int ms, int dr, long du) {
        call_score = 0;
        int RperD = (re + ms) / de;
        if (RperD / 1 < 1) call_score = 0;
        else if (RperD / 1 < 2) call_score += 20;
        else if (RperD / 1 < 3) call_score += 40;
        else if (RperD / 1 < 4) call_score += 60;
        else if (RperD / 1 < 5) call_score += 80;
        else call_score += 100;
        call_score -= ms / 100;
        call_score -= dr / 10;
        call_score+= (du/1800);
        if (call_score>99)call_score=100;
    }

    public int getStatusBarHeight1() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public Point getScreenSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }


    public void onItemSelected(int position) {
        if (position == POS_LOGOUT) {
            finish();
        }
        slidingRootNav.closeMenu();

        Fragment selectedScreen = CenteredTextFragment.createFor(screenTitles[position]);
        showFragment(selectedScreen);
    }

    private void showFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

    }


    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.textColorSecondary))
                .withTextTint(color(R.color.textColorPrimary))
                .withSelectedIconTint(color(R.color.darkblue))
                .withSelectedTextTint(color(R.color.darkblue));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    private final static Comparator myComparator = new Comparator() {
        private final Collator collator = Collator.getInstance();

        @Override
        public int compare(Object object1, Object object2) {
            return collator.compare(object1.toString(), object2.toString());
        }
    };

    private Cursor getCallHistoryCursor(Context context) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, CALL_PROJECTION,
                    null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return cursor;
    }

    long nowdate2 = System.currentTimeMillis();

    private void callLog() {
        Cursor curCallLog = getCallHistoryCursor(this);
        if (curCallLog.moveToFirst() && curCallLog.getCount() > 0) {
            while (curCallLog.isAfterLast() == false) {
                long date = curCallLog.getLong(curCallLog.getColumnIndex(CallLog.Calls.DATE));
                if (date < 10000000000L) date = date * 1000;
                if (nowdate2 - date > 7948800000L)
                    // 86400000   1day   // 604800000  1week
                    // 1209600000L 2week // 2592000000L 4week
                    // 12776000000L 3month
                    break;
                StringBuffer sb = new StringBuffer();
                long take = curCallLog.getInt(curCallLog.getColumnIndex(CallLog.Calls.DURATION));
                if (curCallLog.getString(curCallLog.getColumnIndex(CallLog.Calls.TYPE)).equals(MESSAGE_TYPE_INBOX)) {
                    receive_time += take;
                    receive++;
                } else if (curCallLog.getString(curCallLog.getColumnIndex(CallLog.Calls.TYPE)).equals(MESSAGE_TYPE_SENT)) {
                    delivery_time += take;
                    delivery++;
                } else if (curCallLog.getString(curCallLog.getColumnIndex(CallLog.Calls.TYPE)).equals(MESSAGE_TYPE_CONVERSATIONS)) {
                    missed++;
                } else if (curCallLog.getString(curCallLog.getColumnIndex(CallLog.Calls.TYPE)).equals(MESSAGE_TYPE_DRAFT)) {
                    draft++;
                }
                durationcount += take;
                callcount++;
                curCallLog.moveToNext();
            }
        }
    }

    long sec, mint, hour, day;

    private String timeToString(Long time) {
        sec = 0;
        mint = 0;
        hour = 0;
        day = 0;
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    sec = time % 60;
                    break;
                case 1:
                    mint = time % 60;
                    break;
                case 2:
                    hour = time % 60;
                    break;
                case 3:
                    day = time % 24;
                    break;
            }
            time /= 60;
        }
        String date = "00:00:00";
        if (day != 0)
            date = (day + "day+" + String.format("%02d", hour) + ":" + String.format("%02d", mint) + ":" + String.format("%02d", sec));
        else
            date = (String.format("%02d", hour) + ":" + String.format("%02d", mint) + ":" + String.format("%02d", sec));
        return date;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    public void login() {
        LinearLayout sns_login = (LinearLayout) findViewById(R.id.insta_login);
        sns_login.setVisibility(View.GONE);

        ImageView pic = (ImageView) findViewById(R.id.user_profile_pic);
        Picasso.with(this).load(appPreferences.getString(AppPreferences.PROFILE_PIC)).into(pic);
        TextView name = (TextView) findViewById(R.id.user_id);
        name.setText(Html.fromHtml(appPreferences.getString(AppPreferences.USER_NAME)));
        name.setMovementMethod(LinkMovementMethod.getInstance());
        TextView Like_cnt = (TextView) findViewById(R.id.sns_likes);
        Like_cnt.setText(appPreferences.getString(AppPreferences.User_like_cnt));
        TextView Follower_cnt = (TextView) findViewById(R.id.sns_follwers);
        Follower_cnt.setText(appPreferences.getString(AppPreferences.Follows));
        TextView Followed_cnt = (TextView) findViewById(R.id.sns_following);
        Followed_cnt.setText(appPreferences.getString(AppPreferences.Followed));
        TextView Photo_cnt = (TextView) findViewById(R.id.sns_photos);
        Photo_cnt.setText(appPreferences.getString(post_cnt));
        TextView Video_cnt = (TextView) findViewById(R.id.sns_videos);
        Video_cnt.setText(appPreferences.getString(AppPreferences.video_cnt));
        TextView Comment_cnt = (TextView) findViewById(R.id.sns_comments);
        Comment_cnt.setText(appPreferences.getString(AppPreferences.comments));
        TextView Follow_mod_followed = (TextView) findViewById(R.id.f_mod_f);
        Follow_mod_followed.setText(appPreferences.getString(AppPreferences.Following_Mod_Follweded));
        TextView Comment_mod_post = (TextView) findViewById(R.id.c_mod_p);
        Comment_mod_post.setText(appPreferences.getString(AppPreferences.Comment_Mod_Counts));
        TextView Like_mod_post = (TextView) findViewById(R.id.l_mod_p);
        Like_mod_post.setText(appPreferences.getString(AppPreferences.Likes_Mod_Counts));
        TextView Recent_post = (TextView) findViewById(R.id.Recent_post);
        Recent_post.setText(appPreferences.getString(AppPreferences.Recent_post_date));
        TextView Go_my_page = (TextView) findViewById(R.id.go_my_page);
        Go_my_page.setText("www.instagram.com/" + appPreferences.getString(AppPreferences.USER_NAME));
        Linkify.addLinks(Go_my_page, Linkify.WEB_URLS);
        LinearLayout sns_profile = (LinearLayout) findViewById(R.id.insta_profile);
        sns_profile.setVisibility(View.VISIBLE);
    }

    public void SetClick() {
        final Instrumentation inst = new Instrumentation();
        new Thread() {
            public void run() {
                MotionEvent event = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 200, 1555, 0);
                MotionEvent event1 = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 200, 1555, 0);
                inst.sendPointerSync(event);
                inst.sendPointerSync(event1);

                this.interrupt();
            }
        }.start();
    }

    public void logout() {
        token = null;
        appPreferences.clear();
        Toast toast = Toast.makeText(getApplicationContext(), "인스타그램 로그아웃 완료", Toast.LENGTH_LONG);
        toast.show();
        sns_score=0;
    }

    @Override
    public void onTokenReceived(String auth_token) {
        if (auth_token == null)
            return;
        appPreferences.putString(AppPreferences.TOKEN, auth_token);
        token = auth_token;
        getUserInfoByAccessToken(token);
    }

    public void gogogo(View view) {
        authenticationDialog = new AuthenticationDialog(this, this);
        authenticationDialog.setCancelable(true);
        authenticationDialog.show();
    }

    public void logout_logout(View view) {
        logout();
        LinearLayout sns_profile = (LinearLayout) findViewById(R.id.insta_profile);
        LinearLayout sns_login = (LinearLayout) findViewById(R.id.insta_login);
        sns_profile.setVisibility(View.GONE);
        sns_login.setVisibility(View.VISIBLE);
        SetClick();
    }

    private void getUserInfoByAccessToken(String token) {
        new RequestInstagramAPI().execute();
        new RequestInstagramAPI2().execute();
    }

    private class RequestInstagramAPI extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(getResources().getString(R.string.get_user_info_url) + token);

            try {
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();
                return EntityUtils.toString(httpEntity);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonData = jsonObject.getJSONObject("data");
                    JSONObject jsonData2 = jsonData.getJSONObject("counts");

                    Log.d("instagramjson", "instagramjosn" + jsonObject);
                    if (jsonData.has("id")) {
                        sns_score = 0;
                        appPreferences.putString(AppPreferences.USER_ID, "<a href=" + R.string.instagram_domain + jsonData.getString("id") + ">" + jsonData.getString("id") + "</a>");
                        appPreferences.putString(AppPreferences.USER_NAME, jsonData.getString("username"));
                        appPreferences.putString(AppPreferences.PROFILE_PIC, jsonData.getString("profile_picture"));
                        appPreferences.putString(AppPreferences.Follows, jsonData2.getString("follows"));
                        appPreferences.putString(AppPreferences.Followed, jsonData2.getString("followed_by"));
                        full_name = jsonData.getString("full_name");
                        following_mod_followed = jsonData2.getDouble("follows") / jsonData2.getDouble("followed_by");
                        following_mod_followed = Math.round(following_mod_followed);
                        sns_score += (long) following_mod_followed;
                        Log.d("following_mod_followed", "following_mod_followed, " + following_mod_followed);
                        if (sns_score > 100) sns_score = 100;
                        appPreferences.putString(AppPreferences.Following_Mod_Follweded, String.valueOf(following_mod_followed));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "!!", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    private class RequestInstagramAPI2 extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(getResources().getString(R.string.get_user_info_U) + token);
            try {
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();
                return EntityUtils.toString(httpEntity);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonData2 = jsonObject.getJSONArray("data");
                    cnt = user_like = video_cnt = posting_cnt = 0;

                    for (int i = 0; i < jsonData2.length(); i++) {
                        JSONObject jsondata3 = jsonData2.getJSONObject(i);
                        JSONObject jsondata4 = jsondata3.getJSONObject("comments");
                        JSONObject jsondata5 = jsondata3.getJSONObject("likes");

                        if (i == 0) {
                            String recent_post_date = jsondata3.getString("created_time");
                            long recent_post_date_long = Long.parseLong(recent_post_date) * 1000;
                            Date date = new Date(recent_post_date_long);
                            DateFormat foramtter = new SimpleDateFormat("YYYY MM dd");
                            appPreferences.putString(AppPreferences.Recent_post_date, "recent post : " + foramtter.format(date));
                        }
                        cnt += jsondata4.getInt("count");
                        user_like += jsondata5.getInt("count");
                        if (jsondata3.has("videos")) video_cnt++;
                        else posting_cnt++;
                    }

                    like_mod_posting = Math.round((double) user_like / (double) (posting_cnt + video_cnt));
                    comments_mod_posting = Math.round((double) cnt / (double) (posting_cnt + video_cnt));
                    Log.d("like_mod_posting", "like_mod_postiong " + like_mod_posting);
                    Log.d("comments_mod_postiong", "comments_mod_postiong" + comments_mod_posting);

                    appPreferences.putString(AppPreferences.Likes_Mod_Counts, String.valueOf(Math.round(((double) user_like / (double) (posting_cnt + video_cnt)) * 1000) / 1000.0));
                    appPreferences.putString(AppPreferences.Comment_Mod_Counts, String.valueOf(Math.round(((double) cnt / (double) (posting_cnt + video_cnt)) * 1000) / 1000.0));
                    sns_score += like_mod_posting + comments_mod_posting;
                    appPreferences.putString(AppPreferences.comments, String.valueOf(cnt));
                    appPreferences.putString(AppPreferences.post_cnt, String.valueOf(posting_cnt));
                    appPreferences.putString(AppPreferences.video_cnt, String.valueOf(video_cnt));
                    appPreferences.putString(AppPreferences.User_like_cnt, String.valueOf(user_like));

                    login();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "!!", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    //계좌내역 백그라운드 메소드
    public class JSONTask extends AsyncTask<String, String, String> {
        private URL url;

        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("user_id", Mainlogin.userId); //token 가져오기
                Log.d("아이디", "" + Mainlogin.userId);
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

                    Log.d("온다", "시작");

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();

                    String line = "";

                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }


                    buffer.deleteCharAt(0);
                    buffer.deleteCharAt(buffer.length() - 1);


                    JSONObject object = new JSONObject(buffer.toString());


                    //사용자 정보 조회
                    token = Constants.TOKEN_PREFIX + object.getString("usertoken");
                    Map params = new LinkedHashMap<>();
                    params.put("user_seq_no", object.getString("userseqnum"));
                    Log.d("데이터", "" + buffer.toString() + " " + token + " " + object.getString("userseqnum") + " " + params.get("user_seq_no"));


                    Call<Map> call = RetrofitCustomAdapter.getInstance().userMe(token, params);


                    call.enqueue(new Callback<Map>() {
                        @Override
                        public void onResponse(Call<Map> call, Response<Map> response) {
                            Log.d("리스폰스", "" + response.body());
                            Object obj = response.body().get("res_list");
                            Gson gson = new Gson();
                            String str = gson.toJson(obj);

                            JsonParser parser = new JsonParser();
                            JsonArray obj2 = (JsonArray) parser.parse(str);
                            JsonObject obj3 = (JsonObject) obj2.get(0);
                            Log.d("파싱", "" + obj2.toString() + " " + obj3.get("fintech_use_num"));
                            fintechusenum = obj3.get("fintech_use_num").toString();
                            fintechusenum = fintechusenum.substring(1, fintechusenum.length() - 1);
                            Log.d("파싱", " " + token + " " + fintechusenum);
                            //account(token,fintechusenum);  //잔액조회
                            transaction_list(token, fintechusenum);//거래내역
                            //deposit(token,fintechusenum);
                        }

                        @Override
                        public void onFailure(Call<Map> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });

                    return "완료";//서버로 부터 받은 값을 리턴해줌(userToken, userseqnum)

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

        protected void transaction_list(String token, String user_num) {
            Map params = new LinkedHashMap<>();
            Log.d("거래내역", "" + token + " " + user_num);
            Log.d("esty", "첫");
            params.put("fintech_use_num", user_num);
            params.put("inquiry_type", "A");
            params.put("from_date", "20160404");
            params.put("to_date", "20190615");
            params.put("sort_order", "D");
            params.put("page_index", "00001");
            params.put("tran_dtime", "20160310101921");
            params.put("befor_inquiry_trace_info", "");

            Log.d("esty", "시작");

            Call<Map> call = RetrofitCustomAdapter.getInstance().accountTrasactionList(token, params); // rest client 호출
            call.enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {
                    Object obj = response.body().get("res_list");
                    Gson gson = new Gson();
                    String str = gson.toJson(obj);
                    Log.d("esty", "중간" + response.body());
                    JsonParser parser = new JsonParser();
                    JsonArray obj2 = (JsonArray) parser.parse(str);
                    int len = obj2.size();

                    ArrayList<Month> list = new ArrayList<>();


                    for (int i = 0; i < 12; i++) {
                        list.add(new Month(0, 0, 0));
                    }

                    Date currentTime = Calendar.getInstance().getTime();
                    SimpleDateFormat month = new SimpleDateFormat("MM", Locale.getDefault());
                    int Month = Integer.parseInt(month.format(currentTime));      //현재의 월

                    JsonObject object;
                    for (int i = 0; i < len; i++) {
                        object = (JsonObject) obj2.get(i);
                        String ob = object.get("inout_type").getAsString();
                        int money = Integer.parseInt(object.get("tran_amt").getAsString());
                        int thisMonth = Integer.parseInt(object.get("tran_date").getAsString().substring(4, 6));
                        if (ob.equals("입금")) {
                            if (Month == thisMonth) {   //현재 월
                                list.get(Month).in_money += money;
                                list.get(Month).deal++;
                            } else if (Month - 1 == thisMonth) {    //1달 전
                                list.get(Month - 1).in_money += money;
                                list.get(Month - 1).deal++;
                            } else if (Month - 2 == thisMonth) {    //2달 전
                                list.get(Month - 2).in_money += money;
                                list.get(Month - 2).deal++;
                            } else if (Month - 3 == thisMonth) {    //3달 전
                                list.get(Month - 3).in_money += money;
                                list.get(Month - 3).deal++;
                            }
                        } else if (ob.equals("출금")) {
                            if (Month == thisMonth) {   //현재 월
                                list.get(Month).out_money += money;
                                list.get(Month).deal++;
                            } else if (Month - 1 == thisMonth) {    //1달 전
                                list.get(Month - 1).out_money += money;
                                list.get(Month - 1).deal++;
                            } else if (Month - 2 == thisMonth) {    //2달 전
                                list.get(Month - 2).out_money += money;
                                list.get(Month - 2).deal++;
                            } else if (Month - 3 == thisMonth) {    //3달 전
                                list.get(Month - 3).out_money += money;
                                list.get(Month - 3).deal++;
                            }
                        }
                    }

                    back_month.setText(Month + "월");
                    Monthago.setText(Month - 1 + "월");
                    twoMonthago.setText(Month - 2 + "월");
                    threeMonthago.setText(Month - 3 + "월");


                    input_data.setText("+" + String.format("%,d", +list.get(Month).in_money) + "원");
                    output_data.setText("-" + String.format("%,d", +list.get(Month).out_money) + "원");


                    Monthago_in.setText("+" + String.format("%,d", +list.get(Month - 1).in_money) + "원");
                    Monthago_out.setText("-" + String.format("%,d", +list.get(Month - 1).out_money) + "원");
                    Monthago_deal.setText(" " + list.get(Month - 1).deal + "건");

                    twoMonthago_in.setText("+" + String.format("%,d", +list.get(Month - 2).in_money) + "원");
                    twoMonthago_out.setText("-" + String.format("%,d", +list.get(Month - 2).out_money) + "원");
                    twoMonthago_deal.setText(" " + list.get(Month - 2).deal + "건");

                    threeMonthago_in.setText("+" + String.format("%,d", +list.get(Month - 3).in_money) + "원");
                    threeMonthago_out.setText("-" + String.format("%,d", +list.get(Month - 3).out_money) + "원");
                    threeMonthago_deal.setText(" " + list.get(Month - 3).deal + "건");


                    List<ILineDataSet> lineDataSets = new ArrayList<>();
                    ArrayList<Entry> in_money = new ArrayList<>();
                    ArrayList<Entry> out_money = new ArrayList<>();
                    ArrayList<Entry> balance = new ArrayList<>();

                    in_money.add(new Entry((Month - 3), list.get(Month - 3).in_money));
                    in_money.add(new Entry((Month - 2), list.get(Month - 2).in_money));
                    in_money.add(new Entry((Month - 1), list.get(Month - 1).in_money));
                    in_money.add(new Entry(Month, list.get(Month).in_money));

                    LineDataSet in_data = new LineDataSet(in_money, "수입");
                    in_data.setLineWidth(2);
                    in_data.setCircleRadius(6);
                    in_data.setCircleColor(Color.parseColor("#FFA1B4DC"));
                    in_data.setCircleColorHole(Color.BLUE);
                    in_data.setColor(Color.parseColor("#FFA1B4DC"));
                    in_data.setDrawCircleHole(true);
                    in_data.setDrawCircles(true);
                    in_data.setDrawHorizontalHighlightIndicator(false);
                    in_data.setDrawHighlightIndicators(false);
                    in_data.setDrawValues(false);


                    out_money.add(new Entry(Month - 3, list.get(Month - 3).out_money));
                    out_money.add(new Entry(Month - 2, list.get(Month - 2).out_money));
                    out_money.add(new Entry(Month - 1, list.get(Month - 1).out_money));
                    out_money.add(new Entry(Month, list.get(Month).out_money));

                    LineDataSet out_data = new LineDataSet(out_money, "지출");
                    out_data.setLineWidth(2);
                    out_data.setCircleRadius(6);
                    out_data.setCircleColor(Color.parseColor("#ff0000"));
                    out_data.setCircleColorHole(Color.RED);
                    out_data.setColor(Color.parseColor("#ff0000"));
                    out_data.setDrawCircleHole(true);
                    out_data.setDrawCircles(true);
                    out_data.setDrawHorizontalHighlightIndicator(false);
                    out_data.setDrawHighlightIndicators(false);
                    out_data.setDrawValues(false);


                    int threeagobalance = list.get(Month - 3).in_money - list.get(Month - 3).out_money;
                    int twoagobalance = threeagobalance + (list.get(Month - 2).in_money - list.get(Month - 2).out_money);
                    int agobalance = twoagobalance + (list.get(Month - 1).in_money - list.get(Month - 1).out_money);
                    int nowbalance = agobalance + (list.get(Month).in_money - list.get(Month).out_money);

                    balance.add(new Entry(Month - 3, threeagobalance));
                    balance.add(new Entry(Month - 2, twoagobalance));
                    balance.add(new Entry(Month - 1, agobalance));
                    balance.add(new Entry(Month, nowbalance));

                    LineDataSet balance_data = new LineDataSet(balance, "잔액");
                    balance_data.setLineWidth(2);
                    balance_data.setCircleRadius(6);
                    balance_data.setCircleColor(R.color.gradStart);
                    balance_data.setCircleColorHole(R.color.gradStart);
                    balance_data.setColor(R.color.gradStart);
                    balance_data.setDrawCircleHole(true);
                    balance_data.setDrawCircles(true);
                    balance_data.setDrawHorizontalHighlightIndicator(false);
                    balance_data.setDrawHighlightIndicators(false);
                    balance_data.setDrawValues(false);

                    lineDataSets.add(in_data);
                    lineDataSets.add(out_data);
                    lineDataSets.add(balance_data);

                    LineData line = new LineData(lineDataSets);
                    lineChart = (LineChart) findViewById(R.id.chart);
                    lineChart.setData(line);


                    XAxis xAxis = lineChart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setTextColor(Color.BLACK);
                    xAxis.enableGridDashedLine(8, 24, 0);

                    YAxis yLAxis = lineChart.getAxisLeft();
                    yLAxis.setTextColor(Color.BLACK);

                    YAxis yRAxis = lineChart.getAxisRight();
                    yRAxis.setDrawLabels(false);
                    yRAxis.setDrawAxisLine(false);
                    yRAxis.setDrawGridLines(false);

                    Description description = new Description();
                    description.setText("");

                    lineChart.setDoubleTapToZoomEnabled(false);
                    lineChart.setDrawGridBackground(false);
                    lineChart.setDescription(description);
                    lineChart.getXAxis().setGranularity(1f);
                    lineChart.animateY(2000, Easing.EasingOption.EaseInCubic);
                    lineChart.invalidate();


                    double agomonth = list.get(Month - 1).out_money;
                    double twoagomonth = list.get(Month - 2).out_money;
                    double threeagomont = list.get(Month - 3).out_money;
                    double monthAvg = (agomonth + twoagomonth + threeagomont) / 3;

//                  back_score = (Math.pow(agomonth, 2) + Math.pow(twoagomonth, 2) + Math.pow(threeagomont, 2)) - ((Math.pow((agomonth + twoagomonth + threeagomont), 2)) / 3);   //(a^2+b^2+c^2)-((a+b+c)^2)/3
//                  back_score = (((agomonth + twoagomonth + threeagomont) / 3) / back_score) * 50; //평균/back_score*50

                    balance_score = monthAvg / (Math.abs(Math.abs(monthAvg - agomonth) + Math.abs(monthAvg - twoagomonth) + Math.abs(monthAvg - threeagomont)));
                    save_money = ((twoagobalance - threeagobalance) + (agobalance - twoagobalance) + (nowbalance - agobalance)) / 3;  //저축평균

                    bank_score = (int) (balance_score * 10) + (int) (save_money / 10000);

                    //저축 평균 (잔액 4월-3월 5월-4월 6월-5월)/3 -> 평균
                    Log.d("결과값", "결과" + bank_score);
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }
    }
}
