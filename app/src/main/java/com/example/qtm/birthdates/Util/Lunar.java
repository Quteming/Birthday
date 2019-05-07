package com.example.qtm.birthdates.Util;

/**
 * Created by firer on 2017/8/18.
 */
public class Lunar {
        public int lunarYear;
        public int lunarMonth;
        public int lunarDay;
        public boolean isleap;
        public String toString(){
            if (lunarMonth <= 9){
                if (lunarDay <= 9)
                    return lunarYear+"-0"+lunarMonth+"-0"+lunarDay;
                return lunarYear+"-0"+lunarMonth+"-"+lunarDay;
            }else {
                if (lunarDay <= 9)
                    return lunarYear+"-"+lunarMonth+"-0"+lunarDay;
                return lunarYear+"-"+lunarMonth+"-"+lunarDay;
            }
        }
    }