package com.guojin.srm.oss.web.purchase;

import javax.validation.Valid;

import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.purchase.request.AddProjectTypeRequest;
import com.guojin.srm.api.bean.purchase.request.QueryProjectTypeRequest;
import com.guojin.srm.api.bean.purchase.request.UpdateProjectTypeRequest;
import com.guojin.srm.api.bean.purchase.response.ProjectTypeResponse;
import com.guojin.srm.api.biz.purchase.IProjectTypeBiz;
import com.guojin.srm.oss.web.BaseBusinessController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guojin.srm.common.base.IDParamRequest;
import com.guojin.srm.common.base.PageResultVO;
import com.guojin.srm.common.exception.BizException;
import com.guojin.srm.common.session.SessUser;
import com.guojin.srm.common.utils.ResultDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yuqian
 * @email yuqian@99366.com
 * @date 2021年10月26日 13时47分38秒
 */

@Api(value = "ProjectTypeController", tags = { "项目类型表" })
@RestController
@RequestMapping("/purchase/projectType")
@Slf4j
public class ProjectTypeController extends BaseBusinessController {

	@Reference
	private IProjectTypeBiz iprojectTypeBiz;

	@ApiOperation(value = "获取列表信息", notes = "获取列表信息")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryProjectTypeRequest")
	@RequiresPermissions("purchase:projecttype:list")
	@PostMapping("/list")
	public ResultDto<PageResultVO<ProjectTypeResponse>> list(@Valid @RequestBody QueryProjectTypeRequest request)
			throws BizException {
		// 查询列表数据
		PageResultVO<ProjectTypeResponse> result = iprojectTypeBiz.queryList(request);
		return success(result);
	}

	@ApiOperation(value = "获取详细信息", notes = "获取详细信息")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
	@RequiresPermissions("purchase:projecttype:info")
	@PostMapping("/info")
	public ResultDto<ProjectTypeResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
		ProjectTypeResponse projectTypeResponse = iprojectTypeBiz.queryProjectType(request.getId());
		return success(projectTypeResponse);
	}

	@ApiOperation(value = "新增", notes = "获取新增信息")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddProjectTypeRequest")
	@RequiresPermissions("purchase:projecttype:save")
	@PostMapping("/save")
	public ResultDto save(@RequestBody @Valid AddProjectTypeRequest request) throws BizException {
		SessUser sessUser = getUser();
		iprojectTypeBiz.save(request,sessUser);
		return success();
	}

	@ApiOperation(value = "修改", notes = "修改")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateProjectTypeRequest")
	@RequiresPermissions("purchase:projecttype:update")
	@PostMapping("/update")
	public ResultDto update(@RequestBody @Valid UpdateProjectTypeRequest request) throws BizException {
		SessUser sessUser = getUser();
		iprojectTypeBiz.update(request,sessUser);
		return success();
	}

/*	@ApiOperation(value = "删除", notes = "删除")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
	@RequiresPermissions("purchase:projecttype:delete")
	@PostMapping("/delete")
	public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
		iprojectTypeBiz.deleteById(request.getId());
		return success();
	}*/
}
