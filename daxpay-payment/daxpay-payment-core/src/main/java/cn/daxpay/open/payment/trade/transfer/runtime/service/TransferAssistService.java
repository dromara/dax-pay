package cn.daxpay.open.payment.trade.transfer.runtime.service;

import cn.daxpay.open.payment.strategy.transfer.TransferStrategyContext;
import cn.daxpay.open.payment.trade.notice.service.TradeNoticeBridge;
import cn.daxpay.open.payment.trade.transfer.dao.AlipayTransferOrderManager;
import cn.daxpay.open.payment.trade.transfer.dao.DouyinTransferOrderManager;
import cn.daxpay.open.payment.trade.transfer.dao.TransferTradeManager;
import cn.daxpay.open.payment.trade.transfer.dao.WechatTransferOrderManager;
import cn.daxpay.open.payment.trade.transfer.entity.AlipayTransferOrder;
import cn.daxpay.open.payment.trade.transfer.entity.DouyinTransferOrder;
import cn.daxpay.open.payment.trade.transfer.entity.TransferTrade;
import cn.daxpay.open.payment.trade.transfer.entity.WechatTransferOrder;
import cn.daxpay.open.payment.trade.transfer.param.TransferParam;
import cn.daxpay.open.payment.trade.util.CurrencyAmountUtil;
import cn.daxpay.open.platform.core.enums.pay.channel.CurrencyEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeEventEnum;
import cn.daxpay.open.platform.core.util.TradeNoGenerateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/// # 转账编排辅助服务
///
/// 公共流程的原子动作（建单双写 / 终态双写 / 通知注册）单一事实源，
/// 供 [TransferStartService]/[TransferSyncService]/[TransferCallbackService]/[TransferCloseService] 复用。
/// 同时是唯一按通道分发、直接操作通道容器表（[WechatTransferOrder]/[AlipayTransferOrder]/[DouyinTransferOrder]）
/// 的公共入口，其余编排服务只面向公共凭证 [TransferTrade] 与 [TransferStrategyContext]。
/// 所有方法要求调用方已持有对应转账单的分布式锁。
@Slf4j
@Service
@RequiredArgsConstructor
public class TransferAssistService {

    /// 转账状态变更互斥锁前缀(与凭证主键组合)
    public static final String TRADE_LOCK_PREFIX = "payment:transfer-trade:";

    /// 容器镜像更新结果
    ///
    /// @param updated   CAS 是否更新成功(失败=并发竞争, 调用方按幂等处理)
    /// @param notifyUrl 容器上的商户异步通知地址(供通知注册)
    private record MirrorResult(boolean updated, String notifyUrl) {}

    private final WechatTransferOrderManager wechatTransferOrderManager;
    private final AlipayTransferOrderManager alipayTransferOrderManager;
    private final DouyinTransferOrderManager douyinTransferOrderManager;
    private final TransferTradeManager transferTradeManager;
    private final TradeNoticeBridge tradeNoticeBridge;

    /// 按通道+转账单主键查询凭证
    public Optional<TransferTrade> findTradeByContainer(String channel, Long containerId) {
        return transferTradeManager.findByContainerId(containerId, channel);
    }

    /// 幂等查重: 按商户转账号+应用号查容器主键（同一通道内唯一）
    ///
    /// 容器存在即视为该商户转账号已发起过; 凭证缺失属数据异常, 由调用方按失败重试兜底。
    public Optional<Long> findExist(String channel, String bizTransferNo, String appId) {
        return switch (channel) {
            case "wechat" -> wechatTransferOrderManager
                    .findByBizTransferNo(bizTransferNo, appId).map(WechatTransferOrder::getId);
            case "alipay" -> alipayTransferOrderManager
                    .findByBizTransferNo(bizTransferNo, appId).map(AlipayTransferOrder::getId);
            case "douyin" -> douyinTransferOrderManager
                    .findByBizTransferNo(bizTransferNo, appId).map(DouyinTransferOrder::getId);
            default -> throw new IllegalArgumentException("未知转账通道: " + channel);
        };
    }

