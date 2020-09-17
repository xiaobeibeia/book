package com.tsbg.mis.controller.login;

import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.tsbg.mis.annotation.SysLog;
import com.tsbg.mis.annotation.UserLoginToken;
import com.tsbg.mis.jurisdiction.bag.CheckCodePackage;
import com.tsbg.mis.jurisdiction.vo.KaptchaVo;
import com.tsbg.mis.service.jurisdiction.LoginService;
import com.tsbg.mis.util.CommonUtil;
import com.tsbg.mis.util.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/login")
@Api(value = "门户平台-统一登录相关", tags = "门户平台-统一登录相关")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    /**
     * 统一登录
     */
    @ApiOperation(value="统一登录", notes="需要开启redis")
    @ApiImplicitParam(name = "requestJson", value = "传递格式:{\n" +
            "\"accountName\":\"\",\n" +
            "\"userPwd\":\"\"\n" +
            "}", required = true, dataType = "JSONObject")
    @RequestMapping(value="/auth", method=RequestMethod.POST)
    @ResponseBody
    public ResultUtils authLogin(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "accountName,userPwd");
        return loginService.authLogin(requestJson);
    }

    /**
     * 查询当前登录用户的信息
     */
    @ApiOperation(value="获取权限信息", notes="需要开启redis")
    @ApiImplicitParam(name = "userRole", value = "传递参数为：projId", required = true, dataType = "UserRole")
    @RequestMapping(value="/getMyInfo", method=RequestMethod.POST)
    @UserLoginToken
    @ResponseBody
    @SysLog(value = "用户登录")
    public ResultUtils getMyInfo() {
        return loginService.getMyInfo();
    }

    /**
     * 统一登出
     */
    @ApiOperation(value="统一登出", notes="传递token进行注销")
    @RequestMapping(value="/logout", method=RequestMethod.POST)
    @UserLoginToken
    public ResultUtils logout(HttpServletRequest req) {
        return loginService.logout(req);
    }

    /**
     * Google-kaptcha验证码
     */
    @CrossOrigin
    @ApiOperation(value="Google-kaptcha验证码", notes="Google-kaptcha验证码", httpMethod = "GET")
    @RequestMapping(value="/defaultKaptcha", method=RequestMethod.GET)
    public ResultUtils defaultKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        httpServletRequest.setAttribute("Access-Control-Allow-Origin", "http://111.230.250.44" );
        // 生产验证码字符串
        String createText = defaultKaptcha.createText();
        //生成图片
        BufferedImage bufferedImage = defaultKaptcha.createImage(createText);
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStream);
            BASE64Encoder encoder = new BASE64Encoder();
            String base64 = encoder.encode(outputStream.toByteArray());
            String captchaBase64 = "data:image/jpeg;base64," + base64.replaceAll("\r\n", "");
            KaptchaVo kaptchaVo = new KaptchaVo();
            kaptchaVo.setCaptchaBase64(captchaBase64);
            kaptchaVo.setCreateText(createText);
            return new ResultUtils(100, "成功获取图形验证码", kaptchaVo);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResultUtils(501, "获取图形验证码失败");
    }

    /**
     * 发送短信验证码
     *
     * @param staffCode 工号
     * @param phoneNumber 手机号
     * @return
     */
    @ApiOperation(value="发送短信验证码", notes="发送短信验证码", httpMethod = "GET")
    @RequestMapping(value="/sendMessage", method=RequestMethod.GET)
    public ResultUtils sendMessage(@RequestParam String staffCode, @RequestParam String phoneNumber) {
        return loginService.sendMessage(staffCode, phoneNumber);
    }

    /**
     * 判断验证码是否正确，然后修改密码
     *
     * @param checkCodePackage
     * @return
     */
    @ApiOperation(value="判断验证码是否正确，然后修改密码", notes="判断验证码是否正确，然后修改密码", httpMethod = "PUT")
    @RequestMapping(value="/sendMessage", method=RequestMethod.PUT)
    public ResultUtils checkIsCorrectCode(@RequestBody @Validated CheckCodePackage checkCodePackage) {
        return loginService.checkIsCorrectCode(checkCodePackage);
    }

}
