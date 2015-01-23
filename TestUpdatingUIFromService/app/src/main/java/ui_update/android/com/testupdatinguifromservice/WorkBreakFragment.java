package ui_update.android.com.testupdatinguifromservice;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


public class WorkBreakFragment extends Fragment {

    public static final String SERVICE_PAUSE_BOOL = "service.pause";
    public static final String SERVICE_START_DATA = "service.start.data";

    private static long timeSwapBuff = 0L;
    private Intent intent;
    public TextView mTimeTextView;
    public static Button mStartButton, mPauseButton, mStopButton;
    public static String data = "0:0:0";
    //Зберігаю час при якому вспливають Notif/Dialogs
    public static ArrayList<String> mTimeList = PossibleTime.mTimeList();
    //Flag if Notification/Dialogs starts or not
    public static ArrayList<Boolean> mBoolList = PossibleTime.mBoolList();
    //Storing User decisions
    public static ArrayList<Boolean> mResultBreakList = PossibleTime.mResultList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = new Intent(getActivity(), TempService.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_break, container, false);
        initButtons(view);

        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(TempService.BROADCAST_ACTION));

        //появлявляються після нажимання на Нотіфікейшен , і виводить на екран Dialog
        if (getActivity().getIntent().getStringExtra(NotifyReceiver.NOTIFICATION_TIME_DATA) != null) {
            if (getActivity().getIntent().getBooleanExtra(NotifyReceiver.NOTIFICATION_START_BOOL, false)) {
                getActivity().getIntent().removeExtra(NotifyReceiver.NOTIFICATION_START_BOOL);
                mPauseButton.callOnClick();
                new MyALDialog().show(getFragmentManager(), "Dialog");
            }
        }
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            timeSwapBuff = intent.getLongExtra(TempService.BROADCAST_ON_PAUSE_TIME, 0L);
            //Receiving and setting Current time to TextView
            data = intent.getStringExtra(TempService.BROADCAST_DATA);
            mTimeTextView.setText("Time: " + data);

            //Dialog
            for (int i = 0; i < mTimeList.size(); i++)
                if (mTimeList.get(i).equals(data) & !mBoolList.get(i)) {
                    mPauseButton.callOnClick();
                    playBeep();
                    new MyALDialog().show(getFragmentManager(), "Dialog");
                    mBoolList.set(i, true);
                }
            //if person work more then this time , counting is stop and showed work info
            //in original version time should be 8 hour or setting by user
            if (data.equals("0:3:0") | data.equals("0:3:30")) mStopButton.callOnClick();
        }
    };

    public static class MyALDialog extends DialogFragment implements DialogInterface.OnClickListener {

        @Override
        public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                    .setTitle("Would you like to make a break?")
                    .setPositiveButton(R.string.yes, this)
                    .setNegativeButton(R.string.no, this)
                    .setMessage("You are working more than  " + mTimeList.get(mBoolList.indexOf(false) - 1))
                    .setCancelable(false);
            return adb.create();
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    //Saving user Decision
                    mResultBreakList.add(true);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    //Saving user Decision and resume time counter
                    mResultBreakList.add(false);
                    mStartButton.callOnClick();
                    break;
            }
        }
    }

    public void initButtons(View v) {

        mTimeTextView = (TextView) v.findViewById(R.id.textView);
        mTimeTextView.setText("Time: " + data);
        mStartButton = (Button) v.findViewById(R.id.StartButton);
        mPauseButton = (Button) v.findViewById(R.id.PauseButton);
        mStopButton = (Button) v.findViewById(R.id.StopButton);
        mPauseButton.setEnabled(false);
        mStopButton.setEnabled(false);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartButton.setEnabled(false);
                mPauseButton.setEnabled(true);
                mStopButton.setEnabled(true);
                //dispatching saved time
                intent.putExtra(SERVICE_START_DATA, timeSwapBuff);
                getActivity().startService(intent);
            }
        });

        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartButton.setEnabled(true);
                mStopButton.setEnabled(false);
                mPauseButton.setEnabled(false);
                getActivity().startService(new Intent(getActivity(), TempService.class).putExtra(SERVICE_PAUSE_BOOL, true));

            }
        });

        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStopButton.setEnabled(false);
                mStartButton.setEnabled(true);
                mPauseButton.setEnabled(false);
                //cleaning data and stop service
                mBoolList = PossibleTime.eraseBoolArr(mBoolList);
                getActivity().stopService(intent);
                //User decision INFO
                createDialog(getActivity()).show();
                mResultBreakList.clear();
                data = "0:0:0";
                mTimeTextView.setText("Time: 0:0:0");

            }
        });
    }

    //making BEEP sound ))
    public void playBeep() {
        try {
            Uri notification = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getActivity(),
                    notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AlertDialog createDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false)
                .setMessage("You worked :" + data + PossibleTime.ResultInfo(mResultBreakList))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }
}
