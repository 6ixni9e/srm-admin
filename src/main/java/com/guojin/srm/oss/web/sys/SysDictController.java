package com.guojin.srm.oss.web.sys;

import javax.validation.Valid;


import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.sys.request.*;
import com.guojin.srm.api.bean.sys.response.SysDictCodeResponse;
import com.guojin.srm.api.bean.sys.response.SysDictResponse;
import com.guojin.srm.api.biz.sys.ISysDictBiz;
import com.guojin.srm.api.biz.sys.ISysDictCodeBiz;
import com.guojin.srm.oss.web.BaseBusinessController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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


/**
 * @author yuqian
 * @email yuqian@99366.com
 * @date 2021年10月11日 13时37分47秒
 */

@Api(value = "SysDictController", tags = {"字典管理"})
@RestController
@RequestMapping("/sys/dict")
@Slf4j
public class SysDictController extends BaseBusinessController {

    @Reference
    private ISysDictBiz isysDictBiz;
    @Reference
	private ISysDictCodeBiz iSysDictCodeBiz;


    @ApiOperation(value = "获取字典组列表", notes = "获取列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QuerySysDictRequest")
    @RequiresPermissions("sys:sysdict:list")
    @PostMapping("/list")
    public ResultDto<PageResultVO<SysDictResponse>> list(@Valid @RequestBody QuerySysDictRequest request) throws BizException {
        //查询列表数据
        PageResultVO<SysDictResponse> result = isysDictBiz.queryList(request);
        return success(result);
    }


    @ApiOperation(value = "查看字典组详情", notes = "获取详细信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("sys:sysdict:info")
    @PostMapping("/info")
    public ResultDto<SysDictResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
        SysDictResponse sysDictResponse = isysDictBiz.querySysDict(request.getId());
        //根据字典ID查询字典明细列表
		if(sysDictResponse == null){
			return fail("字典不存在");
		}
		QuerySysDictCodeRequest reqTemp = new QuerySysDictCodeRequest();
		reqTemp.setDictId(sysDictResponse.getId());
		PageResultVO<SysDictCodeResponse> pageListData = iSysDictCodeBiz.queryList(reqTemp);
		sysDictResponse.setPageListData(pageListData);
        return success(sysDictResponse);
    }

	@ApiOperation(value = "获取字典明细列表", notes = "获取明细列表信息")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QuerySysDictCodeRequest")
	@RequiresPermissions("sys:dict:info")
	@PostMapping("/detaillist")
	public ResultDto<SysDictResponse> detailList(@RequestBody QuerySysDictCodeRequest request) throws BizException {
		SysDictResponse response = isysDictBiz.querySysDict(request.getDictId());
		//根据字典ID查询字典明细列表
		if(response == null){
			return fail("字典不存在");
		}
		// 查询列表数据
		PageResultVO<SysDictCodeResponse> pageListData = iSysDictCodeBiz.queryList(request);
		response.setPageListData(pageListData);
		return success(response);
	}


    @ApiOperation(value = "新增字典组", notes = "获取新增信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddSysDictRequest")
    @RequiresPermissions("sys:sysdict:save")
    @PostMapping("/save")
    public ResultDto saveDict(@RequestBody @Valid AddSysDictRequest request) throws BizException {
        isysDictBiz.save(request);
        return success();
    }

	@ApiOperation(value = "新增字典明细", notes = "获取新增信息")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddSysDictCodeRequest")
	@RequiresPermissions("sys:dict:save") // @SysLog("保存")
	@PostMapping("/saveDictDetail")
	public ResultDto saveDictDetail(@RequestBody @Valid AddSysDictCodeRequest request) throws BizException {
		iSysDictCodeBiz.save(request);
		return success();
	}

    @ApiOperation(value = "修改字典组信息", notes = "修改")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateSysDictRequest")
    @RequiresPermissions("sys:sysdict:update")
    @PostMapping("/updateDict")
    public ResultDto updateDict(@RequestBody @Valid UpdateSysDictRequest request) throws BizException {
        isysDictBiz.update(request);
        return success();
    }

	@ApiOperation(value = "修改字典明细信息", notes = "修改")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateSysDictCodeRequest")
	@RequiresPermissions("sys:dict:update") // @SysLog("修改")
	@PostMapping("/updateDictDetail")
	public ResultDto updateDictDetail(@RequestBody @Valid UpdateSysDictCodeRequest request) throws BizException {
		iSysDictCodeBiz.update(request);
		return success();
	}

    @ApiOperation(value = "删除字典组", notes = "删除")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("sys:sysdict:deleteDict")
    @PostMapping("/deleteDict")
    public ResultDto deleteDict(@Valid @RequestBody IDParamRequest request) throws BizException {
        isysDictBiz.deleteById(request.getId());
        return success();
    }

	@ApiOperation(value = "删除字典明细", notes = "删除")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
	@RequiresPermissions("sys:sysdict:deleteDict")
	@PostMapping("/deleteDictDetail")
	public ResultDto deleteDictDetail(@Valid @RequestBody IDParamRequest request) throws BizException {
		iSysDictCodeBiz.deleteById(request.getId());
		return success();
	}
}
