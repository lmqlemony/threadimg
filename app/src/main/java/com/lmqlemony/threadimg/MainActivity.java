package com.lmqlemony.threadimg;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private ImageView img;

    Looper looper=Looper.getMainLooper();//获取主线程的Looper对象
    Looper currentLooper=Looper.myLooper();//获取当前的Looper对象
    //Looer.myQueue();获取消息队列，只有主线程系统才会默认给它创建消息队列和Looper对象
    private Handler handler=new Handler(looper){//将主线程的传入将Handle与looper绑定
        public void handleMessage(Message msg) {//Handle去处理信息，
            if (msg.what==0x1111) {
                img.setImageResource(imageId[(++current)%imageId.length]);//在主线程中更新UI组件
                String str= (String) msg.obj;
            }
        };
    };
    private int imageId[]={R.drawable.pic1,R.drawable.pic2,R.drawable.pic3};
    private int current=0;//保存当前显示的图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img=(ImageView) findViewById(R.id.imageView);
        //换图片
        //启动子线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {//会阻塞主线程
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //利用消息机制更新UI
                    //利用发送空消息的方法，发送一个标识，用于区别是哪个子线程请求主线程去更新UI组件
                    //handler.sendEmptyMessage(0x1111);
                    //handler.obtainMessage(0x1111,"更新消息").sendToTarget();
                    /*携带数据的消息*/
                    Message msg=new Message();//或者Message msg=handle.obtainMessage();
                    msg.what=0x1111;
                    msg.obj="更新消息";
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }
}

