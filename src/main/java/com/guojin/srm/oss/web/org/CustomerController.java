package com.guojin.srm.oss.web.org;

import javax.validation.Valid;


import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.org.request.AddCustomerRequest;
import com.guojin.srm.api.bean.org.request.QueryCustomerRequest;
import com.guojin.srm.api.bean.org.request.UpdateCustomerRequest;
import com.guojin.srm.api.bean.org.response.CustomerResponse;
import com.guojin.srm.api.biz.org.ICustomerBiz;
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

@Api(value = "CustomerController", tags = {"商户客户表"})
@RestController
@RequestMapping("/org/customer")
@Slf4j
public class CustomerController extends BaseBusinessController {

    @Reference
    private ICustomerBiz icustomerBiz;


/*    @ApiOperation(value = "获取列表信息", notes = "获取列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryCustomerRequest")
    @RequiresPermissions("org:customer:list")
    @PostMapping("/list")
    public ResultDto<PageResultVO<CustomerResponse>> list(@Valid @RequestBody QueryCustomerRequest request) throws BizException {
        //查询列表数据
        PageResultVO<CustomerResponse> result = icustomerBiz.queryList(request);
        return success(result);
    }


    @ApiOperation(value = "获取详细信息", notes = "获取详细信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("org:customer:info")
    @PostMapping("/info")
    public ResultDto<CustomerResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
        CustomerResponse customerResponse = icustomerBiz.queryCustomer(request.getId());
        return success(customerResponse);
    }


    @ApiOperation(value = "新增", notes = "获取新增信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddCustomerRequest")
    @RequiresPermissions("org:customer:save")
    @PostMapping("/save")
    public ResultDto save(@RequestBody @Valid AddCustomerRequest request) throws BizException {
        icustomerBiz.save(request,getUser());
        return success();
    }

    @ApiOperation(value = "修改", notes = "修改")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateCustomerRequest")
    @RequiresPermissions("org:customer:update")
    @PostMapping("/update")
    public ResultDto update(@RequestBody @Valid UpdateCustomerRequest request) throws BizException {
        icustomerBiz.update(request,getUser());
        return success();
    }*/

    /*@ApiOperation(value = "删除", notes = "删除")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("org:customer:delete")
    @PostMapping("/delete")
    public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
        icustomerBiz.deleteById(request.getId());
        return success();
    }*/
}
