package com.example.roman.feedingfrenzy.Factory;

import android.content.res.Resources;

import com.example.roman.feedingfrenzy.Object.BigFish;
import com.example.roman.feedingfrenzy.Object.GameObject;
import com.example.roman.feedingfrenzy.Object.MiddleFish;
import com.example.roman.feedingfrenzy.Object.Myfish;
import com.example.roman.feedingfrenzy.Object.SmallFish;

/**
 * Created by Roman on 2016/11/6.
 */
public class GameObjectFactory {
    //创建小型敌鱼的方法
    public GameObject createSmallFish(Resources resources){
        return new SmallFish(resources);
    }
    //创建中型敌鱼的方法
    public GameObject createMiddleFish(Resources resources){
        return new MiddleFish(resources);
    }
    //创建大型敌鱼的方法
    public GameObject createBigFish(Resources resources){
        return new BigFish(resources);
    }
    //创建玩家鱼的方法
    public GameObject createMyFish(Resources resources){
        return new Myfish(resources);
    }
}
