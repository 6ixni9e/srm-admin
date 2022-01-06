package com.guojin.srm.oss.web.org;

import javax.validation.Valid;


import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.org.request.AddSupplierLabelRequest;
import com.guojin.srm.api.bean.org.request.QuerySupplierLabelRequest;
import com.guojin.srm.api.bean.org.request.UpdateSupplierLabelRequest;
import com.guojin.srm.api.bean.org.response.SupplierLabelResponse;
import com.guojin.srm.api.biz.org.ISupplierLabelBiz;
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
import com.guojin.srm.common.session.SessUser;
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

@Api(value = "SupplierLabelController", tags = {"供应商标签表"})
@RestController
@RequestMapping("/org/supplierLabel")
@Slf4j
public class SupplierLabelController extends BaseBusinessController {

    @Reference
    private ISupplierLabelBiz isupplierLabelBiz;


    @ApiOperation(value = "获取列表信息", notes = "获取列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QuerySupplierLabelRequest")
    @RequiresPermissions("org:supplierlabel:list")
    @PostMapping("/list")
    public ResultDto<PageResultVO<SupplierLabelResponse>> list(@Valid @RequestBody QuerySupplierLabelRequest request) throws BizException {
        //查询列表数据
        PageResultVO<SupplierLabelResponse> result = isupplierLabelBiz.queryList(request);
        return success(result);
    }


    @ApiOperation(value = "获取详细信息", notes = "获取详细信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("org:supplierlabel:info")
    @PostMapping("/info")
    public ResultDto<SupplierLabelResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
        SupplierLabelResponse supplierLabelResponse = isupplierLabelBiz.querySupplierLabel(request.getId());
        return success(supplierLabelResponse);
    }


    @ApiOperation(value = "新增", notes = "获取新增信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddSupplierLabelRequest")
    @RequiresPermissions("org:supplierlabel:save")
    @PostMapping("/save")
    public ResultDto save(@RequestBody @Valid AddSupplierLabelRequest request) throws BizException {
    	SessUser sessUser = getUser();
        isupplierLabelBiz.save(request, sessUser);
        return success();
    }

    @ApiOperation(value = "修改", notes = "修改")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateSupplierLabelRequest")
    @RequiresPermissions("org:supplierlabel:update")
    @PostMapping("/update")
    public ResultDto update(@RequestBody @Valid UpdateSupplierLabelRequest request) throws BizException {
    	SessUser sessUser = getUser();
        isupplierLabelBiz.update(request, sessUser);
        return success();
    }

   /* @ApiOperation(value = "删除", notes = "删除")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("org:supplierlabel:delete")
    @PostMapping("/delete")
    public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
        isupplierLabelBiz.deleteById(request.getId());
        return success();
    }*/
}
