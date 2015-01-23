package ui_update.android.com.testupdatinguifromservice;

import java.util.ArrayList;
import java.util.Arrays;


public class PossibleTime {

    //Time when notif/dialog starts
    private static String[] mPossibleTime = {"0:0:15", "0:0:30", "0:0:45", "0:1:0", "0:1:15", "0:1:30", "0:1:45", "0:2:0", "0:2:15", "0:2:20"};
    private static Boolean[] mMadeAlarms = {false, false, false, false, false, false, false, false, false, false};

    public static ArrayList<String> mTimeList() {
        ArrayList<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(mPossibleTime));
        return list;
    }

    public static ArrayList<Boolean> mBoolList() {
        ArrayList<Boolean> list = new ArrayList<>();
        list.addAll(Arrays.asList(mMadeAlarms));
        return list;
    }

    public static ArrayList<Boolean> eraseBoolArr(ArrayList<Boolean> arr) {
        for (int i = 0; i < arr.size(); i++) {
            arr.set(i, false);
        }
        return arr;
    }

    public static ArrayList<Boolean> mResultList() {
        return new ArrayList<>();
    }

    //передивляємось , чи Dialog був skip чи confirm. Результат виводим стрінгом
    public static String ResultInfo(ArrayList<Boolean> list) {
        StringBuilder result = new StringBuilder();
        result.append("\nResult : \n");
        for (int i = 0; i < list.size(); i++) {
            result.append(mPossibleTime[i]).append(" : ")
                    .append(list.get(i) ? "Break was confirmed" : "Break was skipped")
                    .append("\n");
        }
        return result.toString();
    }
}
