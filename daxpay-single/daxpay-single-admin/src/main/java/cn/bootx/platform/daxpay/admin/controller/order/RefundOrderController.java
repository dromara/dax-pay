package cn.bootx.platform.daxpay.admin.controller.order;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.spring.util.WebServletUtil;
import cn.bootx.platform.daxpay.param.pay.RefundParam;
import cn.bootx.platform.daxpay.param.pay.RefundSyncParam;
import cn.bootx.platform.daxpay.result.pay.SyncResult;
import cn.bootx.platform.daxpay.service.core.order.refund.service.RefundOrderQueryService;
import cn.bootx.platform.daxpay.service.core.payment.refund.service.RefundService;
import cn.bootx.platform.daxpay.service.core.payment.sync.service.RefundSyncService;
import cn.bootx.platform.daxpay.service.dto.order.refund.RefundOrderDto;
import cn.bootx.platform.daxpay.service.dto.order.refund.RefundChannelOrderDto;
import cn.bootx.platform.daxpay.service.param.order.PayOrderRefundParam;
import cn.bootx.platform.daxpay.service.param.order.RefundOrderQuery;
import cn.hutool.extra.servlet.ServletUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 支付退款控制器
 * @author xxm
 * @since 2024/1/9
 */
@Tag(name = "支付退款控制器")
@RestController
@RequestMapping("/order/refund")
@RequiredArgsConstructor
public class RefundOrderController {
    private final RefundOrderQueryService payRefundQueryService;
    private final RefundService refundService;
    private final RefundSyncService refundSyncService;


    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public ResResult<PageResult<RefundOrderDto>> page(PageParam pageParam, RefundOrderQuery query){
        return Res.ok(payRefundQueryService.page(pageParam, query));
    }

    @Operation(summary = "查询单条")
    @GetMapping("/findById")
    public ResResult<RefundOrderDto> findById(Long id){
        return Res.ok(payRefundQueryService.findById(id));
    }

    @Operation(summary = "通道退款订单列表查询")
    @GetMapping("/listByChannel")
    public ResResult<List<RefundChannelOrderDto>> listByChannel(Long refundId){
        return Res.ok(payRefundQueryService.listByChannel(refundId));
    }

    @Operation(summary = "查询通道退款订单详情")
    @GetMapping("/findChannelById")
    public ResResult<RefundChannelOrderDto> findChannelById(Long id){
        return Res.ok(payRefundQueryService.findChannelById(id));
    }

    @Operation(summary = "手动发起退款")
    @PostMapping("/refund")
    public ResResult<Void> refund(@RequestBody PayOrderRefundParam param){

        String ip = Optional.ofNullable(WebServletUtil.getRequest())
                .map(ServletUtil::getClientIP)
                .orElse("未知");

        RefundParam refundParam = new RefundParam();
        refundParam.setPaymentId(param.getPaymentId());
        refundParam.setRefundChannels(param.getRefundChannels());
        refundParam.setReqTime(LocalDateTime.now());
        refundParam.setClientIp(ip);
        refundService.refund(refundParam);
        return Res.ok();
    }

    @Operation(summary = "退款同步")
    @PostMapping("/syncById")
    public ResResult<SyncResult> syncById(Long id){
        RefundSyncParam refundSyncParam = new RefundSyncParam();
        refundSyncParam.setRefundId(id);
        return Res.ok(refundSyncService.sync(refundSyncParam));
    }
}
