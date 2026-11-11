package cn.daxpay.open.plugin.easypay.service.order;

import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.trade.notice.command.NoticeDispatchCommand;
import cn.daxpay.open.payment.trade.notice.service.NoticeDispatcher;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeContentModeEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeEventEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeFormatEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeTransportEnum;
import cn.daxpay.open.plugin.easypay.dao.EasyPayOrderManager;
import cn.daxpay.open.plugin.easypay.dao.EasyPayRefundOrderManager;
import cn.daxpay.open.plugin.easypay.entity.EasyPayCredential;
import cn.daxpay.open.plugin.easypay.entity.EasyPayOrder;
import cn.daxpay.open.plugin.easypay.entity.EasyPayRefundOrder;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;
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
    private final NoticeDispatcher noticeDispatcher;

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
                // 内核退款单金额已是分, 直接落库; 空值兜底 0 保持原行为
                .setMoney(Objects.isNull(refundOrder.getAmount()) ? 0L : refundOrder.getAmount())
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
        // 同步退款建单即为成功态, 直接注册退款出站通知
        if (Objects.equals(entity.getStatus(), 1)) {
            registerRefundNotice(entity);
        }
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
        registerRefundNotice(entity);
    }

    /// 根据内核退款单定位关联的易支付订单 ID
    private Optional<Long> findEasyPayOrderId(RefundOrder refundOrder) {
        // 优先用商户订单号查
        if (Objects.nonNull(refundOrder.getBizOrderNo())) {
            var opt = easyPayOrderManager.findByOutTradeNo(refundOrder.getBizOrderNo());
            if (opt.isPresent()) {
                return Optional.of(opt.get().getId());
            }
        }
        // 再用平台业务单号查
        if (Objects.nonNull(refundOrder.getTradeNo())) {
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
        if (mapped == 1 && Objects.isNull(entity.getEndTime())) {
            entity.setEndTime(OffsetDateTime.now(ZoneOffset.UTC));
        }
        easyPayRefundOrderManager.updateById(entity);
        // 内核状态同步为成功时同样注册退款通知(与 markSuccess 路径互补)
        if (mapped == 1) {
            registerRefundNotice(entity);
        }
    }

    /// 退款确认成功后注册易支付退款出站通知
    ///
    /// 退款接口不单独接收 notify_url, 按协议惯例回发至原支付单的 notify_url;
    /// content 仅存退款镜像单 id 指针, 报文在发送时由 [cn.daxpay.open.plugin.easypay.notice.EasyPayPayloadBuilder] 实时组装。
    private void registerRefundNotice(EasyPayRefundOrder refundOrder) {
        // 原支付单未关联时无法取得回调地址, 跳过
        if (Objects.isNull(refundOrder.getEasyPayOrderId())) {
            log.info("易支付退款通知跳过: 未关联原支付单, refundNo={}", refundOrder.getRefundNo());
            return;
        }
        EasyPayOrder easyPayOrder = easyPayOrderManager.findByIdNotTenant(refundOrder.getEasyPayOrderId()).orElse(null);
        if (Objects.isNull(easyPayOrder) || StrUtil.isBlank(easyPayOrder.getNotifyUrl())) {
            log.info("易支付退款通知跳过: 原支付单无回调地址, refundNo={}", refundOrder.getRefundNo());
            return;
        }
        String content = JacksonUtil.toJson(Map.of(
                "refundId", refundOrder.getId(),
                "pid", Objects.isNull(refundOrder.getPid()) ? 0 : refundOrder.getPid(),
                "remark", "ref-only; payload assembled at send time"
        ));
        noticeDispatcher.dispatch(new NoticeDispatchCommand()
                .setMchNo(refundOrder.getMchNo())
                .setAppId(refundOrder.getAppId())
                .setEvent(NoticeEventEnum.REFUND_SUCCESS.getCode())
                .setBizId(refundOrder.getId())
                .setBizNo(Objects.nonNull(refundOrder.getRefundNo()) ? refundOrder.getRefundNo() : refundOrder.getBizRefundNo())
                .setTransport(NoticeTransportEnum.HTTP)
                .setFormat(NoticeFormatEnum.EASY_PAY)
                .setContentMode(NoticeContentModeEnum.REF)
                .setContentOrRef(content)
                .setProtocolNotifyUrl(easyPayOrder.getNotifyUrl()));
    }

    /// 内核退款状态码 → 易支付协议状态(1=成功, 0=其它)
    private int mapStatus(String kernelStatus) {
        return Objects.equals(kernelStatus, RefundOrderStatusEnum.SUCCESS.getCode()) ? 1 : 0;
    }
}
