package com.guojin.srm.oss.web.auth;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.auth.request.AddIndividualAuthRequest;
import com.guojin.srm.api.bean.auth.request.QueryIndividualAuthRequest;
import com.guojin.srm.api.bean.auth.request.UpdateIndividualAuthRequest;
import com.guojin.srm.api.bean.auth.response.IndividualAuthReportResponse;
import com.guojin.srm.api.bean.auth.response.IndividualAuthResponse;
import com.guojin.srm.api.biz.auth.IIndividualAuthBiz;
import com.guojin.srm.common.enums.IndividualAuthStatusEnum;
import com.guojin.srm.common.enums.YuncaiOrderStatusEnum;
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
 * @date 2021年10月26日 13时40分14秒
 */

@Api(value = "IndividualAuthController", tags = {"个体认证表"})
@RestController
@RequestMapping("/auth/individualAuth")
@Slf4j
public class IndividualAuthController extends BaseBusinessController {

    private static final String[] TITLE = new String[] {"订单编号","账号昵称","注册账号","个体户名称","支付金额","费用承担方","创建时间","状态" };

    @Reference
    private IIndividualAuthBiz iindividualAuthBiz;


    @ApiOperation(value = "获取列表信息", notes = "获取列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryIndividualAuthRequest")
    @RequiresPermissions("auth:individualauth:list")
    @PostMapping("/list")
    public ResultDto<PageResultVO<IndividualAuthResponse>> list(@Valid @RequestBody QueryIndividualAuthRequest request) throws BizException {
        //查询列表数据
        PageResultVO<IndividualAuthResponse> result = iindividualAuthBiz.queryList(request);
        return success(result);
    }


    @ApiOperation(value = "获取详细信息", notes = "获取详细信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("auth:individualauth:info")
    @PostMapping("/info")
    public ResultDto<IndividualAuthResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
        IndividualAuthResponse individualAuthResponse = iindividualAuthBiz.queryIndividualAuth(request.getId());
        return success(individualAuthResponse);
    }



/*    @ApiOperation(value = "修改", notes = "修改")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateIndividualAuthRequest")
    @RequiresPermissions("auth:individualauth:update")
    @PostMapping("/update")
    public ResultDto update(@RequestBody @Valid UpdateIndividualAuthRequest request) throws BizException {
        iindividualAuthBiz.update(request);
        return success();
    }*/

 /*   @ApiOperation(value = "删除", notes = "删除")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("auth:individualauth:delete")
    @PostMapping("/delete")
    public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
        iindividualAuthBiz.deleteById(request.getId());
        return success();
    }*/

    @ApiOperation(value = "导出", notes = "导出")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryMerchantRequest")
    @PostMapping("/report")
    public ResultDto report(@Valid @RequestBody QueryIndividualAuthRequest request, HttpServletResponse response) throws BizException {
        request.setPageNo(0);
        request.setPageSize(0);
        PageResultVO<IndividualAuthResponse> result = iindividualAuthBiz.queryList(request);
        String fileName ="个体户注册订单列表";
        // 循环数据处理
        List<List<Object>> values = new ArrayList<>();
        List<IndividualAuthReportResponse> list = new ArrayList<IndividualAuthReportResponse>();
        for(IndividualAuthResponse resp:result.getList()){
            IndividualAuthReportResponse reportResponse = new IndividualAuthReportResponse();
            BeanUtil.copyProperties(resp, reportResponse);
            if(resp.getOrderStatus()!=null){
                if(resp.getOrderStatus()== YuncaiOrderStatusEnum.TO_BE_SIGN.getType()){
                    reportResponse.setStatus(YuncaiOrderStatusEnum.TO_BE_SIGN.getRemark());
                }else if(resp.getOrderStatus()==YuncaiOrderStatusEnum.TO_BE_VIDEO.getType()){
                    reportResponse.setStatus(YuncaiOrderStatusEnum.TO_BE_VIDEO.getRemark());
                }else if(resp.getOrderStatus()==YuncaiOrderStatusEnum.TO_BE_SURE.getType()){
                    reportResponse.setStatus(YuncaiOrderStatusEnum.TO_BE_SURE.getRemark());
                }else if(resp.getOrderStatus()==YuncaiOrderStatusEnum.TO_BE_PAY.getType()){
                    reportResponse.setStatus(YuncaiOrderStatusEnum.TO_BE_PAY.getRemark());
                }else if(resp.getOrderStatus()==YuncaiOrderStatusEnum.TO_BE_CERTIFICATION.getType()){
                    reportResponse.setStatus(YuncaiOrderStatusEnum.TO_BE_CERTIFICATION.getRemark());
                }else if(resp.getOrderStatus()==YuncaiOrderStatusEnum.COMPLETED.getType()){
                    reportResponse.setStatus(YuncaiOrderStatusEnum.COMPLETED.getRemark());
                }else if(resp.getOrderStatus()==YuncaiOrderStatusEnum.CANCELLED.getType()){
                    reportResponse.setStatus(YuncaiOrderStatusEnum.CANCELLED.getRemark());
                }else if(resp.getOrderStatus()==YuncaiOrderStatusEnum.FAILED.getType()){
                    reportResponse.setStatus(YuncaiOrderStatusEnum.FAILED.getRemark());
                }else if(resp.getOrderStatus()==YuncaiOrderStatusEnum.REJECTED.getType()){
                    reportResponse.setStatus(YuncaiOrderStatusEnum.REJECTED.getRemark());
                }else if(resp.getOrderStatus()==YuncaiOrderStatusEnum.TO_BE_VALIDATE.getType()){
                    reportResponse.setStatus(YuncaiOrderStatusEnum.TO_BE_VALIDATE.getRemark());
                }else if(resp.getOrderStatus()==YuncaiOrderStatusEnum.REGISTRATION_UNDER_WAY.getType()){
                    reportResponse.setStatus(YuncaiOrderStatusEnum.REGISTRATION_UNDER_WAY.getRemark());
                }else if(resp.getOrderStatus()==YuncaiOrderStatusEnum.SIGNATURE_TO_BE_SUBMITTED.getType()){
                    reportResponse.setStatus(YuncaiOrderStatusEnum.SIGNATURE_TO_BE_SUBMITTED.getRemark());
                }else if(resp.getOrderStatus()==YuncaiOrderStatusEnum.SIGNATURE_CONFIRMATION.getType()){
                    reportResponse.setStatus(YuncaiOrderStatusEnum.SIGNATURE_CONFIRMATION.getRemark());
                }
            }
            if(resp.getIsSelfPaying()==1){
                reportResponse.setIsSelfPaying("供方");
            }else  if(resp.getIsSelfPaying()==2){
                reportResponse.setIsSelfPaying("企业");
            }
            list.add(reportResponse);
        }
        for(IndividualAuthReportResponse reportResponse:list){
            List<Object> temp = new ArrayList<>();
            temp.add(reportResponse.getOrderNo());
            temp.add(reportResponse.getAccountName());
            temp.add(reportResponse.getAccount());
            temp.add(reportResponse.getSupplierName());
            temp.add(reportResponse.getPayAmount());
            temp.add(reportResponse.getIsSelfPaying());
            temp.add(reportResponse.getCreateTime());
            temp.add(reportResponse.getStatus());
            values.add(temp);
        }
        _downloadFile(fileName, TITLE, values, response);
        return success();
    }
}