    /// 按通道装载容器并装配策略上下文（容器或凭证缺失返回 empty）
    public Optional<TransferStrategyContext> loadContext(String channel, Long containerId) {
        TransferTrade trade = transferTradeManager.findByContainerId(containerId, channel).orElse(null);
        if (trade == null) {
            return Optional.empty();
        }
        TransferStrategyContext context = switch (channel) {
            case "wechat" -> wechatTransferOrderManager.findById(containerId)
                    .map(this::buildWechatContext).orElse(null);
            case "alipay" -> alipayTransferOrderManager.findById(containerId)
                    .map(this::buildAlipayContext).orElse(null);
            case "douyin" -> douyinTransferOrderManager.findById(containerId)
                    .map(this::buildDouyinContext).orElse(null);
            default -> throw new IllegalArgumentException("未知转账通道: " + channel);
        };
        if (context == null) {
            return Optional.empty();
        }
        context.setChannel(channel).setTrade(trade);
        return Optional.of(context);
    }

    /// 凭证主键 → 锁键
    public static String tradeLockKey(Long tradeId) {
        return TRADE_LOCK_PREFIX + tradeId;
    }

    // ===== 建单(事务内调用, 容器+凭证双写) =====

    /// 按通道建容器并双写公共资金凭证, 返回装配好的策略上下文
    ///
    /// @param channel 通道编码
    /// @param param   转账参数(公共字段 + 通道特有收款人字段)
    /// @param appId   解析后的应用号
    /// @param mchNo   商户号(上下文已装载, 显式传入避免依赖线程上下文)
    @Transactional(rollbackFor = Exception.class)
    public TransferStrategyContext createOrder(String channel, TransferParam param, String appId, String mchNo) {
        String transferNo = TradeNoGenerateUtil.transfer();
        long amount = CurrencyAmountUtil.majorToMinor(param.getAmount(), CurrencyEnum.CNY);
        switch (channel) {
            case "wechat" -> {
                WechatTransferOrder order = new WechatTransferOrder()
                        .setPayeeOpenid(param.getPayeeAccount())
                        .setUserName(param.getPayeeName())
                        .setTransferNo(transferNo)
                        .setBizTransferNo(param.getBizTransferNo())
                        .setChannelMchNo(param.getChannelMchNo())
                        .setAmount(amount)
                        .setCurrency(CurrencyEnum.CNY.getCode())
                        .setTitle(param.getTitle())
                        .setReason(param.getReason())
                        .setNotifyUrl(param.getNotifyUrl())
                        .setAttach(param.getAttach())
                        .setAppId(appId)
                        .setStatus("processing")
                        .setReqTime(OffsetDateTime.now());
                // 商户号独立赋值(父类 setter 返回 MchBaseEntity, 禁止链式)
                order.setMchNo(mchNo);
                wechatTransferOrderManager.save(order);
                TransferTrade trade = this.buildTrade(order, channel, transferNo);
                transferTradeManager.save(trade);
                return buildWechatContext(order).setChannel(channel).setTrade(trade);
            }
            case "alipay" -> {
                AlipayTransferOrder order = new AlipayTransferOrder()
                        .setPayeeType(param.getPayeeType())
                        .setPayeeAccount(param.getPayeeAccount())
                        .setPayeeName(param.getPayeeName())
                        .setTransferNo(transferNo)
                        .setBizTransferNo(param.getBizTransferNo())
                        .setChannelMchNo(param.getChannelMchNo())
                        .setAmount(amount)
                        .setCurrency(CurrencyEnum.CNY.getCode())
                        .setTitle(param.getTitle())
                        .setReason(param.getReason())
                        .setNotifyUrl(param.getNotifyUrl())
                        .setAttach(param.getAttach())
                        .setAppId(appId)
                        .setStatus("processing")
                        .setReqTime(OffsetDateTime.now());
                // 商户号独立赋值(父类 setter 返回 MchBaseEntity, 禁止链式)
                order.setMchNo(mchNo);
                alipayTransferOrderManager.save(order);
                TransferTrade trade = this.buildTrade(order, channel, transferNo);
                transferTradeManager.save(trade);
                return buildAlipayContext(order).setChannel(channel).setTrade(trade);
            }
            case "douyin" -> {
                DouyinTransferOrder order = new DouyinTransferOrder()
                        .setPayeeType(param.getPayeeType())
                        .setPayeeAccount(param.getPayeeAccount())
                        .setPayeeName(param.getPayeeName())
                        .setTransferNo(transferNo)
                        .setBizTransferNo(param.getBizTransferNo())
                        .setChannelMchNo(param.getChannelMchNo())
                        .setAmount(amount)
                        .setCurrency(CurrencyEnum.CNY.getCode())
                        .setTitle(param.getTitle())
                        .setReason(param.getReason())
                        .setNotifyUrl(param.getNotifyUrl())
                        .setAttach(param.getAttach())
                        .setAppId(appId)
                        .setStatus("processing")
                        .setReqTime(OffsetDateTime.now());
                // 商户号独立赋值(父类 setter 返回 MchBaseEntity, 禁止链式)
                order.setMchNo(mchNo);
                douyinTransferOrderManager.save(order);
                TransferTrade trade = this.buildTrade(order, channel, transferNo);
                transferTradeManager.save(trade);
                return buildDouyinContext(order).setChannel(channel).setTrade(trade);
            }
            default -> throw new IllegalArgumentException("未知转账通道: " + channel);
        }
    }

