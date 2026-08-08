package cn.daxpay.open.payment.trade.transfer.runtime.service;

import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.strategy.transfer.AbsTransferStrategy;
import cn.daxpay.open.payment.strategy.transfer.TransferStrategyContext;
import cn.daxpay.open.payment.strategy.transfer.TransferStrategyFactory;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.runtime.mq.PayArtemisConstants;
import cn.daxpay.open.payment.trade.transfer.bo.TransferResultBo;
import cn.daxpay.open.payment.trade.transfer.entity.TransferTrade;
import cn.daxpay.open.payment.trade.transfer.param.TransferParam;
import cn.daxpay.open.payment.trade.transfer.runtime.mq.TransferSyncMessage;
import cn.daxpay.open.platform.common.artemis.service.ArtemisTemplateService;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.common.redis.lock.LockExecutor;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/// # 转账发起编排服务
///
/// 锁内编排：商户身份装载 → 幂等查重（仅 FAIL 可复用原单重试）
/// → 通道参数校验 → 建单（容器+凭证双写, 独立事务）→ 通道发起（事务外, 远程调用）
/// → 结果处理（成功/处理中/失败, 独立事务 CAS 双写 + 通知）。
/// 容器读写全部收敛在 [TransferAssistService]，本服务只面向凭证与策略上下文。
///
/// 锁键: `payment:transfer:{bizTransferNo}` 防同号并发重入。
@Slf4j
@Service
@RequiredArgsConstructor
public class TransferStartService {

    private final TransferAssistService assistService;
    private final MerchantContextLoader merchantContextLoader;
    private final PaymentContext paymentContext;
    private final LockExecutor lockExecutor;
    private final ArtemisTemplateService artemisTemplateService;

    /// 自注入: 保证建单/重试重置走 Spring 事务代理
    @Lazy
    private final TransferStartService self;

    /// 发起转账入口（按通道独立调用）
    ///
    /// @param channel 通道编码(wechat/alipay/douyin)
    /// @param param   转账参数
    /// @return 平台转账单号(transferNo)
    public String start(String channel, TransferParam param) {
        // 锁租期 60s 覆盖通道 HTTP 超时(40s), 等待 3s 让并发同号请求排队而非立即失败
        return lockExecutor.execute(
                "payment:transfer:" + param.getBizTransferNo(),
                60000, 3000,
                () -> this.startHandle(channel, param),
                () -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.transfer.processing")
        );
    }

    /// 发起转账编排（锁内, 无事务）
    ///
    /// @return 平台转账单号(transferNo)
    public String startHandle(String channel, TransferParam param) {
        // 商户身份装载: 商户端强制当前登录商户, 运营端按传入 mchNo 代发
        merchantContextLoader.initMch(param.getMchNo());
        // 幂等查重: bizTransferNo + mchNo(同一商户下唯一)
        Optional<Long> existOpt = assistService.findExist(channel, param.getBizTransferNo(), paymentContext.getMchNo());
        if (existOpt.isPresent()) {
            Long containerId = existOpt.get();
            TransferTrade existTrade = assistService.findTradeByContainer(channel, containerId)
                    .orElseThrow(() -> new BizInfoException(CommonCode.FAIL_CODE, "pay.error.transfer.notFound"));
            // 仅 FAIL 允许复用原单重试
            if (Objects.equals(existTrade.getStatus(), PayFundStatusEnum.FAIL.getCode())) {
                // 重置原单为处理中后重新发起
                self.resetOrder(channel, existTrade);
                TransferStrategyContext context = assistService.loadContext(channel, containerId)
                        .orElseThrow(() -> new BizInfoException(CommonCode.FAIL_CODE, "pay.error.transfer.notFound"));
                this.transfer(channel, context);
                return context.getTransferNo();
            }
            // 该商户转账号已存在，请勿重复转账
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.transfer.noDuplicate");
        }
        // 新单: 建单(独立事务)后发起
        TransferStrategyContext context = self.createOrder(channel, param);
        this.transfer(channel, context);
        return context.getTransferNo();
    }

