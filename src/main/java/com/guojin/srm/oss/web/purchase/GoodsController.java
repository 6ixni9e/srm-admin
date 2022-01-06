package com.guojin.srm.oss.web.purchase;

import javax.validation.Valid;

import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.purchase.request.AddGoodsRequest;
import com.guojin.srm.api.bean.purchase.request.QueryGoodsRequest;
import com.guojin.srm.api.bean.purchase.request.UpdateGoodsRequest;
import com.guojin.srm.api.bean.purchase.response.GoodsResponse;
import com.guojin.srm.api.biz.purchase.IGoodsBiz;
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
 * @date 2021年10月26日 13时47分37秒
 */

@Api(value = "GoodsController", tags = { "商品表" })
@RestController
@RequestMapping("/purchase/goods")
@Slf4j
public class GoodsController extends BaseBusinessController {

	@Reference
	private IGoodsBiz igoodsBiz;

	@ApiOperation(value = "获取列表信息", notes = "获取列表信息")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryGoodsRequest")
	@RequiresPermissions("purchase:goods:list")
	@PostMapping("/list")
	public ResultDto<PageResultVO<GoodsResponse>> list(@Valid @RequestBody QueryGoodsRequest request)
			throws BizException {
		// 查询列表数据
		request.setMerchantNo(getParty().getPartyCode());
		PageResultVO<GoodsResponse> result = igoodsBiz.queryList(request);
		return success(result);
	}
	
	

	@ApiOperation(value = "获取详细信息", notes = "获取详细信息")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
	@RequiresPermissions("purchase:goods:info")
	@PostMapping("/info")
	public ResultDto<GoodsResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
		GoodsResponse goodsResponse = igoodsBiz.queryGoods(request.getId());
		return success(goodsResponse);
	}

	@ApiOperation(value = "新增", notes = "获取新增信息")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddGoodsRequest")
	@RequiresPermissions("purchase:goods:save")
	@PostMapping("/save")
	public ResultDto save(@RequestBody @Valid AddGoodsRequest request) throws BizException {
		igoodsBiz.save(request, getUser(), getParty().getPartyCode());
		return success();
	}

	@ApiOperation(value = "修改", notes = "修改")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateGoodsRequest")
	@RequiresPermissions("purchase:goods:update")
	@PostMapping("/update")
	public ResultDto update(@RequestBody @Valid UpdateGoodsRequest request) throws BizException {
		igoodsBiz.update(request, getUser());
		return success();
	}

	/*@ApiOperation(value = "删除", notes = "删除")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
	@RequiresPermissions("purchase:goods:delete")
	@PostMapping("/delete")
	public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
		igoodsBiz.deleteById(request.getId());
		return success();
	}*/
}
