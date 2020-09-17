package com.tsbg.mis.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class SendMailUtils {
    //發件人郵箱
    public static String myEmailAccount = "fii-mis-system@mail.foxconn.com";
    //public static String myEmailAccount = "850085812@qq.com";
    //nico.x.xu@mail.foxconn.com

    //發件人郵箱SMTP授權碼
    public static String myEmailPassword = "stutcychjdtxbefg";

    //發件人郵箱服務器
    // QQ郵箱的SMTP服務器地址為: smtp.qq.com
    public static String myEmailSMTPHost = "10.134.28.139";

    //收件人郵箱
    public static String receiveMailAccount = "";

//    public static void main(String[] args) throws Exception {
//        // 1. 创建参数配置, 用于连接邮件服务器的参数配置
//        Properties props = new Properties();                    // 参数配置
//        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
//        props.setProperty("mail.smtp.host", myEmailSMTPHost);   // 发件人的邮箱的 SMTP 服务器地址
//        props.setProperty("mail.smtp.auth", "true");            // 需要请求认证
//
//        // PS: 某些邮箱服务器要求 SMTP 连接需要使用 SSL 安全认证 (为了提高安全性, 邮箱支持SSL连接, 也可以自己开启),
//        //     如果无法连接邮件服务器, 仔细查看控制台打印的 log, 如果有有类似 “连接失败, 要求 SSL 安全连接” 等错误,
//        //     取消下面 /* ... */ 之间的注释代码, 开启 SSL 安全连接。
//        /*
//        // SMTP 服务器的端口 (非 SSL 连接的端口一般默认为 25, 可以不添加, 如果开启了 SSL 连接,
//        //                  需要改为对应邮箱的 SMTP 服务器的端口, 具体可查看对应邮箱服务的帮助,
//        //                  QQ邮箱的SMTP(SLL)端口为465或587, 其他邮箱自行去查看)
//        final String smtpPort = "465";
//        props.setProperty("mail.smtp.port", smtpPort);
//        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//        props.setProperty("mail.smtp.socketFactory.fallback", "false");
//        props.setProperty("mail.smtp.socketFactory.port", smtpPort);
//        */
//
//        // 2. 根据配置创建会话对象, 用于和邮件服务器交互
//        Session session = Session.getInstance(props);
//        // 设置为debug模式, 可以查看详细的发送 log
//        session.setDebug(true);
//
//        // 3. 创建一封邮件
//        MimeMessage message = createMimeMessage(session, myEmailAccount, receiveMailAccount);
//
//        // 4. 根据 Session 获取邮件传输对象
//        Transport transport = session.getTransport();
//
//        // 5. 使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则报错
//        //
//        //    PS_01: 如果连接服务器失败, 都会在控制台输出相应失败原因的log。
//        //    仔细查看失败原因, 有些邮箱服务器会返回错误码或查看错误类型的链接,
//        //    根据给出的错误类型到对应邮件服务器的帮助网站上查看具体失败原因。
//        //
//        //    PS_02: 连接失败的原因通常为以下几点, 仔细检查代码:
//        //           (1) 邮箱没有开启 SMTP 服务;
//        //           (2) 邮箱密码错误, 例如某些邮箱开启了独立密码;
//        //           (3) 邮箱服务器要求必须要使用 SSL 安全连接;
//        //           (4) 请求过于频繁或其他原因, 被邮件服务器拒绝服务;
//        //           (5) 如果以上几点都确定无误, 到邮件服务器网站查找帮助。
//        //
//        transport.connect(myEmailAccount, myEmailPassword);
//
//        // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
//        transport.sendMessage(message, message.getAllRecipients());
//
//        // 7. 关闭连接
//        transport.close();
//    }

    /**
     * 創建一封包含Html格式的郵件
     *
     * @param session     與服務器交互的會話
     * @param sendMail    發件人郵箱
     * @param //mail        收件人郵箱
     * @return
     * @throws Exception
     */
    public static MimeMessage createMimeMessage(String username,Session session, String sendMail, String receiveMail,String mailContent,String subject) throws Exception {
        // 1. 創建郵件
        MimeMessage message = new MimeMessage(session);

        // 2. From: 發件人（自定義名稱）

        message.setFrom(new InternetAddress(sendMail, "FII-MIS-SYSTEM@mail.foxconn.com", "UTF-8"));

        // 3. To: 收件人（可以增加多个收件人、抄送、密送）
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, username, "UTF-8"));
        //message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress("chenq0711@sina.com", username, "UTF-8"));
        //message.setRecipient(MimeMessage.RecipientType.BCC, new InternetAddress("elvis.q.chen@mail.foxconn.com", username, "UTF-8"));
        // 4. Subject: 郵件主題
        message.setSubject(subject, "UTF-8");


        // 5. Content: 郵件正文

        message.setContent(mailContent, "text/html;charset=UTF-8");
        // 6. 设置发件时间
        message.setSentDate(new Date());

        // 7. 保存设置
        message.saveChanges();

        return message;
    }

    public static String getVerifyCode(String key){
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(key);
        String code=m.replaceAll("").trim().substring(0,6);
        return code;
    }


    @RequestMapping("/modifyPwd")
    public static JSONObject send(String username, String receiveMailAccount,String subject,String mailContent) throws Exception {

        Boolean sendStatus;
        JSONObject jsonObject = new JSONObject();
        //获取邮箱

        // 1. 创建参数配置, 用于连接邮件服务器的参数配置
        Properties props = new Properties();                    // 参数配置
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", myEmailSMTPHost);   // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "false");            // 需要请求认证
        try {
            // PS: 某些邮箱服务器要求 SMTP 连接需要使用 SSL 安全认证 (为了提高安全性, 邮箱支持SSL连接, 也可以自己开启),
            //     如果无法连接邮件服务器, 仔细查看控制台打印的 log, 如果有有类似 “连接失败, 要求 SSL 安全连接” 等错误,
            //     取消下面 /* ... */ 之间的注释代码, 开启 SSL 安全连接。
            // SMTP 服务器的端口 (非 SSL 连接的端口一般默认为 25, 可以不添加, 如果开启了 SSL 连接,
            //                  需要改为对应邮箱的 SMTP 服务器的端口, 具体可查看对应邮箱服务的帮助,
            //                  QQ邮箱的SMTP(SLL)端口为465或587, 其他邮箱自行去查看)
            final String smtpPort = "25";
            props.setProperty("mail.smtp.port", smtpPort);
            props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.socketFactory.fallback", "true");
            props.setProperty("mail.smtp.socketFactory.port", smtpPort);

            // 2. 根据配置创建会话对象, 用于和邮件服务器交互
            Session session = Session.getInstance(props);
            // 设置为debug模式, 可以查看详细的发送 log
            session.setDebug(true);

            // 3. 创建一封邮件
            //MimeMessage message = createMimeMessage(session, myEmailAccount, receiveMailAccount);

            MimeMessage message = createMimeMessage(username,session, myEmailAccount,receiveMailAccount,mailContent,subject);
            // 4. 根据 Session 获取邮件传输对象
            Transport transport = session.getTransport();

            // 5. 使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则报错
            //
            //    PS_01: 如果连接服务器失败, 都会在控制台输出相应失败原因的log。
            //    仔细查看失败原因, 有些邮箱服务器会返回错误码或查看错误类型的链接,
            //    根据给出的错误类型到对应邮件服务器的帮助网站上查看具体失败原因。
            //
            //    PS_02: 连接失败的原因通常为以下几点, 仔细检查代码:
            //           (1) 邮箱没有开启 SMTP 服务;
            //           (2) 邮箱密码错误, 例如某些邮箱开启了独立密码;
            //           (3) 邮箱服务器要求必须要使用 SSL 安全连接;
            //           (4) 请求过于频繁或其他原因, 被邮件服务器拒绝服务;
            //           (5) 如果以上几点都确定无误, 到邮件服务器网站查找帮助。
            //
            transport.connect(myEmailAccount, "");

            // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
            transport.sendMessage(message, message.getAllRecipients());
            // 7. 關閉連接
            transport.close();
            sendStatus=true;
        }catch(AddressException e){
            sendStatus = false;
            e.printStackTrace();
        }catch(MessagingException e){
            sendStatus = false;
            e.printStackTrace();
        }
        if(sendStatus){

            jsonObject.put("send", myEmailAccount);
            jsonObject.put("to", receiveMailAccount);

            jsonObject.put("status", "success");
        }else{

            jsonObject.put("send", myEmailAccount);
            jsonObject.put("to", receiveMailAccount);

            jsonObject.put("status", "failed");
        }

        return jsonObject;
    }
}