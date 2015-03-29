package example.com.finder.Utils;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.util.Log;

/**
 * Created by Alexandru on 29-Mar-15.
 */
public class ViewUtils {

    public static void launchRingDialog(final Context context, final Runnable runnable) {
        final ProgressDialog ringProgressDialog = ProgressDialog.show(context, "Please wait ...", "Checking database info ...", true);
        ringProgressDialog.setCancelable(false);
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {

                runnable.run();
                ringProgressDialog.dismiss();
            }
        });
        th.start();

    }


    public static String getBluetoothMacAddress() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // if device does not support Bluetooth
        if(mBluetoothAdapter==null){
            Log.d("loginactivity", "device does not support bluetooth");
            return null;
        }

        return mBluetoothAdapter.getAddress();
    }
}
