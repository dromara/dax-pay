package cn.daxpay.open.channel.llianpay.service.payment.refund;

import cn.daxpay.open.channel.llianpay.client.LlianpayChannelClient;
import cn.daxpay.open.channel.llianpay.client.credential.LlianpaySdkCredential;
import cn.daxpay.open.channel.llianpay.client.req.LlianpayRefundReq;
import cn.daxpay.open.channel.llianpay.client.resp.LlianpayRefundResp;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 连连退款业务服务
///
/// 通过 [LlianpayChannelClient] 调用子应用发起连连退款。
/// 受理成功即返回处理中(连连无退款查询接口, 终态以 notify_url 异步通知为准)。
@Slf4j
@Service
@RequiredArgsConstructor
public class LlianpayRefundService {

    private final LlianpayChannelClient llianpayChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行连连退款
    public RefundResultBo refund(RefundOrder refundOrder, LlianpaySdkCredential credential) {
        LlianpayRefundReq req = new LlianpayRefundReq();
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        req.setOriginOutTradeNo(refundOrder.getTradeNo());
        // 原订单金额(连连退款要求上送原单金额的收款方退款信息与支付方式金额)
        req.setAmount(refundOrder.getAmount());
        req.setOriginAmount(refundOrder.getOrderAmount());
        req.setNotifyUrl(this.buildRefundNotifyUrl(refundOrder));
        req.setCredential(credential);

        DaxResult<LlianpayRefundResp> result = llianpayChannelClient.refund(req);
        if (result.getCode() != 0) {
            log.error("连连通道退款失败: refundNo={}, msg={}", refundOrder.getRefundNo(), result.getMsg());
            return new RefundResultBo()
                    .setComplete(false)
                    .setStatus(RefundOrderStatusEnum.FAIL)
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        // 连连退款受理成功恒为处理中, 终态经异步通知
        return new RefundResultBo()
                .setComplete(false)
                .setStatus(RefundOrderStatusEnum.PROGRESS);
    }

    /// 生成连连退款异步通知地址(notify_url)
    private String buildRefundNotifyUrl(RefundOrder refundOrder) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new IllegalStateException("平台后端访问地址(backendBaseUrl)未配置, 无法生成连连退款回调地址");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/llianpay/refund",
                base, refundOrder.getMchNo(), refundOrder.getChannelMchNo());
    }
}
