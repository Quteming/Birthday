package com.example.qtm.birthdates.Util;

import com.example.qtm.birthdates.Model.Bean.UserBean;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatcherUtil {

    private MatcherUtil() {
    }

    public static List<UserBean> matchers(List<UserBean> userBeanList, String search){
        List<UserBean> mSearchList = new ArrayList<>();
        for (UserBean userBean : userBeanList){
            UserBean matcher = matcher(userBean, search);
            if (matcher != null){
                mSearchList.add(userBean);
            }
        }
        return mSearchList;
    }

    public static UserBean matcher(UserBean userBean, String search){
        UserBean user = matcherChinese(userBean, search);
        if (user != null){
            return user;
        }
        user = matcherFirstChars(userBean, search);
        if (user != null){
            return user;
        }
        user = matchersAllPinyins(userBean, search);
        if (user != null){
            return user;
        }
        return null;
    }

    public static UserBean matcherChinese(UserBean userBean, String search) {
        if (search.length() <=  userBean.getName().length()) {
            Matcher matcher = Pattern.compile(search, Pattern.CASE_INSENSITIVE).matcher(userBean.getName());
            if (matcher.find()) {
                return userBean;
            }
        }
        return null;
    }

    public static UserBean matcherFirstChars(UserBean userBean, String search) {
        if (search.length() <= userBean.getPinyins().length) {
            Matcher matcher = Pattern.compile(search, Pattern.CASE_INSENSITIVE).matcher(userBean.getFirstChars());
            if (matcher.find()) {
                return userBean;
            }
        }
        return null;
    }

    public static UserBean matchersAllPinyins(UserBean userBean, String search) {
        if (search.length() > userBean.getPinyinsTotalLength())
            return null;
        boolean exist = false;
        for (int i = 0; i < userBean.getPinyins().length; i++) {
            String pat = userBean.getPinyins()[i];
            if (pat.length() >= search.length()) {//首个位置索引
                Matcher matcher = Pattern.compile(search, Pattern.CASE_INSENSITIVE).matcher(pat);
                exist = matcher.find() && matcher.start() == 0 ? true : false;
                if (exist) {
                    break;
                }
            } else {
                Matcher matcher = Pattern.compile(pat, Pattern.CASE_INSENSITIVE).matcher(search);
                if (matcher.find() && matcher.start() == 0) {//全拼匹配第一个必须在0位置
                    //替换第一个查询到的字符串
                    String left = matcher.replaceFirst("");
                    //如果为“”说明匹配完毕
                    exist = left.isEmpty() ? true : end(userBean.getPinyins(), left, ++i);
                    if (exist){
                        break;
                    }
                }
            }
        }
        if (exist) {
            return userBean;
        }
        return null;
    }

    /**
     * 根据匹配字符递归查找下一结束位置
     * @param pinyinGroup
     * @param search
     * @param index
     * @return -1 匹配失败
     */
    private static boolean end(String[] pinyinGroup, String search, int index) {
        if (index < pinyinGroup.length) {
            String pinyin = pinyinGroup[index];
            if (pinyin.length() >= search.length()) {//首个位置索引;单个汉字拼音>= 需匹配的字符串
                Matcher matcher = Pattern.compile(search, Pattern.CASE_INSENSITIVE).matcher(pinyin);
                if (matcher.find() && matcher.start() == 0) {
                    return true;
                }
            } else {
                Matcher matcher = Pattern.compile(pinyin, Pattern.CASE_INSENSITIVE).matcher(search);
                if (matcher.find() && matcher.start() == 0) {//全拼匹配第一个必须在0位置；单个汉字拼音 <= 需匹配的字符串;需要对剩下未匹配的递归匹配
                    String left = matcher.replaceFirst("");
                    return end(pinyinGroup, left, index + 1);
                }
            }
        }
        return false;
    }


}
