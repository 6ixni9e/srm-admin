package com.guojin.srm.oss.web.purchase;

import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.purchase.request.QueryTaskDeliverGoodsRequest;
import com.guojin.srm.api.bean.purchase.response.TaskDeliverGoodsResponse;
import com.guojin.srm.api.biz.purchase.ITaskDeliverGoodsBiz;
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
 * @date 2021年10月26日 13时47分39秒
 */

@Api(value = "TaskDeliverGoodsController", tags = {"任务交付商品清单表"})
@RestController
@RequestMapping("/purchase/taskDeliverGoods")
@Slf4j
public class TaskDeliverGoodsController extends BaseBusinessController {

    @Reference
    private ITaskDeliverGoodsBiz itaskDeliverGoodsBiz;


    @ApiOperation(value = "获取列表信息", notes = "获取列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryTaskDeliverGoodsRequest")
    @RequiresPermissions("purchase:taskdelivergoods:list")
    @PostMapping("/list")
    public ResultDto<PageResultVO<TaskDeliverGoodsResponse>> list(@Valid @RequestBody QueryTaskDeliverGoodsRequest request) throws BizException {
        //查询列表数据
        PageResultVO<TaskDeliverGoodsResponse> result = itaskDeliverGoodsBiz.queryList(request);
        return success(result);
    }


    @ApiOperation(value = "获取详细信息", notes = "获取详细信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("purchase:taskdelivergoods:info")
    @PostMapping("/info")
    public ResultDto<TaskDeliverGoodsResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
        TaskDeliverGoodsResponse taskDeliverGoodsResponse = itaskDeliverGoodsBiz.queryTaskDeliverGoods(request.getId());
        return success(taskDeliverGoodsResponse);
    }

//
//    @ApiOperation(value = "新增", notes = "获取新增信息")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddTaskDeliverGoodsRequest")
//    @RequiresPermissions("purchase:taskdelivergoods:save")
//    @PostMapping("/save")
//    public ResultDto save(@RequestBody @Valid AddTaskDeliverGoodsRequest request) throws BizException {
//        itaskDeliverGoodsBiz.save(request);
//        return success();
//    }
//
//    @ApiOperation(value = "修改", notes = "修改")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateTaskDeliverGoodsRequest")
//    @RequiresPermissions("purchase:taskdelivergoods:update")
//    @PostMapping("/update")
//    public ResultDto update(@RequestBody @Valid UpdateTaskDeliverGoodsRequest request) throws BizException {
//        itaskDeliverGoodsBiz.update(request);
//        return success();
//    }

//    @ApiOperation(value = "删除", notes = "删除")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
//    @RequiresPermissions("purchase:taskdelivergoods:delete")
//    @PostMapping("/delete")
//    public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
//        itaskDeliverGoodsBiz.deleteById(request.getId());
//        return success();
//    }
}
