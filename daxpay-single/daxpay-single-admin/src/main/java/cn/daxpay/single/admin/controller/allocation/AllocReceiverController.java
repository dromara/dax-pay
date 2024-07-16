package cn.daxpay.single.admin.controller.allocation;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.dto.LabelValue;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.ValidationUtil;
import cn.daxpay.single.core.code.PaymentApiCode;
import cn.daxpay.single.core.param.payment.allocation.AllocReceiverAddParam;
import cn.daxpay.single.core.param.payment.allocation.AllocReceiverRemoveParam;
import cn.daxpay.single.service.annotation.InitPaymentContext;
import cn.daxpay.single.service.core.payment.allocation.service.AllocReceiverService;
import cn.daxpay.single.service.dto.allocation.AllocReceiverDto;
import cn.daxpay.single.service.param.allocation.receiver.AllocReceiverQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 分账接收方控制器
 * @author xxm
 * @since 2024/3/28
 */
@Tag(name = "分账接收方控制器")
@RestController
@RequestMapping("/allocation/receiver")
@RequiredArgsConstructor
public class AllocReceiverController {

    private final AllocReceiverService receiverService;

    @Operation(summary = "分页")
    @GetMapping("/page")
    public ResResult<PageResult<AllocReceiverDto>> page(PageParam pageParam, AllocReceiverQuery query){
        return Res.ok(receiverService.page(pageParam, query));
    }

    @Operation(summary = "查询详情")
    @GetMapping("/findById")
    public ResResult<AllocReceiverDto> findById(Long id){
        return Res.ok(receiverService.findById(id));
    }

    @Operation(summary = "编码是否存在")
    @GetMapping("/existsByReceiverNo")
    public ResResult<Boolean> existsByReceiverNo(String receiverNo){
        return Res.ok(receiverService.existsByReceiverNo(receiverNo));
    }

    @Operation(summary = "获取可以分账的通道")
    @GetMapping("/findChannels")
    public ResResult<List<LabelValue>> findChannels(){
        return Res.ok(receiverService.findChannels());
    }

    @Operation(summary = "根据通道获取分账接收方类型")
    @GetMapping("/findReceiverTypeByChannel")
    public ResResult<List<LabelValue>> findReceiverTypeByChannel(String channel){
        return Res.ok(receiverService.findReceiverTypeByChannel(channel));
    }

    @InitPaymentContext(value = PaymentApiCode.ALLOCATION_RECEIVER_ADD)
    @Operation(summary = "添加")
    @PostMapping("/add")
    public ResResult<Void> add(@RequestBody AllocReceiverAddParam param){
        param.setReqTime(LocalDateTime.now());
        ValidationUtil.validateParam(param);
        receiverService.addAndSync(param);
        return Res.ok();
    }

    @InitPaymentContext(value = PaymentApiCode.ALLOCATION_RECEIVER_REMOVE)
    @Operation(summary = "删除")
    @PostMapping("/delete")
    public ResResult<Void> delete(@RequestBody AllocReceiverRemoveParam param){
        param.setReqTime(LocalDateTime.now());
        ValidationUtil.validateParam(param);
        receiverService.remove(param);
        return Res.ok();
    }
}
