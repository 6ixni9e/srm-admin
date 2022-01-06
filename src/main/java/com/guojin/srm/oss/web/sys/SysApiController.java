package com.guojin.srm.oss.web.sys;

import javax.validation.Valid;


import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.sys.request.AddSysApiRequest;
import com.guojin.srm.api.bean.sys.request.QuerySysApiRequest;
import com.guojin.srm.api.bean.sys.request.UpdateSysApiRequest;
import com.guojin.srm.api.bean.sys.response.SysApiResponse;
import com.guojin.srm.api.biz.sys.ISysApiBiz;
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

@Api(value = "SysApiController", tags = {"系统接口管理"})
@RestController
@RequestMapping("/sys/api")
@Slf4j
public class SysApiController extends BaseBusinessController {

    @Reference
    private ISysApiBiz isysApiBiz;


    @ApiOperation(value = "获取系统接口列表", notes = "获取系统接口列表")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QuerySysApiRequest")
    @RequiresPermissions("sys:sysapi:list")
    @PostMapping("/list")
    public ResultDto<PageResultVO<SysApiResponse>> list(@Valid @RequestBody QuerySysApiRequest request) throws BizException {
        //查询列表数据
        PageResultVO<SysApiResponse> result = isysApiBiz.queryList(request);
        return success(result);
    }


    @ApiOperation(value = "查看接口详情", notes = "查看接口详情")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("sys:sysapi:info")
    @PostMapping("/info")
    public ResultDto<SysApiResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
        SysApiResponse sysApiResponse = isysApiBiz.querySysApi(request.getId());
        return success(sysApiResponse);
    }


    @ApiOperation(value = "新增系统接口", notes = "新增系统接口")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddSysApiRequest")
    @RequiresPermissions("sys:sysapi:save")
    @PostMapping("/save")
    public ResultDto save(@RequestBody @Valid AddSysApiRequest request) throws BizException {
        isysApiBiz.save(request,getUser());
        return success();
    }

    @ApiOperation(value = "修改系统接口", notes = "修改")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateSysApiRequest")
    @RequiresPermissions("sys:sysapi:update")
    @PostMapping("/update")
    public ResultDto update(@RequestBody @Valid UpdateSysApiRequest request) throws BizException {
        isysApiBiz.update(request);
        return success();
    }

    @ApiOperation(value = "删除系统接口", notes = "删除")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("sys:sysapi:delete")
    @PostMapping("/delete")
    public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
        isysApiBiz.deleteApi(request.getId());
        return success();
    }
}
