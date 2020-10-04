package org.feixu.geer.model;

import org.feixu.geer.enums.SexEnum;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户信息
 */
public class UserInfo {
    private String name;
    private String birthday;
    private SexEnum sex;
    private String agent;
    private String number;
    private Double height;
    private Double weight;
    private String smokeLevel = "无";
    private String drinkWineLevel = "无";
    private String hypertension = "无";
    private String diabetes = "无";

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

    public Double getHeight() {
        return height;
    }

    public UserInfo setHeight(Double height) {
        this.height = height;
        return this;
    }

    public Double getWeight() {
        return weight;
    }

    public UserInfo setWeight(Double weight) {
        this.weight = weight;
        return this;
    }

    public String getSmokeLevel() {
        return smokeLevel;
    }

    public UserInfo setSmokeLevel(String smokeLevel) {
        this.smokeLevel = smokeLevel;
        return this;
    }

    public String getDrinkWineLevel() {
        return drinkWineLevel;
    }

    public UserInfo setDrinkWineLevel(String drinkWineLevel) {
        this.drinkWineLevel = drinkWineLevel;
        return this;
    }

    public String getHypertension() {
        return hypertension;
    }

    public void setHypertension(String hypertension) {
        this.hypertension = hypertension;
    }

    public String getDiabetes() {
        return diabetes;
    }

    public void setDiabetes(String diabetes) {
        this.diabetes = diabetes;
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

    public int getAge() {
        LocalDate birthday = LocalDate.parse(this.getBirthday());
        return (int)(Duration.between(birthday.atStartOfDay(), LocalDateTime.now()).toDays()/365.25);
    }
}
