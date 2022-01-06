//package com.guojin.srm.oss.web.common;
//
//import javax.validation.Valid;
//
//import com.alibaba.dubbo.config.annotation.Reference;
//import com.guojin.srm.api.bean.common.request.AddAnnexRequest;
//import com.guojin.srm.api.bean.common.request.QueryAnnexRequest;
//import com.guojin.srm.api.bean.common.request.UpdateAnnexRequest;
//import com.guojin.srm.api.bean.common.response.AnnexResponse;
//import com.guojin.srm.api.biz.common.IAnnexBiz;
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
// * @date 2021年10月26日 13时42分10秒
// */
//
//@Api(value = "AnnexController", tags = { "附件表" })
//@RestController
//@RequestMapping("/common/annex")
//@Slf4j
//public class AnnexController extends BaseBusinessController {
//
//	@Reference
//	private IAnnexBiz iannexBiz;
//
//	@ApiOperation(value = "获取列表信息", notes = "获取列表信息")
//	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryAnnexRequest")
//	@RequiresPermissions("common:annex:list")
//	@PostMapping("/list")
//	public ResultDto<PageResultVO<AnnexResponse>> list(@Valid @RequestBody QueryAnnexRequest request)
//			throws BizException {
//		// 查询列表数据
//		PageResultVO<AnnexResponse> result = iannexBiz.queryList(request);
//		return success(result);
//	}
//
//	@ApiOperation(value = "获取详细信息", notes = "获取详细信息")
//	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
//	@RequiresPermissions("common:annex:info")
//	@PostMapping("/info")
//	public ResultDto<AnnexResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
//		AnnexResponse annexResponse = iannexBiz.queryAnnex(request.getId());
//		return success(annexResponse);
//	}
//
//	@ApiOperation(value = "新增", notes = "获取新增信息")
//	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddAnnexRequest")
//	@RequiresPermissions("common:annex:save")
//	@PostMapping("/save")
//	public ResultDto save(@RequestBody @Valid AddAnnexRequest request) throws BizException {
//		iannexBiz.save(request);
//		return success();
//	}
//
//	@ApiOperation(value = "修改", notes = "修改")
//	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateAnnexRequest")
//	@RequiresPermissions("common:annex:update")
//	@PostMapping("/update")
//	public ResultDto update(@RequestBody @Valid UpdateAnnexRequest request) throws BizException {
//		iannexBiz.update(request);
//		return success();
//	}
//
//	@ApiOperation(value = "删除", notes = "删除")
//	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
//	@RequiresPermissions("common:annex:delete")
//	@PostMapping("/delete")
//	public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
//		iannexBiz.deleteById(request.getId());
//		return success();
//	}
//}
