package cn.daxpay.open.payment.trade.alloc.runtime.service;

import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.common.lock.TradeLockKeys;
import cn.daxpay.open.payment.trade.alloc.bo.AllocResultBo;
import cn.daxpay.open.payment.trade.alloc.entity.AllocOrder;
import cn.daxpay.open.payment.trade.alloc.enums.AllocCapabilityEnum;
import cn.daxpay.open.payment.trade.alloc.enums.AllocDetailResultEnum;
import cn.daxpay.open.payment.trade.alloc.enums.AllocFinishModeEnum;
import cn.daxpay.open.payment.trade.alloc.enums.AllocOrderStatusEnum;
import cn.daxpay.open.payment.trade.alloc.enums.AllocOrderTypeEnum;
import cn.daxpay.open.payment.trade.alloc.enums.TradeAllocStatusEnum;
import cn.daxpay.open.payment.trade.alloc.param.AllocFinishParam;
import cn.daxpay.open.payment.trade.alloc.runtime.bo.AllocatableContainer;
import cn.daxpay.open.payment.trade.alloc.runtime.mq.AllocSyncMessage;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.trade.order.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.runtime.mq.PayArtemisConstants;
import cn.daxpay.open.payment.strategy.alloc.AbsAllocStrategy;
import cn.daxpay.open.payment.strategy.alloc.AllocStrategyContext;
import cn.daxpay.open.payment.strategy.alloc.AllocStrategyFactory;
import cn.daxpay.open.platform.common.artemis.service.ArtemisTemplateService;
import cn.daxpay.open.platform.common.i18n.util.BizErrorMessageUtil;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.common.redis.lock.LockExecutor;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.CurrencyEnum;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.util.TradeNoGenerateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/// # 分账完结编排服务
///
/// 多次分账的收尾: 对已分账未完结(allocated)的订单发起完结, 解冻剩余冻结资金给商户本身。
/// 与 [AllocStartService] 同构的锁编排: 发起锁(bizAllocNo 幂等) + 订单维度锁(tradeNo)。
/// 完结单(allocType=finish)无接收方明细, 复用分账单表/同步/通知链路, 按整体状态(finishState)推进。
@Slf4j
@Service
@RequiredArgsConstructor
public class AllocFinishService {

    private final AllocAssistService assistService;
    private final AllocCapabilityService capabilityService;
    private final PayTradeManager payTradeManager;
    private final NormalPayOrderManager normalPayOrderManager;
    private final GatewayPayOrderManager gatewayPayOrderManager;
    private final MerchantContextLoader merchantContextLoader;
    private final PaymentContext paymentContext;
    private final LockExecutor lockExecutor;
    private final ArtemisTemplateService artemisTemplateService;

    /// 自注入: 保证建单走 Spring 事务代理
    @Lazy
    private final AllocFinishService self;

    /// 延迟同步间隔(与发起链路一致, 秒)
    private static final int[] DELAY_SYNC_SECONDS = {15, 45, 120};

    /// 发起完结入口(持发起锁)
    public String finish(AllocFinishParam param) {
        // 商户身份装载
        merchantContextLoader.initMch(param.getMchNo());
        String mchNo = paymentContext.getMchNo();
        return lockExecutor.execute(
                TradeLockKeys.alloc(mchNo, param.getBizAllocNo()),
                TradeLockKeys.LONG_EXPIRE, TradeLockKeys.LONG_WAIT,
                () -> this.finishHandle(param),
                () -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.alloc.allocProcessing")
        );
    }

    /// 锁内编排(无事务): 幂等查重 → 订单维度锁 → 建单+完结
    public String finishHandle(AllocFinishParam param) {
        String mchNo = paymentContext.getMchNo();
        // 幂等查重: 完结单与分账单共用 bizAllocNo 幂等维度
        Optional<AllocOrder> existOpt = assistService.findByBizAllocNo(param.getBizAllocNo(), mchNo);
        if (existOpt.isPresent()) {
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.alloc.noDuplicate");
        }
        // 解析原支付 tradeNo(用于构造订单维度锁 key)
        String tradeNo = this.resolveTradeNo(param);
        return lockExecutor.execute(
                TradeLockKeys.allocTradeByPayTradeNo(tradeNo),
                TradeLockKeys.LONG_EXPIRE, TradeLockKeys.LONG_WAIT,
                () -> {
                    AllocStrategyContext context = self.createFinishOrder(param, tradeNo);
                    this.doFinish(context);
                    return context.getAllocOrder().getAllocNo();
                },
                () -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.alloc.allocProcessing")
        );
    }

