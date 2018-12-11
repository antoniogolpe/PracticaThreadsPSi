package psi1819.udc.es.lab03golpe;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class RemoteServ extends Service {
    private Integer count;
    private Integer time_wait;
    private MyThread mythread;

    // Binder given to clients
    private final IBinder sBinder=(IBinder) new SimpleBinder();

    public Integer getCount(){
        return mythread.getCount();
    }

    public void setCount(Integer count_aux){
        Log.d("RemoteServ count val: ",count_aux.toString());
        count=count_aux;
        mythread.setCount(count);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mythread.interrupt();
        Log.d("Servicio remoto","finalizado");
    }

    @Override
    public IBinder onBind(Intent intent) {
    // catch argument extracting Bundle from intent
        Bundle bundle = intent.getExtras();
        count = Integer.valueOf(bundle.getString("COUNT"));
        time_wait = Integer.valueOf(bundle.getString("TIME_WAIT"));

        mythread = new MyThread(count,time_wait);
        mythread.start();

        return sBinder;
    }

    class SimpleBinder extends Binder {
        RemoteServ getService() {
            return RemoteServ.this;
        }
    }
}
