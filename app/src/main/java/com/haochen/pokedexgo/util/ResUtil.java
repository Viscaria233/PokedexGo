package com.haochen.pokedexgo.util;

import com.haochen.pokedexgo.R;

import java.lang.reflect.Field;

/**
 * Created by Haochen on 2016/7/20.
 */
public class ResUtil {
    public static int getResId(Class resClass, String rawName) {
        try {
            Field field = resClass.getField(rawName);
            int resId = field.getInt(rawName);
            return resId;
        } catch (NoSuchFieldException e) {
            return 0;
        } catch (IllegalAccessException e) {
            return 0;
        }
    }

}
