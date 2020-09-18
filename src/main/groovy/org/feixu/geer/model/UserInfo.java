package org.feixu.geer.model;

import org.feixu.geer.enums.SexEnum;

/**
 * 用户信息
 */
public class UserInfo {
    private String name;
    private String birthday;
    private SexEnum sex;
    private String agent;
    private String number;
    private float height;
    private float weight;
    private int smokeLevel;
    private int drinkWineLevel;
    private boolean hypertension;
    private boolean diabetes;

    public String getName() {
        return name;
    }

    public UserInfo setName(String name) {
        this.name = name;
        return this;
    }

    public String getBirthday() {
        return birthday;
    }

    public UserInfo setBirthday(String birthday) {
        this.birthday = birthday;
        return this;
    }

    public SexEnum getSex() {
        return sex;
    }

    public UserInfo setSex(SexEnum sex) {
        this.sex = sex;
        return this;
    }

    public String getAgent() {
        return agent;
    }

    public UserInfo setAgent(String agent) {
        this.agent = agent;
        return this;
    }

    public String getNumber() {
        return number;
    }

    public UserInfo setNumber(String number) {
        this.number = number;
        return this;
    }

    public float getHeight() {
        return height;
    }

    public UserInfo setHeight(float height) {
        this.height = height;
        return this;
    }

    public float getWeight() {
        return weight;
    }

    public UserInfo setWeight(float weight) {
        this.weight = weight;
        return this;
    }

    public int getSmokeLevel() {
        return smokeLevel;
    }

    public UserInfo setSmokeLevel(int smokeLevel) {
        this.smokeLevel = smokeLevel;
        return this;
    }

    public int getDrinkWineLevel() {
        return drinkWineLevel;
    }

    public UserInfo setDrinkWineLevel(int drinkWineLevel) {
        this.drinkWineLevel = drinkWineLevel;
        return this;
    }

    public boolean isHypertension() {
        return hypertension;
    }

    public UserInfo setHypertension(boolean hypertension) {
        this.hypertension = hypertension;
        return this;
    }

    public boolean isDiabetes() {
        return diabetes;
    }

    public UserInfo setDiabetes(boolean diabetes) {
        this.diabetes = diabetes;
        return this;
    }

    // extra

    /**
     * 获取BMI
     * @return
     */
    public String getBMI() {
        return String.format("%.2f", weight/(Math.sqrt(height)));
    }

    /**
     * 获取BMI级别
     * @return
     */
    public String getBMILevel() {
        Float bmi = Float.valueOf(getBMI());
        if (bmi >= 30.0) {
            return "肥胖";
        } else if (bmi >= 25.0) {
            return "过重";
        } else if (bmi >= 20.0) {
            return "正常";
        } else {
            return "体重不足";
        }
    }
}
