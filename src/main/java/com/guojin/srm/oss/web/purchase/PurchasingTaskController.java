package com.guojin.srm.oss.web.purchase;

import cn.hutool.core.util.StrUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.purchase.request.QueryPurchasingTaskRequest;
import com.guojin.srm.api.bean.purchase.response.PurchasingTaskResponse;
import com.guojin.srm.api.bean.purchase.response.TaskExtendResponse;
import com.guojin.srm.api.biz.org.IMerchantBiz;
import com.guojin.srm.api.biz.purchase.IPurchasingTaskBiz;
import com.guojin.srm.common.base.ExtendPageResultVO;
import com.guojin.srm.common.base.IDParamRequest;
import com.guojin.srm.common.constant.BizBaseConstant;
import com.guojin.srm.common.enums.BaseExcCodesEnum;
import com.guojin.srm.common.enums.PartyTypeEnum;
import com.guojin.srm.common.enums.TaskStatusEnum;
import com.guojin.srm.common.exception.BizException;
import com.guojin.srm.common.utils.ResultDto;
import com.guojin.srm.oss.web.BaseBusinessController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yuqian
 * @email yuqian@99366.com
 * @date 2021年10月26日 13时47分38秒
 */

@Api(value = "PurchasingTaskController", tags = {"采购任务管理"})
@RestController
@RequestMapping("/purchase/task")
@Slf4j
public class PurchasingTaskController extends BaseBusinessController {

    @Reference
    private IPurchasingTaskBiz ipurchasingTaskBiz;

    @Reference
    private IMerchantBiz merchantBiz;

    @ApiOperation(value = "获取列表信息", notes = "获取列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryPurchasingTaskRequest")
    @RequiresPermissions("purchase:task:list")
    @PostMapping("/list")
    public ResultDto<ExtendPageResultVO<PurchasingTaskResponse, TaskExtendResponse>> list(@Valid @RequestBody QueryPurchasingTaskRequest request) throws BizException {
        List<String> queryMerchantNoList =new ArrayList<>();
        //TODO PartyTypeEnum.PLATFORM时可以查询所有，PartyTypeEnum.OEM查询OEM机构下的所有数据，PartyType.MCHNT时按单商户查询
        if(PartyTypeEnum.OEM.getValue() == getParty().getPartyType()){
            request.setOemCode(getParty().getPartyCode());
        }else if(PartyTypeEnum.MCHNT.getValue()== getParty().getPartyType()){
            request.setMerchantNo(getParty().getPartyCode());
        }

        ExtendPageResultVO<PurchasingTaskResponse, TaskExtendResponse> result = ipurchasingTaskBiz.queryList(request, getUserId());
        return success(result);
    }


    @ApiOperation(value = "获取详细信息", notes = "获取详细信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("purchase:task:info")
    @PostMapping("/info")
    public ResultDto<PurchasingTaskResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
        PurchasingTaskResponse purchasingTaskResponse = ipurchasingTaskBiz.queryPurchasingTask(request.getId());
        return success(purchasingTaskResponse);
    }

    @ApiOperation(value = "导出列表", notes = "导出列表")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryPurchasingTaskRequest")
    @RequiresPermissions("purchase:task:download")
    @PostMapping("/download")
    public ResultDto download(@RequestBody QueryPurchasingTaskRequest request, HttpServletResponse response) throws BizException {
        request.setPageSize(0);
        if(PartyTypeEnum.OEM.getValue() == getParty().getPartyType()){
            request.setOemCode(getParty().getPartyCode());
        }else if(PartyTypeEnum.MCHNT.getValue()== getParty().getPartyType()){
            request.setMerchantNo(getParty().getPartyCode());
        }
        ExtendPageResultVO<PurchasingTaskResponse, TaskExtendResponse> result = ipurchasingTaskBiz.queryList(request, getUserId());
        if (result.getTotalCount() <= 0) {
            throw new BizException(BaseExcCodesEnum.REMOTE_COMMIT_FAIL, BizBaseConstant.DOWNLOAD_DATA_IS_NULL);
        }
        List<List<Object>> fileData = result.getList().stream().map(purchasingTaskResponse -> {
            List<Object> temp = new ArrayList<>();
            temp.add(purchasingTaskResponse.getTaskNo());
            temp.add(purchasingTaskResponse.getMerchantName());
            temp.add(StrUtil.isNotBlank(purchasingTaskResponse.getPlanNo()) ? purchasingTaskResponse.getPlanName() : purchasingTaskResponse.getProjectName());
            temp.add(purchasingTaskResponse.getTaskName());
            temp.add(purchasingTaskResponse.getCreateTime());
            temp.add(TaskStatusEnum.getByStatus(purchasingTaskResponse.getStatus()).getDesc());
            return temp;
        }).collect(Collectors.toList());
        _downloadFile("PurchaseTask_" + getParty().getPartyCode() + ".xls", new String[]{"任务编号", "所属公司", "所属采购计划/项目", "采购任务名称", "创建时间", "状态"}, fileData, response);
        return success();
    }


}
