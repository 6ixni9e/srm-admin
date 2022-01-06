package com.guojin.srm.oss.web.common;

import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.common.request.AddBusincessScopeRequest;
import com.guojin.srm.api.bean.common.request.QueryBusincessScopeRequest;
import com.guojin.srm.api.bean.common.request.UpdateBusincessScopeRequest;
import com.guojin.srm.api.bean.common.response.BusincessScopeResponse;
import com.guojin.srm.api.bean.common.response.TaxClassTreeResponse;
import com.guojin.srm.api.biz.common.IBusincessScopeBiz;
import com.guojin.srm.common.base.IDParamRequest;
import com.guojin.srm.common.base.PageResultVO;
import com.guojin.srm.common.constant.BizBaseConstant;
import com.guojin.srm.common.enums.BaseExcCodesEnum;
import com.guojin.srm.common.enums.PartyTypeEnum;
import com.guojin.srm.common.exception.BizException;
import com.guojin.srm.common.session.SessParty;
import com.guojin.srm.common.utils.BeanUtil;
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
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yuqian
 * @email yuqian@99366.com
 * @date 2021年11月10日 16时49分09秒
 */

@Api(value = "BusincessScopeController", tags = {"经营范围表"})
@RestController
@RequestMapping("/common/busincessScope")
@Slf4j
public class BusincessScopeController extends BaseBusinessController {

    @Reference
    private IBusincessScopeBiz ibusincessScopeBiz;
//
//
//    @ApiOperation(value = "获取列表信息", notes = "获取列表信息")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryBusincessScopeRequest")
//    @RequiresPermissions("common:busincessscope:list")
//    @PostMapping("/list")
//    public ResultDto<PageResultVO<BusincessScopeResponse>> list(@Valid @RequestBody QueryBusincessScopeRequest request) throws BizException {
//        //查询列表数据
//        PageResultVO<BusincessScopeResponse> result = ibusincessScopeBiz.queryList(request);
//        return success(result);
//    }


//    @ApiOperation(value = "获取详细信息", notes = "获取详细信息")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
//    @RequiresPermissions("common:busincessscope:info")
//    @PostMapping("/info")
//    public ResultDto<BusincessScopeResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
//        BusincessScopeResponse busincessScopeResponse = ibusincessScopeBiz.queryBusincessScope(request.getId());
//        return success(busincessScopeResponse);
//    }

//
//    @ApiOperation(value = "新增", notes = "获取新增信息")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddBusincessScopeRequest")
//    @RequiresPermissions("common:busincessscope:save")
//    @PostMapping("/save")
//    public ResultDto save(@RequestBody @Valid AddBusincessScopeRequest request) throws BizException {
//        ibusincessScopeBiz.save(request);
//        return success();
//    }
//
//    @ApiOperation(value = "修改", notes = "修改")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateBusincessScopeRequest")
//    @RequiresPermissions("common:busincessscope:update")
//    @PostMapping("/update")
//    public ResultDto update(@RequestBody @Valid UpdateBusincessScopeRequest request) throws BizException {
//        ibusincessScopeBiz.update(request);
//        return success();
//    }
//
//    @ApiOperation(value = "删除", notes = "删除")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
//    @RequiresPermissions("common:busincessscope:delete")
//    @PostMapping("/delete")
//    public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
//        ibusincessScopeBiz.deleteById(request.getId());
//        return success();
//    }


    @ApiOperation(value = "获取行业、商品分类、经营范围树", notes = "获取行业、商品分类、经营范围树")
    @RequiresPermissions("common:busincessscope:tree")
    @PostMapping("/tree")
    public ResultDto<TaxClassTreeResponse> tree() throws BizException {
        SessParty party = getParty();
        if(party == null){
            throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
        }
        if(PartyTypeEnum.OEM.getValue() != party.getPartyType() && PartyTypeEnum.PLATFORM.getValue() != party.getPartyType()){
            throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
        }
        TaxClassTreeResponse taxClassTreeResponse = ibusincessScopeBiz.tree();
        return success(taxClassTreeResponse);
    }


    @ApiOperation(value = "获取列表信息", notes = "获取列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryBusincessScopeRequest")
    @RequiresPermissions("common:busincessscope:list")
    @PostMapping("/list")
    public ResultDto<PageResultVO<BusincessScopeResponse>> list(@Valid @RequestBody QueryBusincessScopeRequest request) throws BizException {
        SessParty party = getParty();
        if(party == null){
            throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
        }
        if(PartyTypeEnum.OEM.getValue() != party.getPartyType() && PartyTypeEnum.PLATFORM.getValue() != party.getPartyType()){
            throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
        }
        //查询列表数据
        PageResultVO<BusincessScopeResponse> result = ibusincessScopeBiz.queryList(request);
        return success(result);
    }


    @ApiOperation(value = "导出列表", notes = "导出列表")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryBusincessScopeRequest")
    @RequiresPermissions("common:busincessscope:download")
    @PostMapping("/download")
    public ResultDto download(@RequestBody @Valid QueryBusincessScopeRequest request, HttpServletResponse response) throws BizException {
        SessParty party = getParty();
        if(party == null){
            throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
        }
        if(PartyTypeEnum.OEM.getValue() != party.getPartyType() && PartyTypeEnum.PLATFORM.getValue() != party.getPartyType()){
            throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
        }
        request.setPageSize(0);
        PageResultVO<BusincessScopeResponse> result = ibusincessScopeBiz.queryList(request);
        //转化响应对象
        PageResultVO<Map<String, Object>> tempResult = new PageResultVO<>();
        BeanUtil.copyProperties(result, tempResult);
        if (tempResult.getTotalCount() <= 0) {
            throw new BizException(BaseExcCodesEnum.REMOTE_COMMIT_FAIL, BizBaseConstant.DOWNLOAD_DATA_IS_NULL);
        }
        List<List<Object>> fileData = tempResult.getList().stream().map(map -> {
            List<Object> temp = new ArrayList<>();
            temp.add(map.get("id"));
            temp.add(map.get("taxClassNo"));
            temp.add(map.get("taxClassName"));
            temp.add(map.get("shortName"));
            temp.add(map.get("busincessName"));
            temp.add(map.get("secondCategoryName"));
            temp.add(map.get("firstCategoryName"));
            temp.add(map.get("categoryType"));
            return temp;
        }).collect(Collectors.toList());
        _downloadFile("税收分类编码", new String[]{"ID", "税收分类编码", "编码名称", "编码简称", "所属经营范围", "所属商品分类", "所属行业类目", "类别"}, fileData, response);
        return success();
    }


}
