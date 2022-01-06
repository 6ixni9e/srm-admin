package com.guojin.srm.oss.web.common;

import javax.validation.Valid;


import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.common.request.AddFeedbackRequest;
import com.guojin.srm.api.bean.common.request.QueryFeedbackRequest;
import com.guojin.srm.api.bean.common.request.UpdateFeedbackRequest;
import com.guojin.srm.api.bean.common.response.FeedbackResponse;
import com.guojin.srm.api.biz.common.IFeedbackBiz;
import com.guojin.srm.common.enums.BaseExcCodesEnum;
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

/**
 * @author yuqian
 * @email yuqian@99366.com
 * @date 2021年11月09日 13时45分11秒
 */

@Api(value = "FeedbackController", tags = {"用户反馈"})
@RestController
@RequestMapping("/common/feedback")
@Slf4j
public class FeedbackController extends BaseBusinessController {

    @Reference
    private IFeedbackBiz ifeedbackBiz;


    @ApiOperation(value = "获取列表信息", notes = "获取列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryFeedbackRequest")
    @RequiresPermissions("common:feedback:list")
    @PostMapping("/list")
    public ResultDto<PageResultVO<FeedbackResponse>> list(@Valid @RequestBody QueryFeedbackRequest request) throws BizException {
        SessParty party = getParty();
        if(party == null){
            throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
        }
        if(PartyTypeEnum.OEM.getValue() != party.getPartyType() && PartyTypeEnum.PLATFORM.getValue() != party.getPartyType()){
            throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
        }
        //查询列表数据
        PageResultVO<FeedbackResponse> result = ifeedbackBiz.queryList(request);
        return success(result);
    }


    @ApiOperation(value = "获取详细信息", notes = "获取详细信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("common:feedback:info")
    @PostMapping("/info")
    public ResultDto<FeedbackResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
        FeedbackResponse feedbackResponse = ifeedbackBiz.queryFeedback(request.getId());
        return success(feedbackResponse);
    }


    @ApiOperation(value = "修改(处理)", notes = "修改(处理)")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateFeedbackRequest")
    @RequiresPermissions("common:feedback:update")
    @PostMapping("/update")
    public ResultDto update(@RequestBody @Valid UpdateFeedbackRequest request) throws BizException {
        SessParty party = getParty();
        if(party == null){
            throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
        }
        if(PartyTypeEnum.OEM.getValue() != party.getPartyType() && PartyTypeEnum.PLATFORM.getValue() != party.getPartyType()){
            throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
        }
        ifeedbackBiz.update(request,getUser());
        return success();
    }

}
