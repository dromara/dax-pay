package cn.daxpay.open.payment.trade.alloc.runtime.service;

import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.trade.alloc.bo.AllocResultBo;
import cn.daxpay.open.payment.trade.alloc.dao.AllocDetailManager;
import cn.daxpay.open.payment.trade.alloc.entity.AllocDetail;
import cn.daxpay.open.payment.trade.alloc.entity.AllocOrder;
import cn.daxpay.open.payment.trade.alloc.enums.AllocDetailResultEnum;
import cn.daxpay.open.payment.trade.alloc.param.AllocParam;
import cn.daxpay.open.payment.trade.alloc.runtime.bo.AllocatableContainer;
import cn.daxpay.open.payment.trade.alloc.runtime.mq.AllocSyncMessage;
import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.trade.order.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.runtime.mq.PayArtemisConstants;
import cn.daxpay.open.payment.trade.util.CurrencyAmountUtil;
import cn.daxpay.open.payment.strategy.alloc.AbsAllocStrategy;
import cn.daxpay.open.payment.strategy.alloc.AllocStrategyContext;
import cn.daxpay.open.payment.strategy.alloc.AllocStrategyFactory;
import cn.daxpay.open.platform.common.artemis.service.ArtemisTemplateService;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.common.redis.lock.LockExecutor;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.CurrencyEnum;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.util.TradeNoGenerateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/// # 分账发起编排服务
///
/// 参照 [cn.daxpay.open.payment.trade.transfer.runtime.service.TransferStartService] 设计：
/// - 锁入口(start) 持发起锁, 无事务
/// - 锁内编排(startHandle) 幂等查重 + 建单(独立事务) + 通道发起(事务外)
/// - 建单(createOrder) 独立事务, 通过自注入走 Spring 代理
///
/// 分账需定位原支付订单(PayTrade), 继承其通道凭证快照, 禁止二次路由。
@Slf4j
@Service
@RequiredArgsConstructor
public class AllocStartService {

    private final AllocAssistService assistService;
    private final AllocDetailManager allocDetailManager;
    private final PayTradeManager payTradeManager;
    private final NormalPayOrderManager normalPayOrderManager;
    private final GatewayPayOrderManager gatewayPayOrderManager;
    private final MerchantContextLoader merchantContextLoader;
    private final PaymentContext paymentContext;
    private final LockExecutor lockExecutor;
    private final ArtemisTemplateService artemisTemplateService;

    /// 自注入: 保证建单走 Spring 事务代理
    @Lazy
    private final AllocStartService self;

    /// 发起分账入口(持发起锁)
    public String start(AllocParam param) {
        // 商户身份装载
        merchantContextLoader.initMch(param.getMchNo());
        // 锁租期 60s 覆盖通道 HTTP 超时, 等待 3s 让并发同号请求排队
        return lockExecutor.execute(
                "payment:alloc:" + param.getBizAllocNo(),
                60000, 3000,
                () -> this.startHandle(param),
                () -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.alloc.allocProcessing")
        );
    }

    /// 锁内编排(无事务): 幂等查重 → 订单维度锁 → 锁内建单+发起
    public String startHandle(AllocParam param) {
        String mchNo = paymentContext.getMchNo();
        // 幂等查重: bizAllocNo + mchNo
        Optional<AllocOrder> existOpt = assistService.findByBizAllocNo(param.getBizAllocNo(), mchNo);
        if (existOpt.isPresent()) {
            // 已存在, 直接返回(不支持重复分账, 单次分账语义)
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.alloc.noDuplicate");
        }
        // 解析原支付 tradeNo(只读, 提前到锁外用于构造订单维度锁 key)
        String tradeNo = this.resolveTradeNo(param);
        // 订单维度锁: 防同一订单被不同商户分账单号并发分账(与退款 payment:refund:trade:{tradeNo} 同模式)。
        // 先到者建单并把 pay_trade.alloc_status 标记为 processing(事务提交后才释放锁),
        // 后到者在锁内二次读看到 processing → 校验拒绝, 保证同一订单同时最多一笔分账。
        // 锁租期 60s 覆盖通道 HTTP 超时, 等待 3s 让并发同号请求排队。
        return lockExecutor.execute(
                "payment:alloc-trade:" + tradeNo,
                60000, 3000,
                () -> this.startWithTradeLock(param),
                () -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.alloc.allocProcessing")
        );
    }

