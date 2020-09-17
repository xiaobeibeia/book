package com.tsbg.mis.serviceImpl.baseUtils;


import com.efoxconn.sms.SendSMSServiceSoapProxy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SmsUtil {

    /**
     * 根据短信模板发送
     *
     */
    public static void SendByTemplate(String phone,int formatID,int spaceNum,String content){
        String userName = "F1336911";//短信帳號
        String password = "F1336911";//帳號密碼
        log.info("phone -> {}, formatID -> {}, spaceNum -> {}, content -> {}", phone, formatID, spaceNum, content);
        try
        {
            SendSMSServiceSoapProxy Sp = new SendSMSServiceSoapProxy();
            String formatResult = Sp.sendFormatSMS(userName, password, phone, formatID, spaceNum, content);//發送固定短信
            log.info("SendFormatSMSResult: -> {}", formatResult);
        }
        catch(Exception e)
        {
            log.error("发送短信出现异常: -> {}", e);
        }
    }
}
