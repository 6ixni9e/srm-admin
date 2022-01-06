package com.guojin.srm.oss.web.payment;

import javax.validation.Valid;

import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.payment.request.AddPaymentRequest;
import com.guojin.srm.api.bean.payment.request.QueryPaymentRequest;
import com.guojin.srm.api.bean.payment.request.UpdatePaymentRequest;
import com.guojin.srm.api.bean.payment.response.PaymentResponse;
import com.guojin.srm.api.bean.payment.response.ReceivePayInfoResponse;
import com.guojin.srm.api.biz.payment.IPaymentBiz;
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
 * @date 2021年10月26日 13时44分13秒
 */

@Api(value = "PaymentController", tags = { "付款订单表" })
@RestController
@RequestMapping("/payment/payment")
@Slf4j
public class PaymentController extends BaseBusinessController {

	@Reference
	private IPaymentBiz ipaymentBiz;

	@ApiOperation(value = "获取列表信息", notes = "获取列表信息")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryPaymentRequest")
	@RequiresPermissions("payment:payment:list")
	@PostMapping("/list")
	public ResultDto<PageResultVO<PaymentResponse>> list(@Valid @RequestBody QueryPaymentRequest request)
			throws BizException {
		// 查询列表数据
		PageResultVO<PaymentResponse> result = ipaymentBiz.queryList(request);
		return success(result);
	}

	@ApiOperation(value = "获取详细信息", notes = "获取详细信息")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
	@RequiresPermissions("payment:payment:info")
	@PostMapping("/info")
	public ResultDto<PaymentResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
		PaymentResponse paymentResponse = ipaymentBiz.queryPayment(request.getId());
		return success(paymentResponse);
	}
	
	
	@ApiOperation(value = "获取收付款记录", notes = "获取收付款记录")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryPaymentRequest")
	@RequiresPermissions("payment:payment:queryReceivePayInfo")
	@PostMapping("/queryReceivePayInfo")
	public ResultDto<ReceivePayInfoResponse> queryReceivePayInfo(@Valid @RequestBody QueryPaymentRequest request)
			throws BizException {
		// 查询列表数据
		String oemCode = getOemCode();
		ReceivePayInfoResponse receivePayInfoResponse = ipaymentBiz.queryReceivePayInfo(request.getTaskNo(), oemCode, 1);
		return success(receivePayInfoResponse);
	}

	@ApiOperation(value = "新增", notes = "获取新增信息")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddPaymentRequest")
	@RequiresPermissions("payment:payment:save")
	@PostMapping("/save")
	public ResultDto save(@RequestBody @Valid AddPaymentRequest request) throws BizException {
		ipaymentBiz.save(request);
		return success();
	}

	@ApiOperation(value = "修改", notes = "修改")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdatePaymentRequest")
	@RequiresPermissions("payment:payment:update")
	@PostMapping("/update")
	public ResultDto update(@RequestBody @Valid UpdatePaymentRequest request) throws BizException {
		ipaymentBiz.update(request);
		return success();
	}

	/*@ApiOperation(value = "删除", notes = "删除")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
	@RequiresPermissions("payment:payment:delete")
	@PostMapping("/delete")
	public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
		ipaymentBiz.deleteById(request.getId());
		return success();
	}*/
}
