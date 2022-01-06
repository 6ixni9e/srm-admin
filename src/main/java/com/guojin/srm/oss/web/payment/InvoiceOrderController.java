package com.guojin.srm.oss.web.payment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.payment.request.AddInvoiceOrderRequest;
import com.guojin.srm.api.bean.payment.request.QueryInvoiceOrderRequest;
import com.guojin.srm.api.bean.payment.response.InvoiceOrderResponse;
import com.guojin.srm.api.bean.payment.response.SupplierInvoiceInfoResponse;
import com.guojin.srm.api.biz.payment.IInvoiceOrderBiz;
import com.guojin.srm.common.enums.*;
import com.guojin.srm.oss.web.BaseBusinessController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guojin.srm.common.base.IDParamRequest;
import com.guojin.srm.common.base.PageResultVO;
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

@Api(value = "InvoiceOrderController", tags = { "开票订单表" })
@RestController
@RequestMapping("/payment/invoiceOrder")
@Slf4j
public class InvoiceOrderController extends BaseBusinessController {

	@Reference
	private IInvoiceOrderBiz iinvoiceOrderBiz;
	
	@ApiOperation(value = "获取列表信息", notes = "获取列表信息")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryInvoiceOrderRequest")
	@RequiresPermissions("payment:invoiceorder:list")
	@PostMapping("/list")
	public ResultDto<PageResultVO<InvoiceOrderResponse>> list(@Valid @RequestBody QueryInvoiceOrderRequest request)
			throws BizException {
		// 查询列表数据
		SessParty sessParty = getParty();
		if (sessParty == null) {
			return fail(BaseExcCodesEnum.NO_OPERATE_RIGHT);
		}
		if (PartyTypeEnum.OEM.getValue() == sessParty.getPartyType()) {
			// OEM机构查询所有OEM机构下的数据
			request.setOemCode(getUser().getOemCode());
		} else if (PartyTypeEnum.MCHNT.getValue() == sessParty.getPartyType()) {
			// 企业查询归属企业的数据
			request.setMerchantNo(sessParty.getPartyCode());
		}
		PageResultVO<InvoiceOrderResponse> result = iinvoiceOrderBiz.queryListAdmin(request);
		return success(result);
	}

	@ApiOperation(value = "获取详细信息", notes = "获取详细信息")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
	@RequiresPermissions("payment:invoiceorder:info")
	@PostMapping("/info")
	public ResultDto<InvoiceOrderResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
		InvoiceOrderResponse invoiceOrderResponse = iinvoiceOrderBiz.queryInvoiceOrder(request.getId());
		return success(invoiceOrderResponse);
	}
	
	@ApiOperation(value = "企业pc端任务详情-开票明细", notes = "开票明细")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
	@RequiresPermissions("payment:invoiceorder:getInvoiceInfoOnSupplier")
	@PostMapping("/getInvoiceInfoOnSupplier")
	public ResultDto<SupplierInvoiceInfoResponse> getInvoiceInfoOnSupplier(@Valid @RequestBody IDParamRequest request)
			throws BizException {
		//这里查询
		SupplierInvoiceInfoResponse invoiceInfoResponse = iinvoiceOrderBiz.getInvoiceInfoOnSupplier(request.getId(), 1);
		return success(invoiceInfoResponse);
	}

	@ApiOperation(value = "新增", notes = "获取新增信息")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddInvoiceOrderRequest")
	@RequiresPermissions("payment:invoiceorder:save")
	@PostMapping("/save")
	public ResultDto save(@RequestBody @Valid AddInvoiceOrderRequest request) throws BizException {
		iinvoiceOrderBiz.save(request);
		return success();
	}

//	@ApiOperation(value = "修改", notes = "修改")
//	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateInvoiceOrderRequest")
//	@RequiresPermissions("payment:invoiceorder:update")
//	@PostMapping("/update")
//	public ResultDto update(@RequestBody @Valid UpdateInvoiceOrderRequest request) throws BizException {
//		iinvoiceOrderBiz.update(request, getUser());
//		return success();
//	}
/*
	@ApiOperation(value = "删除", notes = "删除")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
	@RequiresPermissions("payment:invoiceorder:delete")
	@PostMapping("/delete")
	public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
		iinvoiceOrderBiz.deleteById(request.getId());
		return success();
	}
	*/
	
	@ApiOperation(value = "导出列表", notes = "导出列表")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryInvoiceOrderRequest")
	@RequiresPermissions("payment:invoiceorder:download")
    @PostMapping("/download")
    public ResultDto download(@RequestBody QueryInvoiceOrderRequest request, HttpServletResponse response) throws BizException{
		// 查询列表数据
		request.setPageSize(0); //设置不分页
		// 查询列表数据
		SessParty sessParty = getParty();
		if (sessParty == null) {
			return fail(BaseExcCodesEnum.NO_OPERATE_RIGHT);
		}
		if (PartyTypeEnum.OEM.getValue() == sessParty.getPartyType()) {
			// OEM机构查询所有OEM机构下的数据
			request.setOemCode(getUser().getOemCode());
		} else if (PartyTypeEnum.MCHNT.getValue() == sessParty.getPartyType()) {
			// 企业查询归属企业的数据
			request.setMerchantNo(sessParty.getPartyCode());
		}
		PageResultVO<InvoiceOrderResponse> result = iinvoiceOrderBiz.queryListAdmin(request);
		if (CollectionUtil.isNotEmpty(result.getList())) {
			//文件名
			String fileName = "InvoiceOrder_"+getParty().getPartyCode()+".xls";
			// 循环数据处理
			List<List<Object>> values = new ArrayList<>();
			for (InvoiceOrderResponse item : result.getList()) {
				List<Object> temp = new ArrayList<>();
				temp.add(item.getInvoiceOrderNo()); //开票订单编号
				if (Objects.isNull(item.getInvoicingMethod())){
					temp.add("开票方式不存在");
				} else {
					temp.add(InvoiceMethodEnum.getByType(item.getInvoicingMethod()).getRemark()); //开票方式
				}
				if (Objects.isNull(item.getInvoicingType())){
					temp.add("开票类型不存在");
				} else {
					temp.add(InvoiceTypeEnum.getByType(item.getInvoicingType()).getRemark()); //开票类型
				}
				temp.add(item.getAmount()); //开票金额
				temp.add(item.getSupplierName()); //提交方
				temp.add(item.getOemName()); //所属OEM
				temp.add(item.getCreateTime()); //创建时间
				if (Objects.isNull(item.getStatus())){
					temp.add("订单状态不存在");
				} else {
					temp.add(InvoiceStatusEnum.getByType(item.getStatus()).getRemark());
				}
				values.add(temp);
			}
			_downloadFile(fileName,TITLES,values,response);
		}else {
			return fail("暂无数据导出！");
		}
		return success();
    }
	private static final String[] TITLES = new String[] {"开票订单编号","开票方式","开票类型","开票金额","提交方","所属OEM","提交时间","状态"};
}
