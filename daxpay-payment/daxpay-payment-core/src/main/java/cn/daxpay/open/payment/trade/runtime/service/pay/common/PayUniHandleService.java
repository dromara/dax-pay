package cn.daxpay.open.payment.trade.runtime.service.pay.common;

import cn.daxpay.open.payment.trade.enums.GatewayOrderStatusEnum;
import cn.daxpay.open.payment.trade.enums.NormalPayOrderStatusEnum;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.trade.order.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.notice.service.TradeNoticeBridge;
import cn.daxpay.open.payment.trade.runtime.service.plugin.PayPluginAssistService;
import cn.daxpay.open.payment.trade.util.PayTradeAmountUtil;
import cn.daxpay.open.payment.trade.util.PayTradeProviderUtil;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeEventEnum;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

/// # 交易统一处理服务
///
/// 支付成功/失败/关闭后的统一处理逻辑, 按 trade_type 回写对应业务容器。
/// 通道回执字段(含 payBody)统一写容器; trade 仅资金态、outOrderNo、relationOrderNo(实际上送串)。
@Slf4j
@Service
@RequiredArgsConstructor
public class PayUniHandleService {

    private final PayTradeManager payTradeManager;
    private final NormalPayOrderManager payNormalOrderManager;
    private final GatewayPayOrderManager gatewayPayOrderManager;
    private final PayPluginAssistService payPluginAssistService;
    private final TradeNoticeBridge tradeNoticeBridge;

    /// 支付发起后处理
    /// 不论是否完成都更新交易单; 仅资金状态为 SUCCESS 时同步容器为 PAID。
    /// 回执字段(transOrderNo/buyerId/payBody 等)写容器; 特殊 relation 同步写 trade。
    public void payAfterHandel(PayTrade trade, PayTradeResultBo result) {
        // 特殊通道返回的上送变形号写 trade 反查权威
        if (result.getRelationOrderNo() != null) {
            trade.setRelationOrderNo(result.getRelationOrderNo());
        }
        if (isGateway(trade)) {
            GatewayPayOrder order = gatewayPayOrderManager.findById(trade.getContainerId()).orElse(null);
            // 渠道分布报表依赖 trade.provider: 空则从容器/method 兜底
            applyProviderFallback(trade, order);
            updateTradeWithPosted(trade);
            if (order != null) {
                applyGatewayReceipts(order, result);
                if (Objects.equals(trade.getStatus(), PayFundStatusEnum.SUCCESS.getCode())) {
                    order.setStatus(GatewayOrderStatusEnum.PAID.getCode());
                    order.setPayTime(trade.getPayTime());
                }
                gatewayPayOrderManager.updateById(order);
            }
        } else {
            NormalPayOrder order = payNormalOrderManager.findById(trade.getContainerId()).orElse(null);
            applyProviderFallback(trade, order);
            updateTradeWithPosted(trade);
            if (order != null) {
                applyNormalReceipts(order, result);
                if (Objects.equals(trade.getStatus(), PayFundStatusEnum.SUCCESS.getCode())) {
                    order.setStatus(NormalPayOrderStatusEnum.PAID.getCode());
                    order.setPayTime(trade.getPayTime());
                }
                payNormalOrderManager.updateById(order);
            }
        }
        // 出站通知 + 插件: 仅支付成功时
        if (Objects.equals(trade.getStatus(), PayFundStatusEnum.SUCCESS.getCode())) {
            // 商户出站通知(系统协议)
            tradeNoticeBridge.dispatchPay(trade, NoticeEventEnum.PAY_SUCCESS);
            payPluginAssistService.paySuccess(trade);
        }
    }

