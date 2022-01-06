package com.guojin.srm.oss.web.purchase;

import javax.validation.Valid;


import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.purchase.request.AddGoodsReviewRequest;
import com.guojin.srm.api.bean.purchase.request.QueryGoodsInfoRequest;
import com.guojin.srm.api.bean.purchase.request.QueryGoodsReviewRequest;
import com.guojin.srm.api.bean.purchase.request.UpdateGoodsReviewRequest;
import com.guojin.srm.api.bean.purchase.response.GoodsReviewResponse;
import com.guojin.srm.api.bean.purchase.response.vo.GoodsInfoVO;
import com.guojin.srm.api.biz.purchase.IGoodsReviewBiz;
import com.guojin.srm.common.enums.BaseExcCodesEnum;
import com.guojin.srm.common.enums.PartyTypeEnum;
import com.guojin.srm.common.session.SessParty;
import com.guojin.srm.common.session.SessUser;
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

import java.util.List;

/**
 * @author yuqian
 * @email yuqian@99366.com
 * @date 2021年11月09日 09时20分59秒
 */

@Api(value="GoodsReviewController", tags={"商品编码修正记录表"})
@RestController
@RequestMapping("/purchase/goodsReview")
@Slf4j
public class GoodsReviewController extends BaseBusinessController{

	@Reference
	private IGoodsReviewBiz igoodsReviewBiz;

	@ApiOperation(value="搜索商品信息", notes="搜索商品信息")
	@ApiImplicitParam(name ="request", value = "请求对象", required = true, dataType = "QueryGoodsInfoRequest")
	@RequiresPermissions("purchase:goodsreview:querygoods")
	@PostMapping("/queryGoods")
	public ResultDto<List<GoodsInfoVO>> queryGoods(@Valid @RequestBody QueryGoodsInfoRequest request) throws Exception{
		SessParty party = getParty();
		if(party == null){
			throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
		}
		if(PartyTypeEnum.OEM.getValue() != party.getPartyType() && PartyTypeEnum.PLATFORM.getValue() != party.getPartyType()){
			throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
		}
		//查询列表数据
		List<GoodsInfoVO> result = igoodsReviewBiz.queryGoods(request);
		return success(result);
	}

	@ApiOperation(value="获取列表信息", notes="获取列表信息")
	@ApiImplicitParam(name ="request", value = "请求对象", required = true, dataType = "QueryGoodsReviewRequest")
	@RequiresPermissions("purchase:goodsreview:list")
	@PostMapping("/list")
	public ResultDto<PageResultVO<GoodsReviewResponse>> list(@Valid @RequestBody QueryGoodsReviewRequest request) throws BizException{
		SessParty party = getParty();
		if(party == null){
			throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
		}
		if(PartyTypeEnum.OEM.getValue() != party.getPartyType() && PartyTypeEnum.PLATFORM.getValue() != party.getPartyType()){
			throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
		}
		//查询列表数据	
		PageResultVO<GoodsReviewResponse> result = igoodsReviewBiz.queryList(request);
		return success(result);
	}

	@ApiOperation(value="获取详细信息", notes="获取详细信息")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
	@RequiresPermissions("purchase:goodsreview:info")
	@PostMapping("/info")
	public ResultDto<GoodsReviewResponse> info(@Valid  @RequestBody IDParamRequest request) throws BizException{
		GoodsReviewResponse goodsReviewResponse = igoodsReviewBiz.queryGoodsReview(request.getId());
		return success(goodsReviewResponse);
	}


	@ApiOperation(value="修改", notes="修改")
	@ApiImplicitParam(name ="request", value = "请求对象", required = true, dataType = "UpdateGoodsReviewRequest")
	@RequiresPermissions("purchase:goodsreview:update")
	@PostMapping("/update")
	public ResultDto update(@RequestBody @Valid UpdateGoodsReviewRequest request) throws BizException{
		SessParty party = getParty();
		if(party == null){
			throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
		}
		if(PartyTypeEnum.OEM.getValue() != party.getPartyType() && PartyTypeEnum.PLATFORM.getValue() != party.getPartyType()){
			throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
		}
		SessUser sessUser = getUser();
		igoodsReviewBiz.update(request,sessUser);
		return success();
	}

}
