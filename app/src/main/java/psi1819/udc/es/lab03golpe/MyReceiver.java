package psi1819.udc.es.lab03golpe;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.usb.UsbManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;
import android.widget.ToggleButton;

public class MyReceiver extends BroadcastReceiver {

    Boolean mRemote=false;
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i;
        switch (intent.getAction()) {
            case Intent.ACTION_SCREEN_OFF:
                Log.d("BroadcastReceiver event", "screen off");
                Intent intentServ = new Intent(context, RemoteServ.class);
                intentServ.putExtra("COUNT", "20");
                intentServ.putExtra("TIME_WAIT", "500");
                context.bindService(intentServ, mConn, context.BIND_AUTO_CREATE);
                Log.d(" Servicio remoto", "init");
                break;
            case Intent.ACTION_SCREEN_ON:
                Log.d("BroadcastReceiver event", "screen on");
                Intent intentServ2 = new Intent(context, RemoteServ.class);
                intentServ2.putExtra("COUNT", "20");
                intentServ2.putExtra("TIME_WAIT", "500");
                context.bindService(intentServ2, mConn, context.BIND_AUTO_CREATE);
                Log.d(" Servicio remoto", "init");
                break;
        }
    }

    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder bind){
            RemoteServ.SimpleBinder sBinder=(RemoteServ.SimpleBinder) bind;
            mRemote = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {

        }
    };


}
