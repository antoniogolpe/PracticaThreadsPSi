package psi1819.udc.es.lab03golpe;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import psi1819.udc.es.lab03golpe.RemoteServ.SimpleBinder;

public class ServActiv extends AppCompatActivity {

    //Local Service
    ToggleButton tb_localService;
    EditText et_time_wait;
    EditText et_count;

    //Remote Service
    ToggleButton tb_remoteService;
    TextView tv;
    EditText et;
    Button but_get;
    Button but_send;
    Button but_task;
    TextView tv_result;
    Button but_thread;
    RemoteServ remoteServ;
    boolean mRemote;

    LinearLayout layout;

    MyReceiver receiver=new MyReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serv);

        tb_localService = (ToggleButton) findViewById(R.id.tb_localService);
        tb_localService.setChecked(false);
        et_time_wait = (EditText) findViewById(R.id.et_time_wait);
        et_count = (EditText) findViewById(R.id.et_count);

        tb_remoteService = (ToggleButton) findViewById(R.id.tb_remoteService);
        tb_remoteService.setChecked(false);
        tv = (TextView) findViewById(R.id.tv);
        et = (EditText) findViewById(R.id.et);
        but_get = (Button) findViewById(R.id.but_get);
        but_send = (Button) findViewById(R.id.but_send);

        but_task = (Button) findViewById(R.id.but_task);
        tv_result = (TextView) findViewById(R.id.tv_result);

        but_thread = (Button) findViewById(R.id.but_thread);

        layout = (LinearLayout) findViewById(R.id.layout);

        mRemote=false;

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(receiver, filter);

        tb_localService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                String count = et_count.getText().toString();
                String time_wait = et_time_wait.getText().toString();

                if(b){
                    Intent intent = new Intent(getApplicationContext(),LocalServ.class);
                    intent.putExtra("COUNT",count);
                    intent.putExtra("TIME_WAIT",time_wait);
                    startService(intent);
                    Log.d(" Servicio local","init");
                }else{
                    Intent intent = new Intent(getApplicationContext(),LocalServ.class);
                    stopService(intent);
                    Log.d(" Servicio local","stop");
                }
            }
        });

        tb_remoteService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                String count = et_count.getText().toString();
                String time_wait = et_time_wait.getText().toString();

                if(b){// to connect
                    Intent intent = new Intent(getApplicationContext(), RemoteServ.class);
                    intent.putExtra("COUNT",count);
                    intent.putExtra("TIME_WAIT",time_wait);
                    bindService(intent,mConn, getApplicationContext().BIND_AUTO_CREATE);
                    Log.d(" Servicio remoto","init");
                }else{
                    unbindService(mConn);
                    mRemote=false;
                    Log.d(" Servicio remoto","stop");
                }
            }
        });

        but_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mRemote) {
                    Integer aux = remoteServ.getCount();
                    tv.setText(aux.toString());
                }
            }
        });

        but_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mRemote) {
                    Integer aux = Integer.valueOf(et.getText().toString());
                    remoteServ.setCount(aux);
                }
            }
        });

        but_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MyAsyncTask().execute();
            }
        });

        but_thread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String count = et_count.getText().toString();
                String time_wait = et_time_wait.getText().toString();
                MyThread myThread = new MyThread(Integer.valueOf(count),Integer.valueOf(time_wait));
                myThread.setAuto_cerrado(true);
                ProgressBar progressBar = new ProgressBar(getApplicationContext(),null,android.R.attr.progressBarStyleHorizontal);
                layout.addView(progressBar);
                progressBar.setMax(Integer.valueOf(count));
                myThread.setProgressBar(progressBar);
                myThread.start();
            }
        });
    }

    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder bind){
            SimpleBinder sBinder=(SimpleBinder) bind;
            remoteServ=sBinder.getService();
            mRemote = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {

        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        if (mRemote) {
            Log.d("Servicio remoto","stop");
            unbindService(mConn);
            mRemote = false;
        }
        //unregisterReceiver(receiver);
        super.onStop();
    }

    //Clase privada AsyncTask
    private class MyAsyncTask extends AsyncTask<String, Void, String> {
        Integer count;

        @Override
        protected String doInBackground(String... params) {
            count = Integer.valueOf(et_count.getText().toString());
            Integer time_wait = Integer.valueOf(et_time_wait.getText().toString());

            Log.d("Count value:", count.toString());

            while(count>0) {
                try {
                    Thread.sleep(time_wait);
                } catch(InterruptedException e) {}
                count--;
                Log.d("Count value:", count.toString());
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("Async Task ","finalizado");
            tv_result.setText(tv_result.getText().toString()+" "+count.toString());
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
