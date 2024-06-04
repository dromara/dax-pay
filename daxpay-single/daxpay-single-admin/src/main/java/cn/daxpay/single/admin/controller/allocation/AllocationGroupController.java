package cn.daxpay.single.admin.controller.allocation;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.ValidationUtil;
import cn.daxpay.single.service.core.payment.allocation.service.AllocationGroupService;
import cn.daxpay.single.service.dto.allocation.AllocationGroupDto;
import cn.daxpay.single.service.dto.allocation.AllocationGroupReceiverResult;
import cn.daxpay.single.service.param.allocation.group.AllocationGroupBindParam;
import cn.daxpay.single.service.param.allocation.group.AllocationGroupParam;
import cn.daxpay.single.service.param.allocation.group.AllocationGroupUnbindParam;
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


    @Operation(summary = "编码是否存在")
    @GetMapping("/existsByGroupNo")
    public ResResult<Boolean> existsByGroupNo(String groupNo){
        return Res.ok(allocationGroupService.existsByGroupNo(groupNo));
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

    @Operation(summary = "设置默认分账组")
    @PostMapping("/setDefault")
    public ResResult<Void> setDefault(Long id){
        allocationGroupService.setUpDefault(id);
        return Res.ok();
    }

    @Operation(summary = "清除默认分账组")
    @PostMapping("/clearDefault")
    public ResResult<Void> clearDefault(Long id){
        allocationGroupService.clearDefault(id);
        return Res.ok();
    }

}
