package com.guojin.srm.oss.web.sys;

import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.sys.request.AddSysRoleRequest;
import com.guojin.srm.api.bean.sys.request.QuerySysRoleRequest;
import com.guojin.srm.api.bean.sys.request.UpdateSysRoleRequest;
import com.guojin.srm.api.bean.sys.response.SysRoleResponse;
import com.guojin.srm.api.bean.sys.response.UserBean;
import com.guojin.srm.api.biz.sys.ISysPartyRoleBiz;
import com.guojin.srm.api.biz.sys.ISysRoleBiz;
import com.guojin.srm.common.base.IDParamRequest;
import com.guojin.srm.common.base.PageResultVO;
import com.guojin.srm.common.enums.CommonStatusEnum;
import com.guojin.srm.common.enums.RoleTypeEnum;
import com.guojin.srm.common.exception.BizException;
import com.guojin.srm.common.session.SessParty;
import com.guojin.srm.common.utils.BeanUtil;
import com.guojin.srm.common.utils.ResultDto;
import com.guojin.srm.oss.web.BaseBusinessController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author yuqian
 * @email yuqian@99366.com
 * @date 2021年10月11日 13时37分47秒
 */

@Api(value = "SysRoleController", tags = {"系统角色管理"})
@RestController
@RequestMapping("/sys/role")
@Slf4j
public class SysRoleController extends BaseBusinessController {

    @Reference
    private ISysRoleBiz isysRoleBiz;
    @Reference
    private ISysPartyRoleBiz iSysPartyRoleBiz;


    @ApiOperation(value = "获取角色列表信息", notes = "获取列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QuerySysRoleRequest")
    @RequiresPermissions("sys:sysrole:list")
    @PostMapping("/list")
    public ResultDto<PageResultVO<SysRoleResponse>> list(@Valid @RequestBody QuerySysRoleRequest request) throws BizException {
        // 查询列表数据
        request.setUserId(getUserId());
        SessParty sessParty = getParty();
        if (null != sessParty) {
            request.setPartyId(sessParty.getId());
        }
        //查询列表数据
        PageResultVO<SysRoleResponse> result = isysRoleBiz.queryList(request);
        return success(result);
    }


    @ApiOperation(value = "查看角色详情", notes = "获取详细信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("sys:sysrole:info")
    @PostMapping("/info")
    public ResultDto<SysRoleResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
        SysRoleResponse sysRoleResponse = isysRoleBiz.queryRoleAndMenuList(request.getId(), getUser());
        return success(sysRoleResponse);
    }


    @ApiOperation(value = "获取用户角色信息(自己创建的+机构类型对应的角色)", notes = "获取用户可管理的角色(自己创建的+机构类型对应的角色)")
    @GetMapping(value = "/getRoleByUser")
    @RequiresAuthentication
    public ResultDto<List<SysRoleResponse>> getRoleByUser() throws BizException {
        //查询自己创建的角色
        List<SysRoleResponse> list = isysRoleBiz.queryRoleListByCreateUser(getUser(), getParty());
        //查询对应机构类型的角色
        List<SysRoleResponse> list2 = iSysPartyRoleBiz.queryRoleListByPartyType(getParty());
        list2.forEach(item ->{
            if(!list.contains(item)){
                list.add(item);
            }
        });
//        list.addAll(list2);
        return success(list);
    }

    @ApiOperation(value = "获取系统角色信息", notes = "获取用户创建的角色信息")
    @GetMapping(value = "/getSysRole")
    @RequiresAuthentication
    public ResultDto<List<SysRoleResponse>> getSysRole() throws BizException {
        List<SysRoleResponse> roleBeanList = isysRoleBiz.queryRoleListBySys(CommonStatusEnum.VALID.getValue(), RoleTypeEnum.SYSTEM_ADMIN.getType());
        return success(roleBeanList);
    }

    /**
     * 查询当前用户所属角色列表,是超级管理员返回空
     */
    @ApiOperation(value = "获取用户所属的角色信息", notes = "获取用户所属的角色信息")
    @GetMapping(value = "/getUserRole")
    @RequiresAuthentication
    public ResultDto getUserRole() throws BizException {
        UserBean userBean = new UserBean();
        BeanUtil.copyProperties(getUser(), userBean);
        List<SysRoleResponse> list = isysRoleBiz.getUserPartyRole(userBean);
        return success(list);
    }

    @ApiOperation(value = "新增角色", notes = "获取新增信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddSysRoleRequest")
    @RequiresPermissions("sys:sysrole:save")
    @PostMapping("/save")
    public ResultDto save(@RequestBody @Valid AddSysRoleRequest request) throws BizException {
        isysRoleBiz.save(request,getUser());
        return success();
    }

    @ApiOperation(value = "修改角色", notes = "修改")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateSysRoleRequest")
    @RequiresPermissions("sys:sysrole:update")
    @PostMapping("/update")
    public ResultDto update(@RequestBody @Valid UpdateSysRoleRequest request) throws BizException {
        isysRoleBiz.update(request,getUser());
        return success();
    }

    @ApiOperation(value = "删除角色", notes = "删除")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("sys:sysrole:delete")
    @PostMapping("/delete")
    public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
        isysRoleBiz.deleteRole(request.getId(),getUser());
        return success();
    }
}
