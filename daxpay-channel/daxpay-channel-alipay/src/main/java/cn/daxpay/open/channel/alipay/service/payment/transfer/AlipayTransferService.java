package cn.daxpay.open.channel.alipay.service.payment.transfer;

import cn.daxpay.open.channel.alipay.client.AlipayChannelClient;
import cn.daxpay.open.channel.alipay.client.credential.AlipaySdkCredential;
import cn.daxpay.open.channel.alipay.client.req.AlipayTransferReq;
import cn.daxpay.open.channel.alipay.client.resp.AlipayTransferResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.strategy.transfer.TransferStrategyContext;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.transfer.bo.TransferResultBo;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 支付宝转账执行业务服务
///
/// 通过 [AlipayChannelClient] 调用子应用 dax-pay-channel-one 完成支付宝单笔转账
/// (alipay.fund.trans.uni.transfer)。请求构建、响应解析与状态映射在本类中完成。
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayTransferService {

    private final AlipayChannelClient alipayChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行支付宝转账
    ///
    /// @param context    转账策略上下文(通道特有字段: payeeType/payeeAccount/payeeName)
    /// @param credential 通道调用凭证
    /// @return 转账结果
    public TransferResultBo transfer(TransferStrategyContext context, AlipaySdkCredential credential) {
        AlipayTransferReq req = new AlipayTransferReq();
        req.setOutBizNo(context.getTransferNo());
        req.setAmount(context.getAmount());
        req.setTitle(context.getTitle());
        req.setRemark(context.getReason());
        req.setPayeeType(context.getPayeeType());
        req.setPayeeAccount(context.getPayeeAccount());
        req.setPayeeName(context.getPayeeName());
        req.setNotifyUrl(this.buildNotifyUrl(context));
        req.setCredential(credential);

        DaxResult<AlipayTransferResp> result = alipayChannelClient.transfer(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.alipay.transferFailed", result.getMsg());
        }
        AlipayTransferResp resp = result.getData();
        TransferResultBo bo = new TransferResultBo()
                .setOutTransferNo(resp.getOrderId());
        // 同步返回 SUCCESS 直接成功, 其余(DEALING等)视为处理中, 交同步/回调确认
        if (Objects.equals(resp.getStatus(), "SUCCESS")) {
            bo.setStatus(PayFundStatusEnum.SUCCESS);
        } else {
            bo.setStatus(PayFundStatusEnum.PROCESSING);
        }
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
        return mapSyncResult(result.getData());
    }

    /// 映射同步结果为平台资金态
    ///
    /// 支付宝状态: SUCCESS(成功) / FAIL(失败) / REFUND(退回) / DEALING(处理中) / CLOSED(关闭)
    private TransferResultBo mapSyncResult(AlipayTransferResp resp) {
        String status = resp.getStatus();
        TransferResultBo bo = new TransferResultBo()
                .setOutTransferNo(resp.getOrderId())
                .setSyncErrorMsg(resp.getFailReason());
        if (Objects.equals(status, "SUCCESS")) {
            bo.setStatus(PayFundStatusEnum.SUCCESS);
        } else if (Objects.equals(status, "FAIL") || Objects.equals(status, "CLOSED")) {
            bo.setStatus(PayFundStatusEnum.CLOSE);
        } else {
            // DEALING/REFUND/未知: 保持处理中, 由后续同步轮询确认
            bo.setStatus(PayFundStatusEnum.PROCESSING);
        }
        return bo;
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