    /// 组装公共资金凭证（relationNo 默认=平台转账单号, 特殊通道变形后覆盖）
    private TransferTrade buildTrade(WechatTransferOrder order, String channel, String transferNo) {
        TransferTrade trade = new TransferTrade()
                .setTradeNo(transferNo)
                .setBizTransferNo(order.getBizTransferNo())
                .setContainerId(order.getId())
                .setContainerChannel(channel)
                .setChannel(channel)
                .setProvider(channel)
                .setAmount(order.getAmount())
                .setCurrency(order.getCurrency())
                .setStatus("processing")
                .setRelationNo(transferNo)
                .setTitle(order.getTitle())
                .setAppId(order.getAppId());
        // 商户号独立赋值(父类 setter 返回 MchBaseEntity, 禁止链式)
        trade.setMchNo(order.getMchNo());
        return trade;
    }

    /// 组装公共资金凭证（relationNo 默认=平台转账单号, 特殊通道变形后覆盖）
    private TransferTrade buildTrade(AlipayTransferOrder order, String channel, String transferNo) {
        TransferTrade trade = new TransferTrade()
                .setTradeNo(transferNo)
                .setBizTransferNo(order.getBizTransferNo())
                .setContainerId(order.getId())
                .setContainerChannel(channel)
                .setChannel(channel)
                .setProvider(channel)
                .setAmount(order.getAmount())
                .setCurrency(order.getCurrency())
                .setStatus("processing")
                .setRelationNo(transferNo)
                .setTitle(order.getTitle())
                .setAppId(order.getAppId());
        // 商户号独立赋值(父类 setter 返回 MchBaseEntity, 禁止链式)
        trade.setMchNo(order.getMchNo());
        return trade;
    }

    /// 组装公共资金凭证（relationNo 默认=平台转账单号, 特殊通道变形后覆盖）
    private TransferTrade buildTrade(DouyinTransferOrder order, String channel, String transferNo) {
        TransferTrade trade = new TransferTrade()
                .setTradeNo(transferNo)
                .setBizTransferNo(order.getBizTransferNo())
                .setContainerId(order.getId())
                .setContainerChannel(channel)
                .setChannel(channel)
                .setProvider(channel)
                .setAmount(order.getAmount())
                .setCurrency(order.getCurrency())
                .setStatus("processing")
                .setRelationNo(transferNo)
                .setTitle(order.getTitle())
                .setAppId(order.getAppId());
        // 商户号独立赋值(父类 setter 返回 MchBaseEntity, 禁止链式)
        trade.setMchNo(order.getMchNo());
        return trade;
    }

    // ===== 终态/中间态双写(事务内调用) =====

