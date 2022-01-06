package com.guojin.srm.oss.web.org;

import javax.validation.Valid;


import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.common.response.LabelResponse;
import com.guojin.srm.api.bean.common.response.OperationLogResponse;
import com.guojin.srm.api.bean.org.request.AddOemRequest;
import com.guojin.srm.api.bean.org.request.QueryOemRequest;
import com.guojin.srm.api.bean.org.request.UpdateOemRequest;
import com.guojin.srm.api.bean.org.response.OemResponse;
import com.guojin.srm.api.biz.common.IOperationLogBiz;
import com.guojin.srm.api.biz.org.IOemBiz;
import com.guojin.srm.common.enums.LogTypeEnum;
import com.guojin.srm.common.enums.PartyTypeEnum;
import com.guojin.srm.oss.biz.AliyunOssBiz;
import com.guojin.srm.oss.web.BaseBusinessController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
 * @date 2021年10月26日 13时37分29秒
 */

@Api(value = "OemController", tags = {"OEM机构信息"})
@RestController
@RequestMapping("/org/oem")
@Slf4j
public class OemController extends BaseBusinessController {

    @Reference
    private IOemBiz ioemBiz;
    @Reference
    private IOperationLogBiz operationLogBiz;
    @Autowired
    private AliyunOssBiz aliyunOssBiz;


    @ApiOperation(value = "获取列表信息", notes = "获取列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryOemRequest")
    @RequiresPermissions("org:oem:list")
    @PostMapping("/list")
    public ResultDto<PageResultVO<OemResponse>> list(@Valid @RequestBody QueryOemRequest request) throws BizException {
        //查询列表数据
        if(PartyTypeEnum.OEM.getValue() == getParty().getPartyType()){
            request.setOemCode(getUser().getOemCode());
        }
        PageResultVO<OemResponse> result = ioemBiz.queryList(request);
        return success(result);
    }


    @ApiOperation(value = "获取详细信息", notes = "获取详细信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("org:oem:info")
    @PostMapping("/info")
    public ResultDto<OemResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
        OemResponse oemResponse = ioemBiz.queryOem(request.getId());
        oemResponse.setShowLogoUrl(aliyunOssBiz.getPrivateUrl(null,oemResponse.getLogoUrl()));
        List<OperationLogResponse> logList = operationLogBiz.queryByBizId(oemResponse.getOemCode(), LogTypeEnum.OEM);
        oemResponse.setLogList(logList);
        return success(oemResponse);
    }


    @ApiOperation(value = "新增", notes = "获取新增信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddOemRequest")
    @RequiresPermissions("org:oem:save")
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/save")
    public ResultDto save(@RequestBody @Valid AddOemRequest request) throws BizException {
        ioemBiz.save(request,getUser());
        return success();
    }

    @ApiOperation(value = "修改", notes = "修改")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateOemRequest")
    @RequiresPermissions("org:oem:update")
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/update")
    public ResultDto update(@RequestBody @Valid UpdateOemRequest request) throws BizException {
        ioemBiz.update(request,getUser(),getParty());
        return success();
    }

/*
    @ApiOperation(value = "删除", notes = "删除")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("org:oem:delete")
    @PostMapping("/delete")
    public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
        ioemBiz.deleteById(request.getId());
        return success();
    }
*/

    @ApiOperation(value = "冻结", notes = "冻结")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @PostMapping("/frozen")
    public ResultDto frozen(@Valid @RequestBody IDParamRequest request) throws BizException {
        ioemBiz.frozen(request,getUser());
        return success();
    }

    @ApiOperation(value = "获取下拉列表信息", notes = "获取下拉列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryOemRequest")
    @PostMapping("/listLabel")
    public ResultDto<List<LabelResponse>> listLabel(@Valid @RequestBody QueryOemRequest request) throws BizException {
        //查询列表数据
        if(PartyTypeEnum.OEM.getValue() == getParty().getPartyType()){
            request.setOemCode(getUser().getOemCode());
        }
        List<LabelResponse> result = ioemBiz.labelList(request);
        return success(result);
    }
}
