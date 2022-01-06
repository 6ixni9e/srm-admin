package com.guojin.srm.oss.web.sys;

import javax.validation.Valid;


import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.sys.request.AddSysMenuRequest;
import com.guojin.srm.api.bean.sys.request.QueryMenuTreeRequest;
import com.guojin.srm.api.bean.sys.request.QuerySysMenuRequest;
import com.guojin.srm.api.bean.sys.request.UpdateSysMenuRequest;
import com.guojin.srm.api.bean.sys.response.MenuVueEleBean;
import com.guojin.srm.api.bean.sys.response.SysMenuResponse;
import com.guojin.srm.api.bean.sys.response.TreeMenuBean;
import com.guojin.srm.api.biz.sys.ISysMenuBiz;
import com.guojin.srm.api.biz.sys.ISysMenuVueEleBiz;
import com.guojin.srm.common.enums.BaseExcCodesEnum;
import com.guojin.srm.common.enums.UserTypeEnum;
import com.guojin.srm.common.session.SessUser;
import com.guojin.srm.oss.properties.SysConfigProperties;
import com.guojin.srm.oss.web.BaseBusinessController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.guojin.srm.common.base.IDParamRequest;
import com.guojin.srm.common.base.PageResultVO;
import com.guojin.srm.common.exception.BizException;
import com.guojin.srm.common.utils.ResultDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuqian
 * @email yuqian@99366.com
 * @date 2021年10月11日 13时37分47秒
 */

@Api(value = "SysMenuController", tags = {"系统菜单管理"})
@RestController
@RequestMapping("/sys/menu")
@Slf4j
public class SysMenuController extends BaseBusinessController {

    @Reference
    private ISysMenuBiz isysMenuBiz;
    @Reference
    private ISysMenuVueEleBiz iSysMenuVueEleBiz;
    @Autowired
    private SysConfigProperties sysConfigProperties;


    @ApiOperation(value = "获取菜单列表信息", notes = "获取列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QuerySysMenuRequest")
    @RequiresPermissions("sys:menu:list")
    @PostMapping("/list")
    public ResultDto<PageResultVO<SysMenuResponse>> list(@Valid @RequestBody QuerySysMenuRequest request) throws BizException {
		SessUser user = getUser();
		if(!user.getUsertype().equals(UserTypeEnum.SUPER_ADMIN.getType())){
			return fail(BaseExcCodesEnum.NO_DATA_RIGHT);
		}
        //查询列表数据
        PageResultVO<SysMenuResponse> result = isysMenuBiz.queryList(request);
        return success(result);
    }


    @ApiOperation(value = "获取菜单详情", notes = "获取详细信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("sys:sysmenu:info")
    @PostMapping("/info")
    public ResultDto<SysMenuResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
        SysMenuResponse sysMenuResponse = isysMenuBiz.querySysMenu(request.getId());
        return success(sysMenuResponse);
    }


    @ApiOperation(value = "获取用户所有菜单树信息", notes = "菜单管理菜单树")
    @RequiresPermissions("sys:menu:treeList")
    @GetMapping("/menuTreeList")
    public ResultDto<List<TreeMenuBean>> menuTreeList() throws BizException {
        List<TreeMenuBean> list = new ArrayList<>();
        QueryMenuTreeRequest request=new QueryMenuTreeRequest();
        request.setHasButton(false);
        //先查询后台菜单
        request.setBelong(0);//后台菜单
        TreeMenuBean ossTreeMenu = isysMenuBiz.queryMenuTree(request,getUser());
        list.add(ossTreeMenu);
        //再查询企业小程序菜单
        request.setBelong(1);//企业小程序菜单
        TreeMenuBean appTreeMenu = isysMenuBiz.queryMenuTree(request,getUser());
        list.add(appTreeMenu);
        return success(list);
    }

    @ApiOperation(value = "获取用户角色树信息", notes = "角色管理菜单树")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryMenuTreeRequest")
    @RequiresPermissions("sys:menu:treeList")
    @PostMapping("/roleTreeList")
    public ResultDto<List<TreeMenuBean>> roleTreeList(@RequestBody QueryMenuTreeRequest request) throws BizException {
        SessUser user=getUser();
        if(!user.getUsertype().equals(UserTypeEnum.SUPER_ADMIN.getType()) && request.getRoleId()==null){
            return fail("角色不能为空");
        }
        List<TreeMenuBean> list=new ArrayList<>();
        //默认先查询后台权限树
        request.setBelong(0);
        request.setHasButton(true);
        TreeMenuBean treeMenuBean=isysMenuBiz.queryMenuTree(request,getUser());
        list.add(treeMenuBean);
        //再查询小程序权限树
        request.setBelong(1);
        TreeMenuBean appMenuBean = isysMenuBiz.queryMenuTree(request,getUser());
        list.add(appMenuBean);
        return success(list);
    }

    @ApiOperation(value = "获取路由信息", notes = "获取路由信息")
    @GetMapping("/routeList")
    public ResultDto<MenuVueEleBean> routeList() throws BizException {
        Integer belong = 0;//默认查询后台菜单树（运营后台）
        MenuVueEleBean menuVueEleBean=iSysMenuVueEleBiz.queryMenuVueTree(getUser(),belong,sysConfigProperties.getSysMenuIds());
        return success(menuVueEleBean);
    }


    @ApiOperation(value = "新增菜单", notes = "获取新增信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddSysMenuRequest")
    @RequiresPermissions("sys:sysmenu:save")
    @PostMapping("/save")
    public ResultDto save(@RequestBody @Valid AddSysMenuRequest request) throws BizException {
        SessUser user=getUser();
        if(!user.getUsertype().equals(UserTypeEnum.SUPER_ADMIN.getType())){
            return fail(BaseExcCodesEnum.NO_DATA_RIGHT);
        }
        isysMenuBiz.save(request);
        return success();
    }

    @ApiOperation(value = "修改菜单", notes = "修改")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateSysMenuRequest")
    @RequiresPermissions("sys:sysmenu:update")
    @PostMapping("/update")
    public ResultDto update(@RequestBody @Valid UpdateSysMenuRequest request) throws BizException {
        SessUser user=getUser();
        if(!user.getUsertype().equals(UserTypeEnum.SUPER_ADMIN.getType())){
            return fail(BaseExcCodesEnum.NO_DATA_RIGHT);
        }
        isysMenuBiz.update(request);
        return success();
    }

    @ApiOperation(value = "删除菜单", notes = "删除")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("sys:sysmenu:delete")
    @PostMapping("/delete")
    public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
        SessUser user=getUser();
        if(!user.getUsertype().equals(UserTypeEnum.SUPER_ADMIN.getType())){
            return fail(BaseExcCodesEnum.NO_DATA_RIGHT);
        }
        isysMenuBiz.deleteById(request.getId());
        return success();
    }
}
