package com.guojin.srm.oss.web.org;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.common.response.LabelResponse;
import com.guojin.srm.api.bean.common.response.OperationLogResponse;
import com.guojin.srm.api.bean.org.request.AddMerchantRequest;
import com.guojin.srm.api.bean.org.request.FrozenMerchantRequest;
import com.guojin.srm.api.bean.org.request.QueryMerchantRequest;
import com.guojin.srm.api.bean.org.request.UpdateMerchantRequest;
import com.guojin.srm.api.bean.org.response.MerchantReportResponse;
import com.guojin.srm.api.bean.org.response.MerchantResponse;
import com.guojin.srm.api.biz.common.IOperationLogBiz;
import com.guojin.srm.api.biz.org.IMerchantBiz;
import com.guojin.srm.common.enums.LogTypeEnum;
import com.guojin.srm.common.enums.PartyTypeEnum;
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
 * @date 2021年10月26日 13时37分29秒
 */

@Api(value = "MerchantController", tags = {"商户企业表"})
@RestController
@RequestMapping("/org/merchant")
@Slf4j
public class MerchantController extends BaseBusinessController {

    private static final String[] TITLE = new String[] {"企业编号","企业名称","企业简称","企业分类","所属行业","所属OEM","创建时间","状态" };

    @Reference
    private IMerchantBiz imerchantBiz;

    @Reference
    private IOperationLogBiz operationLogBiz;


    @ApiOperation(value = "获取列表信息", notes = "获取列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryMerchantRequest")
    @RequiresPermissions("org:merchant:list")
    @PostMapping("/list")
    public ResultDto<PageResultVO<MerchantResponse>> list(@Valid @RequestBody QueryMerchantRequest request) throws BizException {
        //查询列表数据
        if(PartyTypeEnum.OEM.getValue() == getParty().getPartyType()){
            request.setOemCode(getUser().getOemCode());
        }else if(PartyTypeEnum.PLATFORM.getValue() == getParty().getPartyType()){

        }else{
            request.setMerchantNo(getParty().getPartyCode());
        }
        PageResultVO<MerchantResponse> result = imerchantBiz.queryList(request);
        return success(result);
    }


    @ApiOperation(value = "获取详细信息", notes = "获取详细信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("org:merchant:info")
    @PostMapping("/info")
    public ResultDto<MerchantResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
        MerchantResponse merchantResponse = imerchantBiz.queryMerchant(request.getId());
        List<OperationLogResponse> logList = operationLogBiz.queryByBizId(merchantResponse.getMerchantNo(), LogTypeEnum.MERCHANT);
        merchantResponse.setLogList(logList);
        return success(merchantResponse);
    }


    @ApiOperation(value = "新增", notes = "获取新增信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddMerchantRequest")
    @RequiresPermissions("org:merchant:save")
    @PostMapping("/save")
    public ResultDto save(@RequestBody @Valid AddMerchantRequest request) throws BizException {
        imerchantBiz.save(request,getUser());
        return success();
    }

    @ApiOperation(value = "修改", notes = "修改")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateMerchantRequest")
    @RequiresPermissions("org:merchant:update")
    @PostMapping("/update")
    public ResultDto update(@RequestBody @Valid UpdateMerchantRequest request) throws BizException {
        imerchantBiz.update(request,getUser());
        return success();
    }

    @ApiOperation(value = "获取下拉列表信息", notes = "获取下拉列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryOemRequest")
    @PostMapping("/listLabel")
    public ResultDto<List<LabelResponse>> listLabel(@Valid @RequestBody QueryMerchantRequest request) throws BizException {
        //查询列表数据
        if(PartyTypeEnum.OEM.getValue() == getParty().getPartyType()){
            request.setOemCode(getUser().getOemCode());
        }else if(PartyTypeEnum.PLATFORM.getValue() == getParty().getPartyType()){

        }else{
            request.setMerchantNo(getParty().getPartyCode());
        }
        List<LabelResponse> result = imerchantBiz.labelList(request);
        return success(result);
    }

//    @ApiOperation(value = "删除", notes = "删除")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
//    @RequiresPermissions("org:merchant:delete")
//    @PostMapping("/delete")
//    public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
//        imerchantBiz.deleteById(request.getId());
//        return success();
//    }

    @ApiOperation(value = "冻结", notes = "冻结")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @PostMapping("/frozen")
    public ResultDto frozen(@Valid @RequestBody FrozenMerchantRequest request) throws BizException {
        imerchantBiz.frozen(request,getUser());
        return success();
    }

    @ApiOperation(value = "导出", notes = "导出")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryMerchantRequest")
    @PostMapping("/report")
    public ResultDto report(@Valid @RequestBody QueryMerchantRequest request, HttpServletResponse response) throws BizException {
        //查询列表数据
        if(PartyTypeEnum.OEM.getValue() == getParty().getPartyType()){
            request.setOemCode(getUser().getOemCode());
        }else if(PartyTypeEnum.PLATFORM.getValue() == getParty().getPartyType()){

        }else{
            request.setMerchantNo(getParty().getPartyCode());
        }
        request.setPageNo(0);
        request.setPageSize(0);
        PageResultVO<MerchantResponse> result = imerchantBiz.queryReportList(request);
        String fileName ="企业列表";
        // 循环数据处理
        List<List<Object>> values = new ArrayList<>();
        List<MerchantReportResponse> list = new ArrayList<MerchantReportResponse>();
        for(MerchantResponse resp:result.getList()){
            MerchantReportResponse reportResponse = new MerchantReportResponse();
            BeanUtil.copyProperties(resp, reportResponse);
            if(resp.getType()==0){
                reportResponse.setType("集团公司");
            }else if(resp.getType()==1){
                reportResponse.setType("分公司");
            }else if(resp.getType()==2){
                reportResponse.setType("子公司");
            }else if(resp.getType()==3){
                reportResponse.setType("独立公司");
            }
            if(resp.getStatus()==0){
                reportResponse.setStatus("待生效");
            }else if(resp.getStatus()==1){
                reportResponse.setStatus("正常");
            }else if(resp.getStatus()==2){
                reportResponse.setStatus("冻结");
            }
            list.add(reportResponse);
        }
        for(MerchantReportResponse reportResponse:list){
            List<Object> temp = new ArrayList<>();
            temp.add(reportResponse.getMerchantNo());
            temp.add(reportResponse.getMerchantName());
            temp.add(reportResponse.getShortName());
            temp.add(reportResponse.getType());
            temp.add(reportResponse.getIndustryName());
            temp.add(reportResponse.getOemName());
            temp.add(reportResponse.getCreateTime());
            temp.add(reportResponse.getStatus());
            values.add(temp);
        }
        _downloadFile(fileName, TITLE, values, response);
        return success();
    }
}
