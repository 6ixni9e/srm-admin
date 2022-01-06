package com.guojin.srm.oss.web.common;

import javax.validation.Valid;


import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.common.request.AddBannerRequest;
import com.guojin.srm.api.bean.common.request.QueryBannerRequest;
import com.guojin.srm.api.bean.common.request.UpdateBannerRequest;
import com.guojin.srm.api.bean.common.response.BannerResponse;
import com.guojin.srm.api.biz.common.IBannerBiz;
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
 * @date 2021年10月26日 13时42分11秒
 */

@Api(value = "BannerController", tags = {"Banner管理"})
@RestController
@RequestMapping("/common/banner")
@Slf4j
public class BannerController extends BaseBusinessController {

    @Reference
    private IBannerBiz ibannerBiz;


    @ApiOperation(value = "获取Banner列表信息", notes = "获取列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryBannerRequest")
    @RequiresPermissions("common:banner:list")
    @PostMapping("/list")
    public ResultDto<PageResultVO<BannerResponse>> list(@Valid @RequestBody QueryBannerRequest request) throws BizException {
        //查询列表数据
        PageResultVO<BannerResponse> result = ibannerBiz.queryList(request);
        return success(result);
    }


    @ApiOperation(value = "查看Banner详情", notes = "获取详细信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("common:banner:info")
    @PostMapping("/info")
    public ResultDto<BannerResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
        BannerResponse bannerResponse = ibannerBiz.queryBanner(request.getId());
        return success(bannerResponse);
    }


    @ApiOperation(value = "新增Banner", notes = "获取新增信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddBannerRequest")
    @RequiresPermissions("common:banner:save")
    @PostMapping("/save")
    public ResultDto save(@RequestBody @Valid AddBannerRequest request) throws BizException {
        ibannerBiz.save(request);
        return success();
    }

    @ApiOperation(value = "修改Banner", notes = "修改")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateBannerRequest")
    @RequiresPermissions("common:banner:update")
    @PostMapping("/update")
    public ResultDto update(@RequestBody @Valid UpdateBannerRequest request) throws BizException {
        ibannerBiz.update(request);
        return success();
    }

    @ApiOperation(value = "删除Banner", notes = "删除")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("common:banner:delete")
    @PostMapping("/delete")
    public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
        ibannerBiz.deleteById(request.getId());
        return success();
    }
}
