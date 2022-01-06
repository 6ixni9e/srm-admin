package com.guojin.srm.oss.web.org;

import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.org.request.AddSupplierRequest;
import com.guojin.srm.api.bean.org.request.QuerySupplierRequest;
import com.guojin.srm.api.bean.org.request.UpdateSupplierRequest;
import com.guojin.srm.api.bean.org.response.SupplierResponse;
import com.guojin.srm.api.biz.org.ISupplierBiz;
import com.guojin.srm.common.base.IDParamRequest;
import com.guojin.srm.common.base.PageResultVO;
import com.guojin.srm.common.exception.BizException;
import com.guojin.srm.common.session.SessUser;
import com.guojin.srm.common.utils.ResultDto;
import com.guojin.srm.oss.web.BaseBusinessController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author yuqian
 * @email yuqian@99366.com
 * @date 2021年10月26日 13时37分29秒
 */

@Api(value = "SupplierController", tags = {"供应商信息表"})
@RestController
@RequestMapping("/org/supplier")
@Slf4j
public class SupplierController extends BaseBusinessController {

    @Reference
    private ISupplierBiz isupplierBiz;


    @ApiOperation(value = "获取列表信息", notes = "获取列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QuerySupplierRequest")
    @RequiresPermissions("org:supplier:list")
    @PostMapping("/list")
    public ResultDto<PageResultVO<SupplierResponse>> list(@Valid @RequestBody QuerySupplierRequest request) throws BizException {
        //查询列表数据
        PageResultVO<SupplierResponse> result = isupplierBiz.queryList(request);
        return success(result);
    }


    @ApiOperation(value = "获取详细信息", notes = "获取详细信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("org:supplier:info")
    @PostMapping("/info")
    public ResultDto<SupplierResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
        SupplierResponse supplierResponse = isupplierBiz.querySupplier(request.getId());
        return success(supplierResponse);
    }


/*    @ApiOperation(value = "新增", notes = "获取新增信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddSupplierRequest")
    @RequiresPermissions("org:supplier:save")
    @PostMapping("/save")
    public ResultDto save(@RequestBody @Valid AddSupplierRequest request) throws BizException {
        SessUser sessUser = getUser();
        isupplierBiz.save(request, sessUser);
        return success();
    }

    @ApiOperation(value = "修改", notes = "修改")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateSupplierRequest")
    @RequiresPermissions("org:supplier:update")
    @PostMapping("/update")
    public ResultDto update(@RequestBody @Valid UpdateSupplierRequest request) throws BizException {
        isupplierBiz.update(request);
        return success();
    }*/

/*    @ApiOperation(value = "删除", notes = "删除")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("org:supplier:delete")
    @PostMapping("/delete")
    public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
        isupplierBiz.deleteById(request.getId());
        return success();
    }*/

 /*   @ApiOperation(value = "冻结", notes = "冻结")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @PostMapping("/frozen")
    public ResultDto frozen(@Valid @RequestBody IDParamRequest request) throws BizException {
        isupplierBiz.frozen(request, getUser());
        return success();
    }*/
}
