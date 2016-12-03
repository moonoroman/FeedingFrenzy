package com.example.roman.feedingfrenzy.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.example.roman.feedingfrenzy.Object.BigFish;
import com.example.roman.feedingfrenzy.Object.EnemyFish;
import com.example.roman.feedingfrenzy.Object.GameObject;
import com.example.roman.feedingfrenzy.Factory.GameObjectFactory;
import com.example.roman.feedingfrenzy.Object.MiddleFish;
import com.example.roman.feedingfrenzy.Object.Myfish;
import com.example.roman.feedingfrenzy.R;
import com.example.roman.feedingfrenzy.Object.SmallFish;
import com.example.roman.feedingfrenzy.constant.ConstantUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roman on 2016/11/6.
 */
public class MainView extends BaseView{
    private int sumScore;			// 游戏总得分
    private int speedTime;			// 游戏速度的倍数
    /*
    private float bg_x;				// 图片的坐标
    private float bg_x2;
    */
    private float play_bt_w;
    private float play_bt_h;
    private boolean isPlay;			// 标记游戏运行状态
    private boolean isTouchPlane;	// 判断玩家是否按下屏幕
    private Bitmap background; 		// 背景图片
    //private Bitmap background2;
    private Myfish myfish;		// 玩家鱼
    private List<EnemyFish> enemyFishlist;
    private GameObjectFactory factory;

