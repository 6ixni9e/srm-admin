package com.guojin.srm.oss.web.auth;

import javax.validation.Valid;


import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.auth.request.AddCompanyAuthRequest;
import com.guojin.srm.api.bean.auth.request.QueryCompanyAuthRequest;
import com.guojin.srm.api.bean.auth.request.UpdateCompanyAuthRequest;
import com.guojin.srm.api.bean.auth.response.CompanyAuthResponse;
import com.guojin.srm.api.bean.common.response.OperationLogResponse;
import com.guojin.srm.api.biz.auth.ICompanyAuthBiz;
import com.guojin.srm.api.biz.common.IOperationLogBiz;
import com.guojin.srm.common.enums.LogTypeEnum;
import com.guojin.srm.common.enums.PartyTypeEnum;
import com.guojin.srm.oss.biz.AliyunOssBiz;
import com.guojin.srm.oss.biz.ElectronicSignatureBiz;
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
 * @date 2021年10月26日 13时40分14秒
 */

@Api(value = "CompanyAuthController", tags = {"企业认证表"})
@RestController
@RequestMapping("/auth/companyAuth")
@Slf4j
public class CompanyAuthController extends BaseBusinessController {

    @Reference
    private ICompanyAuthBiz icompanyAuthBiz;

    @Autowired
    private AliyunOssBiz aliyunOssBiz;

    @Reference
    private IOperationLogBiz operationLogBiz;

    @ApiOperation(value = "获取列表信息", notes = "获取列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryCompanyAuthRequest")
    @RequiresPermissions("auth:companyauth:list")
    @PostMapping("/list")
    public ResultDto<PageResultVO<CompanyAuthResponse>> list(@Valid @RequestBody QueryCompanyAuthRequest request) throws BizException {
        //查询列表数据
        if(PartyTypeEnum.OEM.getValue() == getParty().getPartyType()){
            request.setOemCode(getUser().getOemCode());
        }
        PageResultVO<CompanyAuthResponse> result = icompanyAuthBiz.queryList(request);
        return success(result);
    }


    @ApiOperation(value = "获取详细信息", notes = "获取详细信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("auth:companyauth:info")
    @PostMapping("/info")
    public ResultDto<CompanyAuthResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
        CompanyAuthResponse companyAuthResponse = icompanyAuthBiz.queryCompanyAuth(request.getId());
        if (companyAuthResponse.getLicenseUrl() != null) {
            companyAuthResponse.setLicenseUrl(aliyunOssBiz.getPrivateUrl(null, companyAuthResponse.getLicenseUrl()));
        }
        if (companyAuthResponse.getLegalIdcardHaflbody() != null) {
            companyAuthResponse.setLegalIdcardHaflbody(aliyunOssBiz.getPrivateUrl(null, companyAuthResponse.getLegalIdcardHaflbody()));
        }
        if (companyAuthResponse.getLegalIdcardBack() != null) {
            companyAuthResponse.setLegalIdcardBack(aliyunOssBiz.getPrivateUrl(null, companyAuthResponse.getLegalIdcardBack()));
        }
        if (companyAuthResponse.getLegalIdcardFront() != null) {
            companyAuthResponse.setLegalIdcardFront(aliyunOssBiz.getPrivateUrl(null, companyAuthResponse.getLegalIdcardFront()));
        }
        String[] legalImg = new String[]{companyAuthResponse.getLegalIdcardFront(), companyAuthResponse.getLegalIdcardBack()};
        String[] licenseImg = new String[]{companyAuthResponse.getLicenseUrl(), companyAuthResponse.getLegalIdcardHaflbody()};
        companyAuthResponse.setLicenseImg(licenseImg);
        companyAuthResponse.setLegalImg(legalImg);
        List<OperationLogResponse> logList = operationLogBiz.queryByBizId(companyAuthResponse.getBelong(), LogTypeEnum.AUTH);
        companyAuthResponse.setLogList(logList);
        return success(companyAuthResponse);
    }


    @ApiOperation(value = "新增", notes = "获取新增信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddCompanyAuthRequest")
    @RequiresPermissions("auth:companyauth:save")
    @PostMapping("/save")
    public ResultDto save(@RequestBody @Valid AddCompanyAuthRequest request) throws BizException {
        icompanyAuthBiz.save(request, getUser(), getParty());
        return success();
    }

    @ApiOperation(value = "修改", notes = "修改")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateCompanyAuthRequest")
    @RequiresPermissions("auth:companyauth:update")
    @PostMapping("/update")
    public ResultDto update(@RequestBody @Valid UpdateCompanyAuthRequest request) throws BizException {
        icompanyAuthBiz.update(request, getUser());
        return success();
    }

  /*  @ApiOperation(value = "删除", notes = "删除")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("auth:companyauth:delete")
    @PostMapping("/delete")
    public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
        icompanyAuthBiz.deleteById(request.getId());
        return success();
    }*/
}
