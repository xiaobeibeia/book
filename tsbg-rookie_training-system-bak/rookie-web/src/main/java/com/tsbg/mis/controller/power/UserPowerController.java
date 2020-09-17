package com.tsbg.mis.controller.power;

import com.tsbg.mis.annotation.RequiredPerms;
import com.tsbg.mis.annotation.UserLoginToken;
import com.tsbg.mis.jurisdiction.bag.Key;
import com.tsbg.mis.jurisdiction.vo.RoleAndInfoVo;
import com.tsbg.mis.jurisdiction.vo.UserInfoVo;
import com.tsbg.mis.service.rookie.UserPowerService;
import com.tsbg.mis.util.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 菁干班系统用户权限管理
 *
 * @author 汪永晖
 */
@RestController
@RequestMapping("/userPower")
@Api(value = "菁干班系统用户权限管理", tags = "菁干班系统用户权限管理")
public class UserPowerController {

    @Autowired
    private UserPowerService userPowerService;

    /**
     * 查询菁干班系统所有用户
     *
     * @return
     */
    @ApiOperation(value = "查询菁干班系统所有用户", notes = "菁干班权限管理页面展示的数据", httpMethod = "POST")
    @PostMapping("/inquire")
    @UserLoginToken
    public ResultUtils inquireManager() {
        return userPowerService.inquireManager();
    }

    /**
     * 菁干班系统新增用户——搜索用户
     *
     * @param key 模糊搜索关键字
     * @return
     */
    @ApiOperation(value = "菁干班系统新增用户——搜索用户", notes = "从 user_info 表中搜索用户", httpMethod = "POST")
    @ApiImplicitParam(name = "key", value = "关键值，可以是工号或者姓名的一部分", required = true, dataType = "String")
    @PostMapping("/search/all")
    @UserLoginToken
    public ResultUtils searchAll(@RequestBody Key key) {
        return userPowerService.searchAllUser(key);
    }

    /**
     * 菁干班系统新增用户
     *
     * @param userInfoVo
     * @return
     */
    @ApiOperation(value = "菁干班系统新增用户", notes = "添加菁干班系统的用户", httpMethod = "POST")
    @ApiImplicitParam(name = "userInfoVo", value = "包含 userId, userCode, userName 的对象", required = true, dataType = "UserInfoVo")
    @PostMapping("/add")
    @UserLoginToken
    public ResultUtils addManager(@RequestBody UserInfoVo userInfoVo) {
        return userPowerService.addManager(userInfoVo);
    }

    /**
     * 搜索菁干班系统的用户
     *
     * @param key 模糊搜索关键字
     * @return
     */
    @ApiOperation(value = "搜索菁干班系统的用户", notes = "从 user_role 表中搜索用户", httpMethod = "POST")
    @ApiImplicitParam(name = "key", value = "关键值，可以是工号或者姓名的一部分", required = true, dataType = "String")
    @PostMapping("/search/hrm")
    @UserLoginToken
    public ResultUtils searchHrm(@RequestBody Key key) {
        return userPowerService.searchHrmManager(key);
    }

    /**
     * 查询菁干班系统所有角色信息
     *
     * @return
     */
    @ApiOperation(value = "查询菁干班系统所有角色信息", notes = "当管理员要修改某个用户的权限时调用此方法", httpMethod = "POST")
    @PostMapping("/selectRole")
    @UserLoginToken
    public ResultUtils inquireRole() {
        return userPowerService.inquireRoleList();
    }

    /**
     * 修改菁干班用户角色信息
     *
     * @param roleAndInfoVo
     * @return
     */
    @ApiOperation(value = "修改菁干班用户角色信息", notes = "修改菁干班用户角色信息", httpMethod = "POST")
    @ApiImplicitParam(name = "roleAndInfoVo", value = "修改之后的角色信息", required = true, dataType = "RoleAndInfoVo")
    @PostMapping("/modify/role")
    @UserLoginToken
    public ResultUtils modifyRoleInformation(@RequestBody RoleAndInfoVo roleAndInfoVo) {
        return userPowerService.modifyRoleByUserId(roleAndInfoVo);
    }

    /**
     * 启用菁干班系统用户
     *
     * @return
     */
    @ApiOperation(value = "启用菁干班系统用户", notes = "启用菁干班系统用户", httpMethod = "POST")
    @ApiImplicitParam(name = "id", value = "用户ID", dataType = "Integer", paramType = "path")
    @PostMapping("/enable/{id}")
    @UserLoginToken
    public ResultUtils enable(@PathVariable Integer id) {
        return userPowerService.enableUser(id);
    }

    /**
     * 停用菁干班系统用户
     *
     * @return
     */
    @ApiOperation(value = "停用菁干班系统用户", notes = "停用菁干班系统用户", httpMethod = "POST")
    @ApiImplicitParam(name = "id", value = "用户ID", dataType = "Integer", paramType = "path")
    @PostMapping("/disable/{id}")
    @UserLoginToken
    public ResultUtils disable(@PathVariable Integer id) {
        return userPowerService.disableUser(id);
    }
}

