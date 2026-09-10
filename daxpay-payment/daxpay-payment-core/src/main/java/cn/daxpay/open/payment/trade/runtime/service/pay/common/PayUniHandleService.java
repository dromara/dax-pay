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
import cn.daxpay.open.payment.trade.order.entity.PayReceiptContainer;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.notice.service.TradeNoticeBridge;
import cn.daxpay.open.payment.trade.flow.service.FundFlowService;
import cn.daxpay.open.payment.trade.runtime.service.plugin.PayPluginAssistService;
import cn.daxpay.open.payment.trade.util.PayTradeAmountUtil;
import cn.daxpay.open.payment.trade.util.PayTradeProviderUtil;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeEventEnum;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.LongFunction;

/// # 交易统一处理服务
///
/// 支付成功/失败/关闭后的统一处理逻辑, 按 trade_type 回写对应业务容器。
/// 双容器([NormalPayOrder] / [GatewayPayOrder])经 [PayReceiptContainer] 契约统一操作,
/// 加载/保存与各终态业务码差异由 [ContainerRoute] 路由承载。
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
    private final PayRiskAssistService payRiskAssistService;
    private final FundFlowService fundFlowService;

    /// 支付发起后处理
    /// 不论是否完成都更新交易单; 仅资金状态为 SUCCESS 时同步容器为 PAID。
    /// 回执字段(transOrderNo/buyerId/payBody 等)写容器; 特殊 relation 同步写 trade。
    @Transactional(rollbackFor = Exception.class)
    public void payAfterHandel(PayTrade trade, PayTradeResultBo result) {
        // 特殊通道返回的上送变形号写 trade 反查权威
        if (Objects.nonNull(result.getRelationOrderNo())) {
            trade.setRelationOrderNo(result.getRelationOrderNo());
        }
        // CAS 前置态: 支付发起后仅 INIT/PROCESSING 可流转
        Set<String> expectFrom = Set.of(
                PayFundStatusEnum.INIT.getCode(),
                PayFundStatusEnum.PROCESSING.getCode());
        ContainerRoute route = router(trade);
        PayReceiptContainer order = route.loader().apply(trade.getContainerId());
        // 渠道分布报表依赖 trade.provider: 空则从容器/method 兜底
        applyProviderFallback(trade, order);
        if (!updateTradeWithPosted(trade, expectFrom)) {
            log.warn("payAfterHandel CAS 失败, 状态已被其他线程改变, tradeNo={}", trade.getTradeNo());
            return;
        }
        if (Objects.nonNull(order)) {
            applyReceipts(order, result);
            if (Objects.equals(trade.getStatus(), PayFundStatusEnum.SUCCESS.getCode())) {
                order.setStatus(route.paidCode());
                order.setPayTime(trade.getPayTime());
            }
            route.saver().accept(order);
        }
        // 出站通知 + 插件 + 事后风控: 仅支付成功时
        if (Objects.equals(trade.getStatus(), PayFundStatusEnum.SUCCESS.getCode())) {
            this.afterSuccess(trade, result.getBuyerId());
        }
    }

    /// 支付成功后续处理(同步路径), 含通道回执写容器
    @Transactional(rollbackFor = Exception.class)
    public void paySuccess(PayTrade trade, PaySyncResultBo syncResult) {
        // CAS 前置态: 同步路径仅 PROCESSING 可翻转(终态收款证据走异常订单人工处置, 2026-08-29 决策)
        Set<String> expectFrom = Set.of(
                PayFundStatusEnum.PROCESSING.getCode());
        ContainerRoute route = router(trade);
        PayReceiptContainer order = route.loader().apply(trade.getContainerId());
        applySyncReceipts(trade, order, syncResult);
        applyProviderFallback(trade, order);
        if (!updateTradeWithPosted(trade, expectFrom)) {
            log.warn("paySuccess(sync) CAS 失败, 状态已被其他线程改变, tradeNo={}", trade.getTradeNo());
            return;
        }
        if (Objects.nonNull(order)) {
            // provider 已在 applySyncReceipts 回填, 此处不重复兜底
            order.setStatus(route.paidCode());
            order.setPayTime(trade.getPayTime());
            route.saver().accept(order);
        }
        this.afterSuccess(trade, Objects.nonNull(syncResult) ? syncResult.getBuyerId() : null);
    }

    /// 支付成功后续处理(回调路径)；可选回写 buyerId 后补录风控
    @Transactional(rollbackFor = Exception.class)
    public void paySuccess(PayTrade trade, String buyerId) {
        // CAS 前置态: 回调路径仅 PROCESSING/INIT 可流转为 SUCCESS
        Set<String> expectFrom = Set.of(
                PayFundStatusEnum.PROCESSING.getCode(),
                PayFundStatusEnum.INIT.getCode());
        ContainerRoute route = router(trade);
        PayReceiptContainer order = route.loader().apply(trade.getContainerId());
        if (Objects.nonNull(order) && StrUtil.isNotBlank(buyerId)) {
            order.setBuyerId(buyerId);
        }
        applyProviderFallback(trade, order);
        if (!updateTradeWithPosted(trade, expectFrom)) {
            log.warn("paySuccess(callback) CAS 失败, 状态已被其他线程改变, tradeNo={}", trade.getTradeNo());
            return;
        }
        markContainerPaid(trade, order, route);
        // 商户出站通知(系统协议)
        this.afterSuccess(trade, buyerId);
    }

    /// 人工确认支付成功(异常订单处置专用)
    ///
    /// 终态(FAIL/CLOSE/CANCEL)翻转为 SUCCESS: 由运营在异常订单页核实通道已收款后触发,
    /// 入账时间取处置时刻(通道完成时间未结构化留存, 补单语义)。
    /// 统一走 [afterSuccess] 补发商户 PAY_SUCCESS 通知/插件/事后风控/资金流水。
    @Transactional(rollbackFor = Exception.class)
    public void confirmPaySuccess(PayTrade trade) {
        // CAS 前置态: 仅终态 FAIL/CLOSE/CANCEL 可人工确认翻转
        Set<String> expectFrom = Set.of(
                PayFundStatusEnum.FAIL.getCode(),
                PayFundStatusEnum.CLOSE.getCode(),
                PayFundStatusEnum.CANCEL.getCode());
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        trade.setStatus(PayFundStatusEnum.SUCCESS.getCode());
        trade.setPayTime(now);
        trade.setCloseTime(null);
        ContainerRoute route = router(trade);
        PayReceiptContainer order = route.loader().apply(trade.getContainerId());
        applyProviderFallback(trade, order);
        if (!updateTradeWithPosted(trade, expectFrom)) {
            log.warn("confirmPaySuccess CAS 失败, 状态已被其他线程改变, tradeNo={}", trade.getTradeNo());
            return;
        }
        markContainerPaid(trade, order, route);
        this.afterSuccess(trade, null);
    }

    /// 支付成功统一后置动作: 资金流水 + 商户出站通知 + 插件 + 事后风控
    private void afterSuccess(PayTrade trade, String buyerId) {
        // 资金流水(收款, 幂等)
        fundFlowService.savePayFlow(trade);
        // 商户出站通知(系统协议)
        tradeNoticeBridge.dispatchPay(trade, NoticeEventEnum.PAY_SUCCESS);
        payPluginAssistService.paySuccess(trade);
        // 事后风控补录（仅用付款用户 buyerId，不用通道内部 userId）
        payRiskAssistService.checkAfterPay(trade, buyerId);
    }

    /// 支付失败处理: 资金态 FAIL, 容器态 FAILED（与主动关单 closed 区分）
    @Transactional(rollbackFor = Exception.class)
    public void payFail(PayTrade trade, String errMsg) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        trade.setStatus(PayFundStatusEnum.FAIL.getCode());
        trade.setCloseTime(now);
        // CAS 前置态: 仅 PROCESSING 可转为 FAIL
        if (!updateTradeWithPosted(trade, Set.of(PayFundStatusEnum.PROCESSING.getCode()))) {
            log.warn("payFail CAS 失败, 状态已被其他线程改变, tradeNo={}", trade.getTradeNo());
            return;
        }
        this.markContainerFailed(trade, now, errMsg);
        // 商户出站通知(系统协议)
        tradeNoticeBridge.dispatchPay(trade, NoticeEventEnum.PAY_FAIL);
        payPluginAssistService.payFail(trade);
    }

    /// 支付关闭处理
    /// @param useCancel true=撤销(资金态置 CANCEL), false=关闭(资金态置 CLOSE)
    @Transactional(rollbackFor = Exception.class)
    public void payClose(PayTrade trade, boolean useCancel) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        trade.setStatus(useCancel
                ? PayFundStatusEnum.CANCEL.getCode()
                : PayFundStatusEnum.CLOSE.getCode());
        trade.setCloseTime(now);
        // CAS 前置态: INIT/PROCESSING 可关闭
        if (!updateTradeWithPosted(trade, Set.of(
                PayFundStatusEnum.INIT.getCode(),
                PayFundStatusEnum.PROCESSING.getCode()))) {
            log.warn("payClose CAS 失败, 状态已被其他线程改变, tradeNo={}", trade.getTradeNo());
            return;
        }
        this.markContainerClosed(trade, now, false, null);
        // 商户出站通知: 撤销(资金态 CANCEL)发 PAY_CANCEL, 普通关闭发 PAY_CLOSE
        tradeNoticeBridge.dispatchPay(trade, useCancel ? NoticeEventEnum.PAY_CANCEL : NoticeEventEnum.PAY_CLOSE);
        payPluginAssistService.payClose(trade);
    }

    /// 支付超时关闭: 资金态 CLOSE, 容器态 EXPIRED
    @Transactional(rollbackFor = Exception.class)
    public void payTimeout(PayTrade trade) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        trade.setStatus(PayFundStatusEnum.CLOSE.getCode());
        trade.setCloseTime(now);
        // CAS 前置态: 仅 PROCESSING 可超时关闭
        if (!updateTradeWithPosted(trade, Set.of(PayFundStatusEnum.PROCESSING.getCode()))) {
            log.warn("payTimeout CAS 失败, 状态已被其他线程改变, tradeNo={}", trade.getTradeNo());
            return;
        }
        this.markContainerClosed(trade, now, true, null);
        // 商户出站通知: 超时关闭(容器态 EXPIRED)发 PAY_TIMEOUT, 区别于主动关单
        tradeNoticeBridge.dispatchPay(trade, NoticeEventEnum.PAY_TIMEOUT);
        payPluginAssistService.payClose(trade);
    }

    /// 回写入账金额后更新资金凭证（CAS 式，保证状态变更原子性）
    ///
    /// @param trade      已设置目标状态的实体
    /// @param expectFrom 合法的前置状态集合，仅当 DB 当前状态在此集合内时才更新
    /// @return true=更新成功；false=状态已被其他线程改变（调用方应幂等退出）
    private boolean updateTradeWithPosted(PayTrade trade, Set<String> expectFrom) {
        PayTradeAmountUtil.applyPostedAmount(trade);
        return payTradeManager.casUpdateStatus(trade, expectFrom);
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

    /// 容器置已支付; 若已加载实体则复用, 避免重复查询, 并同步 provider 回填
    private void markContainerPaid(PayTrade trade, PayReceiptContainer order, ContainerRoute route) {
        if (Objects.isNull(order)) {
            return;
        }
        order.setStatus(route.paidCode());
        order.setPayTime(trade.getPayTime());
        if (StrUtil.isBlank(order.getProvider()) && StrUtil.isNotBlank(trade.getProvider())) {
            order.setProvider(trade.getProvider());
        }
        route.saver().accept(order);
    }

    /// 支付失败: 容器 FAILED
    private void markContainerFailed(PayTrade trade, OffsetDateTime now, String errMsg) {
        String msg = truncateErrorMsg(errMsg);
        ContainerRoute route = router(trade);
        PayReceiptContainer order = route.loader().apply(trade.getContainerId());
        if (Objects.nonNull(order)) {
            order.setStatus(route.failedCode());
            order.setCloseTime(now);
            order.setErrorMsg(msg);
            route.saver().accept(order);
        }
    }

    /// 容器置关闭(CLOSED)或超时(EXPIRED)
    private void markContainerClosed(PayTrade trade, OffsetDateTime now, boolean expired, String errMsg) {
        ContainerRoute route = router(trade);
        PayReceiptContainer order = route.loader().apply(trade.getContainerId());
        if (Objects.nonNull(order)) {
            order.setStatus(expired
                    ? route.expiredCode()
                    : route.closedCode());
            order.setCloseTime(now);
            if (Objects.nonNull(errMsg)) {
                order.setErrorMsg(truncateErrorMsg(errMsg));
            }
            route.saver().accept(order);
        }
    }

    /// 错误信息截断, 避免通道超长报文撑爆列
    private static String truncateErrorMsg(String errMsg) {
        if (Objects.isNull(errMsg)) {
            return null;
        }
        return errMsg.length() <= 500 ? errMsg : errMsg.substring(0, 500);
    }

    /// 容器写入支付回执字段(transOrderNo/buyerId/payBody 等)
    private void applyReceipts(PayReceiptContainer order, PayTradeResultBo result) {
        order.setTransOrderNo(result.getTransOrderNo());
        // 特殊通道返回变形上送号时回写容器展示; 空则保留创建时的 orderNo 副本
        if (Objects.nonNull(result.getRelationOrderNo())) {
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

    /// 容器写入同步查单回执字段(含 provider 回填)
    private void applySyncReceipts(PayTrade trade, PayReceiptContainer order, PaySyncResultBo syncResult) {
        if (Objects.isNull(syncResult)) {
            return;
        }
        if (Objects.nonNull(syncResult.getProvider())) {
            String providerCode = syncResult.getProvider().getCode();
            if (Objects.nonNull(order)) {
                order.setProvider(providerCode);
            }
            // 冗余至资金凭证, 渠道分布报表/资金列表免 JOIN 容器
            trade.setProvider(providerCode);
        }
        if (Objects.isNull(order)) {
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
    private void applyProviderFallback(PayTrade trade, PayReceiptContainer order) {
        String containerProvider = Objects.nonNull(order) ? order.getProvider() : null;
        String method = Objects.nonNull(order) ? order.getMethod() : null;
        String provider = PayTradeProviderUtil.coalesceProvider(trade.getProvider(), containerProvider, method);
        if (StrUtil.isBlank(provider)) {
            return;
        }
        trade.setProvider(provider);
        if (Objects.nonNull(order) && StrUtil.isBlank(order.getProvider())) {
            order.setProvider(provider);
        }
    }

    /// 容器路由: 按交易类型绑定容器加载/保存函数与各终态业务码
    ///
    /// loader 返回 null 表示容器不存在(预下单前的关单等场景); saver 的强转安全
    /// 由"容器必经同一路由的 loader 加载"保证。
    private record ContainerRoute(
            LongFunction<PayReceiptContainer> loader,
            Consumer<PayReceiptContainer> saver,
            String paidCode,
            String failedCode,
            String closedCode,
            String expiredCode) {
    }

    /// 按资金凭证交易类型路由到对应业务容器
    private ContainerRoute router(PayTrade trade) {
        if (isGateway(trade)) {
            return new ContainerRoute(
                    id -> gatewayPayOrderManager.findById(id).orElse(null),
                    order -> gatewayPayOrderManager.updateById((GatewayPayOrder) order),
                    GatewayOrderStatusEnum.PAID.getCode(),
                    GatewayOrderStatusEnum.FAILED.getCode(),
                    GatewayOrderStatusEnum.CLOSED.getCode(),
                    GatewayOrderStatusEnum.EXPIRED.getCode());
        }
        return new ContainerRoute(
                id -> payNormalOrderManager.findById(id).orElse(null),
                order -> payNormalOrderManager.updateById((NormalPayOrder) order),
                NormalPayOrderStatusEnum.PAID.getCode(),
                NormalPayOrderStatusEnum.FAILED.getCode(),
                NormalPayOrderStatusEnum.CLOSED.getCode(),
                NormalPayOrderStatusEnum.EXPIRED.getCode());
    }

    /// 判断资金凭证是否为网关支付类型
    private boolean isGateway(PayTrade trade) {
        return Objects.equals(trade.getTradeType(), PayTradeTypeEnum.GATEWAY.getCode());
    }
}
