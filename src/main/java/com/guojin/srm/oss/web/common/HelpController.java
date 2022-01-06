package com.guojin.srm.oss.web.common;

import javax.validation.Valid;


import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.common.request.AddHelpRequest;
import com.guojin.srm.api.bean.common.request.QueryHelpRequest;
import com.guojin.srm.api.bean.common.request.UpdateHelpRequest;
import com.guojin.srm.api.bean.common.response.HelpResponse;
import com.guojin.srm.api.bean.common.response.OperationLogResponse;
import com.guojin.srm.api.biz.common.IHelpBiz;
import com.guojin.srm.api.biz.common.IOperationLogBiz;
import com.guojin.srm.common.enums.BaseExcCodesEnum;
import com.guojin.srm.common.enums.LogTypeEnum;
import com.guojin.srm.common.enums.PartyTypeEnum;
import com.guojin.srm.common.session.SessParty;
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

import java.util.List;

/**
 * @author yuqian
 * @email yuqian@99366.com
 * @date 2021年11月09日 09时24分21秒
 */

@Api(value = "HelpController", tags = {"常见问题管理"})
@RestController
@RequestMapping("/common/help")
@Slf4j
public class HelpController extends BaseBusinessController {

    @Reference
    private IHelpBiz ihelpBiz;
    @Reference
    private IOperationLogBiz iOperationLogBiz;

    @ApiOperation(value = "查询常见问题列表", notes = "查询常见问题列表")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryHelpRequest")
    @RequiresPermissions("common:help:list")
    @PostMapping("/list")
    public ResultDto<PageResultVO<HelpResponse>> list(@Valid @RequestBody QueryHelpRequest request) throws BizException {
        SessParty party = getParty();
        if(party == null){
            throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
        }
        if(PartyTypeEnum.OEM.getValue() != party.getPartyType() && PartyTypeEnum.PLATFORM.getValue() != party.getPartyType()){
            throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
        }
        //查询列表数据
        PageResultVO<HelpResponse> result = ihelpBiz.queryList(request);

        return success(result);
    }


    @ApiOperation(value = "常见问题详情", notes = "常见问题详情")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("common:help:info")
    @PostMapping("/info")
    public ResultDto<HelpResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
        HelpResponse helpResponse = ihelpBiz.queryHelp(request.getId());
        List<OperationLogResponse> list = iOperationLogBiz.queryByBizId(helpResponse.getId()+"", LogTypeEnum.HELP);
        helpResponse.setLogList(list);
        return success(helpResponse);
    }


    @ApiOperation(value = "新增", notes = "获取新增信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddHelpRequest")
    @RequiresPermissions("common:help:save")
    @PostMapping("/save")
    public ResultDto save(@RequestBody @Valid AddHelpRequest request) throws BizException {
        SessParty party = getParty();
        if(party == null){
            throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
        }
        if(PartyTypeEnum.OEM.getValue() != party.getPartyType() && PartyTypeEnum.PLATFORM.getValue() != party.getPartyType()){
            throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
        }
        ihelpBiz.save(request,getUser());
        return success();
    }

    @ApiOperation(value = "修改、隐藏、显示", notes = "隐藏、显示时仅需传参id和status")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateHelpRequest")
    @RequiresPermissions("common:help:update")
    @PostMapping("/update")
    public ResultDto update(@RequestBody @Valid UpdateHelpRequest request) throws BizException {
        SessParty party = getParty();
        if(party == null){
            throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
        }
        if(PartyTypeEnum.OEM.getValue() != party.getPartyType() && PartyTypeEnum.PLATFORM.getValue() != party.getPartyType()){
            throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
        }
        ihelpBiz.update(request,getUser());
        return success();
    }
}
