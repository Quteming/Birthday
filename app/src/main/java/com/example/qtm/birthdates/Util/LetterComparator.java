package com.example.qtm.birthdates.Util;

import com.example.qtm.birthdates.Model.Bean.UserBean;

import java.util.Comparator;

public class LetterComparator implements Comparator<UserBean> {
    @Override
    public int compare(UserBean o1, UserBean o2) {
        if (o2.getTag().equals("#")){
            return -1;
        }else if (o1.getTag().equals("#")){
            return 1;
        }else {
            return o1.getTag().compareTo(o2.getTag());
        }
    }
}
