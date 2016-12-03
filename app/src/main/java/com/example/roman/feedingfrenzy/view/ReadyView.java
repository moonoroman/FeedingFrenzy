package com.example.roman.feedingfrenzy.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.example.roman.feedingfrenzy.R;
import com.example.roman.feedingfrenzy.constant.ConstantUtil;

/**
 * Created by Roman on 2016/11/6.
 */
public class ReadyView extends BaseView{
    private Bitmap background;				// 背景图片
    private Bitmap play_button;
    private Bitmap instru_button;
    private float playbutton_x;             // 图片的坐标
    private float playbutton_y;
    private float instrubutton_x;
    private float instrubutton_y;

    public ReadyView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        paint.setTextSize(40);
        thread = new Thread(this);
    }
    // 视图改变的方法
    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
        super.surfaceChanged(arg0, arg1, arg2, arg3);
    }
    // 视图创建的方法
    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        // TODO Auto-generated method stub
        super.surfaceCreated(arg0);
        initBitmap();
        if(thread.isAlive()){
            thread.start();
        }
        else{
            thread = new Thread(this);
            thread.start();
        }
    }
    // 视图销毁的方法
    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        // TODO Auto-generated method stub
        super.surfaceDestroyed(arg0);
        release();
    }
    // 响应触屏事件的方法
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN
                && event.getPointerCount() == 1) {
            float x = event.getX();
            float y = event.getY();
            //判断第一个按钮是否被按下
            if (x > playbutton_x && x < playbutton_x + play_button.getWidth()
                    && y > playbutton_y && y < playbutton_y + play_button.getHeight()) {
                drawSelf();
                mainActivity.getHandler().sendEmptyMessage(ConstantUtil.TO_MAIN_VIEW);
            }
            //判断第二个按钮是否被按下
            else if (x > instrubutton_x && x < instrubutton_x + instru_button.getWidth()
                    && y > instrubutton_y && y < instrubutton_y + instru_button.getHeight()) {
                drawSelf();
                mainActivity.getHandler().sendEmptyMessage(ConstantUtil.END_GAME);
            }
            return true;
        }
        //响应屏幕单点移动的消息
        else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            return true;
        }
        return false;
    }
    // 初始化图片资源方法
    @Override
    public void initBitmap() {
        // TODO Auto-generated method stub
        background = BitmapFactory.decodeResource(getResources(),R.drawable.bg);
        play_button = BitmapFactory.decodeResource(getResources(),R.drawable.play);
        instru_button = BitmapFactory.decodeResource(getResources(),R.drawable.instruction);
        scalex = screen_width / background.getWidth();
        scaley = screen_height / background.getHeight();
        playbutton_x = screen_width / 2 - play_button.getWidth() / 2;
        playbutton_y = screen_height / 2 - play_button.getHeight()/2;
        instrubutton_x = screen_width / 2 - instru_button.getWidth() / 2;
        instrubutton_y = playbutton_y + instru_button.getHeight() + 80;
    }
    // 释放图片资源的方法
    @Override
    public void release() {
        // TODO Auto-generated method stub
        if (!play_button.isRecycled()) {
            play_button.recycle();
        }
        if (!instru_button.isRecycled()) {
            instru_button.recycle();
        }
        if (!background.isRecycled()) {
            background.recycle();
        }
    }
    // 绘图方法
    @Override
    public void drawSelf() {
        // TODO Auto-generated method stub
        try {
            canvas = sfh.lockCanvas();
            canvas.drawColor(Color.BLACK); 						// 绘制背景色
            canvas.save();
            canvas.scale(scalex, scaley, 0, 0);					// 计算背景图片与屏幕的比例
            canvas.drawBitmap(background, 0, 0, paint); 		// 绘制背景图
            canvas.restore();
            //绘制按钮
            canvas.drawBitmap(play_button, playbutton_x, playbutton_y, paint);
            canvas.drawBitmap(instru_button, instrubutton_x, instrubutton_y, paint);
            canvas.restore();
        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            if (canvas != null)
                sfh.unlockCanvasAndPost(canvas);
        }
    }
    // 线程运行的方法
    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (threadFlag) {
            long startTime = System.currentTimeMillis();
            drawSelf();
            long endTime = System.currentTimeMillis();
            try {
                if (endTime - startTime < 400)
                    Thread.sleep(400 - (endTime - startTime));
            } catch (InterruptedException err) {
                err.printStackTrace();
            }
        }
    }
}
