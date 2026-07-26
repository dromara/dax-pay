package cn.daxpay.open.plugin.easypay.service.order;

import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.plugin.easypay.dao.EasyPayOrderManager;
import cn.daxpay.open.plugin.easypay.dao.EasyPayRefundOrderManager;
import cn.daxpay.open.plugin.easypay.entity.EasyPayCredential;
import cn.daxpay.open.plugin.easypay.entity.EasyPayRefundOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.Optional;

/// # 易支付协议退款订单生命周期回写服务
///
/// 仅承担退款双写与状态钩子:
/// - [createFromKernelRefund]: 退款发起后由 [cn.daxpay.open.plugin.easypay.service.api.v2.EasyPayRefundV2Service] 调用, 创建/更新易支付退款镜像
/// - [markSuccess]: 异步退款最终成功时由 [cn.daxpay.open.plugin.easypay.strategy.EasyPayPluginStrategy] 经插件链调用
///
/// 管理端入口(分页/详情/同步) 见 [EasyPayRefundOrderQueryService], 与本类解耦以避免循环依赖:
/// 钩子链位于 [cn.daxpay.open.payment.trade.runtime.service.plugin.PayPluginAssistService] 的下游,
/// 若本类同时持有 [cn.daxpay.open.payment.trade.order.service.RefundOrderService] 用于同步透传,
/// 会经 RefundOrderService -> RefundService -> RefundSettleService -> PayPluginAssistService -> 插件链 -> 本类 形成构造期循环依赖。
@Slf4j
@Service
@RequiredArgsConstructor
public class EasyPayRefundOrderService {

    private final EasyPayRefundOrderManager easyPayRefundOrderManager;
    private final EasyPayOrderManager easyPayOrderManager;

    /// 退款发起后由内核退款单创建/更新易支付退款记录(双写)
    public void createFromKernelRefund(RefundOrder refundOrder, EasyPayCredential credential) {
        // 幂等：已存在则按内核状态更新
        var existing = easyPayRefundOrderManager.findByRefundId(refundOrder.getId());
        if (existing.isPresent()) {
            updateStatusFromKernel(existing.get(), refundOrder);
            return;
        }
        EasyPayRefundOrder entity = new EasyPayRefundOrder()
                .setRefundId(refundOrder.getId())
                .setRefundNo(refundOrder.getRefundNo())
                .setBizRefundNo(refundOrder.getBizRefundNo())
                .setTradeNo(refundOrder.getTradeNo())
                .setOutTradeNo(refundOrder.getBizOrderNo())
                .setPid(credential.getPid())
                .setAppId(credential.getAppId())
                .setMoney(fenToYuan(refundOrder.getAmount()))
                .setStatus(mapStatus(refundOrder.getStatus()))
                .setApiVersion("v2")
                .setAddTime(OffsetDateTime.now(ZoneOffset.UTC));
        // MchBaseEntity#setMchNo 返回父类类型，单独赋值不链式
        entity.setMchNo(credential.getMchNo());
        if (Objects.equals(entity.getStatus(), 1)) {
            entity.setEndTime(OffsetDateTime.now(ZoneOffset.UTC));
        }
        // 关联易支付订单
        findEasyPayOrderId(refundOrder).ifPresent(entity::setEasyPayOrderId);
        easyPayRefundOrderManager.save(entity);
    }

    /// 退款成功钩子回写(异步退款最终成功时由插件策略调用)
    public void markSuccess(RefundOrder refundOrder) {
        var opt = easyPayRefundOrderManager.findByRefundIdNotTenant(refundOrder.getId());
        if (opt.isEmpty()) {
            // 同步退款时记录在 refund() 返回后才创建，此处跳过
            return;
        }
        EasyPayRefundOrder entity = opt.get();
        if (Objects.equals(entity.getStatus(), 1)) {
            // 已是成功，幂等跳过
            return;
        }
        entity.setStatus(1).setEndTime(OffsetDateTime.now(ZoneOffset.UTC));
        easyPayRefundOrderManager.updateById(entity);
    }

    /// 根据内核退款单定位关联的易支付订单 ID
    private Optional<Long> findEasyPayOrderId(RefundOrder refundOrder) {
        // 优先用商户订单号查
        if (refundOrder.getBizOrderNo() != null) {
            var opt = easyPayOrderManager.findByOutTradeNo(refundOrder.getBizOrderNo());
            if (opt.isPresent()) {
                return Optional.of(opt.get().getId());
            }
        }
        // 再用平台业务单号查
        if (refundOrder.getTradeNo() != null) {
            var opt = easyPayOrderManager.findByTradeNo(refundOrder.getTradeNo());
            if (opt.isPresent()) {
                return Optional.of(opt.get().getId());
            }
        }
        return Optional.empty();
    }

    /// 根据内核状态更新易支付退款单状态
    private void updateStatusFromKernel(EasyPayRefundOrder entity, RefundOrder refundOrder) {
        int mapped = mapStatus(refundOrder.getStatus());
        if (Objects.equals(entity.getStatus(), mapped)) {
            return;
        }
        entity.setStatus(mapped);
        if (mapped == 1 && entity.getEndTime() == null) {
            entity.setEndTime(OffsetDateTime.now(ZoneOffset.UTC));
        }
        easyPayRefundOrderManager.updateById(entity);
    }

    /// 内核退款状态码 → 易支付协议状态(1=成功, 0=其它)
    private int mapStatus(String kernelStatus) {
        return Objects.equals(kernelStatus, RefundOrderStatusEnum.SUCCESS.getCode()) ? 1 : 0;
    }

    /// 分(最小货币单位) → 元(BigDecimal)
    private BigDecimal fenToYuan(Long fen) {
        if (fen == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(fen).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
}
