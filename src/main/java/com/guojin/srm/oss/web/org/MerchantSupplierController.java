package com.guojin.srm.oss.web.org;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.org.request.AddMerchantSupplierRequest;
import com.guojin.srm.api.bean.org.request.QueryMerchantSupplierRequest;
import com.guojin.srm.api.bean.org.request.QuerySupplierRequest;
import com.guojin.srm.api.bean.org.request.UpdateMerchantSupplierRequest;
import com.guojin.srm.api.bean.org.response.MerchantSupplierResponse;
import com.guojin.srm.api.bean.org.response.SupplierReportResponse;
import com.guojin.srm.api.bean.org.response.SupplierResponse;
import com.guojin.srm.api.biz.org.IMerchantSupplierBiz;
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
 * @date 2021年10月26日 13时37分29秒
 */

@Api(value = "MerchantSupplierController", tags = {"商户供应商表"})
@RestController
@RequestMapping("/org/merchantSupplier")
@Slf4j
public class MerchantSupplierController extends BaseBusinessController {

    private static final String[] TITLE = new String[] {"id","供应商名称","供应商账号","所属企业","所属OEM","身份","创建时间","状态" };

    @Reference
    private IMerchantSupplierBiz imerchantSupplierBiz;


    @ApiOperation(value = "获取列表信息", notes = "获取列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryMerchantSupplierRequest")
    @RequiresPermissions("org:merchantsupplier:list")
    @PostMapping("/list")
    public ResultDto<PageResultVO<MerchantSupplierResponse>> list(@Valid @RequestBody QueryMerchantSupplierRequest request) throws BizException {
        //查询列表数据
        PageResultVO<MerchantSupplierResponse> result = imerchantSupplierBiz.queryList(request);
        return success(result);
    }


    @ApiOperation(value = "获取列表信息", notes = "获取列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryMerchantSupplierRequest")
    @RequiresPermissions("org:merchantsupplier:queryList")
    @PostMapping("/queryList")
    public ResultDto<PageResultVO<MerchantSupplierResponse>> queryList(@Valid @RequestBody QueryMerchantSupplierRequest request) throws BizException {
        //查询列表数据
        if(PartyTypeEnum.OEM.getValue() == getParty().getPartyType()){
            request.setOemCode(getUser().getOemCode());
        }else if(PartyTypeEnum.PLATFORM.getValue() == getParty().getPartyType()){

        }else if(PartyTypeEnum.SUPPLIER.getValue() == getParty().getPartyType()){
            request.setSupplierNo(getParty().getPartyCode());
        }else{
            request.setMerchantNo(getParty().getPartyCode());
        }
        PageResultVO<MerchantSupplierResponse> result = imerchantSupplierBiz.querySupplierList(request);
        return success(result);
    }


    @ApiOperation(value = "获取详细信息", notes = "获取详细信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("org:merchantsupplier:info")
    @PostMapping("/info")
    public ResultDto<MerchantSupplierResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
        MerchantSupplierResponse merchantSupplierResponse = imerchantSupplierBiz.queryMerchantSupplier(request.getId());
        return success(merchantSupplierResponse);
    }


/*    @ApiOperation(value = "新增", notes = "获取新增信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddMerchantSupplierRequest")
    @RequiresPermissions("org:merchantsupplier:save")
    @PostMapping("/save")
    public ResultDto save(@RequestBody @Valid AddMerchantSupplierRequest request) throws BizException {
        imerchantSupplierBiz.save(request);
        return success();
    }*/

/*    @ApiOperation(value = "修改", notes = "修改")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateMerchantSupplierRequest")
    @RequiresPermissions("org:merchantsupplier:update")
    @PostMapping("/update")
    public ResultDto update(@RequestBody @Valid UpdateMerchantSupplierRequest request) throws BizException {
        imerchantSupplierBiz.update(request);
        return success();
    }*/

  /*  @ApiOperation(value = "删除", notes = "删除")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("org:merchantsupplier:delete")
    @PostMapping("/delete")
    public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
        imerchantSupplierBiz.deleteById(request.getId());
        return success();
    }*/

    @ApiOperation(value = "导出", notes = "导出")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryMerchantRequest")
    @PostMapping("/report")
    public ResultDto report(@Valid @RequestBody QueryMerchantSupplierRequest request, HttpServletResponse response) throws BizException {
        request.setPageNo(0);
        request.setPageSize(0);
        if(PartyTypeEnum.OEM.getValue() == getParty().getPartyType()){
            request.setOemCode(getUser().getOemCode());
        }else if(PartyTypeEnum.PLATFORM.getValue() == getParty().getPartyType()){

        }else if(PartyTypeEnum.SUPPLIER.getValue() == getParty().getPartyType()){
            request.setSupplierNo(getParty().getPartyCode());
        }else{
            request.setMerchantNo(getParty().getPartyCode());
        }
        PageResultVO<MerchantSupplierResponse> result = imerchantSupplierBiz.querySupplierList(request);
        String fileName ="供应商列表";
        List<List<Object>> values = new ArrayList<>();
        List<SupplierReportResponse> list = new ArrayList<SupplierReportResponse>();
        for(MerchantSupplierResponse resp:result.getList()){
            SupplierReportResponse reportResponse = new SupplierReportResponse();
            BeanUtil.copyProperties(resp, reportResponse);
            if(resp.getType()==0){
                reportResponse.setType("企业");
            }else if(resp.getType()==1){
                reportResponse.setType("个体户");
            }else if(resp.getType()==2){
                reportResponse.setType("自然人");
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
        for(SupplierReportResponse resp:list){
            List<Object> temp = new ArrayList<>();
            temp.add(resp.getId());
            temp.add(resp.getSupplierName());
            temp.add(resp.getContactMobile());
            temp.add(resp.getMerchantName());
            temp.add(resp.getOemName());
            temp.add(resp.getType());
            temp.add(resp.getCreateTime());
            temp.add(resp.getStatus());
            values.add(temp);
        }
        _downloadFile(fileName, TITLE, values, response);
        return success();
    }
}
