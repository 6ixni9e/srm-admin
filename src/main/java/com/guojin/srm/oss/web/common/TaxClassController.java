package com.guojin.srm.oss.web.common;

import javax.validation.Valid;


import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.common.request.AddTaxClassRequest;
import com.guojin.srm.api.bean.common.request.QueryTaxClassRequest;
import com.guojin.srm.api.bean.common.request.UpdateTaxClassRequest;
import com.guojin.srm.api.bean.common.response.TaxClassResponse;
import com.guojin.srm.api.bean.common.response.TaxClassTreeResponse;
import com.guojin.srm.api.biz.common.ITaxClassBiz;
import com.guojin.srm.oss.web.BaseBusinessController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @date 2021年11月10日 16时49分10秒
 */

@Api(value = "TaxClassController", tags = {"税收分类表"})
@RestController
@RequestMapping("/common/taxClass")
@Slf4j
public class TaxClassController extends BaseBusinessController {

    @Reference
    private ITaxClassBiz itaxClassBiz;

//
//    @ApiOperation(value = "获取列表信息", notes = "获取列表信息")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryTaxClassRequest")
//    @RequiresPermissions("common:taxclass:list")
//    @PostMapping("/list")
//    public ResultDto<PageResultVO<TaxClassResponse>> list(@Valid @RequestBody QueryTaxClassRequest request) throws BizException {
//        //查询列表数据
//        PageResultVO<TaxClassResponse> result = itaxClassBiz.queryList(request);
//        return success(result);
//    }
//
//
//    @ApiOperation(value = "获取详细信息", notes = "获取详细信息")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
//    @RequiresPermissions("common:taxclass:info")
//    @PostMapping("/info")
//    public ResultDto<TaxClassResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
//        TaxClassResponse taxClassResponse = itaxClassBiz.queryTaxClass(request.getId());
//        return success(taxClassResponse);
//    }
//
//
//    @ApiOperation(value = "新增", notes = "获取新增信息")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddTaxClassRequest")
//    @RequiresPermissions("common:taxclass:save")
//    @PostMapping("/save")
//    public ResultDto save(@RequestBody @Valid AddTaxClassRequest request) throws BizException {
//        itaxClassBiz.save(request);
//        return success();
//    }
//
//    @ApiOperation(value = "修改", notes = "修改")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateTaxClassRequest")
//    @RequiresPermissions("common:taxclass:update")
//    @PostMapping("/update")
//    public ResultDto update(@RequestBody @Valid UpdateTaxClassRequest request) throws BizException {
//        itaxClassBiz.update(request);
//        return success();
//    }
//
//    @ApiOperation(value = "删除", notes = "删除")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
//    @RequiresPermissions("common:taxclass:delete")
//    @PostMapping("/delete")
//    public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
//        itaxClassBiz.deleteById(request.getId());
//        return success();
//    }
//





}
