package com.example.roman.feedingfrenzy.Object;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.roman.feedingfrenzy.R;
import com.example.roman.feedingfrenzy.constant.ConstantUtil;

import java.util.Random;

/**
 * Created by Roman on 2016/11/6.
 */
public class MiddleFish extends EnemyFish{
    private static int left_currentCount = 0;	 //	从左边出现的对象当前的数量
    private static int right_currentCount = 0;	 //	从右边出现的对象当前的数量
    private Bitmap middleFish;// 对象图片
    public static int sumCount = 6;	 	 	 //	对象总的数量
    public MiddleFish(Resources resources) {
        super(resources);
        // TODO Auto-generated constructor stub
        Random ran = new Random();
        setDirection(ran.nextInt(2)+3);
    }
    //初始化数据
    @Override
    public void initial(int arg0,float arg1,float arg2){
        isAlive = true;
        Random ran = new Random();
        speed = ran.nextInt(2) + 6 * arg0;
        if (getDirection()==ConstantUtil.ENEMY_DIR_LEFT){
            object_x = -object_width * (left_currentCount*2 + 1);
            left_currentCount++;
            if(left_currentCount >= sumCount){
                left_currentCount = 0;
            }
        }
        if (getDirection()==ConstantUtil.ENEMY_DIR_RIGHT) {
            if (!isConvert) {
                middleFish = convert(middleFish);
                isConvert = true;
            }
            object_x = screen_width + object_width * (right_currentCount*2);
            right_currentCount++;
            if(right_currentCount >= sumCount){
                right_currentCount = 0;
            }
        }
        object_y = ran.nextInt((int)(screen_height - object_height));
    }
    // 初始化图片资源
    @Override
    public void initBitmap() {
        // TODO Auto-generated method stub
        middleFish = BitmapFactory.decodeResource(resources, R.drawable.middle);
        object_width = middleFish.getWidth();			//获得每一帧位图的宽
        object_height = middleFish.getHeight();		//获得每一帧位图的高
    }
    // 对象的绘图函数
    @Override
    public void drawSelf(Canvas canvas) {
        // TODO Auto-generated method stub
        if(isAlive){
            if(isVisible) {
                canvas.save();
                canvas.clipRect(object_x, object_y, object_x + object_width, object_y + object_height);
                canvas.drawBitmap(middleFish, object_x, object_y, paint);
                canvas.restore();
            }
            logic();
        }
    }
    // 释放资源
    @Override
    public void release() {
        // TODO Auto-generated method stub
        if(!middleFish.isRecycled()){
            middleFish.recycle();
        }
    }
}
