package com.tsbg.mis.controller.menu;

import com.tsbg.mis.annotation.PassToken;
import com.tsbg.mis.annotation.UserLoginToken;
import com.tsbg.mis.rookie.bag.MenuPackage;
import com.tsbg.mis.rookie.group.Create;
import com.tsbg.mis.rookie.group.Update;
import com.tsbg.mis.service.rookie.MenuService;
import com.tsbg.mis.util.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author 汪永晖
 */
@RestController
@RequestMapping("/menu")
@Validated
@Api(value = "菜单管理", tags = "菜单管理")
@CrossOrigin(value = "*")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @ApiOperation(value = "查询所有菜单列表", notes = "查询所有菜单列表", httpMethod = "GET")
    @GetMapping("/query")
    @UserLoginToken
    public ResultUtils query() {
        return menuService.query();
    }

    @ApiOperation(value = "修改菜单列表", notes = "修改菜单列表", httpMethod = "PUT")
    @PutMapping("/update")
    @UserLoginToken
    public ResultUtils update(@RequestBody @Validated(Update.class) MenuPackage menuPackage) {
        return menuService.update(menuPackage);
    }

    @ApiOperation(value = "新增菜单列表", notes = "新增菜单列表", httpMethod = "POST")
    @PostMapping("/insert")
    @UserLoginToken
    public ResultUtils insert(@RequestBody @Validated(Create.class) MenuPackage menuPackage) {
        return menuService.insert(menuPackage);
    }

    @ApiOperation(value = "删除菜单列表", notes = "删除菜单列表", httpMethod = "DELETE")
    @DeleteMapping("/delete/{menuId}")
    @UserLoginToken
    public ResultUtils delete(@PathVariable Integer menuId) {
        return menuService.delete(menuId);
    }

}
