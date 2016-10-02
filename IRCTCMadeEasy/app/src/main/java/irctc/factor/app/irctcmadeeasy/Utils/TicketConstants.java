package irctc.factor.app.irctcmadeeasy.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import irctc.factor.app.irctcmadeeasy.Activity.AdBlocker;
import irctc.factor.app.irctcmadeeasy.database.DaoMaster;

/**
 * Created by hassanhussain on 7/7/2016.
 */
public class TicketConstants {
    public static List<String> TRAIN_CONST_LIST = new ArrayList<>();
    public static List<StationDetails> STATION_CONST_LIST = new ArrayList<>();
    public static SQLiteDatabase mWriteDatabase = null;
    static DaoMaster.DevOpenHelper mDbHelper = null;

    public static String mJsAutoPrefix = "javascript: var jsonString ";
    public static String JavaScriptForAutoFill = "";

    public static void LoadDataFromFile(Context context, boolean station_trains) {
        String csvFile = "stations.prop";
        if(!station_trains)
            csvFile = "trains.prop";

        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        try {
            br = new BufferedReader(new InputStreamReader(context.getAssets().open(csvFile)));
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] list_arr = line.split(cvsSplitBy);
                for (String s: list_arr) {
                    s = s.substring(1, s.length()-1);
                    if(station_trains) {
                        StationDetails station = new StationDetails();
                        station.StationCode = s.substring(s.indexOf("-") + 1).trim();
                        station.StationName = s.substring(0, s.indexOf("-")).trim();
                        station.StationFullName = s;
                       STATION_CONST_LIST.add(station);
                    }
                    else
                        TRAIN_CONST_LIST.add(s);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean Initialize(Context context) {
        LoadDataFromFile(context, true);
        LoadDataFromFile(context, false);
        return (TRAIN_CONST_LIST.isEmpty() || STATION_CONST_LIST.isEmpty());
    }

    public static boolean InitializeDatabase(Context context) {
        mDbHelper = new DaoMaster.DevOpenHelper(context, "irctc_db_info2.db", null);
        return true;
    }

    public static void InitializeAdsModule(Context con){
        AdBlocker.init(con);
    }

    public static boolean InitializeJavaScript(Context context){
        InputStream input;
        try {
            StringBuffer sBuffer = new StringBuffer();
            input = context.getAssets().open("javascript.js");
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String data = "";
            if (input != null) {
                while ((data = reader.readLine()) != null) {
                    sBuffer.append(data + "\n");
                }
                input.close();
            }
            // String-ify the script byte-array using BASE64 encoding !!!
            JavaScriptForAutoFill = sBuffer.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static SQLiteDatabase getReadableDatabase(Context context){ return getDbHelper(context).getReadableDatabase(); }
    public static SQLiteDatabase getWritableDatabase(Context context){ return getDbHelper(context).getWritableDatabase(); }

    public static DaoMaster.DevOpenHelper getDbHelper(Context context){
        if(mDbHelper == null)
            mDbHelper = new DaoMaster.DevOpenHelper(context, "irctc_db_info2.db", null);
        return mDbHelper;
    }

    public static boolean CheckIfDatePast(String strDate){
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(new Date());

        SimpleDateFormat dateFormat = new SimpleDateFormat ("dd-MM-yyyy");
        try {
            Date date1 = dateFormat.parse(currentDate);
            Date date2 = dateFormat.parse(strDate);
            return (date2.after(date1)||date2.equals(date1))?true:false;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }


}
