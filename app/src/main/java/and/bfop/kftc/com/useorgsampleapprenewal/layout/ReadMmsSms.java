package and.bfop.kftc.com.useorgsampleapprenewal.layout;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ReadMmsSms {

    private static Context context;
    private static ContentResolver contentResolver;
    static String myPhoneNum;

    static SQLiteDatabase database;

    static Cursor curSMS;
    static Cursor curMMS;
    static ArrayList<Obj_Setup> al_setup;

    public static String grade="nograde";
    static boolean checkgrade = false;

    public static boolean pay = false;
    public static int paycount = 0;
    public static String payofcharge;

    public ReadMmsSms(Context context) {
        this.context = context;
        contentResolver = context.getContentResolver();
        // 내 전화번호
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        myPhoneNum = "0" + tm.getLine1Number().substring(3); // 퍼미션 READ_PHONE_STATE
    }

    // 전화번호로 이름 찾기(SMS)
    public static String phoneToName_sms(String phoneNumber) {
        if (phoneNumber.equals(myPhoneNum)) return "내게 보낸 메세지";
        Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = contentResolver.query(uri, new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) { return null; }
        String name = "("+phoneNumber+")";
        if(cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME));
        }
        if(cursor != null && !cursor.isClosed()) { cursor.close(); }
        return name;
    }

    // thread_id로 전화번호 가져오기
    public String getPhoneByTheadId(long thread_id) {
        String phone = "";
        Uri uri = ContentUris.withAppendedId(Uri.parse("content://mms-sms/canonical-address"), thread_id);
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        try {
            if (cursor.moveToFirst()) {
                phone = cursor.getString(0);
            }
        }
        finally {
            cursor.close();
        }

        return phone;
    }

    // 전화번호로 이름 가져오기(MMS)
    public String phoneToName_mms(String phoneNumber) {
        Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        String[] projection = {PhoneLookup.DISPLAY_NAME, PhoneLookup.NORMALIZED_NUMBER };
        Cursor cursor = contentResolver.query(uri, projection, null, null, null);

        String name = null;
        try {
            if (cursor.moveToFirst()) {
                phoneNumber = cursor.getString(cursor.getColumnIndex(PhoneLookup.NORMALIZED_NUMBER));
                name = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME));
            }
        }
        finally {
            cursor.close();
        }
        // if there is a display name, then return that
        if(name != null){ return name;  }
        else{
            if (phoneNumber.equals(myPhoneNum)) return "나";
            return "("+phoneNumber+")"; // if there is not a display name, then return just phone number
        }
    }

    // thead_Id로 이름 가져오기
    // (Umarov) http://stackoverflow.com/questions/39953872/android-how-to-find-contact-name-and-number-from-recipient-ids
    public String getNameByTheadId(long thead_Id) {

        String name = "";
        Uri uri = ContentUris.withAppendedId(Uri.parse("content://mms-sms/canonical-address"), thead_Id);
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        try {
            if (cursor.moveToFirst()) {
                name = phoneToName_mms(cursor.getString(0));
            }
        }
        finally {
            cursor.close();
        }

        return name;
    }

    // android MMS 모니터링 http://devroid.com/80181708954
    private String getMmsText(String id) {
        Uri partURI = Uri.parse("content://mms/part/" + id);
        InputStream is = null;
        StringBuilder sb = new StringBuilder();
        try {
            is = contentResolver.openInputStream(partURI);
            if (is != null) {
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                BufferedReader reader = new BufferedReader(isr);
                String temp = reader.readLine();
                while (temp != null) {
                    sb.append(temp);
                    // if (sb.length() > 100) break;
                    temp = reader.readLine();
                }
            }
        } catch (IOException e) {}
        finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {}
            }
        }
        return sb.toString().trim();
    }

    // MMS 메시지(텍스트/이모티콘) 가져오기
    public String messageFromMms(String mmsId) {
        String selectionPart = "mid=" + mmsId;
        Uri uriPart = Uri.parse("content://mms/part");
        Cursor cursor = contentResolver.query(uriPart, null, selectionPart, null, null);

        String messageBody = "";
        if (cursor.moveToFirst()) {
            do {
                String partId = cursor.getString(cursor.getColumnIndex("_id"));
                String type = cursor.getString(cursor.getColumnIndex("ct"));

                if ("text/plain".equals(type)) {
                    String data = cursor.getString(cursor.getColumnIndex("_data"));

                    if (! messageBody.isEmpty()) messageBody += "\n";
                    if (data != null) { messageBody += getMmsText(partId); }
                    else { messageBody += cursor.getString(cursor.getColumnIndex("text")); }
                }
            } while( cursor.moveToNext() );
            cursor.close();
        }
        if(cursor != null && !cursor.isClosed()) { cursor.close(); }

        return messageBody;
    }

    // 설치를 위한 내부 클래스
    private class Obj_Setup {
        private String table;
        private int id;
        private long date;
        public Obj_Setup(String table, int id, long date) {
            this.table = table;
            this.id = id;
            this.date = date;
        }
        public String getTable() { return table; }
        public int getID() { return id; }
        public long getDate() { return date; }
    }

    // 날짜 Long 내림차순
    private class DateDesc_C implements Comparator<Obj_Setup> {
        @Override
        public int compare(Obj_Setup o1, Obj_Setup o2) {
            return o1.getDate() > o2.getDate() ? -1 : o1.getDate() < o2.getDate() ? 1:0;
        }
    }

    public ArrayList<Obj_Setup> readSetup() {
        int id = 0;
        long date;
        ArrayList<Obj_Setup> al_setup = new ArrayList<Obj_Setup>();
        Cursor curSMS = contentResolver.query(Uri.parse("content://sms"),
                new String[] { "_id", "date" }, null, null, "date DESC");
        while (curSMS.moveToNext()) {
            id = curSMS.getInt(curSMS.getColumnIndex("_id"));
            date = curSMS.getLong(curSMS.getColumnIndex("date"));
            if (date < 10000000000L) date = date * 1000;
            al_setup.add(new Obj_Setup("sms", id, date));
        }
        curSMS.close();

        ArrayList<Obj_Setup> al_setmms = new ArrayList<Obj_Setup>();
        Cursor curMMS = contentResolver.query(Uri.parse("content://mms"),
                new String[] { "_id", "date" }, null, null, "date DESC");
        while (curMMS.moveToNext()) {
            id = curMMS.getInt(curMMS.getColumnIndex("_id"));
            date = curMMS.getLong(curMMS.getColumnIndex("date"));
            if (date < 10000000000L) date = date * 1000;
            al_setmms.add(new Obj_Setup("mms", id, date));
        }
        curMMS.close();

        al_setup.addAll(al_setmms);
        Collections.sort(al_setup, new DateDesc_C());

        return al_setup;
    }

    class setupAsyncTask extends AsyncTask<Obj_Setup, Integer, Long> {

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Long doInBackground(Obj_Setup... params) {
            long result = 0;
            long nowdate = System.currentTimeMillis();
            Obj_Setup one;
            String grading[] = {"normal", "white", "silver", "gold", "vip", "vvip"};
            for (int i = 0; i<al_setup.size(); i++) {
                publishProgress(i); // 작업 진행상황 보고서 출력
                one = al_setup.get(i);
                if (one.getTable().equals("sms")) {
                    if (curSMS.moveToNext()) {
                        final String body = curSMS.getString(curSMS.getColumnIndex("body"));
                        // 내용 없는 메시지 제외
                        if (!body.trim().isEmpty()) {
                            if (body.contains("멤버십") && !checkgrade) {
                                for (int k = 0; k < grading.length; k++) {
                                    if (body.toUpperCase().contains(grading[k].toUpperCase())) {
                                        checkgrade = true;
                                        grade = grading[k];
                                    }
                                }
                            }
                            final String phone = curSMS.getString(curSMS.getColumnIndex("address"));
                            final String name = phoneToName_sms(phone);
                            long date = curSMS.getLong(curSMS.getColumnIndex("date"));
                            if (date < 10000000000L) date = date * 1000;
                            if (body.contains("통신요금") && (nowdate-date < 7948800000L) && !pay) {
                                if (body.contains("자동")) {
                                    String[] my_payment = body.split("\n| ");
                                    for(int m=0;m<my_payment.length;m++) {
                                        boolean endsWith = my_payment[m].endsWith("원");
                                        if (endsWith) {
                                            payofcharge = my_payment[m].replaceAll("[^0-9]", "");
                                        }
                                    }
                                    paycount++;
                                    if (paycount == 3)
                                        pay = true;
                                }
                            }
                        }
                    }
                    else { }
                }
                else {
                    if (curMMS.moveToNext()) {
                        String body = messageFromMms(curMMS.getString(curMMS.getColumnIndex("_id")));
                        // 내용 없는 메시지 제외
                        if (! body.trim().isEmpty()) {
                            if (body.contains("멤버십") && !checkgrade) {
                                for (int k = 0; k < grading.length; k++) {
                                    if (body.toUpperCase().contains(grading[k].toUpperCase())) {
                                        checkgrade = true;
                                        grade = grading[k];
                                    }
                                }
                            }
                            // thread_id > 상대방 이름(전화번호)
                            long thread_id = curMMS.getLong(curMMS.getColumnIndexOrThrow("thread_id"));
                            String phone = getPhoneByTheadId(thread_id);
                            // String name = phoneToName_mms(phone); // IllegalArgumentException - 커서 생성 줄에서
                            final String name = getNameByTheadId(thread_id);
                            // String name = phoneToName_sms(phone); // IllegalArgumentException - 커서 생성 줄에서
                            long date = curMMS.getLong(curMMS.getColumnIndex("date"));
                            if (date < 10000000000L) date = date * 1000;
                            if (body.contains("통신요금") && (nowdate-date < 7948800000L) && !pay) {
                                if (body.contains("자동")) {
                                    String[] my_payment = body.split("\n| ");
                                    for(int m=0;m<my_payment.length;m++) {
                                        boolean endsWith = my_payment[m].endsWith("원");
                                        if (endsWith) {
                                            payofcharge = my_payment[m].replaceAll("[^0-9]", "");
                                        }
                                    }
                                    paycount++;
                                    if (paycount == 3)
                                        pay = true;
                                }
                            }
                        }
                    }
                    else { }
                }
            }
            return result;
        }
        @Override
        protected void onPostExecute(Long result) {
            curSMS.close();
            curMMS.close();
            super.onPostExecute(result);
        }

    }
    public void collectMessage(SQLiteDatabase db) {
        this.database = db;
        al_setup = readSetup();
        curSMS = contentResolver.query(Uri.parse("content://sms"),
                new String[] { "_id", "thread_id", "address", "date", "body", "read" },
                null, null,
                "date DESC");
        curMMS = contentResolver.query(Uri.parse("content://mms"),
                new String[] { "_id", "thread_id", "date", "read" },
                null, null,
                "date DESC");
        new setupAsyncTask().execute(al_setup.toArray(new Obj_Setup[al_setup.size()]));
    }
}