    /// 建单（事务内: 通道参数校验 + 容器/凭证双写）
    @Transactional(rollbackFor = Exception.class)
    public TransferStrategyContext createOrder(String channel, TransferParam param) {
        // 通道特有参数校验(微信金额档位/收款人类型等)
        AbsTransferStrategy strategy = TransferStrategyFactory.create(channel);
        strategy.doValidateParam(param);
        // 商户号取上下文(initMch 已装载), 显式传给辅助服务避免依赖线程上下文
        return assistService.createOrder(channel, param, paymentContext.getMchNo());
    }

    /// 重试重置（事务内: 原单回 PROCESSING, 清空通道单号/完成时间/错误信息）
    @Transactional(rollbackFor = Exception.class)
    public void resetOrder(String channel, TransferTrade trade) {
        assistService.resetForRetry(channel, trade);
    }

    /// 调通道发起并处理结果（事务外, 远程调用不占事务）
    private void transfer(String channel, TransferStrategyContext context) {
        TransferTrade trade = context.getTrade();
        try {
            AbsTransferStrategy strategy = TransferStrategyFactory.create(channel);
            TransferResultBo result = strategy.doTransfer(context);
            if (Objects.equals(result.getStatus(), PayFundStatusEnum.SUCCESS)) {
                // 通道同步返回成功
                assistService.success(channel, trade,
                        result.getOutTransferNo(), result.getFinishTime(), result.getRelationNo(), null);
            } else {
                // 处理中: 回写通道单号(含特有字段: 微信拉起确认参数/抖音转账场景)并注册延迟同步
                assistService.processing(channel, trade,
                        result.getOutTransferNo(), result.getTransferBody(),
                        context.getTransferScene(), context.getWxAppId());
                this.registerDelaySync(trade.getTradeNo());
            }
        } catch (Exception e) {
            // 发起异常: 置 FAIL 并记录错误, 然后向上抛出, 避免接口返回"假成功"误导调用方
            log.error("转账发起失败: tradeNo={}, channel={}", trade.getTradeNo(), channel, e);
            // BizException.getMessage() 返回 messageKey(未解析), 需按固定中文解析为本地化文案再落库,
            // 避免订单 errorMsg 存 key 字符串
            String errorMsg = resolveErrorMsg(e);
            assistService.fail(channel, trade, errorMsg);
            // BizException 直接透传, 保留通道原始 messageKey(由 RestExceptionHandler 按请求 locale 解析),
            // 避免外层"转账发起失败"与通道前缀叠加成双重前缀; 非 BizException(如网络异常)无 messageKey, 兜底包装
            if (e instanceof BizException biz) {
                throw biz;
            }
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "pay.error.transfer.createFailed", errorMsg);
        }
    }

    /// 解析异常为本地化错误消息
    ///
    /// [BizException] 的 getMessage() 返回 i18n messageKey(未经 I18nUtil 解析), 直接记录会导致订单 errorMsg
    /// 存 key 字符串(如 "error.channel.alipay.transferFailed")。本方法按固定中文(Locale.CHINA)解析,
    /// 与 [cn.daxpay.open.platform.system.handler.exception.RestExceptionHandler] 的日志行为一致,
    /// 保证落库文案不随请求语言变化。非 BizException 的 getMessage() 已是真实文案, 直接使用。
    private String resolveErrorMsg(Throwable e) {
        if (e instanceof BizException biz) {
            String key = biz.resolveMessageKey();
            if (key != null) {
                return I18nUtil.get(key, Locale.CHINA, biz.getArgs());
            }
        }
        return e.getMessage();
    }

    /// 注册 2 分钟延迟同步(发送失败由定时同步任务兜底)
    private void registerDelaySync(String tradeNo) {
        try {
            TransferSyncMessage message = new TransferSyncMessage().setTransferNo(tradeNo);
            artemisTemplateService.sendDelay(PayArtemisConstants.TRANSFER_SYNC_QUEUE,
                    JacksonUtil.toJson(message), 120);
        } catch (Exception e) {
            log.warn("注册转账延迟同步失败, 由定时任务兜底, tradeNo={}", tradeNo, e);
        }
    }
}
