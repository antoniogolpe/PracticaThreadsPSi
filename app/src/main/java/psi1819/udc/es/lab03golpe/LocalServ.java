package psi1819.udc.es.lab03golpe;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class LocalServ extends Service {

    private Integer count;
    private Integer time_wait;
    private MyThread mythread;

   /* private void funcion_servicio(){

            while (count >= 0) {
                Log.d("Count value:", count.toString());
                try {
                    Thread.sleep(time_wait);
                } catch (InterruptedException e) {
                }
                count--;
            }

    }

    Runnable runnable = new Runnable() {
        public void run() {
            funcion_servicio();
        }
    };*/

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Servicio local","creado");
        Bundle bundle = intent.getExtras();
        count = Integer.valueOf(bundle.getString("COUNT"));
        time_wait = Integer.valueOf(bundle.getString("TIME_WAIT"));

        Log.d("Servicio local","count = "+count.toString());
        Log.d("Servicio local","time_wait = "+time_wait.toString());

        mythread = new MyThread(count,time_wait);
        mythread.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mythread.interrupt();
        Log.d("Servicio local","finalizado");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
}
