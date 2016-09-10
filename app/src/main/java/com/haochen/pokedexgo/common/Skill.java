package com.haochen.pokedexgo.common;

import com.haochen.pokedexgo.R;
import com.haochen.pokedexgo.util.ResUtil;
import com.haochen.pokedexgo.util.TypeUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Haochen on 2016/8/3.
 */
public abstract class Skill implements Serializable {
    protected String id;
    private HashMap<Locale, String> nameMap = new HashMap<>();
    protected String typeResName;
    protected String power;
    protected String duration;

    public String getId() {
        return id;
    }

    public String getName() {
        return nameMap.get(Config.locale);
    }

    public String getName(Locale locale) {
        return nameMap.get(locale);
    }

    public int getTypeFieldResId() {
        return ResUtil.getResId(R.drawable.class, typeResName + "_field");
    }

    public int getTypeNameResId() {
        return ResUtil.getResId(R.string.class, typeResName);
    }

    public int getTypeId() {
        return TypeUtil.toTypeId(typeResName);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addName(Locale locale, String name) {
        nameMap.put(locale, name);
    }

    public String getTypeResName() {
        return typeResName;
    }

    public void setTypeResName(String typeResName) {
        this.typeResName = typeResName;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