    /// 转账成功处理: 容器+凭证双 CAS 转 SUCCESS, 成功后注册商户通知
    ///
    /// 允许 PROCESSING/FAIL 来源(FAIL→SUCCESS 为同步纠正路径)。
    ///
    /// @param transferBody 微信拉起确认参数(有值才回写容器, 终态沿用既有值)
    /// @return true=本次流转成功; false=CAS 竞争失败/终态幂等, 调用方应幂等退出
    @Transactional(rollbackFor = Exception.class)
    public boolean success(String channel, TransferTrade trade, String outTransferNo,
                           OffsetDateTime finishTime, String relationNo, String transferBody) {
        if (!Objects.equals(trade.getStatus(), "processing")
                && !Objects.equals(trade.getStatus(), "fail")) {
            log.warn("转账成功忽略: tradeNo={} 状态为 {} 非 processing/fail", trade.getTradeNo(), trade.getStatus());
            return false;
        }
        // 凭证 CAS
        trade.setStatus("success");
        trade.setFinishTime(finishTime != null ? finishTime : OffsetDateTime.now());
        trade.setOutTransferNo(outTransferNo);
        trade.setRelationNo(relationNo);
        boolean tradeUpdated = transferTradeManager.casUpdateStatus(trade, Set.of("processing", "fail"));
        // 容器镜像 CAS
        MirrorResult mirror = mirrorContainer(channel, trade, Set.of("processing", "fail"), null, transferBody, null);
        if (!tradeUpdated && !mirror.updated()) {
            log.warn("转账成功CAS竞争失败: tradeNo={}", trade.getTradeNo());
            return false;
        }
        // 注册商户通知(同事务落库)
        tradeNoticeBridge.dispatchTransfer(trade, mirror.notifyUrl(), NoticeEventEnum.TRANSFER_SUCCESS);
        return true;
    }

    /// 转账失败处理: 容器+凭证双 CAS 转 FAIL, 成功后注册商户通知
    ///
    /// @return true=本次流转成功; false=CAS 竞争失败/终态幂等, 调用方应幂等退出
    @Transactional(rollbackFor = Exception.class)
    public boolean fail(String channel, TransferTrade trade, String errorMsg) {
        if (!Objects.equals(trade.getStatus(), "processing")) {
            log.warn("转账失败忽略: tradeNo={} 状态为 {} 非 processing", trade.getTradeNo(), trade.getStatus());
            return false;
        }
        trade.setStatus("fail");
        boolean tradeUpdated = transferTradeManager.casUpdateStatus(trade, Set.of("processing"));
        MirrorResult mirror = mirrorContainer(channel, trade, Set.of("processing"), errorMsg, null, null);
        if (!tradeUpdated && !mirror.updated()) {
            log.warn("转账失败CAS竞争失败: tradeNo={}", trade.getTradeNo());
            return false;
        }
        // 注册商户通知(同事务落库)
        tradeNoticeBridge.dispatchTransfer(trade, mirror.notifyUrl(), NoticeEventEnum.TRANSFER_FAIL);
        return true;
    }

    /// 转账关闭处理: 容器+凭证双 CAS 转 CLOSE, 成功后注册商户通知
    ///
    /// @return true=本次流转成功; false=CAS 竞争失败/终态幂等, 调用方应幂等退出
    @Transactional(rollbackFor = Exception.class)
    public boolean close(String channel, TransferTrade trade, String errorMsg) {
        if (!Objects.equals(trade.getStatus(), "processing")) {
            log.warn("转账关闭忽略: tradeNo={} 状态为 {} 非 processing", trade.getTradeNo(), trade.getStatus());
            return false;
        }
        trade.setStatus("close");
        boolean tradeUpdated = transferTradeManager.casUpdateStatus(trade, Set.of("processing"));
        MirrorResult mirror = mirrorContainer(channel, trade, Set.of("processing"), errorMsg, null, null);
        if (!tradeUpdated && !mirror.updated()) {
            log.warn("转账关闭CAS竞争失败: tradeNo={}", trade.getTradeNo());
            return false;
        }
        // 注册商户通知(同事务落库)
        tradeNoticeBridge.dispatchTransfer(trade, mirror.notifyUrl(), NoticeEventEnum.TRANSFER_CLOSE);
        return true;
    }

