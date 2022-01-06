package com.guojin.srm.oss.web.purchase;

import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.purchase.request.QueryTaskCheckDistributeGoodsRequest;
import com.guojin.srm.api.bean.purchase.response.TaskCheckDistributeGoodsResponse;
import com.guojin.srm.api.biz.purchase.ITaskCheckDistributeGoodsBiz;
import com.guojin.srm.common.base.IDParamRequest;
import com.guojin.srm.common.base.PageResultVO;
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

import javax.validation.Valid;

/**
 * @author yuqian
 * @email yuqian@99366.com
 * @date 2021年10月26日 13时47分38秒
 */

@Api(value = "TaskCheckDistributeGoodsController", tags = {"任务验收商品分配清单表"})
@RestController
@RequestMapping("/purchase/taskCheckDistributeGoods")
@Slf4j
public class TaskCheckDistributeGoodsController extends BaseBusinessController {

    @Reference
    private ITaskCheckDistributeGoodsBiz itaskCheckDistributeGoodsBiz;


    @ApiOperation(value = "获取列表信息", notes = "获取列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryTaskCheckDistributeGoodsRequest")
    @RequiresPermissions("purchase:taskcheckdistributegoods:list")
    @PostMapping("/list")
    public ResultDto<PageResultVO<TaskCheckDistributeGoodsResponse>> list(@Valid @RequestBody QueryTaskCheckDistributeGoodsRequest request) throws BizException {
        //查询列表数据
        PageResultVO<TaskCheckDistributeGoodsResponse> result = itaskCheckDistributeGoodsBiz.queryList(request);
        return success(result);
    }


    @ApiOperation(value = "获取详细信息", notes = "获取详细信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("purchase:taskcheckdistributegoods:info")
    @PostMapping("/info")
    public ResultDto<TaskCheckDistributeGoodsResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
        TaskCheckDistributeGoodsResponse taskCheckDistributeGoodsResponse = itaskCheckDistributeGoodsBiz.queryTaskCheckDistributeGoods(request.getId());
        return success(taskCheckDistributeGoodsResponse);
    }

//
//    @ApiOperation(value = "新增", notes = "获取新增信息")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddTaskCheckDistributeGoodsRequest")
//    @RequiresPermissions("purchase:taskcheckdistributegoods:save")
//    @PostMapping("/save")
//    public ResultDto save(@RequestBody @Valid AddTaskCheckDistributeGoodsRequest request) throws BizException {
//        itaskCheckDistributeGoodsBiz.save(request);
//        return success();
//    }

//    @ApiOperation(value = "修改", notes = "修改")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateTaskCheckDistributeGoodsRequest")
//    @RequiresPermissions("purchase:taskcheckdistributegoods:update")
//    @PostMapping("/update")
//    public ResultDto update(@RequestBody @Valid UpdateTaskCheckDistributeGoodsRequest request) throws BizException {
//        itaskCheckDistributeGoodsBiz.update(request);
//        return success();
//    }

//    @ApiOperation(value = "删除", notes = "删除")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
//    @RequiresPermissions("purchase:taskcheckdistributegoods:delete")
//    @PostMapping("/delete")
//    public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
//        itaskCheckDistributeGoodsBiz.deleteById(request.getId());
//        return success();
//    }
}
