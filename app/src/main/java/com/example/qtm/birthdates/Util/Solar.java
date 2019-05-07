package com.example.qtm.birthdates.Util;

/**
 * Created by firer on 2017/8/18.
 */
public class Solar {
        public int solarYear;
        public int solarMonth;
        public int solarDay;
        public String toString(){
                if (solarMonth <= 9){
                        if (solarDay <= 9)
                                return solarYear+"-0"+solarMonth+"-0"+solarDay;
                        return solarYear+"-0"+solarMonth+"-"+solarDay;
                }else {
                        if (solarDay <= 9)
                                return solarYear+"-"+solarMonth+"-0"+solarDay;
                        return solarYear+"-"+solarMonth+"-"+solarDay;
                }
        }
}
