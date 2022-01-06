package com.guojin.srm.oss.web.purchase;

import javax.validation.Valid;

import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.purchase.request.*;
import com.guojin.srm.api.bean.purchase.response.AddClassifyBackDataResponse;
import com.guojin.srm.api.bean.purchase.response.GoodsCategoryResponse;
import com.guojin.srm.api.biz.purchase.IGoodsCategoryBiz;
import com.guojin.srm.oss.web.BaseBusinessController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
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
 * @date 2021年10月26日 13时47分37秒
 */

@Api(value = "GoodsCategoryController", tags = { "商品类目表" })
@RestController
@RequestMapping("/purchase/goodsCategory")
@Slf4j
public class GoodsCategoryController extends BaseBusinessController {

	@Reference
	private IGoodsCategoryBiz igoodsCategoryBiz;
//
//	@ApiOperation(value = "获取列表信息", notes = "获取列表信息")
//	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryGoodsCategoryRequest")
//	@RequiresPermissions("purchase:goodscategory:list")
//	@PostMapping("/list")
//	public ResultDto<PageResultVO<GoodsCategoryResponse>> list(@Valid @RequestBody QueryGoodsCategoryRequest request)
//			throws BizException {
//		// 查询列表数据
//		PageResultVO<GoodsCategoryResponse> result = igoodsCategoryBiz.queryList(request);
//		return success(result);
//	}
//
//	@ApiOperation(value = "获取详细信息", notes = "获取详细信息")
//	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
//	@RequiresPermissions("purchase:goodscategory:info")
//	@PostMapping("/info")
//	public ResultDto<GoodsCategoryResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
//		GoodsCategoryResponse goodsCategoryResponse = igoodsCategoryBiz.queryGoodsCategory(request.getId());
//		return success(goodsCategoryResponse);
//	}

//	@ApiOperation(value = "新增", notes = "获取新增信息")
//	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddGoodsCategoryRequest")
//	@RequiresPermissions("purchase:goodscategory:save")
//	@PostMapping("/save")
//	public ResultDto save(@RequestBody @Valid AddGoodsCategoryRequest request) throws BizException {
//		igoodsCategoryBiz.save(request,getUser());
//		return success();
//	}
//
//	@ApiOperation(value = "修改", notes = "修改")
//	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateGoodsCategoryRequest")
//	@RequiresPermissions("purchase:goodscategory:update")
//	@PostMapping("/update")
//	public ResultDto update(@RequestBody @Valid UpdateGoodsCategoryRequest request) throws BizException {
//		igoodsCategoryBiz.update(request,getUser());
//		return success();
//	}
//
//	@ApiOperation(value = "删除", notes = "删除")
//	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
//	@RequiresPermissions("purchase:goodscategory:delete")
//	@PostMapping("/delete")
//	public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
//		igoodsCategoryBiz.deleteById(request.getId());
//		return success();
//	}

	@ApiOperation(value = "新增分类", notes = "新增分类")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddClassifyGoodsCategoryRequest")
	@RequiresPermissions("purchase:goodscategory:addClassify")
	@PostMapping("/addClassify")
	public ResultDto addClassify(@RequestBody @Valid AddClassifyGoodsCategoryRequest request) throws BizException {
		igoodsCategoryBiz.addClassify(request, getUserId());
		return success();
	}

	@ApiOperation(value = "修改分类", notes = "修改分类")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateClassifyGoodsCategoryRequest")
	@RequiresPermissions("purchase:goodscategory:updateClassify")
	@PostMapping("/updateClassify")
	public ResultDto updateClassify(@RequestBody @Valid UpdateClassifyGoodsCategoryRequest request) throws BizException {
		igoodsCategoryBiz.updateClassify(request, getUserId());
		return success();
	}

	@ApiOperation(value = "新增分类反显-上级分类和经营范围列表", notes = "新增分类反显-上级分类和经营范围列表")
	@RequiresPermissions("purchase:goodscategory:addClassifyBackData")
	@PostMapping("/addClassifyBackData")
	public ResultDto addClassifyBackData() throws BizException {
		AddClassifyBackDataResponse addClassifyBackData = igoodsCategoryBiz.addClassifyBackData();
		return success(addClassifyBackData);
	}

	@ApiOperation(value = "修改分类反显-选择商品分类列表", notes = "修改分类反显-选择商品分类列表")
	@RequiresPermissions("purchase:goodscategory:updateClassifyBackDataGoodsCategorys")
	@PostMapping("/updateClassifyBackDataGoodsCategorys")
	public ResultDto updateClassifyBackDataGoodsCategorys() throws BizException {
		List<GoodsCategoryResponse> goodsCategoryResponseList = igoodsCategoryBiz.updateClassifyBackDataGoodsCategorys();
		return success(goodsCategoryResponseList);
	}

	@ApiOperation(value = "修改分类反显-所属上级分类列表", notes = "修改分类反显-所属上级分类列表")
	@RequiresPermissions("purchase:goodscategory:updateClassifyBackDataParentGoodsCategorys")
	@PostMapping("/updateClassifyBackDataParentGoodsCategorys")
	public ResultDto updateClassifyBackDataParentGoodsCategorys() throws BizException {
		List<GoodsCategoryResponse> goodsCategoryResponseList = igoodsCategoryBiz.updateClassifyBackDataParentGoodsCategorys();
		return success(goodsCategoryResponseList);
	}

	@ApiOperation(value = "修改分类反显-根据商品分类获取名称、类别、经营范围", notes = "修改分类反显-根据商品分类获取名称、类别、经营范围")
	@RequiresPermissions("purchase:goodscategory:updateClassifyBackOthersData")
	@PostMapping("/updateClassifyBackOthersData")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "idParamRequest")
	public ResultDto updateClassifyBackOthersData(@RequestBody @Validated IDParamRequest idParamRequest) throws BizException {
		GoodsCategoryResponse goodsCategoryResponse = igoodsCategoryBiz.updateClassifyBackOthersData(idParamRequest.getId());
		return success(goodsCategoryResponse);
	}

}
