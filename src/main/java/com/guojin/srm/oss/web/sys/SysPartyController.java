//package com.guojin.srm.oss.web.sys;
//
//import com.alibaba.dubbo.config.annotation.Reference;
//import com.guojin.srm.api.bean.sys.request.QuerySysPartyRequest;
//import com.guojin.srm.api.bean.sys.response.SysPartyResponse;
//import com.guojin.srm.api.biz.sys.ISysPartyBiz;
//import com.guojin.srm.common.base.PageResultVO;
//import com.guojin.srm.common.enums.OrganStatusEnum;
//import com.guojin.srm.common.enums.PartyTypeEnum;
//import com.guojin.srm.common.exception.BizException;
//import com.guojin.srm.common.session.SessUser;
//import com.guojin.srm.common.utils.ResultDto;
//import com.guojin.srm.oss.web.BaseBusinessController;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.validation.Valid;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author yuqian
// * @email yuqian@99366.com
// * @date 2021年10月11日 13时37分47秒
// */
//
//@Api(value = "SysPartyController", tags = {"机构信息"})
//@RestController
//@RequestMapping("/sys/party")
//@Slf4j
//public class SysPartyController extends BaseBusinessController {
//
//    @Reference
//    private ISysPartyBiz isysPartyBiz;
//
//
//    @ApiOperation(value = "获取列表信息", notes = "获取列表信息")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QuerySysPartyRequest")
//    @RequiresPermissions("sys:sysparty:list")
//    @PostMapping("/list")
//    public ResultDto<PageResultVO<SysPartyResponse>> list(@Valid @RequestBody QuerySysPartyRequest request) throws BizException {
//        //查询列表数据
//        PageResultVO<SysPartyResponse> result = isysPartyBiz.queryList(request);
//        return success(result);
//    }
//}
