package com.guojin.srm.oss.web.purchase;

import javax.validation.Valid;

import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.purchase.request.AddPurchaseGoodsRequest;
import com.guojin.srm.api.bean.purchase.request.QueryPurchaseGoodsRequest;
import com.guojin.srm.api.bean.purchase.request.UpdatePurchaseGoodsRequest;
import com.guojin.srm.api.bean.purchase.response.PurchaseGoodsResponse;
import com.guojin.srm.api.biz.purchase.IPurchaseGoodsBiz;
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
 * @date 2021年10月26日 13时47分38秒
 */

@Api(value = "PurchaseGoodsController", tags = { "业务采购商品清单表" })
@RestController
@RequestMapping("/purchase/purchaseGoods")
@Slf4j
public class PurchaseGoodsController extends BaseBusinessController {

	@Reference
	private IPurchaseGoodsBiz ipurchaseGoodsBiz;

	@ApiOperation(value = "获取列表信息", notes = "获取列表信息")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryPurchaseGoodsRequest")
	@RequiresPermissions("purchase:purchasegoods:list")
	@PostMapping("/list")
	public ResultDto<PageResultVO<PurchaseGoodsResponse>> list(@Valid @RequestBody QueryPurchaseGoodsRequest request)
			throws BizException {
		// 查询列表数据
		PageResultVO<PurchaseGoodsResponse> result = ipurchaseGoodsBiz.queryList(request);
		return success(result);
	}

	@ApiOperation(value = "获取详细信息", notes = "获取详细信息")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
	@RequiresPermissions("purchase:purchasegoods:info")
	@PostMapping("/info")
	public ResultDto<PurchaseGoodsResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
		PurchaseGoodsResponse purchaseGoodsResponse = ipurchaseGoodsBiz.queryPurchaseGoods(request.getId());
		return success(purchaseGoodsResponse);
	}

	@ApiOperation(value = "新增", notes = "获取新增信息")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddPurchaseGoodsRequest")
	@RequiresPermissions("purchase:purchasegoods:save")
	@PostMapping("/save")
	public ResultDto save(@RequestBody @Valid AddPurchaseGoodsRequest request) throws BizException {
		ipurchaseGoodsBiz.save(request);
		return success();
	}

	@ApiOperation(value = "修改", notes = "修改")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdatePurchaseGoodsRequest")
	@RequiresPermissions("purchase:purchasegoods:update")
	@PostMapping("/update")
	public ResultDto update(@RequestBody @Valid UpdatePurchaseGoodsRequest request) throws BizException {
		ipurchaseGoodsBiz.update(request);
		return success();
	}

	/*@ApiOperation(value = "删除", notes = "删除")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
	@RequiresPermissions("purchase:purchasegoods:delete")
	@PostMapping("/delete")
	public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
		ipurchaseGoodsBiz.deleteById(request.getId());
		return success();
	}*/
}
