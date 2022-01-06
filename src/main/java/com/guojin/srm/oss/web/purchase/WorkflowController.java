package com.guojin.srm.oss.web.purchase;

import javax.validation.Valid;

import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.purchase.request.AddWorkflowRequest;
import com.guojin.srm.api.bean.purchase.request.QueryWorkflowRequest;
import com.guojin.srm.api.bean.purchase.request.UpdateWorkflowRequest;
import com.guojin.srm.api.bean.purchase.response.WorkflowResponse;
import com.guojin.srm.api.biz.purchase.IWorkflowBiz;
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

/**
 * @author yuqian
 * @email yuqian@99366.com
 * @date 2021年11月04日 12时00分26秒
 */

@Api(value = "WorkflowController", tags = { "工作流表" })
@RestController
@RequestMapping("/purchase/workflow")
@Slf4j
public class WorkflowController extends BaseBusinessController {

	@Reference
	private IWorkflowBiz iworkflowBiz;

	@ApiOperation(value = "获取列表信息", notes = "获取列表信息")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryWorkflowRequest")
	@RequiresPermissions("purchase:workflow:list")
	@PostMapping("/list")
	public ResultDto<PageResultVO<WorkflowResponse>> list(@Valid @RequestBody QueryWorkflowRequest request)
			throws BizException {
		// 查询列表数据
		request.setUserId(this.getUserId());
		request.setMerchantNo(this.getParty().getPartyCode());
		return success(iworkflowBiz.queryList(request));

	}

//	@ApiOperation(value = "获取详细信息", notes = "获取详细信息")
//	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
//	@RequiresPermissions("purchase:workflow:info")
//	@PostMapping("/info")
//	public ResultDto<WorkflowResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
//		WorkflowResponse workflowResponse = iworkflowBiz.queryWorkflow(request.getId());
//		return success(workflowResponse);
//	}
//
//	@ApiOperation(value = "新增", notes = "获取新增信息")
//	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddWorkflowRequest")
//	@RequiresPermissions("purchase:workflow:save")
//	@PostMapping("/save")
//	public ResultDto save(@RequestBody @Valid AddWorkflowRequest request) throws BizException {
//		iworkflowBiz.save(request);
//		return success();
//	}
//
//	@ApiOperation(value = "修改", notes = "修改")
//	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateWorkflowRequest")
//	@RequiresPermissions("purchase:workflow:update")
//	@PostMapping("/update")
//	public ResultDto update(@RequestBody @Valid UpdateWorkflowRequest request) throws BizException {
//		iworkflowBiz.update(request);
//		return success();
//	}
//
//	@ApiOperation(value = "删除", notes = "删除")
//	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
//	@RequiresPermissions("purchase:workflow:delete")
//	@PostMapping("/delete")
//	public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
//		iworkflowBiz.deleteById(request.getId());
//		return success();
//	}
}
