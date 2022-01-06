package com.guojin.srm.oss.web.contract;

import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.common.response.OperationLogResponse;
import com.guojin.srm.api.bean.contract.request.AddContractTempleteRequest;
import com.guojin.srm.api.bean.contract.request.QueryContractTempleteRequest;
import com.guojin.srm.api.bean.contract.request.UpdateContractTempleteRequest;
import com.guojin.srm.api.bean.contract.response.ContractTempleteResponse;
import com.guojin.srm.api.biz.common.IOperationLogBiz;
import com.guojin.srm.api.biz.contract.IContractTempleteBiz;
import com.guojin.srm.common.base.IDParamRequest;
import com.guojin.srm.common.base.PageResultVO;
import com.guojin.srm.common.enums.LogTypeEnum;
import com.guojin.srm.common.enums.PartyTypeEnum;
import com.guojin.srm.common.exception.BizException;
import com.guojin.srm.common.redis.RedisService;
import com.guojin.srm.common.utils.BeanUtil;
import com.guojin.srm.common.utils.ResultDto;
import com.guojin.srm.oss.web.BaseBusinessController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * @author yuqian
 * @email yuqian@99366.com
 * @date 2021年10月26日 13时43分15秒
 */

@Api(value = "ContractTempleteController", tags = {"合同模板"})
@RestController
@RequestMapping("/contract/contractTemplete")
@Slf4j
public class ContractTempleteController extends BaseBusinessController {

    @Reference
    private IContractTempleteBiz icontractTempleteBiz;

    @Reference
    private IOperationLogBiz operationLogBiz;

    @Autowired
    private RedisService redisService;


    @ApiOperation(value = "获取列表信息", notes = "获取列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryContractTempleteRequest")
    @RequiresPermissions("contract:contracttemplete:list")
    @PostMapping("/list")
    public ResultDto<PageResultVO<ContractTempleteResponse>> list(@Valid @RequestBody QueryContractTempleteRequest request) throws BizException {
        //查询列表数据
        if(PartyTypeEnum.MCHNT.getValue() == getParty().getPartyType()){
            request.setMerchantNo(getParty().getPartyCode());
        }
        PageResultVO<ContractTempleteResponse> result = icontractTempleteBiz.queryList(request);
        return success(result);
    }


    @ApiOperation(value = "获取详细信息", notes = "获取详细信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("contract:contracttemplete:info")
    @PostMapping("/info")
    public ResultDto<ContractTempleteResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
        ContractTempleteResponse contractTempleteResponse = icontractTempleteBiz.queryContractTemplete(request.getId());
        List<OperationLogResponse> logList = operationLogBiz.queryByBizId(contractTempleteResponse.getTempleteNo(), LogTypeEnum.CONTRACT_TEMPLETE);
        contractTempleteResponse.setLogList(logList);
        return success(contractTempleteResponse);
    }


    @ApiOperation(value = "新增", notes = "获取新增信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddContractTempleteRequest")
    @RequiresPermissions("contract:contracttemplete:save")
    @PostMapping("/save")
    public ResultDto save(@RequestBody @Valid AddContractTempleteRequest request) throws BizException {
        icontractTempleteBiz.adminSave(request,getUser(),getParty());
        return success();
    }

    @ApiOperation(value = "修改", notes = "修改")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateContractTempleteRequest")
    @RequiresPermissions("contract:contracttemplete:update")
    @PostMapping("/update")
    public ResultDto update(@RequestBody @Valid UpdateContractTempleteRequest request) throws BizException {
        icontractTempleteBiz.adminUpdate(request,getUser());
        return success();
    }

//    @ApiOperation(value = "删除", notes = "删除")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
//    @RequiresPermissions("contract:contracttemplete:delete")
//    @PostMapping("/delete")
//    public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
//        icontractTempleteBiz.deleteById(request.getId(), getUser());
//        return success();
//    }
}
