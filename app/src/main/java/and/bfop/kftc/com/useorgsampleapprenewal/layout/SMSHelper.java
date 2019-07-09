package and.bfop.kftc.com.useorgsampleapprenewal.layout;

import android.content.ContentResolver;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import static and.bfop.kftc.com.useorgsampleapprenewal.layout.SildingActivity.context;
import static and.bfop.kftc.com.useorgsampleapprenewal.layout.SildingActivity.tbl_sms;

public class SMSHelper extends SQLiteOpenHelper {

    static ContentResolver contentResolver;

    public SMSHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        contentResolver = context.getContentResolver();
    }
    // ArrayList 중복 제거(유일본 추출) > String 리턴
    private String uniqueListString(ArrayList<String> srcList) {
        String resultString = "";
        HashSet hs = new HashSet(srcList);
        Iterator it = hs.iterator();
        while (it.hasNext()) resultString += it.next();
        return resultString;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + tbl_sms +
                " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, chosung TEXT, phone TEXT, date LONG, " +
                "body TEXT, chk INTEGER not null default 0);";
        db.execSQL(sql);

        new ReadMmsSms(context).collectMessage(db);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 참고 : onUpgrade 메소드 샘플 http://blog.daum.net/andro_java/1248
        // 이미 배포한 앱의 데이터베이스 구조 변경이 있는 업그레이드 시 위 페이지 참고할 것
        // 단순히 DROP TABLE > Create 하면 기존 데이터 다 날아감
    }
}