    /// 支付成功后续处理(同步/回调路径), 含通道回执写容器
    public void paySuccess(PayTrade trade, PaySyncResultBo syncResult) {
        if (isGateway(trade)) {
            GatewayPayOrder order = gatewayPayOrderManager.findById(trade.getContainerId()).orElse(null);
            applyGatewaySyncReceipts(trade, order, syncResult);
            applyProviderFallback(trade, order);
            updateTradeWithPosted(trade);
            if (order != null) {
                order.setStatus(GatewayOrderStatusEnum.PAID.getCode());
                order.setPayTime(trade.getPayTime());
                gatewayPayOrderManager.updateById(order);
            }
            // 商户出站通知(系统协议)
            tradeNoticeBridge.dispatchPay(trade, NoticeEventEnum.PAY_SUCCESS);
            payPluginAssistService.paySuccess(trade);
            return;
        }
        NormalPayOrder order = payNormalOrderManager.findById(trade.getContainerId()).orElse(null);
        applyNormalSyncReceipts(trade, order, syncResult);
        applyProviderFallback(trade, order);
        updateTradeWithPosted(trade);
        if (order != null) {
            order.setStatus(NormalPayOrderStatusEnum.PAID.getCode());
            order.setPayTime(trade.getPayTime());
            payNormalOrderManager.updateById(order);
        }
        // 商户出站通知(系统协议)
        tradeNoticeBridge.dispatchPay(trade, NoticeEventEnum.PAY_SUCCESS);
        payPluginAssistService.paySuccess(trade);
    }

    /// 支付成功后续处理(回调路径, 无回执详情)
    public void paySuccess(PayTrade trade) {
        if (isGateway(trade)) {
            GatewayPayOrder order = gatewayPayOrderManager.findById(trade.getContainerId()).orElse(null);
            applyProviderFallback(trade, order);
            updateTradeWithPosted(trade);
            markContainerPaid(trade, order);
        } else {
            NormalPayOrder order = payNormalOrderManager.findById(trade.getContainerId()).orElse(null);
            applyProviderFallback(trade, order);
            updateTradeWithPosted(trade);
            markContainerPaid(trade, order);
        }
        // 商户出站通知(系统协议)
        tradeNoticeBridge.dispatchPay(trade, NoticeEventEnum.PAY_SUCCESS);
        payPluginAssistService.paySuccess(trade);
    }