    /// 转账处理中回写(非终态): 补通道转账单号/特有字段(微信拉起确认参数/抖音转账场景), 不改变状态
    @Transactional(rollbackFor = Exception.class)
    public void processing(String channel, TransferTrade trade, String outTransferNo,
                           String transferBody, String transferScene) {
        trade.setOutTransferNo(outTransferNo);
        transferTradeManager.updateById(trade);
        // 容器回写: 通道单号 + 特有字段
        mirrorProcessing(channel, trade.getContainerId(), outTransferNo, transferBody, transferScene);
    }

    /// 重试重置: 容器+凭证双表回 PROCESSING, 清空通道单号/完成时间/错误信息
    ///
    /// 仅允许 FAIL 状态复用原单重试, CAS 保证并发下仅一方成功。
    @Transactional(rollbackFor = Exception.class)
    public void resetForRetry(String channel, TransferTrade trade) {
        trade.setStatus("processing");
        trade.setOutTransferNo(null);
        trade.setFinishTime(null);
        transferTradeManager.casUpdateStatus(trade, Set.of("fail"));
        mirrorContainer(channel, trade, Set.of("fail"), null, null, null);
    }

    // ===== 容器镜像(按通道分发, 编排服务唯一接触具体容器的地方) =====

    /// 按通道装载容器并镜像状态更新(CAS), 返回更新结果与通知地址
    ///
    /// 容器状态/完成时间/通道单号取自凭证 [trade]; errorMsg 单独传入(success 传 null 清空);
    /// [transferBody]/[transferScene] 有值才覆盖, 终态沿用容器既有值。
    private MirrorResult mirrorContainer(String channel, TransferTrade trade, Set<String> expectFrom,
                                         String errorMsg, String transferBody, String transferScene) {
        return switch (channel) {
            case "wechat" -> {
                WechatTransferOrder order = wechatTransferOrderManager.findById(trade.getContainerId()).orElse(null);
                if (order == null) {
                    log.warn("转账容器不存在, 跳过容器更新: tradeNo={}", trade.getTradeNo());
                    yield new MirrorResult(false, null);
                }
                order.setStatus(trade.getStatus());
                order.setFinishTime(trade.getFinishTime());
                order.setOutTransferNo(trade.getOutTransferNo());
                order.setErrorMsg(errorMsg);
                if (transferBody != null) {
                    order.setTransferBody(transferBody);
                }
                boolean updated = wechatTransferOrderManager.casUpdateStatus(order, expectFrom);
                yield new MirrorResult(updated, order.getNotifyUrl());
            }
            case "alipay" -> {
                AlipayTransferOrder order = alipayTransferOrderManager.findById(trade.getContainerId()).orElse(null);
                if (order == null) {
                    log.warn("转账容器不存在, 跳过容器更新: tradeNo={}", trade.getTradeNo());
                    yield new MirrorResult(false, null);
                }
                order.setStatus(trade.getStatus());
                order.setFinishTime(trade.getFinishTime());
                order.setOutTransferNo(trade.getOutTransferNo());
                order.setErrorMsg(errorMsg);
                boolean updated = alipayTransferOrderManager.casUpdateStatus(order, expectFrom);
                yield new MirrorResult(updated, order.getNotifyUrl());
            }
            case "douyin" -> {
                DouyinTransferOrder order = douyinTransferOrderManager.findById(trade.getContainerId()).orElse(null);
                if (order == null) {
                    log.warn("转账容器不存在, 跳过容器更新: tradeNo={}", trade.getTradeNo());
                    yield new MirrorResult(false, null);
                }
                order.setStatus(trade.getStatus());
                order.setFinishTime(trade.getFinishTime());
                order.setOutTransferNo(trade.getOutTransferNo());
                order.setErrorMsg(errorMsg);
                if (transferScene != null) {
                    order.setTransferScene(transferScene);
                }
                boolean updated = douyinTransferOrderManager.casUpdateStatus(order, expectFrom);
                yield new MirrorResult(updated, order.getNotifyUrl());
            }
            default -> throw new IllegalArgumentException("未知转账通道: " + channel);
        };
    }

