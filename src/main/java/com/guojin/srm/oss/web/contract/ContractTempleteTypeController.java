package com.guojin.srm.oss.web.contract;

import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.common.response.LabelResponse;
import com.guojin.srm.api.bean.contract.request.AddContractTempleteTypeRequest;
import com.guojin.srm.api.bean.contract.request.QueryContractTempleteTypeRequest;
import com.guojin.srm.api.bean.contract.request.UpdateContractTempleteTypeRequest;
import com.guojin.srm.api.bean.contract.response.ContractTempleteTypeResponse;
import com.guojin.srm.api.biz.contract.IContractTempleteTypeBiz;
import com.guojin.srm.common.base.IDParamRequest;
import com.guojin.srm.common.base.PageResultVO;
import com.guojin.srm.common.enums.BaseExcCodesEnum;
import com.guojin.srm.common.enums.PartyTypeEnum;
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

import javax.validation.Valid;
import java.util.List;

/**
 * @author yuqian
 * @email yuqian@99366.com
 * @date 2021年10月26日 13时43分15秒
 */

@Api(value = "ContractTempleteTypeController", tags = {"合同模板类型表"})
@RestController
@RequestMapping("/contract/contractTempleteType")
@Slf4j
public class ContractTempleteTypeController extends BaseBusinessController {

    @Reference
    private IContractTempleteTypeBiz icontractTempleteTypeBiz;


    @ApiOperation(value = "获取列表信息", notes = "获取列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryContractTempleteTypeRequest")
    @RequiresPermissions("contract:contracttempletetype:list")
    @PostMapping("/list")
    public ResultDto<PageResultVO<ContractTempleteTypeResponse>> list(@Valid @RequestBody QueryContractTempleteTypeRequest request) throws BizException {
        SessParty party = getParty();
        if(party == null){
            throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
        }
        if(PartyTypeEnum.OEM.getValue() != party.getPartyType() && PartyTypeEnum.PLATFORM.getValue() != party.getPartyType()){
            throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
        }
        //查询列表数据
        PageResultVO<ContractTempleteTypeResponse> result = icontractTempleteTypeBiz.queryList(request);
        return success(result);
    }


    @ApiOperation(value = "获取详细信息", notes = "获取详细信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("contract:contracttempletetype:info")
    @PostMapping("/info")
    public ResultDto<ContractTempleteTypeResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
        SessParty party = getParty();
        if(party == null){
            throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
        }
        if(PartyTypeEnum.OEM.getValue() != party.getPartyType() && PartyTypeEnum.PLATFORM.getValue() != party.getPartyType()){
            throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
        }
        ContractTempleteTypeResponse contractTempleteTypeResponse = icontractTempleteTypeBiz.queryContractTempleteType(request.getId());
        return success(contractTempleteTypeResponse);
    }


    @ApiOperation(value = "新增", notes = "获取新增信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddContractTempleteTypeRequest")
    @RequiresPermissions("contract:contracttempletetype:save")
    @PostMapping("/save")
    public ResultDto save(@RequestBody @Valid AddContractTempleteTypeRequest request) throws BizException {
        SessParty party = getParty();
        if(party == null){
            throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
        }
        if(PartyTypeEnum.OEM.getValue() != party.getPartyType() && PartyTypeEnum.PLATFORM.getValue() != party.getPartyType()){
            throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
        }
        icontractTempleteTypeBiz.save(request,getUser());
        return success();
    }

    @ApiOperation(value = "修改", notes = "修改")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateContractTempleteTypeRequest")
    @RequiresPermissions("contract:contracttempletetype:update")
    @PostMapping("/update")
    public ResultDto update(@RequestBody @Valid UpdateContractTempleteTypeRequest request) throws BizException {
        SessParty party = getParty();
        if(party == null){
            throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
        }
        if(PartyTypeEnum.OEM.getValue() != party.getPartyType() && PartyTypeEnum.PLATFORM.getValue() != party.getPartyType()){
            throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
        }
        icontractTempleteTypeBiz.update(request,getUser());
        return success();
    }

/*    @ApiOperation(value = "删除", notes = "删除")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("contract:contracttempletetype:delete")
    @PostMapping("/delete")
    public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
        icontractTempleteTypeBiz.deleteById(request.getId());
        return success();
    }*/

    @ApiOperation(value = "获取下拉列表信息", notes = "获取下拉列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryOemRequest")
    @PostMapping("/listLabel")
    public ResultDto<List<LabelResponse>> listLabel(@Valid @RequestBody QueryContractTempleteTypeRequest request) throws BizException {
        //查询列表数据
        List<LabelResponse> result = icontractTempleteTypeBiz.labelList(request);
        return success(result);
    }

    @ApiOperation(value = "获取所有下拉列表信息", notes = "获取所有下拉列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryOemRequest")
    @PostMapping("/listAllLabel")
    public ResultDto<List<LabelResponse>> listAllLabel(@Valid @RequestBody QueryContractTempleteTypeRequest request) throws BizException {
        //查询列表数据
        List<LabelResponse> result = icontractTempleteTypeBiz.queryAllLabelList(request);
        return success(result);
    }
}
