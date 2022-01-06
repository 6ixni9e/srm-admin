package com.guojin.srm.oss.web.org;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.org.request.AddApplyRequest;
import com.guojin.srm.api.bean.org.request.QueryApplyRequest;
import com.guojin.srm.api.bean.org.request.UpdateApplyRequest;
import com.guojin.srm.api.bean.org.response.ApplyReportResponse;
import com.guojin.srm.api.bean.org.response.ApplyResponse;
import com.guojin.srm.api.biz.org.IApplyBiz;
import com.guojin.srm.common.enums.BaseExcCodesEnum;
import com.guojin.srm.common.enums.PartyTypeEnum;
import com.guojin.srm.common.session.SessParty;
import com.guojin.srm.common.utils.BeanUtil;
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

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuqian
 * @email yuqian@99366.com
 * @date 2021年10月26日 13时37分28秒
 */

@Api(value = "ApplyController", tags = {"入驻申请表"})
@RestController
@RequestMapping("/org/apply")
@Slf4j
public class ApplyController extends BaseBusinessController {
    private static final String[] TITLE = new String[] {"id","申请人姓名","申请人手机号","所属行业","所属类型","备注","申请时间","跟进人" };

    @Reference
    private IApplyBiz iapplyBiz;


    @ApiOperation(value = "获取列表信息", notes = "获取列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryApplyRequest")
    @RequiresPermissions("org:apply:list")
    @PostMapping("/list")
    public ResultDto<PageResultVO<ApplyResponse>> list(@Valid @RequestBody QueryApplyRequest request) throws BizException {
        SessParty party = getParty();
        if(party == null){
            throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
        }
        if(PartyTypeEnum.OEM.getValue() != party.getPartyType() && PartyTypeEnum.PLATFORM.getValue() != party.getPartyType()){
            throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
        }
        //查询列表数据
        if(PartyTypeEnum.OEM.getValue() == getParty().getPartyType()){
            request.setOemCode(getUser().getOemCode());
        }
        PageResultVO<ApplyResponse> result = iapplyBiz.queryList(request);
        return success(result);
    }


    @ApiOperation(value = "获取详细信息", notes = "获取详细信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("org:apply:info")
    @PostMapping("/info")
    public ResultDto<ApplyResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
        ApplyResponse applyResponse = iapplyBiz.queryApply(request.getId());
        return success(applyResponse);
    }


  /*  @ApiOperation(value = "新增", notes = "获取新增信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddApplyRequest")
    @RequiresPermissions("org:apply:save")
    @PostMapping("/save")
    public ResultDto save(@RequestBody @Valid AddApplyRequest request) throws BizException {
        iapplyBiz.save(request);
        return success();
    }*/

    @ApiOperation(value = "修改", notes = "修改")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateApplyRequest")
    @RequiresPermissions("org:apply:update")
    @PostMapping("/update")
    public ResultDto update(@RequestBody @Valid UpdateApplyRequest request) throws BizException {
        SessParty party = getParty();
        if(party == null){
            throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
        }
        if(PartyTypeEnum.OEM.getValue() != party.getPartyType() && PartyTypeEnum.PLATFORM.getValue() != party.getPartyType()){
            throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
        }
        iapplyBiz.update(request,getUser());
        return success();
    }

   /* @ApiOperation(value = "删除", notes = "删除")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("org:apply:delete")
    @PostMapping("/delete")
    public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
        iapplyBiz.deleteById(request.getId());
        return success();
    }*/

    @ApiOperation(value = "导出", notes = "导出")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryMerchantRequest")
    @PostMapping("/report")
    public ResultDto report(@Valid @RequestBody QueryApplyRequest request, HttpServletResponse response) throws BizException {
        SessParty party = getParty();
        if(party == null){
            throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
        }
        if(PartyTypeEnum.OEM.getValue() != party.getPartyType() && PartyTypeEnum.PLATFORM.getValue() != party.getPartyType()){
            throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
        }
        request.setPageNo(0);
        request.setPageSize(0);
        PageResultVO<ApplyResponse> result = iapplyBiz.queryList(request);
        String fileName ="用户入驻申请列表";
        // 循环数据处理
        List<List<Object>> values = new ArrayList<>();
        List<ApplyReportResponse> list = new ArrayList<ApplyReportResponse>();
        for(ApplyResponse resp:result.getList()){
            ApplyReportResponse reportResponse = new ApplyReportResponse();
            BeanUtil.copyProperties(resp, reportResponse);
            if(resp.getType()!=null&&resp.getType()>0){
                if(resp.getType()==1){
                    reportResponse.setType("企业");
                }else if(resp.getType()==2){
                    reportResponse.setType("个体户");
                }else if(resp.getType()==3){
                    reportResponse.setType("自然人");
                }
            }
            list.add(reportResponse);
        }
        for(ApplyReportResponse reportResponse:list){
            List<Object> temp = new ArrayList<>();
            temp.add(reportResponse.getId());
            temp.add(reportResponse.getApplyName());
            temp.add(reportResponse.getApplyMobile());
            temp.add(reportResponse.getIndustry());
            temp.add(reportResponse.getType());
            temp.add(reportResponse.getRemark());
            temp.add(reportResponse.getCreateTime());
            temp.add(reportResponse.getBelong());
            values.add(temp);
        }
        _downloadFile(fileName, TITLE, values, response);
        return success();
    }
}
