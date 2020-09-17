package com.tsbg.mis.util;

import org.springframework.core.convert.converter.Converter;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDateUtils implements Converter<String,Date> {


    @Override
    public Date convert(String inputDate) {
        String[] possiblePatterns =
                {
                        "yyyy-MM-dd",
                        "yyyy-MM-dd HH:mm:ss",
                        "yyyyMMdd",
                        "yyyyMMdd HH:mm:ss",
                        "yyyy/MM/dd",
                        "yyyy/MM/dd HH:mm:ss",
                        "yyyy年MM月dd日",
                        "yyyy年MM月dd日 HH:mm:ss",
                        "yyyy MM dd",
                        "yyyy MM dd HH:mm:ss"
                };

        SimpleDateFormat df = new SimpleDateFormat();
        for(String pattern:possiblePatterns){
            df.applyPattern(pattern);
            df.setLenient(false);//设置解析日期格式是否严格解析日期
            ParsePosition pos = new ParsePosition(0);
            Date date = df.parse(inputDate, pos);
            if(date!=null){
                return date;
            }
        }
        return null;
    }
}
