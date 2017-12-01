package com.android.fisewatchlauncher.utils;

import android.text.TextUtils;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.Arrays;
import java.util.List;

/**
 * @author mare
 * @Description:GreenDao保存List<String>类型数据
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/8/11
 * @time 9:52
 */
public class DaoStringConverter implements PropertyConverter<List<String>, String> {
    @Override
    public List<String> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        else {
            List<String> list = Arrays.asList(databaseValue.split(","));
            return list;
        }
    }

    @Override
    public String convertToDatabaseValue(List<String> entityProperty) {
        if(entityProperty==null){
            return null;
        }
        else{
            return TextUtils.join(",",entityProperty);
        }
    }
}