    /// 处理中回写: 按通道装载容器补通道单号/特有字段(非 CAS, 不改变状态)
    private void mirrorProcessing(String channel, Long containerId, String outTransferNo,
                                  String transferBody, String transferScene) {
        switch (channel) {
            case "wechat" -> wechatTransferOrderManager.findById(containerId).ifPresent(order -> {
                order.setOutTransferNo(outTransferNo);
                if (transferBody != null) {
                    order.setTransferBody(transferBody);
                }
                wechatTransferOrderManager.updateById(order);
            });
            case "alipay" -> alipayTransferOrderManager.findById(containerId).ifPresent(order -> {
                order.setOutTransferNo(outTransferNo);
                alipayTransferOrderManager.updateById(order);
            });
            case "douyin" -> douyinTransferOrderManager.findById(containerId).ifPresent(order -> {
                order.setOutTransferNo(outTransferNo);
                if (transferScene != null) {
                    order.setTransferScene(transferScene);
                }
                douyinTransferOrderManager.updateById(order);
            });
            default -> throw new IllegalArgumentException("未知转账通道: " + channel);
        }
    }

    // ===== 策略上下文装配 =====

    private TransferStrategyContext buildWechatContext(WechatTransferOrder order) {
        return new TransferStrategyContext()
                .setMchNo(order.getMchNo())
                .setChannelMchNo(order.getChannelMchNo())
                .setTransferNo(order.getTransferNo())
                .setBizTransferNo(order.getBizTransferNo())
                .setOutTransferNo(order.getOutTransferNo())
                .setAmount(order.getAmount())
                .setCurrency(order.getCurrency())
                .setTitle(order.getTitle())
                .setReason(order.getReason())
                .setNotifyUrl(order.getNotifyUrl())
                .setStatus(order.getStatus())
                .setFinishTime(order.getFinishTime())
                .setPayeeOpenid(order.getPayeeOpenid())
                .setTransferScene(order.getTransferScene())
                .setUserName(order.getUserName());
    }

    private TransferStrategyContext buildAlipayContext(AlipayTransferOrder order) {
        return new TransferStrategyContext()
                .setMchNo(order.getMchNo())
                .setChannelMchNo(order.getChannelMchNo())
                .setTransferNo(order.getTransferNo())
                .setBizTransferNo(order.getBizTransferNo())
                .setOutTransferNo(order.getOutTransferNo())
                .setAmount(order.getAmount())
                .setCurrency(order.getCurrency())
                .setTitle(order.getTitle())
                .setReason(order.getReason())
                .setNotifyUrl(order.getNotifyUrl())
                .setStatus(order.getStatus())
                .setFinishTime(order.getFinishTime())
                .setPayeeType(order.getPayeeType())
                .setPayeeAccount(order.getPayeeAccount())
                .setPayeeName(order.getPayeeName());
    }

    private TransferStrategyContext buildDouyinContext(DouyinTransferOrder order) {
        return new TransferStrategyContext()
                .setMchNo(order.getMchNo())
                .setChannelMchNo(order.getChannelMchNo())
                .setTransferNo(order.getTransferNo())
                .setBizTransferNo(order.getBizTransferNo())
                .setOutTransferNo(order.getOutTransferNo())
                .setAmount(order.getAmount())
                .setCurrency(order.getCurrency())
                .setTitle(order.getTitle())
                .setReason(order.getReason())
                .setNotifyUrl(order.getNotifyUrl())
                .setStatus(order.getStatus())
                .setFinishTime(order.getFinishTime())
                .setPayeeType(order.getPayeeType())
                .setPayeeAccount(order.getPayeeAccount())
                .setPayeeName(order.getPayeeName())
                .setTransferScene(order.getTransferScene());
    }
}
