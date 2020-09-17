package com.tsbg.mis.serviceImpl.jurisdiction.base;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.tsbg.mis.annotation.SysLog;
import com.tsbg.mis.log.model.SystemLog;
import com.tsbg.mis.dao.jurisdiction.SystemLogDao;
import com.tsbg.mis.util.IPUtils;
import com.tsbg.mis.util.ResultUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;

/**
 * 系统日志，切面处理类
 *
 * @Author: 海波
 * @Date: 2019/12/5 19:37
 */
@Aspect
@Component
public class SystemLogAspect {
    @Autowired
    private SystemLogDao systemLogDao;
    @Autowired
    private TokenAnalysis tokenAnalysis;
    @Value("${version.number}")
    private String versionNumber;

    @Pointcut("@annotation(com.tsbg.mis.annotation.SysLog)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        ResultUtils result = (ResultUtils)point.proceed();

        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;

        //保存日志
        if(result != null){
            saveSystemLog(point, time, result);
        }
        return result;
    }

    private void saveSystemLog(ProceedingJoinPoint joinPoint, long time, ResultUtils result) {
        if(null != joinPoint){
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            SystemLog sysLog = new SystemLog();
            if(signature != null ){
                Method method = signature.getMethod();
                if(method != null){
                    SysLog syslog = method.getAnnotation(SysLog.class);
                    if (syslog != null) {
                        //注解上的描述
                        sysLog.setOperation(syslog.value());
                    }
                }
            }

            //请求的方法名
            if(joinPoint.getTarget() != null){
                String className = joinPoint.getTarget().getClass().getName();
                if(null != signature){
                    String methodName = signature.getName();
                    sysLog.setRequestMethod(className + "." + methodName + "()");
                }
            }

            //返回code&message
            sysLog.setRequestCode(result.getCode());
            sysLog.setRequestMessage(result.getMessage());

            if(result.getData()!=null){
                String s = JSONObject.toJSONString(result.getData().toString().length());
                if(Integer.valueOf(s)>20000){
                    sysLog.setDataValue(JSONObject.toJSONString(result.getData().toString().substring(0,20000)));
                }else {
                    sysLog.setDataValue(JSONObject.toJSONString(result.getData()));
                }
            }

            //请求的参数
            Object[] args = joinPoint.getArgs();
            try {
                if(args != null && args.length<=1){
                    String params = new Gson().toJson(args[0]);
                    if (params.length() > 5000) {
                        //大于5000,不保存
                        params = params.substring(0, 5000);
                    }
                    sysLog.setParams(params);
                }else{
                    ArrayList arr = new ArrayList();
                    if(args != null && args.length>=1){
                        for (Object obj:args) {
                            arr.add(obj.toString());
                        }
                    }
                    sysLog.setParams(arr.toString());
                }
            } catch (Exception e) {

            }

            //获取request
            ServletRequestAttributes attr=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request =attr.getRequest();
            if(request != null){
                //设置IP地址
                sysLog.setRequestIp(IPUtils.getIpAddress(request));
            }
            //从token里取出工号&用户名
            String userCode = tokenAnalysis.getTokenUser().getAccountName();
            String userName = tokenAnalysis.getTokenUser().getUserName();
            sysLog.setRequestUserCode(userCode);
            sysLog.setRequestUserName(userName);

            //记录当前系统版本号
            sysLog.setVersionNumber(versionNumber);

            sysLog.setTime(time);
            sysLog.setCreateTime(new Date());
            //保存系统日志
            systemLogDao.insert(sysLog);
        }
    }

}
