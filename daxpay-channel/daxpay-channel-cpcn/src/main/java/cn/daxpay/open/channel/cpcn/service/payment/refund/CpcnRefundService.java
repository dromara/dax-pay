package cn.daxpay.open.channel.cpcn.service.payment.refund;

import cn.daxpay.open.channel.cpcn.client.CpcnChannelClient;
import cn.daxpay.open.channel.cpcn.client.credential.CpcnSdkCredential;
import cn.daxpay.open.channel.cpcn.client.req.CpcnRefundReq;
import cn.daxpay.open.channel.cpcn.client.resp.CpcnRefundResp;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 中金退款业务服务
///
/// 通过 [CpcnChannelClient] 调用子应用发起中金 5021 退款。
/// 受理成功即返回处理中(中金无退款查询接口, 终态以 NoticeURL 异步通知为准)。
@Slf4j
@Service
@RequiredArgsConstructor
public class CpcnRefundService {

    private final CpcnChannelClient cpcnChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行中金退款
    public RefundResultBo refund(RefundOrder refundOrder, CpcnSdkCredential credential) {
        CpcnRefundReq req = new CpcnRefundReq();
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        // 原支付交易号 = 下单时上送的 TxSN, 上送 PaymentTxSN
        req.setOriginOutTradeNo(refundOrder.getTradeNo());
        req.setAmount(refundOrder.getAmount());
        req.setReason(refundOrder.getReason());
        req.setNotifyUrl(this.buildRefundNotifyUrl(refundOrder));
        req.setCredential(credential);

        DaxResult<CpcnRefundResp> result = cpcnChannelClient.refund(req);
        if (result.getCode() != 0) {
            log.error("中金通道退款失败: refundNo={}, msg={}", refundOrder.getRefundNo(), result.getMsg());
            return new RefundResultBo()
                    .setComplete(false)
                    .setStatus(RefundOrderStatusEnum.FAIL)
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        // 5021 受理成功恒为处理中, 终态经异步通知
        return new RefundResultBo()
                .setComplete(false)
                .setStatus(RefundOrderStatusEnum.PROGRESS);
    }

    /// 生成中金退款异步通知地址(NoticeURL)
    private String buildRefundNotifyUrl(RefundOrder refundOrder) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new IllegalStateException("平台后端访问地址(backendBaseUrl)未配置, 无法生成中金退款回调地址");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/cpcn/refund",
                base, refundOrder.getMchNo(), refundOrder.getChannelMchNo());
    }
}