    /// 订单锁内编排(无事务): 建单(独立事务, 锁内二次读校验)后发起
    public String startWithTradeLock(AllocParam param) {
        AllocStrategyContext context = self.createOrder(param);
        this.doAlloc(context);
        return context.getAllocOrder().getAllocNo();
    }

    /// 建单(事务内: 校验原支付 + 建分账单+明细)
    @Transactional(rollbackFor = Exception.class)
    public AllocStrategyContext createOrder(AllocParam param) {
        String mchNo = paymentContext.getMchNo();
        // 定位原支付资金凭证
        PayTrade trade = resolveTrade(param);
        // 校验可分账
        validateAllocatable(trade, param);
        // 装配通道凭证快照(按交易形态分发容器)
        AllocatableContainer container = this.resolveContainer(trade);
        if (container == null) {
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.alloc.orderNotFound");
        }
        // 通道策略校验
        AbsAllocStrategy strategy = AllocStrategyFactory.create(trade.getChannel());
        // 生成分账单号
        String allocNo = TradeNoGenerateUtil.alloc();
        // 构建主单
        AllocOrder allocOrder = buildAllocOrder(param, trade, container, allocNo, mchNo);
        // 构建明细
        List<AllocDetail> details = buildDetails(param, allocNo);
        // 计算总分账金额
        long totalAmount = details.stream().mapToLong(AllocDetail::getAmount).sum();
        allocOrder.setAmount(totalAmount);
        // 持久化
        assistService.createOrder(allocOrder, details);
        // 装配策略上下文
        AllocStrategyContext strategyContext = new AllocStrategyContext()
                .setAllocOrder(allocOrder)
                .setDetails(details)
                .setChannel(trade.getChannel())
                .setMchNo(mchNo)
                .setChannelMchNo(allocOrder.getChannelMchNo())
                .setChannelAppId(allocOrder.getChannelAppId())
                .setOutOrderNo(allocOrder.getOutOrderNo())
                .setNotifyUrl(allocOrder.getNotifyUrl());
        // 通道策略发起前校验(接收方类型/姓名等通道差异约束, 校验失败回滚建单事务)
        strategy.doValidateParam(strategyContext);
        return strategyContext;
    }

