package com.guojin.srm.oss.web.purchase;

import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.purchase.request.QueryTaskCheckGoodsRequest;
import com.guojin.srm.api.bean.purchase.response.TaskCheckGoodsResponse;
import com.guojin.srm.api.biz.purchase.ITaskCheckGoodsBiz;
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

@Api(value = "TaskCheckGoodsController", tags = {"任务验收商品清单表"})
@RestController
@RequestMapping("/purchase/taskCheckGoods")
@Slf4j
public class TaskCheckGoodsController extends BaseBusinessController {

    @Reference
    private ITaskCheckGoodsBiz itaskCheckGoodsBiz;


    @ApiOperation(value = "获取列表信息", notes = "获取列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryTaskCheckGoodsRequest")
    @RequiresPermissions("purchase:taskcheckgoods:list")
    @PostMapping("/list")
    public ResultDto<PageResultVO<TaskCheckGoodsResponse>> list(@Valid @RequestBody QueryTaskCheckGoodsRequest request) throws BizException {
        //查询列表数据
        PageResultVO<TaskCheckGoodsResponse> result = itaskCheckGoodsBiz.queryList(request);
        return success(result);
    }


    @ApiOperation(value = "获取详细信息", notes = "获取详细信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("purchase:taskcheckgoods:info")
    @PostMapping("/info")
    public ResultDto<TaskCheckGoodsResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
        TaskCheckGoodsResponse taskCheckGoodsResponse = itaskCheckGoodsBiz.queryTaskCheckGoods(request.getId());
        return success(taskCheckGoodsResponse);
    }


//    @ApiOperation(value = "新增", notes = "获取新增信息")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddTaskCheckGoodsRequest")
//    @RequiresPermissions("purchase:taskcheckgoods:save")
//    @PostMapping("/save")
//    public ResultDto save(@RequestBody @Valid AddTaskCheckGoodsRequest request) throws BizException {
//        itaskCheckGoodsBiz.save(request);
//        return success();
//    }
//
//    @ApiOperation(value = "修改", notes = "修改")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateTaskCheckGoodsRequest")
//    @RequiresPermissions("purchase:taskcheckgoods:update")
//    @PostMapping("/update")
//    public ResultDto update(@RequestBody @Valid UpdateTaskCheckGoodsRequest request) throws BizException {
//        itaskCheckGoodsBiz.update(request);
//        return success();
//    }

//    @ApiOperation(value = "删除", notes = "删除")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
//    @RequiresPermissions("purchase:taskcheckgoods:delete")
//    @PostMapping("/delete")
//    public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
//        itaskCheckGoodsBiz.deleteById(request.getId());
//        return success();
//    }
}
