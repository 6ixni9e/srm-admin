package com.guojin.srm.oss.web.org;

import javax.validation.Valid;


import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.org.request.*;
import com.guojin.srm.api.bean.org.response.MerchantChannelResponse;
import com.guojin.srm.api.biz.org.IMerchantChannelBiz;
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
 * @date 2021年10月26日 13时37分29秒
 */

@Api(value = "MerchantChannelController", tags = {"商户支付通道表"})
@RestController
@RequestMapping("/org/merchantChannel")
@Slf4j
public class MerchantChannelController extends BaseBusinessController {

    @Reference
    private IMerchantChannelBiz imerchantChannelBiz;
//
//
//    @ApiOperation(value = "获取列表信息", notes = "获取列表信息")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryMerchantChannelRequest")
//    @RequiresPermissions("org:merchantchannel:list")
//    @PostMapping("/list")
//    public ResultDto<PageResultVO<MerchantChannelResponse>> list(@Valid @RequestBody QueryMerchantChannelRequest request) throws BizException {
//        //查询列表数据
//        PageResultVO<MerchantChannelResponse> result = imerchantChannelBiz.queryList(request);
//        return success(result);
//    }
//
//
//    @ApiOperation(value = "获取详细信息", notes = "获取详细信息")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
//    @RequiresPermissions("org:merchantchannel:info")
//    @PostMapping("/info")
//    public ResultDto<MerchantChannelResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
//        MerchantChannelResponse merchantChannelResponse = imerchantChannelBiz.queryMerchantChannel(request.getId());
//        return success(merchantChannelResponse);
//    }
//
//
//    @ApiOperation(value = "新增", notes = "获取新增信息")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddMerchantChannelRequest")
//    @RequiresPermissions("org:merchantchannel:save")
//    @PostMapping("/save")
//    public ResultDto save(@RequestBody @Valid AddMerchantChannelRequest request) throws BizException {
//        imerchantChannelBiz.save(request);
//        return success();
//    }
//
//    @ApiOperation(value = "修改", notes = "修改")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateMerchantChannelRequest")
//    @RequiresPermissions("org:merchantchannel:update")
//    @PostMapping("/update")
//    public ResultDto update(@RequestBody @Valid UpdateMerchantChannelRequest request) throws BizException {
//        imerchantChannelBiz.update(request);
//        return success();
//    }
//
//    @ApiOperation(value = "删除", notes = "删除")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
//    @RequiresPermissions("org:merchantchannel:delete")
//    @PostMapping("/delete")
//    public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
//        imerchantChannelBiz.deleteById(request.getId());
//        return success();
//    }

    @ApiOperation(value = "基础配置提交", notes = "基础配置提交")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "ConfigMerchantChannelRequest")
    @RequiresPermissions("org:merchantchannel:config")
    @PostMapping("/config")
    public ResultDto config(@Valid @RequestBody ConfigMerchantChannelRequest request) throws BizException {
        imerchantChannelBiz.config(request, getUserId());
        return success();
    }

    @ApiOperation(value = "基础配置查询", notes = "基础配置查询")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryConfigMerchantChannelRequest")
    @RequiresPermissions("org:merchantchannel:queryConfig")
    @PostMapping("/queryConfig")
    public ResultDto queryConfig(@Valid @RequestBody QueryConfigMerchantChannelRequest request) throws BizException {
        MerchantChannelResponse merchantChannelResponse = imerchantChannelBiz.queryConfig(request.getMerchantNo());
        return success(merchantChannelResponse);
    }
}