    /// 建完结单(事务内: 校验原支付 + 建完结单)
    @Transactional(rollbackFor = Exception.class)
    public AllocStrategyContext createFinishOrder(AllocFinishParam param, String tradeNo) {
        String mchNo = paymentContext.getMchNo();
        PayTrade trade = payTradeManager.findByTradeNo(tradeNo)
                .orElseThrow(() -> new BizInfoException(CommonCode.FAIL_CODE, "pay.error.alloc.orderNotFound"));
        this.validateFinishable(trade);
        // 通道能力校验: 独立完结须通道声明 FINISH 能力
        if (!capabilityService.supports(trade.getChannel(), AllocCapabilityEnum.FINISH)) {
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.alloc.finishChannelNotSupport");
        }
        // 容器冗余(标题/应用号)
        AllocatableContainer container = this.resolveContainer(trade);
        if (Objects.isNull(container)) {
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.alloc.orderNotFound");
        }
        // 归属校验(资金操作, 防跨商户完结)
        if (!Objects.equals(container.getMchNo(), mchNo)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.orderNotBelong");
        }
        // 构建完结单(无明细, 金额 0)
        String allocNo = TradeNoGenerateUtil.alloc();
        AllocOrder finishOrder = new AllocOrder()
                .setAllocNo(allocNo)
                .setBizAllocNo(param.getBizAllocNo())
                .setTradeNo(trade.getTradeNo())
                .setTradeType(trade.getTradeType())
                .setBizOrderNo(container.getBizOrderNo())
                .setOutOrderNo(trade.getOutOrderNo())
                .setTitle(param.getDescription())
                .setDescription(param.getDescription())
                .setAmount(0L)
                .setOrderAmount(trade.getAmount())
                .setCurrency(CurrencyEnum.CNY.getCode())
                .setStatus(AllocOrderStatusEnum.PROCESSING.getCode())
                .setAllocType(AllocOrderTypeEnum.FINISH.getCode())
                .setFinishMode(AllocFinishModeEnum.AUTO.getCode())
                .setChannel(trade.getChannel())
                .setProvider(trade.getProvider())
                .setProduct(container.getProduct())
                .setChannelMchNo(trade.getChannelMchNo())
                .setCapability(container.getCapability())
                .setChannelAppId(container.getChannelAppId())
                .setNotifyUrl(param.getNotifyUrl())
                .setAttach(param.getAttach())
                .setAppId(container.getAppId());
        // 商户号独立赋值(父类 setter 返回 MchBaseEntity, 禁止链式)
        finishOrder.setMchNo(mchNo);
        assistService.createOrder(finishOrder, new ArrayList<>());
        return new AllocStrategyContext()
                .setAllocOrder(finishOrder)
                .setDetails(new ArrayList<>())
                .setChannel(trade.getChannel())
                .setMchNo(mchNo)
                .setChannelMchNo(finishOrder.getChannelMchNo())
                .setChannelAppId(finishOrder.getChannelAppId())
                .setOutOrderNo(finishOrder.getOutOrderNo())
                .setNotifyUrl(finishOrder.getNotifyUrl());
    }

