package com.guojin.srm.oss.web.common;

import javax.validation.Valid;


import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.common.request.AddIndustryRequest;
import com.guojin.srm.api.bean.common.request.QueryIndustryRequest;
import com.guojin.srm.api.bean.common.request.UpdateIndustryRequest;
import com.guojin.srm.api.bean.common.response.LabelResponse;
import com.guojin.srm.api.bean.common.response.IndustryResponse;
import com.guojin.srm.api.bean.purchase.request.QueryGoodsCategoryRequest;
import com.guojin.srm.api.biz.common.IIndustryBiz;
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

import java.util.List;

/**
 * @author yuqian
 * @email yuqian@99366.com
 * @date 2021年10月26日 13时42分11秒
 */

@Api(value = "IndustryController", tags = {"行业类别表"})
@RestController
@RequestMapping("/common/industry")
@Slf4j
public class IndustryController extends BaseBusinessController {

    @Reference
    private IIndustryBiz iindustryBiz;


    @ApiOperation(value = "获取列表信息", notes = "获取列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryIndustryRequest")
    @RequiresPermissions("common:industry:list")
    @PostMapping("/list")
    public ResultDto<PageResultVO<IndustryResponse>> list(@Valid @RequestBody QueryIndustryRequest request) throws BizException {
        //查询列表数据
        PageResultVO<IndustryResponse> result = iindustryBiz.queryList(request);
        return success(result);
    }


    @ApiOperation(value = "获取详细信息", notes = "获取详细信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("common:industry:info")
    @PostMapping("/info")
    public ResultDto<IndustryResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
        IndustryResponse industryResponse = iindustryBiz.queryIndustry(request.getId());
        return success(industryResponse);
    }


    @ApiOperation(value = "新增", notes = "获取新增信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddIndustryRequest")
    @RequiresPermissions("common:industry:save")
    @PostMapping("/save")
    public ResultDto save(@RequestBody @Valid AddIndustryRequest request) throws BizException {
        iindustryBiz.save(request);
        return success();
    }

    @ApiOperation(value = "修改", notes = "修改")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateIndustryRequest")
    @RequiresPermissions("common:industry:update")
    @PostMapping("/update")
    public ResultDto update(@RequestBody @Valid UpdateIndustryRequest request) throws BizException {
        iindustryBiz.update(request);
        return success();
    }

    @ApiOperation(value = "删除", notes = "删除")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("common:industry:delete")
    @PostMapping("/delete")
    public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
        iindustryBiz.deleteById(request.getId());
        return success();
    }

    @ApiOperation(value = "获取下拉列表信息", notes = "获取下拉列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryIndustryRequest")
    @RequiresPermissions("common:industry:listLabel")
    @PostMapping("/listLabel")
    public ResultDto<List<LabelResponse>> listLabel(@Valid @RequestBody QueryGoodsCategoryRequest request) throws BizException {
        //查询列表数据
        List<LabelResponse> result = iindustryBiz.labelList(request);
        return success(result);
    }
}
