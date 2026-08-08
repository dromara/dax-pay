package cn.daxpay.open.channel.alipay.service.payment.transfer;

import cn.daxpay.open.channel.alipay.client.AlipayChannelClient;
import cn.daxpay.open.channel.alipay.client.credential.AlipaySdkCredential;
import cn.daxpay.open.channel.alipay.client.req.AlipayTransferReq;
import cn.daxpay.open.channel.alipay.client.resp.AlipayTransferResp;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayTransferSceneConfig;
import cn.daxpay.open.channel.alipay.service.direct.AlipayTransferSceneConfigService;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.strategy.transfer.TransferStrategyContext;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.transfer.bo.TransferResultBo;
import cn.daxpay.open.payment.trade.transfer.dao.AlipayTransferOrderManager;
import cn.daxpay.open.payment.trade.transfer.entity.AlipayTransferOrder;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/// # 支付宝转账执行业务服务
///
/// 通过 [AlipayChannelClient] 调用子应用 dax-pay-channel-one 完成支付宝单笔转账
/// (alipay.fund.trans.uni.transfer)。请求构建、响应解析与状态映射在本类中完成。
///
/// 转账场景(transfer_scene_name): 2026 年起新接入商户必填, 由 [AlipayTransferSceneConfigService]
/// 按通道商户配置注入(显式 configId 优先, 否则用默认场景)。
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayTransferService {

    /// 支付宝时间格式(东八区本地时间字面量)
    private static final DateTimeFormatter ALIPAY_DATE_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /// 支付宝业务码: 转账订单不存在
    private static final String ORDER_NOT_EXIST = "ORDER_NOT_EXIST";

    private final AlipayChannelClient alipayChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;
    // 转账场景配置(2026 新商户必填)
    private final AlipayTransferSceneConfigService alipayTransferSceneConfigService;
    // 支付宝容器(回写资金流水号等通道特有字段)
    private final AlipayTransferOrderManager alipayTransferOrderManager;

    /// 执行支付宝转账
    ///
    /// @param context    转账策略上下文(通道特有字段: payeeType/payeeAccount/payeeName/transferScene)
    /// @param credential 通道调用凭证
    /// @return 转账结果
    public TransferResultBo transfer(TransferStrategyContext context, AlipaySdkCredential credential) {
        // 转账场景配置(2026 新商户必填): 显式场景配置ID优先, 否则用通道商户默认
        AlipayTransferSceneConfig scene = alipayTransferSceneConfigService.findEffective(
                context.getChannelMchNo(), context.getTransferScene());

        AlipayTransferReq req = new AlipayTransferReq();
        req.setOutBizNo(context.getTransferNo());
        req.setAmount(context.getAmount());
        req.setTitle(context.getTitle());
        req.setRemark(context.getReason());
        req.setPayeeType(context.getPayeeType());
        req.setPayeeAccount(context.getPayeeAccount());
        req.setPayeeName(context.getPayeeName());
        req.setNotifyUrl(this.buildNotifyUrl(context));
        req.setTransferSceneName(scene.getSceneName());
        // 转账场景报备信息由发起方手动填写,从上下文透传(不再从场景配置取)
        req.setReportInfos(context.getReportInfos());
        req.setCredential(credential);

        DaxResult<AlipayTransferResp> result = alipayChannelClient.transfer(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.alipay.transferFailed", result.getMsg());
        }
        AlipayTransferResp resp = result.getData();
        // 支付宝业务失败(如收款人信息错误) → 直接失败, 不进入 PROCESSING
        if (StrUtil.isNotBlank(resp.getSubCode())) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.alipay.transferFailed",
                    StrUtil.blankToDefault(resp.getSubMsg(), resp.getSubCode()));
        }
        TransferResultBo bo = new TransferResultBo()
                .setOutTransferNo(resp.getOrderId())
                .setPayFundOrderId(resp.getPayFundOrderId())
                .setFinishTime(this.parseAlipayDate(resp.getTransDate()));
        // 同步返回 SUCCESS 直接成功, 其余(DEALING等)视为处理中, 交同步/回调确认
        if (Objects.equals(resp.getStatus(), "SUCCESS")) {
            bo.setStatus(PayFundStatusEnum.SUCCESS);
        } else {
            bo.setStatus(PayFundStatusEnum.PROCESSING);
        }
        // 回写支付宝特有字段(资金流水号)到容器
        this.writeBackPayFundOrderId(context, resp.getPayFundOrderId());
        return bo;
    }

    /// 同步查询支付宝转账状态
    ///
    /// @param context    转账策略上下文
    /// @param credential 通道调用凭证
    /// @return 转账同步结果
    public TransferResultBo sync(TransferStrategyContext context, AlipaySdkCredential credential) {
        AlipayTransferReq req = new AlipayTransferReq();
        req.setOutBizNo(context.getTransferNo());
        req.setCredential(credential);

        DaxResult<AlipayTransferResp> result = alipayChannelClient.transferSync(req);
        if (result.getCode() != 0) {
            return new TransferResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg())
                    .setStatus(PayFundStatusEnum.PROCESSING);
        }
        AlipayTransferResp resp = result.getData();
        // 回写资金流水号(同步查询可能补获)
        this.writeBackPayFundOrderId(context, resp.getPayFundOrderId());
        // 支付宝业务失败(code != 10000): 按 subCode 决策资金态
        if (StrUtil.isNotBlank(resp.getSubCode())) {
            return mapBusinessError(resp);
        }
        return mapSyncResult(resp);
    }

    /// 映射同步结果为平台资金态
    ///
    /// 支付宝状态(uni.transfer): SUCCESS(成功) / FAIL(失败) / DEALING(处理中) / REFUND(退票)
    /// - FAIL → fail(终态失败, 允许复用原单号重试, 与支付宝"相同 out_biz_no 重发"指引一致)
    /// - REFUND → close(退票资金退回, 等同关闭)
    private TransferResultBo mapSyncResult(AlipayTransferResp resp) {
        String status = resp.getStatus();
        TransferResultBo bo = new TransferResultBo()
                .setOutTransferNo(resp.getOrderId())
                .setPayFundOrderId(resp.getPayFundOrderId())
                .setFinishTime(this.parseAlipayDate(resp.getFinishTime()))
                .setSyncErrorCode(resp.getErrorCode())
                .setSyncErrorMsg(resp.getFailReason());
        if (Objects.equals(status, "SUCCESS")) {
            bo.setStatus(PayFundStatusEnum.SUCCESS);
        } else if (Objects.equals(status, "FAIL")) {
            bo.setStatus(PayFundStatusEnum.FAIL);
        } else if (Objects.equals(status, "CLOSED") || Objects.equals(status, "REFUND")) {
            bo.setStatus(PayFundStatusEnum.CLOSE);
        } else {
            // DEALING/未知: 保持处理中, 由后续同步轮询确认
            bo.setStatus(PayFundStatusEnum.PROCESSING);
        }
        return bo;
    }

    /// 映射支付宝业务失败响应(网关返回 code != 10000, 非 SDK 异常)
    ///
    /// - ORDER_NOT_EXIST → FAIL(转账订单不存在 = 发起未成功, 商户可凭原 out_biz_no 重试)
    /// - 其他 subCode → syncSuccess=false + PROCESSING(同步失败, 等待定时轮询重试)
    private TransferResultBo mapBusinessError(AlipayTransferResp resp) {
        // 转账订单不存在 → 终态失败
        if (ORDER_NOT_EXIST.equals(resp.getSubCode())) {
            log.warn("支付宝转账订单不存在, 置为终态失败: subCode={}, subMsg={}",
                    resp.getSubCode(), resp.getSubMsg());
            return new TransferResultBo()
                    .setStatus(PayFundStatusEnum.FAIL)
                    .setSyncErrorCode(resp.getSubCode())
                    .setSyncErrorMsg(StrUtil.blankToDefault(resp.getSubMsg(), "转账订单不存在"));
        }
        // 其他业务错误 → 同步失败, 保持 PROCESSING 等待重试
        log.warn("支付宝转账查询业务失败: subCode={}, subMsg={}", resp.getSubCode(), resp.getSubMsg());
        return new TransferResultBo()
                .setStatus(PayFundStatusEnum.PROCESSING)
                .setSyncSuccess(false)
                .setSyncErrorCode(resp.getSubCode())
                .setSyncErrorMsg(StrUtil.blankToDefault(resp.getSubMsg(), "支付宝转账查询失败"));
    }

    /// 回写支付宝资金流水号到容器(支付宝特有, 非状态流转, 直接更新)
    private void writeBackPayFundOrderId(TransferStrategyContext context, String payFundOrderId) {
        if (StrUtil.isBlank(payFundOrderId) || context.getTrade() == null) {
            return;
        }
        Long containerId = context.getTrade().getContainerId();
        alipayTransferOrderManager.lambdaUpdate()
                .eq(AlipayTransferOrder::getId, containerId)
                .set(AlipayTransferOrder::getPayFundOrderId, payFundOrderId)
                .update();
    }

    /// 解析支付宝时间字面量(东八区 yyyy-MM-dd HH:mm:ss)为 OffsetDateTime
    ///
    /// 支付宝返回的时间字段无时区后缀, 按通道时间解析规范先用 LocalDateTime 接住再附加东八区偏移。
    private OffsetDateTime parseAlipayDate(String dateStr) {
        if (StrUtil.isBlank(dateStr)) {
            return null;
        }
        return LocalDateTime.parse(dateStr, ALIPAY_DATE_FMT).atOffset(ZoneOffset.ofHours(8));
    }

    /// 生成支付宝转账异步通知地址(支付宝→平台)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/alipay`
    /// (与支付/退款共用统一回调端点, 由 [cn.daxpay.open.channel.alipay.service.callback.AlipayCallbackService]
    /// 按表单参数区分: 含 out_biz_no 且无 out_request_no/out_trade_no → 转账回调)
    private String buildNotifyUrl(TransferStrategyContext context) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            // backendBaseUrl 未配置时抛清晰异常
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/alipay",
                base, context.getMchNo(), context.getChannelMchNo());
    }
}
