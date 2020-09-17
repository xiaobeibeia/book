package com.tsbg.mis.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public  class ExcelTimeUtils {

    public static String getTimeString(Date time) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(time);
            return dateString;
         }
    public static String getCurrentTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(new Date());
        return dateString;
    }

    public static String getTimehhString(Date time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(time);
        return dateString;
    }

    public static String getDateString() {
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String temp_str=sdf.format(dt);
        return temp_str;
    }

    /*@Test
    public void parseString2Date1()throws Exception {
        //首先这种时间格式应该是美国时间的一种格式，因为这里不写上Locale.US"的话，则会抛ParseException异常
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);

        String cstStr = "Wed, 17 Oct 2018 20:17:40 CST";
        String bstStr = "Wed, 17 Oct 2018 20:17:40 BST";

        System.out.println(sdf.parse(cstStr));
        System.out.println(sdf.parse(bstStr));

        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

        System.out.println(sdf.parse(cstStr));
        System.out.println(sdf.parse(bstStr));
    }*/

}
