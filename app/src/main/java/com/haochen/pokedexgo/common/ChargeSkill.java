package com.haochen.pokedexgo.common;

/**
 * Created by Haochen on 2016/8/3.
 */
public class ChargeSkill extends Skill {
    private String skillBar;
    private String damageScalar;
    private String healScalar;
    private String damageWindow;
    private String ct;

    public String getSkillBar() {
        return skillBar;
    }

    public void setSkillBar(String skillBar) {
        this.skillBar = skillBar;
    }

    public String getDamageScalar() {
        return damageScalar;
    }

    public void setDamageScalar(String damageScalar) {
        this.damageScalar = damageScalar;
    }

    public String getHealScalar() {
        return healScalar;
    }

    public void setHealScalar(String healScalar) {
        this.healScalar = healScalar;
    }

    public String getDamageWindow() {
        return damageWindow;
    }

    public void setDamageWindow(String damageWindow) {
        this.damageWindow = damageWindow;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }
}
