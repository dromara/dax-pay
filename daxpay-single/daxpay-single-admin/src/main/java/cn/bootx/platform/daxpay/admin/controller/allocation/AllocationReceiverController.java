package cn.bootx.platform.daxpay.admin.controller.allocation;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.daxpay.service.core.payment.allocation.service.AllocationReceiverService;
import cn.bootx.platform.daxpay.service.dto.allocation.AllocationReceiverDto;
import cn.bootx.platform.daxpay.service.param.allocation.AllocationReceiverParam;
import cn.bootx.platform.daxpay.service.param.allocation.AllocationReceiverQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 对账接收方控制器
 * @author xxm
 * @since 2024/3/28
 */
@Tag(name = "对账接收方控制器")
@RestController
@RequestMapping("/allocation/receiver")
@RequiredArgsConstructor
public class AllocationReceiverController {

    private final AllocationReceiverService receiverService;

    @Operation(summary = "分页")
    @GetMapping("/page")
    public ResResult<PageResult<AllocationReceiverDto>> page(PageParam pageParam, AllocationReceiverQuery query){
        return Res.ok(receiverService.page(pageParam, query));
    }

    @Operation(summary = "查询详情")
    @GetMapping("/findById")
    public ResResult<AllocationReceiverDto> findById(Long id){
        return Res.ok(receiverService.findById(id));
    }

    @Operation(summary = "新增")
    @PostMapping("")
    public ResResult<Void> add(AllocationReceiverParam param){
        receiverService.add(param);
        return Res.ok();
    }

    @Operation(summary = "修改")
    @PostMapping("update")
    public ResResult<Void> update(AllocationReceiverParam param){
        receiverService.update(param);
        return Res.ok();
    }

    @Operation(summary = "同步到三方支付系统中")
    @PostMapping("registerByGateway")
    public ResResult<Void> registerByGateway(Long id){
        receiverService.registerByGateway(id);
        return Res.ok();
    }

    @Operation(summary = "从三方支付系统中删除")
    @PostMapping("removeByGateway")
    public ResResult<Void> removeByGateway(Long id){
        receiverService.removeByGateway(id);
        return Res.ok();
    }

}
