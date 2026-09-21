package cn.daxpay.open.payment.trade.transfer.runtime.service;

import cn.daxpay.open.payment.strategy.transfer.TransferStrategyContext;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.notice.service.TradeNoticeBridge;
import cn.daxpay.open.payment.trade.transfer.dao.AlipayTransferOrderManager;
import cn.daxpay.open.payment.trade.transfer.dao.DouyinTransferOrderManager;
import cn.daxpay.open.payment.trade.transfer.dao.TransferTradeManager;
import cn.daxpay.open.payment.trade.transfer.dao.WechatTransferOrderManager;
import cn.daxpay.open.payment.trade.transfer.entity.AlipayTransferOrder;
import cn.daxpay.open.payment.trade.transfer.entity.DouyinTransferOrder;
import cn.daxpay.open.payment.trade.transfer.entity.TransferContainer;
import cn.daxpay.open.payment.trade.transfer.entity.TransferTrade;
import cn.daxpay.open.payment.trade.transfer.entity.WechatTransferOrder;
import cn.daxpay.open.payment.trade.transfer.param.TransferParam;
import cn.daxpay.open.payment.trade.transfer.param.TransferReportInfo;
import cn.daxpay.open.platform.core.enums.pay.channel.CurrencyEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeEventEnum;
import cn.daxpay.open.platform.core.util.TradeNoGenerateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.LongFunction;

/// # 转账编排辅助服务
///
/// 公共流程的原子动作（建单双写 / 终态双写 / 通知注册）单一事实源，
/// 供 [TransferStartService]/[TransferSyncService]/[TransferCallbackService]/[TransferCloseService] 复用。
/// 同时是唯一按通道分发、直接操作通道容器表（[WechatTransferOrder]/[AlipayTransferOrder]/[DouyinTransferOrder]）
/// 的公共入口，其余编排服务只面向公共凭证 [TransferTrade] 与 [TransferStrategyContext]。
/// 三容器经 [TransferContainer] 契约统一操作, 加载/保存/CAS 与特有字段回写差异由 [ChannelRoute] 路由承载。
/// 所有方法要求调用方已持有对应转账单的分布式锁。
@Slf4j
@Service
@RequiredArgsConstructor
public class TransferAssistService {

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

    /// 幂等查重: 按商户转账号+商户号查容器主键（同一商户下唯一）
    ///
    /// 容器存在即视为该商户转账号已发起过; 凭证缺失属数据异常, 由调用方按失败重试兜底。
    public Optional<Long> findExist(String channel, String bizTransferNo, String mchNo) {
        return switch (channel) {
            case "wechat" -> wechatTransferOrderManager
                    .findByBizTransferNo(bizTransferNo, mchNo).map(WechatTransferOrder::getId);
            case "alipay" -> alipayTransferOrderManager
                    .findByBizTransferNo(bizTransferNo, mchNo).map(AlipayTransferOrder::getId);
            case "douyin" -> douyinTransferOrderManager
                    .findByBizTransferNo(bizTransferNo, mchNo).map(DouyinTransferOrder::getId);
            default -> throw new IllegalArgumentException("未知转账通道: " + channel);
        };
    }

