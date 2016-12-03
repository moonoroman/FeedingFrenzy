package com.example.roman.feedingfrenzy.Object;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import com.example.roman.feedingfrenzy.Factory.GameObjectFactory;
import com.example.roman.feedingfrenzy.Interface.IMyfish;
import com.example.roman.feedingfrenzy.R;
import com.example.roman.feedingfrenzy.view.MainView;
import com.example.roman.feedingfrenzy.constant.ConstantUtil;

/**
 * Created by Roman on 2016/11/6.
 */
public class Myfish extends GameObject implements IMyfish {
    private float middle_x;			 // 鱼的中心坐标
    private float middle_y;
    public Bitmap myfishpic;			 // 鱼的图片素材
    public int Direction = ConstantUtil.RIGHT;
    private MainView mainView;
    private GameObjectFactory factory;

    public Myfish(Resources resources) {
        super(resources);
        // TODO Auto-generated constructor stub
        initBitmap();
        this.speed = 8;
        factory = new GameObjectFactory();
    }

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    // 设置屏幕宽度和高度
    @Override
    public void setScreenWH(float screen_width, float screen_height) {
        super.setScreenWH(screen_width, screen_height);
        object_x = screen_width/2 - object_width/2;
        object_y = screen_height/2 - object_height/2;
        middle_x = object_x + object_width/2;
        middle_y = object_y + object_height/2;
    }
    // 初始化图片资源的
    @Override
    public void initBitmap() {
        // TODO Auto-generated method stub
        myfishpic = BitmapFactory.decodeResource(resources, R.drawable.mainfish);
        object_width = myfishpic.getWidth(); // 获得每一帧位图的宽
        object_height = myfishpic.getHeight(); 	// 获得每一帧位图的高
    }
    // 对象的绘图方法
    @Override
    public void drawSelf(Canvas canvas) {
        // TODO Auto-generated method stub
        canvas.save();
        canvas.clipRect(object_x, object_y, object_x + object_width, object_y + object_height);
        canvas.drawBitmap(myfishpic, object_x, object_y, paint);
        canvas.restore();
    }
    // 释放资源的方法
    @Override
    public void release() {
        // TODO Auto-generated method stub
        if(!myfishpic.isRecycled()){
            myfishpic.recycle();
        }
    }
    @Override
    public float getMiddle_x() {
        return middle_x;
    }
    @Override
    public void setMiddle_x(float middle_x) {
        this.middle_x = middle_x;
        this.object_x = middle_x - object_width/2;
    }
    @Override
    public float getMiddle_y() {
        return middle_y;
    }
    @Override
    public void setMiddle_y(float middle_y) {
        this.middle_y = middle_y;
        this.object_y = middle_y - object_height/2;
    }

    public Bitmap big(float mul){
        Matrix matrix = new Matrix();
        matrix.postScale(mul,mul);
        object_width = mul*object_width;
        object_height = mul*object_height;
        Bitmap newmyfishpic = Bitmap.createBitmap(myfishpic,0,0,myfishpic.getWidth(),myfishpic.getHeight(),matrix,true);
        if (newmyfishpic.equals(myfishpic)){
            return newmyfishpic;
        }
        myfishpic.recycle();
        return newmyfishpic;
    }
}
