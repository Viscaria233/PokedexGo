package com.haochen.pokedexgo.util;

import android.content.res.Resources;

import com.haochen.pokedexgo.R;

import java.util.HashMap;

/**
 * Created by Haochen on 2016/7/21.
 */
public class TypeUtil {

    private static HashMap<String, TypeInfo> map;
    private static int typeNumber = 18;
    static {
        map = new HashMap<>();
        map.put("普", new TypeInfo(0, "type_normal"));
        map.put("火", new TypeInfo(1, "type_fire"));
        map.put("水", new TypeInfo(2, "type_water"));
        map.put("草", new TypeInfo(3, "type_grass"));
        map.put("电", new TypeInfo(4, "type_electric"));
        map.put("冰", new TypeInfo(5, "type_ice"));
        map.put("飞", new TypeInfo(6, "type_flying"));
        map.put("毒", new TypeInfo(7, "type_poison"));
        map.put("斗", new TypeInfo(8, "type_fighting"));
        map.put("恶", new TypeInfo(9, "type_dark"));
        map.put("超", new TypeInfo(10, "type_psychic"));
        map.put("地", new TypeInfo(11, "type_ground"));
        map.put("岩", new TypeInfo(12, "type_rock"));
        map.put("虫", new TypeInfo(13, "type_bug"));
        map.put("鬼", new TypeInfo(14, "type_ghost"));
        map.put("钢", new TypeInfo(15, "type_steel"));
        map.put("龙", new TypeInfo(16, "type_dragon"));
        map.put("妖", new TypeInfo(17, "type_fairy"));
        map.put("仙", new TypeInfo(17, "type_fairy"));
    }
    
    private static class TypeInfo {
        int id;
        String srcName;
        public TypeInfo(int id, String srcName) {
            this.id = id;
            this.srcName = srcName;
        }
    }
    
    public static String toTypeResName(String str) {
        if (map.containsKey(str)) {
            return map.get(str).srcName;
        }
        return "";
    }

    public static String toTypeResName(int id) {
        TypeInfo[] infos = map.values().toArray(new TypeInfo[1]);
        for (TypeInfo info : infos) {
            if (info.id == id) {
                return info.srcName;
            }
        }
        return null;
    }

    public static int toTypeId(String str) {
        if (map.containsKey(str)) {
            return map.get(str).id;
        } else {
            TypeInfo[] infos = map.values().toArray(new TypeInfo[1]);
            for (TypeInfo info : infos) {
                if (info.srcName.equals(str)) {
                    return info.id;
                }
            }
        }
        return 0;
    }

    public static String[] getTypeResNameArray() {
        String[] array = new String[typeNumber];
        for (int i = 0; i < typeNumber; ++i) {
            array[i] = toTypeResName(i);
        }
        return array;
    }
}