    /// 支付失败处理: 资金态 FAIL, 容器态 FAILED（与主动关单 closed 区分）
    public void payFail(PayTrade trade, String errMsg) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        trade.setStatus(PayFundStatusEnum.FAIL.getCode());
        trade.setCloseTime(now);
        updateTradeWithPosted(trade);
        this.markContainerFailed(trade, now, errMsg);
        // 商户出站通知(系统协议)
        tradeNoticeBridge.dispatchPay(trade, NoticeEventEnum.PAY_FAIL);
        payPluginAssistService.payFail(trade);
    }

    /// 支付关闭处理
    /// @param useCancel true=撤销(资金态置 CANCEL), false=关闭(资金态置 CLOSE)
    public void payClose(PayTrade trade, boolean useCancel) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        trade.setStatus(useCancel
                ? PayFundStatusEnum.CANCEL.getCode()
                : PayFundStatusEnum.CLOSE.getCode());
        trade.setCloseTime(now);
        updateTradeWithPosted(trade);
        this.markContainerClosed(trade, now, false, null);
        // 商户出站通知(系统协议)
        tradeNoticeBridge.dispatchPay(trade, NoticeEventEnum.PAY_CLOSE);
        payPluginAssistService.payClose(trade);
    }

    /// 支付超时关闭: 资金态 CLOSE, 容器态 EXPIRED
    public void payTimeout(PayTrade trade) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        trade.setStatus(PayFundStatusEnum.CLOSE.getCode());
        trade.setCloseTime(now);
        updateTradeWithPosted(trade);
        this.markContainerClosed(trade, now, true, null);
        // 商户出站通知(系统协议)
        tradeNoticeBridge.dispatchPay(trade, NoticeEventEnum.PAY_CLOSE);
        payPluginAssistService.payClose(trade);
    }

    /// 回写入账金额后更新资金凭证
    private void updateTradeWithPosted(PayTrade trade) {
        PayTradeAmountUtil.applyPostedAmount(trade);
        payTradeManager.updateById(trade);
    }

    /// 仅关闭网关容器(尚未创建 Trade 的预下单超时)
    public void gatewayOrderTimeout(GatewayPayOrder order) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        order.setStatus(GatewayOrderStatusEnum.EXPIRED.getCode());
        order.setCloseTime(now);
        gatewayPayOrderManager.updateById(order);
    }

    /// 仅关闭网关容器(商户主动关单且尚无 Trade)
    public void gatewayOrderClose(GatewayPayOrder order) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        order.setStatus(GatewayOrderStatusEnum.CLOSED.getCode());
        order.setCloseTime(now);
        gatewayPayOrderManager.updateById(order);
    }

    /// 容器置已支付; 若已加载实体则复用, 避免重复查询, 并同步 provider
    private void markContainerPaid(PayTrade trade, GatewayPayOrder order) {
        if (order == null) {
            return;
        }
        order.setStatus(GatewayOrderStatusEnum.PAID.getCode());
        order.setPayTime(trade.getPayTime());
        if (StrUtil.isBlank(order.getProvider()) && StrUtil.isNotBlank(trade.getProvider())) {
            order.setProvider(trade.getProvider());
        }
        gatewayPayOrderManager.updateById(order);
    }

    private void markContainerPaid(PayTrade trade, NormalPayOrder order) {
        if (order == null) {
            return;
        }
        order.setStatus(NormalPayOrderStatusEnum.PAID.getCode());
        order.setPayTime(trade.getPayTime());
        if (StrUtil.isBlank(order.getProvider()) && StrUtil.isNotBlank(trade.getProvider())) {
            order.setProvider(trade.getProvider());
        }
        payNormalOrderManager.updateById(order);
    }

    /// 支付失败: 容器 FAILED
    private void markContainerFailed(PayTrade trade, OffsetDateTime now, String errMsg) {
        String msg = truncateErrorMsg(errMsg);
        if (isGateway(trade)) {
            GatewayPayOrder order = gatewayPayOrderManager.findById(trade.getContainerId()).orElse(null);
            if (order != null) {
                order.setStatus(GatewayOrderStatusEnum.FAILED.getCode());
                order.setCloseTime(now);
                order.setErrorMsg(msg);
                gatewayPayOrderManager.updateById(order);
            }
            return;
        }
        NormalPayOrder normalOrder = payNormalOrderManager.findById(trade.getContainerId()).orElse(null);
        if (normalOrder != null) {
            normalOrder.setStatus(NormalPayOrderStatusEnum.FAILED.getCode());
            normalOrder.setCloseTime(now);
            normalOrder.setErrorMsg(msg);
            payNormalOrderManager.updateById(normalOrder);
        }
    }

    private void markContainerClosed(PayTrade trade, OffsetDateTime now, boolean expired, String errMsg) {
        if (isGateway(trade)) {
            GatewayPayOrder order = gatewayPayOrderManager.findById(trade.getContainerId()).orElse(null);
            if (order != null) {
                order.setStatus(expired
                        ? GatewayOrderStatusEnum.EXPIRED.getCode()
                        : GatewayOrderStatusEnum.CLOSED.getCode());
                order.setCloseTime(now);
                if (errMsg != null) {
                    order.setErrorMsg(truncateErrorMsg(errMsg));
                }
                gatewayPayOrderManager.updateById(order);
            }
            return;
        }
        NormalPayOrder normalOrder = payNormalOrderManager.findById(trade.getContainerId()).orElse(null);
        if (normalOrder != null) {
            normalOrder.setStatus(expired
                    ? NormalPayOrderStatusEnum.EXPIRED.getCode()
                    : NormalPayOrderStatusEnum.CLOSED.getCode());
            normalOrder.setCloseTime(now);
            if (errMsg != null) {
                normalOrder.setErrorMsg(truncateErrorMsg(errMsg));
            }
            payNormalOrderManager.updateById(normalOrder);
        }
    }

    /// 错误信息截断, 避免通道超长报文撑爆列
    private static String truncateErrorMsg(String errMsg) {
        if (errMsg == null) {
            return null;
        }
        return errMsg.length() <= 500 ? errMsg : errMsg.substring(0, 500);
    }

    private void applyNormalReceipts(NormalPayOrder order, PayTradeResultBo result) {
        order.setTransOrderNo(result.getTransOrderNo());
        // 特殊通道返回变形上送号时回写容器展示; 空则保留创建时的 orderNo 副本
        if (result.getRelationOrderNo() != null) {
            order.setRelationOrderNo(result.getRelationOrderNo());
        }
        order.setBuyerId(result.getBuyerId());
        order.setTradeProduct(result.getTradeProduct());
        order.setTradeWay(result.getTradeWay());
        order.setBankType(result.getBankType());
        order.setPromotionType(result.getPromotionType());
        // 支付参数体仅落容器, 作为已拉起缓存标记
        order.setPayBody(result.getPayBody());
        order.setPayBodyType(Objects.nonNull(result.getPayBodyType())
                ? result.getPayBodyType().getCode() : null);
        order.setErrorMsg(null);
    }

    private void applyGatewayReceipts(GatewayPayOrder order, PayTradeResultBo result) {
        order.setTransOrderNo(result.getTransOrderNo());
        if (result.getRelationOrderNo() != null) {
            order.setRelationOrderNo(result.getRelationOrderNo());
        }
        order.setBuyerId(result.getBuyerId());
        order.setTradeProduct(result.getTradeProduct());
        order.setTradeWay(result.getTradeWay());
        order.setBankType(result.getBankType());
        order.setPromotionType(result.getPromotionType());
        // 支付参数体仅落容器
        order.setPayBody(result.getPayBody());
        order.setPayBodyType(Objects.nonNull(result.getPayBodyType())
                ? result.getPayBodyType().getCode() : null);
        order.setErrorMsg(null);
    }

    private void applyNormalSyncReceipts(PayTrade trade, NormalPayOrder order, PaySyncResultBo syncResult) {
        if (syncResult == null) {
            return;
        }
        if (Objects.nonNull(syncResult.getProvider())) {
            String providerCode = syncResult.getProvider().getCode();
            if (order != null) {
                order.setProvider(providerCode);
            }
            // 冗余至资金凭证, 渠道分布报表/资金列表免 JOIN 容器
            trade.setProvider(providerCode);
        }
        if (order == null) {
            return;
        }
        order.setBuyerId(syncResult.getBuyerId());
        order.setTradeProduct(syncResult.getTradeProduct());
        order.setTradeWay(syncResult.getTradeWay());
        order.setBankType(syncResult.getBankType());
        order.setPromotionType(syncResult.getPromotionType());
        order.setErrorMsg(null);
    }

    private void applyGatewaySyncReceipts(PayTrade trade, GatewayPayOrder order, PaySyncResultBo syncResult) {
        if (syncResult == null) {
            return;
        }
        if (Objects.nonNull(syncResult.getProvider())) {
            String providerCode = syncResult.getProvider().getCode();
            if (order != null) {
                order.setProvider(providerCode);
            }
            // 冗余至资金凭证, 渠道分布报表/资金列表免 JOIN 容器
            trade.setProvider(providerCode);
        }
        if (order == null) {
            return;
        }
        order.setBuyerId(syncResult.getBuyerId());
        order.setTradeProduct(syncResult.getTradeProduct());
        order.setTradeWay(syncResult.getTradeWay());
        order.setBankType(syncResult.getBankType());
        order.setPromotionType(syncResult.getPromotionType());
        order.setErrorMsg(null);
    }

    /// trade.provider 为空时从容器 provider / method 兜底, 并回写容器空 provider
    private void applyProviderFallback(PayTrade trade, NormalPayOrder order) {
        String containerProvider = order != null ? order.getProvider() : null;
        String method = order != null ? order.getMethod() : null;
        String provider = PayTradeProviderUtil.coalesceProvider(trade.getProvider(), containerProvider, method);
        if (StrUtil.isBlank(provider)) {
            return;
        }
        trade.setProvider(provider);
        if (order != null && StrUtil.isBlank(order.getProvider())) {
            order.setProvider(provider);
        }
    }

    private void applyProviderFallback(PayTrade trade, GatewayPayOrder order) {
        String containerProvider = order != null ? order.getProvider() : null;
        String method = order != null ? order.getMethod() : null;
        String provider = PayTradeProviderUtil.coalesceProvider(trade.getProvider(), containerProvider, method);
        if (StrUtil.isBlank(provider)) {
            return;
        }
        trade.setProvider(provider);
        if (order != null && StrUtil.isBlank(order.getProvider())) {
            order.setProvider(provider);
        }
    }

    private boolean isGateway(PayTrade trade) {
        return Objects.equals(trade.getTradeType(), PayTradeTypeEnum.GATEWAY.getCode());
    }
}
