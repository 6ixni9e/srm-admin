//package com.guojin.srm.oss.web.purchase;
//
//import javax.validation.Valid;
//
//import com.alibaba.dubbo.config.annotation.Reference;
//import com.guojin.srm.api.bean.purchase.request.AddWorkflowConfigRequest;
//import com.guojin.srm.api.bean.purchase.request.QueryWorkflowConfigRequest;
//import com.guojin.srm.api.bean.purchase.request.UpdateWorkflowConfigRequest;
//import com.guojin.srm.api.bean.purchase.response.WorkflowConfigResponse;
//import com.guojin.srm.api.biz.purchase.IWorkflowConfigBiz;
//import com.guojin.srm.oss.web.BaseBusinessController;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.guojin.srm.common.base.IDParamRequest;
//import com.guojin.srm.common.base.PageResultVO;
//import com.guojin.srm.common.exception.BizException;
//import com.guojin.srm.common.utils.ResultDto;
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * @author yuqian
// * @email yuqian@99366.com
// * @date 2021年11月04日 12时00分26秒
// */
//
//@Api(value = "WorkflowConfigController", tags = { "工作流设置表" })
//@RestController
//@RequestMapping("/purchase/workflowConfig")
//@Slf4j
//public class WorkflowConfigController extends BaseBusinessController {
//
//	@Reference
//	private IWorkflowConfigBiz iworkflowConfigBiz;
//
//	@ApiOperation(value = "获取列表信息", notes = "获取列表信息")
//	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryWorkflowConfigRequest")
//	@RequiresPermissions("purchase:workflowconfig:list")
//	@PostMapping("/list")
//	public ResultDto<PageResultVO<WorkflowConfigResponse>> list(@Valid @RequestBody QueryWorkflowConfigRequest request)
//			throws BizException {
//		// 查询列表数据
//		PageResultVO<WorkflowConfigResponse> result = iworkflowConfigBiz.queryList(request);
//		return success(result);
//	}
//
//	@ApiOperation(value = "获取详细信息", notes = "获取详细信息")
//	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
//	@RequiresPermissions("purchase:workflowconfig:info")
//	@PostMapping("/info")
//	public ResultDto<WorkflowConfigResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
//		WorkflowConfigResponse workflowConfigResponse = iworkflowConfigBiz.queryWorkflowConfig(request.getId());
//		return success(workflowConfigResponse);
//	}
//
//	@ApiOperation(value = "新增", notes = "获取新增信息")
//	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddWorkflowConfigRequest")
//	@RequiresPermissions("purchase:workflowconfig:save")
//	@PostMapping("/save")
//	public ResultDto save(@RequestBody @Valid AddWorkflowConfigRequest request) throws BizException {
//		iworkflowConfigBiz.save(request);
//		return success();
//	}
//
//	@ApiOperation(value = "修改", notes = "修改")
//	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateWorkflowConfigRequest")
//	@RequiresPermissions("purchase:workflowconfig:update")
//	@PostMapping("/update")
//	public ResultDto update(@RequestBody @Valid UpdateWorkflowConfigRequest request) throws BizException {
//		iworkflowConfigBiz.update(request);
//		return success();
//	}
//
//	@ApiOperation(value = "删除", notes = "删除")
//	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
//	@RequiresPermissions("purchase:workflowconfig:delete")
//	@PostMapping("/delete")
//	public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
//		iworkflowConfigBiz.deleteById(request.getId());
//		return success();
//	}
//}
