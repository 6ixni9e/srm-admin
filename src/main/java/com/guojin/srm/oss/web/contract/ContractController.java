package com.guojin.srm.oss.web.contract;

import cn.hutool.core.util.StrUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.contract.request.QueryContractRequest;
import com.guojin.srm.api.bean.contract.response.ContractExtendResponse;
import com.guojin.srm.api.bean.contract.response.ContractResponse;
import com.guojin.srm.api.biz.contract.IContractBiz;
import com.guojin.srm.api.biz.org.IMerchantBiz;
import com.guojin.srm.common.base.ExtendPageResultVO;
import com.guojin.srm.common.base.IDParamRequest;
import com.guojin.srm.common.constant.BizBaseConstant;
import com.guojin.srm.common.enums.BaseExcCodesEnum;
import com.guojin.srm.common.enums.ContractStatusEnum;
import com.guojin.srm.common.enums.PartyTypeEnum;
import com.guojin.srm.common.exception.BizException;
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
import java.util.stream.Collectors;

/**
 * @author yuqian
 * @email yuqian@99366.com
 * @date 2021年10月26日 13时43分15秒
 */

@Api(value = "ContractController", tags = {"采购任务-合同管理"})
@RestController
@RequestMapping("/contract/contract")
@Slf4j
public class ContractController extends BaseBusinessController {

    @Reference
    private IContractBiz icontractBiz;

    @Reference
    private IMerchantBiz merchantBiz;


    @ApiOperation(value = "获取列表信息", notes = "获取列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryContractRequest")
    @RequiresPermissions("contract:contract:list")
    @PostMapping("/list")
    public ResultDto<ExtendPageResultVO<ContractResponse, ContractExtendResponse>> list(@Valid @RequestBody QueryContractRequest request) throws BizException {
        if(PartyTypeEnum.OEM.getValue() == getParty().getPartyType()){
            request.setOemCode(getParty().getPartyCode());
        }else if(PartyTypeEnum.MCHNT.getValue()== getParty().getPartyType()){
            request.setMerchantNo(getParty().getPartyCode());
        }
        //查询列表数据
        ExtendPageResultVO<ContractResponse, ContractExtendResponse> result = icontractBiz.queryList(request,getUserId());
        return success(result);
    }


    @ApiOperation(value = "获取详细信息", notes = "获取详细信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("contract:contract:info")
    @PostMapping("/info")
    public ResultDto<ContractResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
        ContractResponse contractResponse = icontractBiz.queryContract(request.getId());
        return success(contractResponse);
    }


    @ApiOperation(value = "导出列表", notes = "导出列表")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryContractRequest")
    @RequiresPermissions("contract:contract:download")
    @PostMapping("/download")
    public ResultDto download(@RequestBody QueryContractRequest request, HttpServletResponse response) throws BizException {
        request.setPageSize(0);
        if(PartyTypeEnum.OEM.getValue() == getParty().getPartyType()){
            request.setOemCode(getParty().getPartyCode());
        }else if(PartyTypeEnum.MCHNT.getValue()== getParty().getPartyType()){
            request.setMerchantNo(getParty().getPartyCode());
        }
        ExtendPageResultVO<ContractResponse, ContractExtendResponse> result = icontractBiz.queryList(request,getUserId());
        if (result.getTotalCount() <= 0) {
            throw new BizException(BaseExcCodesEnum.REMOTE_COMMIT_FAIL, BizBaseConstant.DOWNLOAD_DATA_IS_NULL);
        }
        List<List<Object>> fileData= result.getList().stream().map(contractResponse-> {
            List<Object> temp = new ArrayList<>();
            temp.add(contractResponse.getContractNo());
            temp.add(contractResponse.getContractName());
            temp.add(contractResponse.getAmount());
            temp.add(contractResponse.getTaskName());
            temp.add(StrUtil.isNotBlank(contractResponse.getPlanName())?contractResponse.getPlanName():contractResponse.getProjectName());
            temp.add(contractResponse.getCreateTime());
            temp.add(ContractStatusEnum.getByStatus(contractResponse.getStatus()).getDesc() );
            return temp;
        }).collect(Collectors.toList());
        _downloadFile("Contract_" + getParty().getPartyCode() + ".xls",  new String[]{"合同编号", "合同名称", "合同类型", "合同金额", "所属任务", "所属计划/项目", "发起时间", "状态"}, fileData, response);
        return success();
    }


}
