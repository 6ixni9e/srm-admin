package com.guojin.srm.oss.web.purchase;

import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.purchase.request.AddPurchasingPlanRequest;
import com.guojin.srm.api.bean.purchase.request.IDParamPurchasingPlanRequest;
import com.guojin.srm.api.bean.purchase.request.QueryPurchasingPlanRequest;
import com.guojin.srm.api.bean.purchase.request.UpdatePurchasingPlanRequest;
import com.guojin.srm.api.bean.purchase.response.PurchasingPlanResponse;
import com.guojin.srm.api.biz.purchase.IPurchasingPlanBiz;
import com.guojin.srm.common.base.IDParamRequest;
import com.guojin.srm.common.base.PageResultVO;
import com.guojin.srm.common.enums.BaseExcCodesEnum;
import com.guojin.srm.common.enums.PlanStatusEnum;
import com.guojin.srm.common.exception.BizException;
import com.guojin.srm.common.session.SessParty;
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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yuqian
 * @email yuqian@99366.com
 * @date 2021年10月26日 13时47分38秒
 */

@Api(value = "PurchasingPlanController", tags = {"采购计划表"})
@RestController
@RequestMapping("/purchase/purchasingPlan")
@Slf4j
public class PurchasingPlanController extends BaseBusinessController {

    @Reference
    private IPurchasingPlanBiz ipurchasingPlanBiz;

    private static final String[] TITLES = new String[] {"计划编号","所属公司","所属OEM","采购计划名称","创建时间","状态"};

    @ApiOperation(value = "获取列表信息", notes = "获取列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryPurchasingPlanRequest")
    @RequiresPermissions("purchase:purchasingplan:list")
    @PostMapping("/list")
    public ResultDto<PageResultVO<PurchasingPlanResponse>> list(@Valid @RequestBody QueryPurchasingPlanRequest request) throws BizException {
        //查询列表数据
        //平台查询所有数据
        SessParty sessParty = getParty();
        if (sessParty == null) {
            return fail(BaseExcCodesEnum.NO_OPERATE_RIGHT);
        }
        if(sessParty.getPartyType().intValue() == 1) { //OEM机构
            request.setOemCode(getUser().getOemCode()); //oemCode编码
        }else if(sessParty.getPartyType().intValue() == 2) { //集团公司 查询自己及下面所有公司的信息
            request.setMerchantNo(sessParty.getPartyCode()); //企业编码
            request.setGroupMerchant(sessParty.getPartyCode()); //归属集团
        }else if(sessParty.getPartyType().intValue() == 3) { //子公司 查询自己及下面所有公司的信息
            request.setMerchantNo(sessParty.getPartyCode()); //企业编码
            request.setSuperiorMerchant(sessParty.getPartyCode());//上级集团公司
        }else if(sessParty.getPartyType().intValue() == 4) { //分公司/独立公司
            request.setMerchantNo(sessParty.getPartyCode()); //企业编码
        }
        PageResultVO<PurchasingPlanResponse> result = ipurchasingPlanBiz.queryList(request);
        return success(result);
    }

    @ApiOperation(value = "导出列表", notes = "导出列表")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryPurchasingPlanRequest")
    @RequiresPermissions("purchase:purchasingplan:export")
    @PostMapping("/export")
    public ResultDto download(@RequestBody QueryPurchasingPlanRequest request, HttpServletResponse response) throws BizException{
        // 查询列表数据
        request.setPageSize(0); //设置不分页
        request.setPageNo(0);
        SessParty sessParty = getParty();
        if (sessParty == null) {
            return fail(BaseExcCodesEnum.NO_OPERATE_RIGHT);
        }
        if(sessParty.getPartyType().intValue() == 1) { //OEM机构
            request.setOemCode(getUser().getOemCode()); //oemCode编码
        }else if(sessParty.getPartyType().intValue() == 2) { //集团公司 查询自己及下面所有公司的信息
            request.setMerchantNo(sessParty.getPartyCode()); //企业编码
            request.setGroupMerchant(sessParty.getPartyCode()); //归属集团
        }else if(sessParty.getPartyType().intValue() == 3) { //子公司 查询自己及下面所有公司的信息
            request.setMerchantNo(sessParty.getPartyCode()); //企业编码
            request.setSuperiorMerchant(sessParty.getPartyCode());//上级集团公司
        }else if(sessParty.getPartyType().intValue() == 4) { //分公司/独立公司
            request.setMerchantNo(sessParty.getPartyCode()); //企业编码
        }
        PageResultVO<PurchasingPlanResponse> result = ipurchasingPlanBiz.queryList(request);
        if (result.getTotalCount() > 0) {
            //文件名
            String fileName = "PurchasingPlan_"+getParty().getPartyCode()+".xls";
            // 循环数据处理
            List<List<Object>> values = new ArrayList<>();
            for (PurchasingPlanResponse item : result.getList()) {
                List<Object> temp = new ArrayList<>();
                temp.add(item.getPlanNo());
                temp.add(item.getMerchantName());
                temp.add(item.getOemName());
                temp.add(item.getPlanName());
                temp.add(item.getCreateTime());
                temp.add(PlanStatusEnum.getByType(item.getStatus()).getRemark()); //项目状态
                values.add(temp);
            }
            _downloadFile(fileName,TITLES,values,response);
        }
        return success();
    }

    @ApiOperation(value = "获取详细信息", notes = "获取详细信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("purchase:purchasingplan:info")
    @PostMapping("/info")
    public ResultDto<PurchasingPlanResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
        PurchasingPlanResponse purchasingPlanResponse = ipurchasingPlanBiz.queryPurchasingPlan(request.getId());
        return success(purchasingPlanResponse);
    }


//    @ApiOperation(value = "新增", notes = "获取新增信息")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddPurchasingPlanRequest")
//    @RequiresPermissions("purchase:purchasingplan:save")
//    @PostMapping("/save")
//    public ResultDto save(@RequestBody @Valid AddPurchasingPlanRequest request) throws BizException {
//        ipurchasingPlanBiz.save(request);
//        return success();
//    }
//
//    @ApiOperation(value = "修改", notes = "修改")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdatePurchasingPlanRequest")
//    @RequiresPermissions("purchase:purchasingplan:update")
//    @PostMapping("/update")
//    public ResultDto update(@RequestBody @Valid UpdatePurchasingPlanRequest request) throws BizException {
//        ipurchasingPlanBiz.update(request);
//        return success();
//    }
//
//    @ApiOperation(value = "删除", notes = "删除")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
//    @RequiresPermissions("purchase:purchasingplan:delete")
//    @PostMapping("/delete")
//    public ResultDto delete(@Valid @RequestBody IDParamPurchasingPlanRequest request) throws BizException {
//        ipurchasingPlanBiz.deleteById(request, getUser());
//        return success();
//    }
}
