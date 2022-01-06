package com.guojin.srm.oss.web.org;

import javax.validation.Valid;


import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.org.request.AddMerchantInvoiceConfigRequest;
import com.guojin.srm.api.bean.org.request.QueryMerchantInvoiceConfigRequest;
import com.guojin.srm.api.bean.org.request.UpdateMerchantInvoiceConfigRequest;
import com.guojin.srm.api.bean.org.response.MerchantInvoiceConfigResponse;
import com.guojin.srm.api.biz.org.IMerchantInvoiceConfigBiz;
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
 * @date 2021年11月05日 19时26分59秒
 */

@Api(value = "MerchantInvoiceConfigController", tags = {"商户发票抬头/开票地址管理"})
@RestController
@RequestMapping("/org/merchantInvoiceConfig")
@Slf4j
public class MerchantInvoiceConfigController extends BaseBusinessController {

    @Reference
    private IMerchantInvoiceConfigBiz imerchantInvoiceConfigBiz;


    @ApiOperation(value = "获取列表信息", notes = "获取列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryMerchantInvoiceConfigRequest")
    @RequiresPermissions("org:merchantinvoiceconfig:list")
    @PostMapping("/list")
    public ResultDto<PageResultVO<MerchantInvoiceConfigResponse>> list(@Valid @RequestBody QueryMerchantInvoiceConfigRequest request) throws BizException {
        //查询列表数据
        PageResultVO<MerchantInvoiceConfigResponse> result = imerchantInvoiceConfigBiz.queryList(request);
        return success(result);
    }


    @ApiOperation(value = "获取详细信息", notes = "获取详细信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("org:merchantinvoiceconfig:info")
    @PostMapping("/info")
    public ResultDto<MerchantInvoiceConfigResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
        MerchantInvoiceConfigResponse merchantInvoiceConfigResponse = imerchantInvoiceConfigBiz.queryMerchantInvoiceConfig(request.getId());
        return success(merchantInvoiceConfigResponse);
    }


    @ApiOperation(value = "新增", notes = "获取新增信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddMerchantInvoiceConfigRequest")
    @RequiresPermissions("org:merchantinvoiceconfig:save")
    @PostMapping("/save")
    public ResultDto save(@RequestBody @Valid AddMerchantInvoiceConfigRequest request) throws BizException {
        imerchantInvoiceConfigBiz.save(request);
        return success();
    }

    @ApiOperation(value = "修改", notes = "修改")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateMerchantInvoiceConfigRequest")
    @RequiresPermissions("org:merchantinvoiceconfig:update")
    @PostMapping("/update")
    public ResultDto update(@RequestBody @Valid UpdateMerchantInvoiceConfigRequest request) throws BizException {
        imerchantInvoiceConfigBiz.update(request);
        return success();
    }

/*    @ApiOperation(value = "删除", notes = "删除")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("org:merchantinvoiceconfig:delete")
    @PostMapping("/delete")
    public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
        imerchantInvoiceConfigBiz.deleteById(request.getId());
        return success();
    }*/
}
