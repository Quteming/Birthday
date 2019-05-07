package com.example.qtm.birthdates.Model.Bean;

import com.github.promeg.pinyinhelper.Pinyin;

public class UserBean {

    private String id;

    /**
     * 对应首字首拼音字母
     */
    private String tag;
    /**
     * 所有字符中的拼音首字母
     */
    private String firstChars;
    /**
     * 对应的所有字母拼音
     */
    private String[] pinyins;

    /**
     * 拼音总长度
     */
    private int pinyinsTotalLength;

    private int sex = 0;//0为男；1为女

    private String name;
    private String birthday;
    private int solarLunar = 0;//0为阳历，1为阴历

    private String nextBirthday;//用于记录下一次的生日年月日，用于标记

    public UserBean() {
    }

    public UserBean(String name) {
        this.name = name;
        nameToPinYin(name);
    }

    public void nameToPinYin(String name){
        String pinYin = Pinyin.toPinyin(name, ";");

        pinyins = pinYin.split(";", -1);

        for (int i = 0; i < pinyins.length; i++){
            pinyinsTotalLength += pinyins[i].length();
        }

        String firstChar = pinyins[0].substring(0, 1);
        int number = (int)firstChar.charAt(0);
        if (number >= 65 && number <= 90) {//大写字母
            tag = firstChar;
        }else if (number >= 97 && number <= 122){//小写字母
            tag = firstChar.toUpperCase();
        }else {
            tag = "#";
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < pinyins.length; i++){
            stringBuilder.append(pinyins[i].substring(0, 1));
        }
        firstChars = stringBuilder.toString();
    }

    public int getSolarLunar() {
        return solarLunar;
    }

    public void setSolarLunar(int solarLunar) {
        this.solarLunar = solarLunar;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
        nameToPinYin(name);
    }

    public String getNextBirthday() {
        return nextBirthday;
    }

    public void setNextBirthday(String nextBirthday) {
        this.nextBirthday = nextBirthday;
    }

    public String getTag() {
        return tag;
    }

    public String getName() {
        return name;
    }

    public String getFirstChars() {
        return firstChars;
    }

    public String[] getPinyins() {
        return pinyins;
    }

    public int getPinyinsTotalLength() {
        return pinyinsTotalLength;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