    /// 通道发起(事务外, 远程调用不占事务)
    private void doAlloc(AllocStrategyContext context) {
        AllocOrder allocOrder = context.getAllocOrder();
        String channel = context.getChannel();
        try {
            AbsAllocStrategy strategy = AllocStrategyFactory.create(channel);
            AllocResultBo result = strategy.doAlloc(context);
            // 回写通道分账号(如有)
            if (result.getOutAllocNo() != null) {
                assistService.processing(allocOrder, result.getOutAllocNo());
            }
            // 按逐明细结果聚合订单状态
            aggregateResult(allocOrder, result.getDetails());
        } catch (Exception e) {
            log.error("分账发起失败: allocNo={}, channel={}", allocOrder.getAllocNo(), channel, e);
            if (e instanceof BizException biz) {
                // 通道明确拒绝(参数/接收方等业务异常): 置 fail, 回退原支付 alloc_status=none 允许重新发起
                assistService.fail(allocOrder, null, resolveErrorMsg(e));
                throw biz;
            }
            // 结果未知(网络/超时等非业务异常): 通道可能已受理, 保留 processing 由延迟同步/定时任务纠正。
            // 不置 fail 以免解锁原支付(alloc_status=none)导致商户换号重发引发重复分账。
            this.registerDelaySync(allocOrder.getAllocNo());
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "pay.error.alloc.syncProcessing");
        }
    }

    /// 按逐明细结果聚合订单状态
    ///
    /// 三通道分账均为异步: 发起成功时明细一律返回 pending, 此时须保持 processing 并注册延迟同步,
    /// 由 [registerDelaySync] 与定时任务兜底推进, 切勿误判为 partial 终态(会导致明细永久 pending 且锁死原支付)。
    /// 参照 [cn.daxpay.open.payment.trade.alloc.runtime.service.AllocSyncService#aggregateResult] 的守卫写法。
    private void aggregateResult(AllocOrder allocOrder, List<AllocResultBo.DetailResult> detailResults) {
        if (detailResults == null || detailResults.isEmpty()) {
            // 无明细结果(通道异常前或未返回明细): 保持 processing, 注册延迟同步兜底
            this.registerDelaySync(allocOrder.getAllocNo());
            return;
        }
        long successCount = detailResults.stream()
                .filter(d -> Objects.equals(d.getResult(), AllocDetailResultEnum.SUCCESS.getCode()))
                .count();
        long failCount = detailResults.stream()
                .filter(d -> Objects.equals(d.getResult(), AllocDetailResultEnum.FAIL.getCode()))
                .count();
        long total = detailResults.size();

        if (successCount == total) {
            // 全部成功
            assistService.success(allocOrder, detailResults);
        } else if (failCount == total) {
            // 全部失败
            String errorMsg = detailResults.stream()
                    .map(AllocResultBo.DetailResult::getErrorMsg)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
            assistService.fail(allocOrder, detailResults, errorMsg);
        } else if (successCount + failCount == total) {
            // 全部明细已有终态结果(部分成功): 终态, 不可再追加
            assistService.partial(allocOrder, detailResults);
        } else {
            // 仍有 pending 明细(异步分账尚未完成): 保持 processing, 注册延迟同步兜底推进
            this.registerDelaySync(allocOrder.getAllocNo());
        }
    }

    /// 按 tradeType 解析分账源容器(normal / gateway)
    private AllocatableContainer resolveContainer(PayTrade trade) {
        if (Objects.equals(trade.getTradeType(), PayTradeTypeEnum.GATEWAY.getCode())) {
            return gatewayPayOrderManager.findById(trade.getContainerId()).orElse(null);
        }
        return normalPayOrderManager.findById(trade.getContainerId()).orElse(null);
    }

    /// 定位原支付资金凭证(锁内二次读, 保证读到最新 alloc_status 状态)
    private PayTrade resolveTrade(AllocParam param) {
        String tradeNo = this.resolveTradeNo(param);
        return payTradeManager.findByTradeNo(tradeNo)
                .orElseThrow(() -> new BizInfoException(CommonCode.FAIL_CODE, "pay.error.alloc.orderNotFound"));
    }

    /// 解析原支付资金交易号(只读, 供构造订单维度锁 key)
    private String resolveTradeNo(AllocParam param) {
        if (param.getTradeNo() != null && !param.getTradeNo().isBlank()) {
            return param.getTradeNo();
        }
        if (param.getBizOrderNo() != null && !param.getBizOrderNo().isBlank()) {
            String bizOrderNo = param.getBizOrderNo();
            String appId = param.getAppId();
            // bizOrderNo → 容器 → trade: 先查普通支付容器, 再查网关容器
            Optional<NormalPayOrder> normalOpt = normalPayOrderManager.findByBizOrderNo(bizOrderNo, appId);
            if (normalOpt.isPresent()) {
                return payTradeManager.findByContainerId(normalOpt.get().getId(), PayTradeTypeEnum.NORMAL.getCode())
                        .map(PayTrade::getTradeNo)
                        .orElseThrow(() -> new BizInfoException(CommonCode.FAIL_CODE, "pay.error.alloc.orderNotFound"));
            }
            return gatewayPayOrderManager.findByBizOrderNo(bizOrderNo, appId)
                    .flatMap(gateway -> payTradeManager.findByContainerId(gateway.getId(), PayTradeTypeEnum.GATEWAY.getCode()))
                    .map(PayTrade::getTradeNo)
                    .orElseThrow(() -> new BizInfoException(CommonCode.FAIL_CODE, "pay.error.alloc.orderNotFound"));
        }
        throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.alloc.tradeNoRequired");
    }

    /// 校验原支付可分账
    private void validateAllocatable(PayTrade trade, AllocParam param) {
        // 原支付须成功
        if (!Objects.equals(trade.getStatus(), "success")) {
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.alloc.orderNotSuccess");
        }
        // 原支付未分账: 处理中或已分账都拒绝
        // (processing 拦截: 配合订单维度锁, 保证同一订单同时最多一笔分账; done 拦截: 已分账终态)
        if (Objects.equals(trade.getAllocStatus(), "processing")
                || Objects.equals(trade.getAllocStatus(), "done")) {
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.alloc.alreadyAllocated");
        }
        // 原支付容器须声明为分账订单
        AllocatableContainer container = this.resolveContainer(trade);
        if (container == null || !Boolean.TRUE.equals(container.getAllocation())) {
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.alloc.notAllocOrder");
        }
        // 接收方账号去重(同账号不可重复, 否则明细重复且通道侧可能重复打款)
        long distinctAccounts = param.getReceivers().stream()
                .map(AllocParam.AllocReceiverParam::getReceiverAccount)
                .distinct().count();
        if (distinctAccounts < param.getReceivers().size()) {
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.alloc.duplicateReceiver");
        }
        // 分账总金额不可超过原支付金额(简化校验, 未扣减退款金额, 后续可增强为可分账余额)
        long totalAmount = param.getReceivers().stream()
                .mapToLong(r -> CurrencyAmountUtil.majorToMinor(r.getAmount(), CurrencyEnum.CNY))
                .sum();
        if (trade.getAmount() == null || totalAmount > trade.getAmount()) {
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.alloc.amountExceed");
        }
    }

    /// 构建分账主单(继承原支付通道快照)
    private AllocOrder buildAllocOrder(AllocParam param, PayTrade trade, AllocatableContainer container,
                                       String allocNo, String mchNo) {
        AllocOrder order = new AllocOrder()
                .setAllocNo(allocNo)
                .setBizAllocNo(param.getBizAllocNo())
                .setTradeNo(trade.getTradeNo())
                .setTradeType(trade.getTradeType())
                .setBizOrderNo(container.getBizOrderNo())
                .setOutOrderNo(trade.getOutOrderNo())
                .setTitle(param.getTitle() != null ? param.getTitle() : container.getTitle())
                .setDescription(param.getDescription())
                .setOrderAmount(trade.getAmount())
                .setCurrency(CurrencyEnum.CNY.getCode())
                .setStatus(cn.daxpay.open.payment.trade.alloc.enums.AllocOrderStatusEnum.PROCESSING.getCode())
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
        order.setMchNo(mchNo);
        return order;
    }

    /// 构建分账明细列表
    private List<AllocDetail> buildDetails(AllocParam param, String allocNo) {
        List<AllocDetail> details = new ArrayList<>();
        for (AllocParam.AllocReceiverParam rp : param.getReceivers()) {
            AllocDetail detail = new AllocDetail()
                    .setAllocNo(allocNo)
                    .setReceiverType(rp.getReceiverType())
                    .setReceiverAccount(rp.getReceiverAccount())
                    .setReceiverName(rp.getReceiverName())
                    .setAmount(CurrencyAmountUtil.majorToMinor(rp.getAmount(), CurrencyEnum.CNY))
                    .setResult(AllocDetailResultEnum.PENDING.getCode());
            details.add(detail);
        }
        return details;
    }

    /// 注册延迟同步(发起返回处理中后, 2 分钟投递 MQ)
    ///
    /// 参照 [cn.daxpay.open.payment.trade.transfer.runtime.service.TransferStartService#registerDelaySync]:
    /// 异步分账发起成功且仍有 pending 明细时调用, 失败仅告警由定时任务兜底。
    private void registerDelaySync(String allocNo) {
        try {
            AllocSyncMessage message = new AllocSyncMessage().setAllocNo(allocNo);
            artemisTemplateService.sendDelay(PayArtemisConstants.ALLOC_SYNC_QUEUE,
                    JacksonUtil.toJson(message), 120);
        } catch (Exception e) {
            log.warn("注册分账延迟同步失败, 由定时任务兜底, allocNo={}", allocNo, e);
        }
    }

    /// 异常本地化解析(key → 固定中文)
    private String resolveErrorMsg(Throwable e) {
        if (e instanceof BizException biz) {
            String key = biz.resolveMessageKey();
            if (key != null) {
                return I18nUtil.get(key, Locale.CHINA, biz.getArgs());
            }
        }
        return e.getMessage();
    }
}
