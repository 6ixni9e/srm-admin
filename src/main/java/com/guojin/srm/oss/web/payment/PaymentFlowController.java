package com.guojin.srm.oss.web.payment;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.payment.request.QueryPaymentFlowRequest;
import com.guojin.srm.api.bean.payment.response.PaymentFlowResponse;
import com.guojin.srm.api.biz.payment.IPaymentFlowBiz;
import com.guojin.srm.oss.web.BaseBusinessController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guojin.srm.common.base.IDParamRequest;
import com.guojin.srm.common.base.PageResultVO;
import com.guojin.srm.common.enums.BaseExcCodesEnum;
import com.guojin.srm.common.enums.PartyTypeEnum;
import com.guojin.srm.common.enums.PayTypeEnum;
import com.guojin.srm.common.enums.PaymentFlowStatusEnum;
import com.guojin.srm.common.exception.BizException;
import com.guojin.srm.common.session.SessParty;
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

@Api(value = "PaymentFlowController", tags = { "付款流水表" })
@RestController
@RequestMapping("/payment/paymentFlow")
@Slf4j
public class PaymentFlowController extends BaseBusinessController {

	@Reference
	private IPaymentFlowBiz ipaymentFlowBiz;

	@ApiOperation(value = "获取列表信息", notes = "获取列表信息")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryPaymentFlowRequest")
	@RequiresPermissions("payment:paymentflow:list")
	@PostMapping("/list")
	public ResultDto<PageResultVO<PaymentFlowResponse>> list(@Valid @RequestBody QueryPaymentFlowRequest request)
			throws BizException {
		// 查询列表数据
		SessParty sessParty = getParty();
		if (sessParty == null) {
			return fail(BaseExcCodesEnum.NO_OPERATE_RIGHT);
		}
		if (PartyTypeEnum.OEM.getValue() == sessParty.getPartyType()) {
			// OEM机构查询所有OEM机构下的数据
			request.setOemCode(getOemCode());
		} else if (PartyTypeEnum.MCHNT.getValue() == sessParty.getPartyType()) {
			// 企业查询归属企业的数据
			request.setMerchantNo(sessParty.getPartyCode());
		}
		PageResultVO<PaymentFlowResponse> result = ipaymentFlowBiz.queryList(request);
		return success(result);
	}

	@ApiOperation(value = "获取详细信息", notes = "获取详细信息")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
	@RequiresPermissions("payment:paymentflow:info")
	@PostMapping("/info")
	public ResultDto<PaymentFlowResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
		PaymentFlowResponse paymentFlowResponse = ipaymentFlowBiz.queryPaymentFlow(request.getId());
		return success(paymentFlowResponse);
	}
	
	@ApiOperation(value = "导出列表", notes = "导出列表")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryPaymentFlowRequest")
	@RequiresPermissions("payment:paymentflow:download")
    @PostMapping("/download")
    public ResultDto download(@RequestBody QueryPaymentFlowRequest request, HttpServletResponse response) throws BizException{
		// 查询列表数据
		request.setPageSize(0); //设置不分页
		// 查询列表数据
		SessParty sessParty = getParty();
		if (sessParty == null) {
			return fail(BaseExcCodesEnum.NO_OPERATE_RIGHT);
		}
		if (PartyTypeEnum.OEM.getValue() == sessParty.getPartyType()) {
			// OEM机构查询所有OEM机构下的数据
			request.setOemCode(getOemCode());
		} else if (PartyTypeEnum.MCHNT.getValue() == sessParty.getPartyType()) {
			// 企业查询归属企业的数据
			request.setMerchantNo(sessParty.getPartyCode());
		}
		PageResultVO<PaymentFlowResponse> result = ipaymentFlowBiz.queryList(request);
		if (result.getTotalCount() > 0) {
			//文件名
			String fileName = "Paymentflow_"+getParty().getPartyCode()+".xls";
			// 循环数据处理
			List<List<Object>> values = new ArrayList<>();
			for (PaymentFlowResponse item : result.getList()) {
				List<Object> temp = new ArrayList<>();
				temp.add(item.getOrderNo()); //订单编号
				temp.add(item.getTaskName()); //任务名称
				temp.add(PayTypeEnum.getByType(item.getPayType()).getDesc()); //结算类型
				temp.add(item.getPayAmount()); //结算金额
				temp.add(item.getPayerName()); //付款方
				temp.add(item.getPayeeName()); //收款方
				temp.add(item.getCreateTime()); //创建时间
				temp.add(PaymentFlowStatusEnum.getByType(item.getStatus()).getDesc()); //状态
				values.add(temp);
			}
			_downloadFile(fileName,TITLES,values,response);
		}else {
			return fail("暂无导出数据");
		}
		return success();
    }
	private static final String[] TITLES = new String[] {"订单编号","任务名称","结算类型","结算金额","付款方","收款方","创建时间","状态"};

}
