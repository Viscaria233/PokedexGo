package com.haochen.pokedexgo.common;

import com.haochen.pokedexgo.R;
import com.haochen.pokedexgo.util.ResUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Haochen on 2016/8/3.
 */
public class Pokemon implements Serializable {
    private String id;
    private HashMap<Locale, String> nameMap = new HashMap<>();
    private List<String> types = new ArrayList<>();
    private String hp;
    private String atk;
    private String def;
    private String sum;
    private String catchPercent;
    private String runPercent;
    private String incubate;
    private String candy;

    public String getId() {
        return id;
    }

    public String getName() {
        return nameMap.get(Config.locale);
    }

    public String getName(Locale locale) {
        return nameMap.get(locale);
    }

    public int getIconResId() {
        return ResUtil.getResId(R.raw.class, "pm_icon_" + id);
    }

    public int getImageResId() {
        return ResUtil.getResId(R.raw.class, "pm_image_" + id);
    }

    public int getTypeSize() {
        return types.size();
    }

    public String getTypeName(int index) {
        if (index < 0 || index >= types.size()) {
            return null;
        }
        return types.get(index);
    }

    public int getTypeResId(int index) {
        if (index < 0 || index >= types.size()) {
            return 0;
        }
        return ResUtil.getResId(R.string.class, types.get(index));
    }

    public int getTypeFieldResId(int index) {
        if (index < 0 || index >= types.size()) {
            return 0;
        }
        return ResUtil.getResId(R.drawable.class, types.get(index) + "_field");
    }

    public String getHp() {
        return hp;
    }

    public String getAtk() {
        return atk;
    }

    public String getDef() {
        return def;
    }

    public String getSum() {
        return sum;
    }

    public String getCatchPercent() {
        return catchPercent;
    }

    public String getRunPercent() {
        return runPercent;
    }

    public String getIncubate() {
        return incubate;
    }

    public String getCandy() {
        return candy;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addName(Locale locale, String name) {
        nameMap.put(locale, name);
    }

    public void addType(String type) {
        this.types.add(type);
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public void setAtk(String atk) {
        this.atk = atk;
    }

    public void setDef(String def) {
        this.def = def;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public void setCatchPercent(String catchPercent) {
        this.catchPercent = catchPercent;
    }

    public void setRunPercent(String runPercent) {
        this.runPercent = runPercent;
    }

    public void setIncubate(String incubate) {
        this.incubate = incubate;
    }

    public void setCandy(String candy) {
        this.candy = candy;
    }
}