    /// 通道完结(事务外)
    private void doFinish(AllocStrategyContext context) {
        AllocOrder finishOrder = context.getAllocOrder();
        String channel = context.getChannel();
        try {
            AbsAllocStrategy strategy = AllocStrategyFactory.create(channel);
            AllocResultBo result = strategy.doFinish(context);
            // 回写通道分账号(如有)
            if (Objects.nonNull(result.getOutAllocNo())) {
                assistService.processing(finishOrder, result.getOutAllocNo());
            }
            String finishState = result.getFinishState();
            if (Objects.equals(finishState, AllocDetailResultEnum.SUCCESS.getCode())) {
                // 即时成功(部分通道完结同步返回)
                assistService.success(finishOrder, null);
            } else if (Objects.equals(finishState, AllocDetailResultEnum.FAIL.getCode())) {
                String errorMsg = Objects.nonNull(result.getSyncErrorMsg())
                        ? result.getSyncErrorMsg()
                        : I18nUtil.get("pay.error.alloc.finishFailedDefault", Locale.CHINA);
                assistService.fail(finishOrder, null, errorMsg);
                throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "pay.error.alloc.finishFailed", errorMsg);
            } else {
                // 受理中: 保持 processing, 延迟同步推进
                this.registerDelaySync(finishOrder.getAllocNo());
            }
        } catch (BizException e) {
            // 通道业务拒绝/能力不支持: 置 fail 并回退原支付状态(即时失败分支已 fail, 二次调用幂等无害)
            assistService.fail(finishOrder, null, BizErrorMessageUtil.resolve(e));
            throw e;
        } catch (Exception e) {
            log.error("分账完结发起失败: allocNo={}, channel={}", finishOrder.getAllocNo(), channel, e);
            // 结果未知: 通道可能已受理, 保留 processing 由延迟同步纠正(不置 fail 防止重复完结)
            this.registerDelaySync(finishOrder.getAllocNo());
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "pay.error.alloc.syncProcessing");
        }
    }

    /// 校验原支付可完结
    private void validateFinishable(PayTrade trade) {
        // 原支付须成功
        if (!Objects.equals(trade.getStatus(), PayFundStatusEnum.SUCCESS.getCode())) {
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.alloc.orderNotSuccess");
        }
        String allocStatus = trade.getAllocStatus();
        // 仅已分账未完结可发起完结
        if (!Objects.equals(allocStatus, TradeAllocStatusEnum.ALLOCATED.getCode())) {
            if (Objects.equals(allocStatus, TradeAllocStatusEnum.PROCESSING.getCode())) {
                throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.alloc.allocProcessing");
            }
            if (TradeAllocStatusEnum.FINISHED.getCode().equals(allocStatus)
                    || TradeAllocStatusEnum.DONE.getCode().equals(allocStatus)) {
                throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.alloc.finishAlreadyFinished");
            }
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.alloc.finishNotAllowed");
        }
    }

    /// 按 tradeType 解析分账源容器(normal / gateway)
    private AllocatableContainer resolveContainer(PayTrade trade) {
        if (Objects.equals(trade.getTradeType(), PayTradeTypeEnum.GATEWAY.getCode())) {
            return gatewayPayOrderManager.findById(trade.getContainerId()).orElse(null);
        }
        return normalPayOrderManager.findById(trade.getContainerId()).orElse(null);
    }

    /// 解析原支付资金交易号(只读, 供构造订单维度锁 key)
    private String resolveTradeNo(AllocFinishParam param) {
        if (Objects.nonNull(param.getTradeNo()) && !param.getTradeNo().isBlank()) {
            return param.getTradeNo();
        }
        if (Objects.nonNull(param.getBizOrderNo()) && !param.getBizOrderNo().isBlank()) {
            String bizOrderNo = param.getBizOrderNo();
            String mchNo = paymentContext.getMchNo();
            // bizOrderNo 幂等唯一维度为商户(uk_normal_order_mch_biz), 定位必须带商户号
            Optional<NormalPayOrder> normalOpt = normalPayOrderManager.findByBizOrderNoAndMch(bizOrderNo, mchNo);
            if (normalOpt.isPresent()) {
                return payTradeManager.findByContainerId(normalOpt.get().getId(), PayTradeTypeEnum.NORMAL.getCode())
                        .map(PayTrade::getTradeNo)
                        .orElseThrow(() -> new BizInfoException(CommonCode.FAIL_CODE, "pay.error.alloc.orderNotFound"));
            }
            return gatewayPayOrderManager.findByBizOrderNoAndMch(bizOrderNo, mchNo)
                    .flatMap(gateway -> payTradeManager.findByContainerId(gateway.getId(), PayTradeTypeEnum.GATEWAY.getCode()))
                    .map(PayTrade::getTradeNo)
                    .orElseThrow(() -> new BizInfoException(CommonCode.FAIL_CODE, "pay.error.alloc.orderNotFound"));
        }
        throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.alloc.tradeNoRequired");
    }

    /// 注册延迟同步(完结受理后, 多级延时投递 MQ)
    private void registerDelaySync(String allocNo) {
        for (int delaySeconds : DELAY_SYNC_SECONDS) {
            try {
                AllocSyncMessage message = new AllocSyncMessage().setAllocNo(allocNo);
                artemisTemplateService.sendDelay(PayArtemisConstants.ALLOC_SYNC_QUEUE,
                        JacksonUtil.toJson(message), delaySeconds);
            } catch (Exception e) {
                log.warn("注册分账完结延迟同步失败, 由定时任务兜底, allocNo={}, delay={}s", allocNo, delaySeconds, e);
            }
        }
    }
}
