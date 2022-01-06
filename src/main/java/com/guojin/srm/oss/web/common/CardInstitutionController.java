//package com.guojin.srm.oss.web.common;
//
//import javax.validation.Valid;
//
//
//import com.alibaba.dubbo.config.annotation.Reference;
//import com.guojin.srm.api.bean.common.request.AddCardInstitutionRequest;
//import com.guojin.srm.api.bean.common.request.QueryCardInstitutionRequest;
//import com.guojin.srm.api.bean.common.request.UpdateCardInstitutionRequest;
//import com.guojin.srm.api.bean.common.response.CardInstitutionResponse;
//import com.guojin.srm.api.biz.common.ICardInstitutionBiz;
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
//@Api(value="CardInstitutionController", tags={"发卡机构数据表"})
//@RestController
//@RequestMapping("/common/cardInstitution")
//@Slf4j
//public class CardInstitutionController extends BaseBusinessController{
//
//	@Reference
//	private ICardInstitutionBiz icardInstitutionBiz;
//
//
//	         @ApiOperation(value="获取列表信息", notes="获取列表信息")
//        @ApiImplicitParam(name ="request", value = "请求对象", required = true, dataType = "QueryCardInstitutionRequest")
//           @RequiresPermissions("common:cardinstitution:list") 	@PostMapping("/list")
//	public ResultDto<PageResultVO<CardInstitutionResponse>> list(@Valid @RequestBody QueryCardInstitutionRequest request) throws BizException{
//		//查询列表数据
//		PageResultVO<CardInstitutionResponse> result = icardInstitutionBiz.queryList(request);
//		return success(result);
//	}
//
//
//
//	         @ApiOperation(value="获取详细信息", notes="获取详细信息")
//        @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
//           @RequiresPermissions("common:cardinstitution:info") 	@PostMapping("/info")
//	public ResultDto<CardInstitutionResponse> info(@Valid  @RequestBody IDParamRequest request) throws BizException{
//		 CardInstitutionResponse cardInstitutionResponse = icardInstitutionBiz.queryCardInstitution(request.getId());
//		 return success(cardInstitutionResponse);
//	}
//
//
//	          @ApiOperation(value="新增", notes="获取新增信息")
//        @ApiImplicitParam(name ="request", value = "请求对象", required = true, dataType = "AddCardInstitutionRequest")
//           @RequiresPermissions("common:cardinstitution:save") 	@PostMapping("/save")
//	public ResultDto save(@RequestBody @Valid AddCardInstitutionRequest request) throws BizException{
//		icardInstitutionBiz.save(request);
//		return success();
//	}
//
//	         @ApiOperation(value="修改", notes="修改")
//        @ApiImplicitParam(name ="request", value = "请求对象", required = true, dataType = "UpdateCardInstitutionRequest")
//            @RequiresPermissions("common:cardinstitution:update") 	@PostMapping("/update")
//	public ResultDto update(@RequestBody @Valid UpdateCardInstitutionRequest request) throws BizException{
//		icardInstitutionBiz.update(request);
//		return success();
//	}
//
//         @ApiOperation(value="删除", notes="删除")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
//               @RequiresPermissions("common:cardinstitution:delete")
//         @PostMapping("/delete")
//    public ResultDto delete(@Valid  @RequestBody IDParamRequest request) throws BizException{
//        icardInstitutionBiz.deleteById(request.getId());
//       return success();
//    }
//}
