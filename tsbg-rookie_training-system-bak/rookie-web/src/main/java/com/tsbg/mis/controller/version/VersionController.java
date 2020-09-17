package com.tsbg.mis.controller.version;

import com.tsbg.mis.annotation.PassToken;
import com.tsbg.mis.util.ResultUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 汪永晖
 */
@RestController
@RequestMapping("/version")
@Api(value = "获取系统版本号", tags = "获取系统版本号")
public class VersionController {

    @Value("${version.number}")
    private String version;

    @GetMapping("/query")
    @PassToken
    public ResultUtils queryVersion() {
        return new ResultUtils(100, "成功获取系统版本号", version);
    }

}
