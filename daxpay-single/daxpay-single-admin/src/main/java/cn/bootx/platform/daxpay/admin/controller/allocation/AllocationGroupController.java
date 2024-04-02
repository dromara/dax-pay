package cn.bootx.platform.daxpay.admin.controller.allocation;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.ValidationUtil;
import cn.bootx.platform.daxpay.service.core.payment.allocation.service.AllocationGroupService;
import cn.bootx.platform.daxpay.service.dto.allocation.AllocationGroupDto;
import cn.bootx.platform.daxpay.service.dto.allocation.AllocationGroupReceiverResult;
import cn.bootx.platform.daxpay.service.param.allocation.AllocationGroupBindParam;
import cn.bootx.platform.daxpay.service.param.allocation.AllocationGroupParam;
import cn.bootx.platform.daxpay.service.param.allocation.AllocationGroupUnbindParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
public class AllocationGroupController {
    private final AllocationGroupService allocationGroupService;

    @Operation(summary = "分页")
    @GetMapping("/page")
    public ResResult<PageResult<AllocationGroupDto>> page(PageParam pageParam, AllocationGroupParam query){
        return Res.ok(allocationGroupService.page(pageParam,query));
    }

    @Operation(summary = "查询详情")
    @GetMapping("/findById")
    public ResResult<AllocationGroupDto> findById(Long id){
        return Res.ok(allocationGroupService.findById(id));
    }

    @Operation(summary = "查询分账接收方信息")
    @GetMapping("/findReceiversByGroups")
    public ResResult<List<AllocationGroupReceiverResult>> findReceiversByGroups(Long groupId){
        return Res.ok(allocationGroupService.findReceiversByGroups(groupId));
    }

    @Operation(summary = "创建")
    @PostMapping("/create")
    public ResResult<Void> create(@RequestBody AllocationGroupParam param){
        allocationGroupService.create(param);
        return Res.ok();
    }

    @Operation(summary = "修改")
    @PostMapping("/update")
    public ResResult<Void> update(@RequestBody AllocationGroupParam param){
        allocationGroupService.update(param);
        return Res.ok();
    }

    @Operation(summary = "删除")
    @PostMapping("/delete")
    public ResResult<Void> delete(Long id){
        allocationGroupService.delete(id);
        return Res.ok();
    }

    @Operation(summary = "批量绑定接收者")
    @PostMapping("/bindReceivers")
    public ResResult<Void> bindReceivers(@RequestBody AllocationGroupBindParam param){
        ValidationUtil.validateParam(param);
        allocationGroupService.bindReceivers(param);
        return Res.ok();
    }

    @Operation(summary = "批量取消绑定接收者")
    @PostMapping("/unbindReceivers")
    public ResResult<Void> unbindReceivers(@RequestBody AllocationGroupUnbindParam param){
        allocationGroupService.unbindReceivers(param);
        return Res.ok();
    }

    @Operation(summary = "取消绑定接收者")
    @PostMapping("/unbindReceiver")
    public ResResult<Void> unbindReceiver(Long receiverId){
        allocationGroupService.unbindReceiver(receiverId);
        return Res.ok();
    }

    @Operation(summary = "修改分账比例")
    @PostMapping("/updateRate")
    public ResResult<Void> updateRate(Long receiverId, Integer rate){
        allocationGroupService.updateRate(receiverId,rate);
        return Res.ok();
    }

}
