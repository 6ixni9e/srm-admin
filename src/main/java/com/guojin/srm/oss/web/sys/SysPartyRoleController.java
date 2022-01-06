package com.guojin.srm.oss.web.sys;

import javax.validation.Valid;


import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.sys.request.AddSysPartyRoleRequest;
import com.guojin.srm.api.bean.sys.request.QuerySysPartyRoleRequest;
import com.guojin.srm.api.bean.sys.request.UpdateSysPartyRoleRequest;
import com.guojin.srm.api.bean.sys.response.SysPartyRoleResponse;
import com.guojin.srm.api.biz.sys.ISysPartyRoleBiz;
import com.guojin.srm.oss.web.BaseBusinessController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guojin.srm.common.base.IDParamRequest;
import com.guojin.srm.common.base.PageResultVO;
import com.guojin.srm.common.exception.BizException;
import com.guojin.srm.common.utils.ResultDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yuqian
 * @email yuqian@99366.com
 * @date 2021年10月11日 13时37分47秒
 */

@Api(value = "SysPartyRoleController", tags = {"机构默认角色管理"})
@RestController
@RequestMapping("/sys/partyRole")
@Slf4j
public class SysPartyRoleController extends BaseBusinessController {

    @Reference
    private ISysPartyRoleBiz isysPartyRoleBiz;


    @ApiOperation(value = "获取机构角色列表", notes = "获取列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QuerySysPartyRoleRequest")
    @RequiresPermissions("sys:syspartyrole:list")
    @PostMapping("/list")
    public ResultDto<PageResultVO<SysPartyRoleResponse>> list(@Valid @RequestBody QuerySysPartyRoleRequest request) throws BizException {
        //查询列表数据
        PageResultVO<SysPartyRoleResponse> result = isysPartyRoleBiz.queryList(request);
        return success(result);
    }

    @ApiOperation(value = "新增机构角色", notes = "获取新增信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddSysPartyRoleRequest")
    @RequiresPermissions("sys:syspartyrole:save")
    @PostMapping("/save")
    public ResultDto save(@RequestBody @Valid AddSysPartyRoleRequest request) throws BizException {
        isysPartyRoleBiz.save(request);
        return success();
    }

    @ApiOperation(value = "修改机构角色", notes = "修改")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateSysPartyRoleRequest")
    @RequiresPermissions("sys:syspartyrole:update")
    @PostMapping("/update")
    public ResultDto update(@RequestBody @Valid UpdateSysPartyRoleRequest request) throws BizException {
        isysPartyRoleBiz.update(request);
        return success();
    }

    @ApiOperation(value = "删除机构角色", notes = "删除")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("sys:syspartyrole:delete")
    @PostMapping("/delete")
    public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
        isysPartyRoleBiz.deleteById(request.getId());
        return success();
    }
}
