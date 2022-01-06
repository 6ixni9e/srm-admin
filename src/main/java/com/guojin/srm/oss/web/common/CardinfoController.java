//package com.guojin.srm.oss.web.common;
//
//import javax.validation.Valid;
//
//
//import com.alibaba.dubbo.config.annotation.Reference;
//import com.guojin.srm.api.bean.common.request.AddCardinfoRequest;
//import com.guojin.srm.api.bean.common.request.QueryCardinfoRequest;
//import com.guojin.srm.api.bean.common.request.UpdateCardinfoRequest;
//import com.guojin.srm.api.bean.common.response.CardinfoResponse;
//import com.guojin.srm.api.biz.common.ICardinfoBiz;
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
// * @date 2021年10月26日 13时42分11秒
// */
//
//@Api(value="CardinfoController", tags={"卡Bin数据表"})
//@RestController
//@RequestMapping("/common/cardinfo")
//@Slf4j
//public class CardinfoController extends BaseBusinessController{
//
//	@Reference
//	private ICardinfoBiz icardinfoBiz;
//
//
//	         @ApiOperation(value="获取列表信息", notes="获取列表信息")
//        @ApiImplicitParam(name ="request", value = "请求对象", required = true, dataType = "QueryCardinfoRequest")
//           @RequiresPermissions("common:cardinfo:list") 	@PostMapping("/list")
//	public ResultDto<PageResultVO<CardinfoResponse>> list(@Valid @RequestBody QueryCardinfoRequest request) throws BizException{
//		//查询列表数据
//		PageResultVO<CardinfoResponse> result = icardinfoBiz.queryList(request);
//		return success(result);
//	}
//
//
//
//	         @ApiOperation(value="获取详细信息", notes="获取详细信息")
//        @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
//           @RequiresPermissions("common:cardinfo:info") 	@PostMapping("/info")
//	public ResultDto<CardinfoResponse> info(@Valid  @RequestBody IDParamRequest request) throws BizException{
//		 CardinfoResponse cardinfoResponse = icardinfoBiz.queryCardinfo(request.getId());
//		 return success(cardinfoResponse);
//	}
//
//
//	          @ApiOperation(value="新增", notes="获取新增信息")
//        @ApiImplicitParam(name ="request", value = "请求对象", required = true, dataType = "AddCardinfoRequest")
//           @RequiresPermissions("common:cardinfo:save") 	@PostMapping("/save")
//	public ResultDto save(@RequestBody @Valid AddCardinfoRequest request) throws BizException{
//		icardinfoBiz.save(request);
//		return success();
//	}
//
//	         @ApiOperation(value="修改", notes="修改")
//        @ApiImplicitParam(name ="request", value = "请求对象", required = true, dataType = "UpdateCardinfoRequest")
//            @RequiresPermissions("common:cardinfo:update") 	@PostMapping("/update")
//	public ResultDto update(@RequestBody @Valid UpdateCardinfoRequest request) throws BizException{
//		icardinfoBiz.update(request);
//		return success();
//	}
//
//         @ApiOperation(value="删除", notes="删除")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
//               @RequiresPermissions("common:cardinfo:delete")
//         @PostMapping("/delete")
//    public ResultDto delete(@Valid  @RequestBody IDParamRequest request) throws BizException{
//        icardinfoBiz.deleteById(request.getId());
//       return success();
//    }
//}