    /// 按通道装载容器并装配策略上下文（容器或凭证缺失返回 empty）
    public Optional<TransferStrategyContext> loadContext(String channel, Long containerId) {
        TransferTrade trade = transferTradeManager.findByContainerId(containerId, channel).orElse(null);
        if (Objects.isNull(trade)) {
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
        if (Objects.isNull(context)) {
            return Optional.empty();
        }
        context.setChannel(channel).setTrade(trade);
        return Optional.of(context);
    }

    // ===== 建单(事务内调用, 容器+凭证双写) =====

    /// 按通道建容器并双写公共资金凭证, 返回装配好的策略上下文
    ///
    /// 公共字段经 [applyCommonCreateFields] 统一写入, 各分支只补通道特有收款人/场景字段。
    ///
    /// @param channel 通道编码
    /// @param param   转账参数(公共字段 + 通道特有收款人字段)
    /// @param mchNo   商户号(上下文已装载, 显式传入避免依赖线程上下文)
    @Transactional(rollbackFor = Exception.class)
    public TransferStrategyContext createOrder(String channel, TransferParam param, String mchNo) {
        String transferNo = TradeNoGenerateUtil.transfer();
        // 金额入参已是分, 直接透传
        long amount = param.getAmount();
        return switch (channel) {
            case "wechat" -> {
                WechatTransferOrder order = new WechatTransferOrder()
                        .setPayeeOpenid(param.getPayeeAccount())
                        .setUserName(param.getPayeeName());
                applyCommonCreateFields(order, param, transferNo, amount);
                // 商户号独立赋值(父类 setter 返回 MchBaseEntity, 禁止链式)
                order.setMchNo(mchNo);
                // 持久化报备信息(FAIL重试时恢复, 与支付宝/抖音容器对齐)
                order.setReportInfos(serializeReportInfos(param.getReportInfos()));
                wechatTransferOrderManager.save(order);
                TransferTrade trade = this.buildTrade(order, channel, transferNo);
                transferTradeManager.save(trade);
                yield buildWechatContext(order).setChannel(channel).setTrade(trade)
                        .setReportInfos(param.getReportInfos());
            }
            case "alipay" -> {
                AlipayTransferOrder order = new AlipayTransferOrder()
                        .setPayeeType(param.getPayeeType())
                        .setPayeeAccount(param.getPayeeAccount())
                        .setPayeeName(param.getPayeeName());
                applyCommonCreateFields(order, param, transferNo, amount);
                order.setMchNo(mchNo);
                // 持久化场景标识与报备信息(FAIL重试时恢复)
                order.setTransferScene(param.getTransferScene());
                order.setReportInfos(serializeReportInfos(param.getReportInfos()));
                alipayTransferOrderManager.save(order);
                TransferTrade trade = this.buildTrade(order, channel, transferNo);
                transferTradeManager.save(trade);
                yield buildAlipayContext(order).setChannel(channel).setTrade(trade)
                        .setTransferScene(param.getTransferScene())
                        .setReportInfos(param.getReportInfos());
            }
            case "douyin" -> {
                DouyinTransferOrder order = new DouyinTransferOrder()
                        .setPayeeType(param.getPayeeType())
                        .setPayeeAccount(param.getPayeeAccount())
                        .setPayeeName(param.getPayeeName())
                        .setTransferScene(param.getTransferScene());
                applyCommonCreateFields(order, param, transferNo, amount);
                order.setMchNo(mchNo);
                // 持久化报备信息(FAIL重试时恢复)
                order.setReportInfos(serializeReportInfos(param.getReportInfos()));
                douyinTransferOrderManager.save(order);
                TransferTrade trade = this.buildTrade(order, channel, transferNo);
                transferTradeManager.save(trade);
                yield buildDouyinContext(order).setChannel(channel).setTrade(trade)
                        .setTransferScene(param.getTransferScene())
                        .setReportInfos(param.getReportInfos());
            }
            default -> throw new IllegalArgumentException("未知转账通道: " + channel);
        };
    }

    /// 建单公共字段写入: 转账单号/商户转账号/通道商户号/金额/标题/通知等(三通道一致)
    private void applyCommonCreateFields(TransferContainer order, TransferParam param, String transferNo, long amount) {
        order.setTransferNo(transferNo)
                .setBizTransferNo(param.getBizTransferNo())
                .setAppId(param.getAppId())
                .setChannelMchNo(param.getChannelMchNo())
                .setAmount(amount)
                .setCurrency(CurrencyEnum.CNY.getCode())
                .setTitle(param.getTitle())
                .setReason(param.getReason())
                .setNotifyUrl(param.getNotifyUrl())
                .setAttach(param.getAttach())
                .setStatus(PayFundStatusEnum.PROCESSING.getCode())
                .setReqTime(OffsetDateTime.now());
    }

    /// 组装公共资金凭证（relationNo 默认=平台转账单号, 特殊通道变形后覆盖）
    private TransferTrade buildTrade(TransferContainer order, String channel, String transferNo) {
        TransferTrade trade = new TransferTrade()
                .setTradeNo(transferNo)
                .setBizTransferNo(order.getBizTransferNo())
                .setAppId(order.getAppId())
                .setContainerId(order.getId())
                .setContainerChannel(channel)
                .setChannel(channel)
                .setProvider(channel)
                .setAmount(order.getAmount())
                .setCurrency(order.getCurrency())
                .setStatus(PayFundStatusEnum.PROCESSING.getCode())
                .setRelationNo(transferNo)
                .setTitle(order.getTitle());
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
        if (!Objects.equals(trade.getStatus(), PayFundStatusEnum.PROCESSING.getCode())
                && !Objects.equals(trade.getStatus(), PayFundStatusEnum.FAIL.getCode())) {
            log.warn("转账成功忽略: tradeNo={} 状态为 {} 非 processing/fail", trade.getTradeNo(), trade.getStatus());
            return false;
        }
        // 凭证 CAS
        trade.setStatus(PayFundStatusEnum.SUCCESS.getCode());
        trade.setFinishTime(Objects.nonNull(finishTime) ? finishTime : OffsetDateTime.now());
        trade.setOutTransferNo(outTransferNo);
        trade.setRelationNo(relationNo);
        boolean tradeUpdated = transferTradeManager.casUpdateStatus(trade, Set.of(PayFundStatusEnum.PROCESSING.getCode(), PayFundStatusEnum.FAIL.getCode()));
        // 容器镜像 CAS
        MirrorResult mirror = mirrorContainer(channel, trade, Set.of(PayFundStatusEnum.PROCESSING.getCode(), PayFundStatusEnum.FAIL.getCode()), null, transferBody, null);
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
        if (!Objects.equals(trade.getStatus(), PayFundStatusEnum.PROCESSING.getCode())) {
            log.warn("转账失败忽略: tradeNo={} 状态为 {} 非 processing", trade.getTradeNo(), trade.getStatus());
            return false;
        }
        trade.setStatus(PayFundStatusEnum.FAIL.getCode());
        boolean tradeUpdated = transferTradeManager.casUpdateStatus(trade, Set.of(PayFundStatusEnum.PROCESSING.getCode()));
        MirrorResult mirror = mirrorContainer(channel, trade, Set.of(PayFundStatusEnum.PROCESSING.getCode()), errorMsg, null, null);
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
        if (!Objects.equals(trade.getStatus(), PayFundStatusEnum.PROCESSING.getCode())) {
            log.warn("转账关闭忽略: tradeNo={} 状态为 {} 非 processing", trade.getTradeNo(), trade.getStatus());
            return false;
        }
        trade.setStatus(PayFundStatusEnum.CLOSE.getCode());
        boolean tradeUpdated = transferTradeManager.casUpdateStatus(trade, Set.of(PayFundStatusEnum.PROCESSING.getCode()));
        MirrorResult mirror = mirrorContainer(channel, trade, Set.of(PayFundStatusEnum.PROCESSING.getCode()), errorMsg, null, null);
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
                           String transferBody, String transferScene, String wxAppId) {
        trade.setOutTransferNo(outTransferNo);
        transferTradeManager.updateById(trade);
        // 容器回写: 通道单号 + 特有字段
        mirrorProcessing(channel, trade.getContainerId(), outTransferNo, transferBody, transferScene, wxAppId);
    }

    /// 重试重置: 容器+凭证双表回 PROCESSING, 清空通道单号/完成时间/错误信息
    ///
    /// 仅允许 FAIL 状态复用原单重试, CAS 保证并发下仅一方成功。
    @Transactional(rollbackFor = Exception.class)
    public void resetForRetry(String channel, TransferTrade trade) {
        trade.setStatus(PayFundStatusEnum.PROCESSING.getCode());
        trade.setOutTransferNo(null);
        trade.setFinishTime(null);
        transferTradeManager.casUpdateStatus(trade, Set.of(PayFundStatusEnum.FAIL.getCode()));
        mirrorContainer(channel, trade, Set.of(PayFundStatusEnum.FAIL.getCode()), null, null, null);
    }

    // ===== 容器镜像(按通道路由, 编排服务唯一接触具体容器的地方) =====

    /// 按通道装载容器并镜像状态更新(CAS), 返回更新结果与通知地址
    ///
    /// 容器状态/完成时间/通道单号取自凭证 [trade]; errorMsg 单独传入(success 传 null 清空);
    /// 特有字段(微信拉起确认参数/抖音转账场景)有值才覆盖, 终态沿用容器既有值。
    private MirrorResult mirrorContainer(String channel, TransferTrade trade, Set<String> expectFrom,
                                         String errorMsg, String transferBody, String transferScene) {
        MirrorExtras extras = new MirrorExtras(transferBody, transferScene, null);
        return switch (channel) {
            case "wechat" -> doMirror(wechatRoute(), trade, expectFrom, errorMsg, extras);
            case "alipay" -> doMirror(alipayRoute(), trade, expectFrom, errorMsg, extras);
            case "douyin" -> doMirror(douyinRoute(), trade, expectFrom, errorMsg, extras);
            default -> throw new IllegalArgumentException("未知转账通道: " + channel);
        };
    }

    /// 终态镜像公共段: 状态/完成时间/通道单号/错误信息 + 特有字段钩子 + CAS
    private <T extends TransferContainer> MirrorResult doMirror(ChannelRoute<T> route, TransferTrade trade,
            Set<String> expectFrom, String errorMsg, MirrorExtras extras) {
        T order = route.loader().apply(trade.getContainerId());
        if (Objects.isNull(order)) {
            log.warn("转账容器不存在, 跳过容器更新: tradeNo={}", trade.getTradeNo());
            return new MirrorResult(false, null);
        }
        order.setStatus(trade.getStatus());
        order.setFinishTime(trade.getFinishTime());
        order.setOutTransferNo(trade.getOutTransferNo());
        order.setErrorMsg(errorMsg);
        route.mirrorExtras().accept(order, extras);
        boolean updated = route.casUpdater().apply(order, expectFrom);
        return new MirrorResult(updated, order.getNotifyUrl());
    }

    /// 处理中回写: 按通道装载容器补通道单号/特有字段(非 CAS, 不改变状态)
    private void mirrorProcessing(String channel, Long containerId, String outTransferNo,
                                  String transferBody, String transferScene, String wxAppId) {
        MirrorExtras extras = new MirrorExtras(transferBody, transferScene, wxAppId);
        switch (channel) {
            case "wechat" -> doProcessing(wechatRoute(), containerId, outTransferNo, extras);
            case "alipay" -> doProcessing(alipayRoute(), containerId, outTransferNo, extras);
            case "douyin" -> doProcessing(douyinRoute(), containerId, outTransferNo, extras);
            default -> throw new IllegalArgumentException("未知转账通道: " + channel);
        }
    }

    /// 处理中回写公共段: 通道单号 + 特有字段钩子 + 保存
    private <T extends TransferContainer> void doProcessing(ChannelRoute<T> route, Long containerId,
            String outTransferNo, MirrorExtras extras) {
        T order = route.loader().apply(containerId);
        if (Objects.isNull(order)) {
            return;
        }
        order.setOutTransferNo(outTransferNo);
        route.processingExtras().accept(order, extras);
        route.saver().accept(order);
    }

    // ===== 策略上下文装配 =====

    /// 上下文公共字段装配(三通道一致的 12 项)
    private TransferStrategyContext buildContext(TransferContainer order) {
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
                .setFinishTime(order.getFinishTime());
    }

    private TransferStrategyContext buildWechatContext(WechatTransferOrder order) {
        return buildContext(order)
                .setPayeeOpenid(order.getPayeeOpenid())
                .setTransferScene(order.getTransferScene())
                // 恢复报备信息(FAIL重试时使用, 与支付宝/抖音容器对齐)
                .setReportInfos(deserializeReportInfos(order.getReportInfos()))
                .setUserName(order.getUserName())
                .setWxAppId(order.getWxAppId());
    }

    private TransferStrategyContext buildAlipayContext(AlipayTransferOrder order) {
        return buildContext(order)
                .setPayeeType(order.getPayeeType())
                .setPayeeAccount(order.getPayeeAccount())
                .setPayeeName(order.getPayeeName())
                // 恢复场景标识与报备信息(FAIL重试时使用)
                .setTransferScene(order.getTransferScene())
                .setReportInfos(deserializeReportInfos(order.getReportInfos()));
    }

    private TransferStrategyContext buildDouyinContext(DouyinTransferOrder order) {
        return buildContext(order)
                .setPayeeType(order.getPayeeType())
                .setPayeeAccount(order.getPayeeAccount())
                .setPayeeName(order.getPayeeName())
                .setTransferScene(order.getTransferScene())
                // 恢复报备信息(FAIL重试时使用)
                .setReportInfos(deserializeReportInfos(order.getReportInfos()));
    }

    // ===== 通道路由 =====

    /// 镜像差异字段包(仅特有通道消费, null 表示不覆盖)
    private record MirrorExtras(String transferBody, String transferScene, String wxAppId) {
    }

    /// 通道路由: 绑定容器加载/保存/CAS 与终态/处理中两段特有字段回写钩子
    ///
    /// casUpdater 对应各 Manager 自有的 CAS 方法; 泛型 [T] 保证钩子以具体容器类型操作, 全链零强转。
    private record ChannelRoute<T extends TransferContainer>(
            LongFunction<T> loader,
            Consumer<T> saver,
            BiFunction<T, Set<String>, Boolean> casUpdater,
            BiConsumer<T, MirrorExtras> mirrorExtras,
            BiConsumer<T, MirrorExtras> processingExtras) {
    }

    private ChannelRoute<WechatTransferOrder> wechatRoute() {
        return new ChannelRoute<>(
                id -> wechatTransferOrderManager.findById(id).orElse(null),
                wechatTransferOrderManager::updateById,
                wechatTransferOrderManager::casUpdateStatus,
                // 终态: 拉起确认参数有值才覆盖
                (order, extras) -> {
                    if (Objects.nonNull(extras.transferBody())) {
                        order.setTransferBody(extras.transferBody());
                    }
                },
                // 处理中: 拉起确认参数/微信 AppId 有值才覆盖
                (order, extras) -> {
                    if (Objects.nonNull(extras.transferBody())) {
                        order.setTransferBody(extras.transferBody());
                    }
                    if (Objects.nonNull(extras.wxAppId())) {
                        order.setWxAppId(extras.wxAppId());
                    }
                });
    }

    private ChannelRoute<AlipayTransferOrder> alipayRoute() {
        return new ChannelRoute<>(
                id -> alipayTransferOrderManager.findById(id).orElse(null),
                alipayTransferOrderManager::updateById,
                alipayTransferOrderManager::casUpdateStatus,
                // 无终态特有字段
                (order, extras) -> {
                },
                // 无处理中特有字段
                (order, extras) -> {
                });
    }

    private ChannelRoute<DouyinTransferOrder> douyinRoute() {
        return new ChannelRoute<>(
                id -> douyinTransferOrderManager.findById(id).orElse(null),
                douyinTransferOrderManager::updateById,
                douyinTransferOrderManager::casUpdateStatus,
                // 终态: 转账场景有值才覆盖
                (order, extras) -> {
                    if (Objects.nonNull(extras.transferScene())) {
                        order.setTransferScene(extras.transferScene());
                    }
                },
                // 处理中: 转账场景有值才覆盖
                (order, extras) -> {
                    if (Objects.nonNull(extras.transferScene())) {
                        order.setTransferScene(extras.transferScene());
                    }
                });
    }

    // ===== 报备信息序列化(容器持久化/重试恢复) =====

    /// 序列化报备信息为JSON字符串(支付宝容器持久化, FAIL重试时恢复)
    private String serializeReportInfos(List<TransferReportInfo> reportInfos) {
        if (Objects.isNull(reportInfos) || reportInfos.isEmpty()) {
            return null;
        }
        return JSONUtil.toJsonStr(reportInfos);
    }

    /// 反序列化报备信息(从容器恢复)
    private List<TransferReportInfo> deserializeReportInfos(String json) {
        if (StrUtil.isBlank(json)) {
            return null;
        }
        return JSONUtil.toList(json, TransferReportInfo.class);
    }
}
