package com.guojin.srm.oss.web.common;

import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.common.request.QueryDistrictRequest;
import com.guojin.srm.api.biz.common.IDistrictBiz;
import com.guojin.srm.common.exception.BizException;
import com.guojin.srm.common.utils.ResultDto;
import com.guojin.srm.oss.web.BaseBusinessController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author bealon
 * @email ccsu123456@qq.com
 * @date 2019年01月08日 19时38分36秒
 */

@Api(value = "DistrictController", tags = { "省市区联动管理" })
@RestController
@RequestMapping("/api/common")
public class DistrictController extends BaseBusinessController {

	@Reference
	private IDistrictBiz iDistrictBiz;

	@ApiOperation(value = "获取地区列表信息", notes = "获取地区列表信息")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryDistrictRequest")
	@PostMapping("/queryDistrictList")
	public ResultDto queryDistrictList(@RequestBody QueryDistrictRequest request) throws BizException {
		// 查询列表数据
		List<Map<String, Object>> list = iDistrictBiz.queryList(request);
		return success(list);
	}
	
	@ApiOperation(value = "获取地区名称", notes = "获取地区名称")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryDistrictRequest")
	@PostMapping("/queryName")
	public ResultDto queryName(@RequestBody QueryDistrictRequest request) throws BizException {
		// 查询列表数据
		String name = iDistrictBiz.queryName(request);
		return success(name);
	}
	
	
}
