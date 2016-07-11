package irctc.factor.app.irctcmadeeasy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import irctc.factor.app.irctcmadeeasy.database.DaoMaster;

/**
 * Created by hassanhussain on 7/7/2016.
 */
public class TicketConstants {
    public static List<String> TRAIN_CONST_LIST = new ArrayList<>();
    public static List<String> STATION_CONST_LIST = new ArrayList<>();
    public static SQLiteDatabase mWriteDatabase = null;
    static DaoMaster.DevOpenHelper mDbHelper = null;

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
                    if(station_trains)
                        STATION_CONST_LIST.add(s);
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
        mDbHelper = new DaoMaster.DevOpenHelper(context, "irctc_db_info.db", null);
        return true;
    }

    public static SQLiteDatabase getReadableDatabase(){ return mDbHelper.getReadableDatabase(); }
    public static SQLiteDatabase getWritableDatabase(){ return mDbHelper.getWritableDatabase(); }
}
