package org.dromara.daxpay.service.controller.allocation;

import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.util.ValidationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dromara.daxpay.service.param.allocation.group.AllocGroupBindParam;
import org.dromara.daxpay.service.param.allocation.group.AllocGroupParam;
import org.dromara.daxpay.service.param.allocation.group.AllocGroupQuery;
import org.dromara.daxpay.service.param.allocation.group.AllocGroupUnbindParam;
import org.dromara.daxpay.service.result.allocation.AllocGroupReceiverResult;
import org.dromara.daxpay.service.result.allocation.AllocGroupResult;
import org.dromara.daxpay.service.service.allocation.AllocGroupService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 分账组
 * @author xxm
 * @since 2024/4/2
 */
@Tag(name = "分账组")
@RestController
@RequestMapping("/allocation/group")
@RequiredArgsConstructor
public class AllocGroupController {
    private final AllocGroupService allocGroupService;

    @Operation(summary = "分页")
    @GetMapping("/page")
    public Result<PageResult<AllocGroupResult>> page(PageParam pageParam, AllocGroupQuery query){
        return Res.ok(allocGroupService.page(pageParam,query));
    }

    @Operation(summary = "查询详情")
    @GetMapping("/findById")
    public Result<AllocGroupResult> findById(Long id){
        return Res.ok(allocGroupService.findById(id));
    }


    @Operation(summary = "编码是否存在")
    @GetMapping("/existsByGroupNo")
    public Result<Boolean> existsByGroupNo(String groupNo, String appId){
        return Res.ok(allocGroupService.existsByGroupNo(groupNo, appId));
    }

    @Operation(summary = "查询分账接收方信息")
    @GetMapping("/findReceiversByGroups")
    public Result<List<AllocGroupReceiverResult>> findReceiversByGroups(Long groupId){
        return Res.ok(allocGroupService.findReceiversByGroups(groupId));
    }

    @Operation(summary = "创建")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody AllocGroupParam param){
        allocGroupService.create(param);
        return Res.ok();
    }

    @Operation(summary = "修改")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody AllocGroupParam param){
        allocGroupService.update(param);
        return Res.ok();
    }

    @Operation(summary = "删除")
    @PostMapping("/delete")
    public Result<Void> delete(Long id){
        allocGroupService.delete(id);
        return Res.ok();
    }

    @Operation(summary = "批量绑定接收者")
    @PostMapping("/bindReceivers")
    public Result<Void> bindReceivers(@RequestBody AllocGroupBindParam param){
        ValidationUtil.validateParam(param);
        allocGroupService.bindReceivers(param);
        return Res.ok();
    }

    @Operation(summary = "批量取消绑定接收者")
    @PostMapping("/unbindReceivers")
    public Result<Void> unbindReceivers(@RequestBody AllocGroupUnbindParam param){
        allocGroupService.unbindReceivers(param);
        return Res.ok();
    }

    @Operation(summary = "取消绑定接收者")
    @PostMapping("/unbindReceiver")
    public Result<Void> unbindReceiver(Long receiverId){
        allocGroupService.unbindReceiver(receiverId);
        return Res.ok();
    }

    @Operation(summary = "修改分账比例")
    @PostMapping("/updateRate")
    public Result<Void> updateRate(Long receiverId, BigDecimal rate){
        allocGroupService.updateRate(receiverId,rate);
        return Res.ok();
    }

    @Operation(summary = "设置默认分账组")
    @PostMapping("/setDefault")
    public Result<Void> setDefault(Long id){
        allocGroupService.setUpDefault(id);
        return Res.ok();
    }

    @Operation(summary = "清除默认分账组")
    @PostMapping("/clearDefault")
    public Result<Void> clearDefault(Long id){
        allocGroupService.clearDefault(id);
        return Res.ok();
    }
}
