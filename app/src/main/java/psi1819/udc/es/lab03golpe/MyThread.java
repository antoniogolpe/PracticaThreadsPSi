package psi1819.udc.es.lab03golpe;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class MyThread extends Thread {

    private Integer count;
    private Integer time_wait;
    private Boolean auto_cerrado;
    ProgressBar progressBar;

    public MyThread (Integer count, Integer time_wait) {
        super ();
        this.count = count;
        this.time_wait = time_wait;
        this.auto_cerrado=false;
    }

    public void funcion_servicio(){
        while(count>=0) {
            Log.d("Count value:", count.toString());
            try {
                Thread.sleep(time_wait);
            } catch(InterruptedException e) {}

            if (progressBar!=null){
                Log.d("Progress bar: ",count.toString());
                progressBar.setProgress(count);
            }
            count--;
        }
    }

    public void run() {
        funcion_servicio();

        if (progressBar!=null) {
            progressBar.post(new Runnable() {
                @Override
                public void run() {
                    ((ViewGroup) progressBar.getParent()).removeView(progressBar);
                }
            });
        }

        if (auto_cerrado) {
            this.interrupt();
        }
    }

    public Integer getCount(){
        return count;
    }

    public void setCount(Integer count_aux){
        Log.d("Thread count value: ",count_aux.toString());
        Integer aux=count;
        count=count_aux;
        if(aux<=0){
            funcion_servicio();
        }
    }

    public void setProgressBar(ProgressBar progressBar){
        this.progressBar = progressBar;
    }

    public void setAuto_cerrado(Boolean b){
        auto_cerrado=b;
    }
}
