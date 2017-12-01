package com.android.fisewatchlauncher.function.location;

import android.widget.Toast;

import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.android.fisewatchlauncher.KApplication;
import com.android.fisewatchlauncher.utils.LogUtils;
import com.android.fisewatchlauncher.utils.ToastUtils;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/10/16
 * @time 20:29
 */
public class CoordinateUtils {

    public static DPoint convert(CoordinateConverter.CoordType from,
                                 CoordinateConverter.CoordType target, DPoint src) {
        DPoint destPoint = null;
        try {
            CoordinateConverter converter = new CoordinateConverter(KApplication.sContext);
            /**
             * 设置坐标来源,这里使用百度坐标作为示例
             * 可选的来源包括：
             * <li>CoordType.BAIDU ： 百度坐标
             * <li>CoordType.MAPBAR ： 图吧坐标
             * <li>CoordType.MAPABC ： 图盟坐标
             * <li>CoordType.SOSOMAP ： 搜搜坐标
             * <li>CoordType.ALIYUN ： 阿里云坐标
             * <li>CoordType.GOOGLE ： 谷歌坐标
             * <li>CoordType.GPS ： GPS坐标
             */
            converter.from(CoordinateConverter.CoordType.BAIDU);
            converter.coord(src);  //设置需要转换的坐标
            destPoint = converter.convert(); //转换成高德坐标
            if (null != destPoint) {
            } else {
                ToastUtils.showShortSafe("坐标转换失败", Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ToastUtils.showShortSafe("坐标转换失败", Toast.LENGTH_SHORT);
            e.printStackTrace();
        } finally {
            return destPoint;
        }
    }

    //判断坐标是否高德地图可用
    public static boolean isAvaliable(DPoint src) {
        //初始化坐标工具类
        CoordinateConverter converter = new CoordinateConverter(KApplication.sContext);
        //判断是否高德地图可用的坐标
        boolean result = converter.isAMapDataAvailable(src.getLatitude(), src.getLongitude());
        if (result) {
            LogUtils.i("该坐标是高德地图可用坐标");
        } else {
            LogUtils.i("该坐标不能用于高德地图");
        }
        return result;
    }
}
