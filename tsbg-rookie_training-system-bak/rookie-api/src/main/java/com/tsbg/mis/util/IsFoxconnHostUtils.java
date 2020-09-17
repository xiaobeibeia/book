package com.tsbg.mis.util;

/**
 * @PackgeName: com.tsbg.mis.utils
 * @ClassName: IsFoxconnHostUtils
 * @Author: Nico
 * Date: 2019/12/18 0018 上午 10:13
 * Description:
 */
public class IsFoxconnHostUtils {
    //本地主机IP
    public static String localHostIP = "";

    //判断是否为富士康SMTP发信主机，若不是，则用QQ邮箱测试
    public static Boolean isFoxconnhost=false;

    public static boolean judge(){
        try {
            localHostIP = IPUtils.getLocalHostIP();
            if(localHostIP.equals("127.0.0.1")){
                isFoxconnhost = true;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return isFoxconnhost;
    }
}