    public MainView(Context context) {
        super(context);
            // TODO Auto-generated constructor stub
        isPlay = true;
        speedTime = 1;
        factory = new GameObjectFactory();
        enemyFishlist = new ArrayList<EnemyFish>();
        myfish = (Myfish) factory.createMyFish(getResources());//生产玩家鱼
        myfish.setMainView(this);
        for(int i = 0; i < SmallFish.sumCount; i++){
            //生产小型鱼
            SmallFish smallPlane = (SmallFish) factory.createSmallFish(getResources());
            enemyFishlist.add(smallPlane);
        }
        for(int i = 0; i < MiddleFish.sumCount; i++){
            //生产中型鱼
            MiddleFish middlePlane = (MiddleFish) factory.createMiddleFish(getResources());
            enemyFishlist.add(middlePlane);
        }
        for(int i = 0; i < BigFish.sumCount; i++){
            //生产大型鱼
            BigFish bigPlane = (BigFish) factory.createBigFish(getResources());
            enemyFishlist.add(bigPlane);
        }
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
        initBitmap(); // 初始化图片资源
        for(GameObject obj:enemyFishlist){
            obj.setScreenWH(screen_width,screen_height);
        }
        myfish.setScreenWH(screen_width,screen_height);
        myfish.setAlive(true);
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
        if(event.getAction() == MotionEvent.ACTION_UP){
            isTouchPlane = false;
            return true;
        }
        else if(event.getAction() == MotionEvent.ACTION_DOWN){
            float x = event.getX();
            float y = event.getY();
            if(x > 10 && x < 10 + play_bt_w && y > 10 && y < 10 + play_bt_h){
                if(isPlay){
                    isPlay = false;
                }
                else{
                    isPlay = true;
                    synchronized(thread){
                        thread.notify();
                    }
                }
                return true;
            }
            //判断玩家鱼是否被按下
            else if(x > myfish.getObject_x() && x < myfish.getObject_x() + myfish.getObject_width()
                    && y > myfish.getObject_y() && y < myfish.getObject_y() + myfish.getObject_height()){
                if(isPlay){
                    isTouchPlane = true;
                }
                return true;
            }
        }
        //响应手指在屏幕移动的事件
        else if(event.getAction() == MotionEvent.ACTION_MOVE && event.getPointerCount() == 1){
            //判断触摸点是否为玩家的鱼
            if(isTouchPlane){
                float x = event.getX();
                float y = event.getY();
                if(x > myfish.getMiddle_x() + 20){
                    if(myfish.getMiddle_x() + myfish.getSpeed() <= screen_width){
                        myfish.setMiddle_x(myfish.getMiddle_x() + myfish.getSpeed());
                        if(myfish.Direction == ConstantUtil.LEFT){
                            myfish.myfishpic = myfish.convert(myfish.myfishpic);
                            myfish.Direction = ConstantUtil.RIGHT;
                        }
                    }
                }
                else if(x < myfish.getMiddle_x() - 20){
                    if(myfish.getMiddle_x() - myfish.getSpeed() >= 0){
                        myfish.setMiddle_x(myfish.getMiddle_x() - myfish.getSpeed());
                        if(myfish.Direction == ConstantUtil.RIGHT){
                            myfish.myfishpic = myfish.convert(myfish.myfishpic);
                            myfish.Direction = ConstantUtil.LEFT;
                        }
                    }
                }
                if(y > myfish.getMiddle_y() + 20){
                    if(myfish.getMiddle_y() + myfish.getSpeed() <= screen_height){
                        myfish.setMiddle_y(myfish.getMiddle_y() + myfish.getSpeed());
                    }
                }
                else if(y < myfish.getMiddle_y() - 20){
                    if(myfish.getMiddle_y() - myfish.getSpeed() >= 0){
                        myfish.setMiddle_y(myfish.getMiddle_y() - myfish.getSpeed());
                    }
                }
                return true;
            }
        }
        return false;
    }
    // 初始化图片资源方法
    @Override
    public void initBitmap() {
        // TODO Auto-generated method stub
        background = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        //background2 = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        scalex = screen_width / background.getWidth();
        scaley = screen_height / background.getHeight();
        /*bg_x = 0;
        bg_x2 = bg_x - screen_width;*/
    }
    //初始化游戏对象
    public void initObject(){
        for(EnemyFish obj:enemyFishlist){
            //初始化小型鱼
            if(obj instanceof SmallFish){
                if(!obj.isAlive()){
                    obj.initial(speedTime,0,0);
                    break;
                }
            }
            //初始化中型鱼
            else if(obj instanceof MiddleFish){
                    if(!obj.isAlive()){
                        obj.initial(speedTime,0,0);
                        break;
                    }
            }
            //初始化大型鱼
            else if(obj instanceof BigFish){
                    if(!obj.isAlive()){
                        obj.initial(speedTime,0,0);
                        break;
                    }
            }
        }
        //提升等级
        if(sumScore >= speedTime*100000 && speedTime < 6){
            speedTime++;
        }
    }
    // 释放图片资源的方法
    @Override
    public void release() {
        // TODO Auto-generated method stub
        for(GameObject obj:enemyFishlist){
            obj.release();
        }
        myfish.release();
        if(!background.isRecycled()){
            background.recycle();
        }
        /*if(!background2.isRecycled()){
            background2.recycle();
        }*/
    }
    // 绘图方法
    @Override
    public void drawSelf() {
        // TODO Auto-generated method stub
        try {
            canvas = sfh.lockCanvas();
            canvas.drawColor(Color.BLACK); // 绘制背景色
            canvas.save();
            // 计算背景图片与屏幕的比例
            canvas.scale(scalex, scaley, 0, 0);
            canvas.drawBitmap(background, 0, 0, paint);   // 绘制背景图
            //canvas.drawBitmap(background2, bg_x2, 0, paint); // 绘制背景图
            canvas.restore();
            //绘制敌鱼
            for(EnemyFish obj:enemyFishlist){
                if(obj.isAlive()){
                    obj.drawSelf(canvas);
                    //检测碰撞
                    if(obj.isCanCollide() && myfish.isAlive()){
                        if(obj.isCollide(myfish)&&myfish.getObject_width()<=obj.getObject_width()){//撞上比自己大的鱼
                            myfish.setAlive(false);
                        }else if(obj.isCollide(myfish)&&myfish.getObject_width()>obj.getObject_width()){//撞上比自己小的鱼
                            obj.setAlive(false);
                            float mul = 1;//吃鱼后的放大倍数
                            if(obj instanceof BigFish)
                                mul = 1.012f;
                            else if (obj instanceof MiddleFish)
                                mul = 1.009f;
                            else if (obj instanceof SmallFish)
                                mul = 1.007f;
                            myfish.myfishpic = myfish.big(mul);
                            sumScore++;
                        }
                    }
                }
            }
            if(!myfish.isAlive()){
                threadFlag = false;
            }
            myfish.drawSelf(canvas);	//绘制玩家
            //绘制积分文字
            paint.setTextSize(50);
            paint.setColor(Color.rgb(0, 0, 0));
            canvas.drawText("Score "+String.valueOf(sumScore), screen_width/2-20, 100, paint);		//绘制文字
        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            if (canvas != null)
                sfh.unlockCanvasAndPost(canvas);
        }
    }
    // 增加游戏分数的方法
    public void addGameScore(int score){
        sumScore += score;			// 游戏总得分
    }
    /*
    // 背景移动的逻辑函数
    public void viewLogic(){
        if(bg_x > bg_x2){
            bg_x += 10;
            bg_x2 = bg_x - background.getWidth();
        }
        else{
            bg_x2 += 10;
            bg_x = bg_x2 - background.getWidth();
        }
        if(bg_x >= background.getWidth()){
            bg_x = bg_x2 - background.getWidth();
        }
        else if(bg_x2 >= background.getWidth()){
            bg_x2 = bg_x - background.getWidth();
        }
    }
    */
    // 线程运行的方法
    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (threadFlag) {
            long startTime = System.currentTimeMillis();
            initObject();
            drawSelf();
            //viewLogic();		//背景移动的逻辑
            long endTime = System.currentTimeMillis();
            if(!isPlay){
                synchronized (thread) {
                    try {
                        thread.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                if (endTime - startTime < 100)
                    Thread.sleep(100 - (endTime - startTime));
            } catch (InterruptedException err) {
                err.printStackTrace();
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Message message = new Message();
        message.what = 	ConstantUtil.TO_END_VIEW;
        message.arg1 = Integer.valueOf(sumScore);
        mainActivity.getHandler().sendMessage(message);
    }
}
