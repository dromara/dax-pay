package cn.daxpay.open.channel.sand.service.payment.refund;

import cn.daxpay.open.channel.sand.client.SandChannelClient;
import cn.daxpay.open.channel.sand.client.credential.SandSdkCredential;
import cn.daxpay.open.channel.sand.client.req.SandRefundReq;
import cn.daxpay.open.channel.sand.client.resp.SandRefundResp;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 杉德退款业务服务
///
/// 通过 [SandChannelClient] 调用子应用发起杉德订单退款(trans.order.refund)。
/// 受理成功即返回处理中(杉德无退款查询接口, 终态以异步通知为准)。
@Slf4j
@Service
@RequiredArgsConstructor
public class SandRefundService {

    private final SandChannelClient sandChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行杉德退款
    public RefundResultBo refund(RefundOrder refundOrder, SandSdkCredential credential) {
        SandRefundReq req = new SandRefundReq();
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        req.setOriginOutTradeNo(refundOrder.getTradeNo());
        req.setAmount(refundOrder.getAmount());
        req.setNotifyUrl(this.buildRefundNotifyUrl(refundOrder));
        req.setCredential(credential);

        DaxResult<SandRefundResp> result = sandChannelClient.refund(req);
        if (result.getCode() != 0) {
            log.error("杉德通道退款失败: refundNo={}, msg={}", refundOrder.getRefundNo(), result.getMsg());
            return new RefundResultBo()
                    .setComplete(false)
                    .setStatus(RefundOrderStatusEnum.FAIL)
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        // 受理成功恒为处理中, 终态经异步通知
        return new RefundResultBo()
                .setComplete(false)
                .setStatus(RefundOrderStatusEnum.PROGRESS);
    }

    /// 生成杉德退款异步通知地址(notifyUrl)
    private String buildRefundNotifyUrl(RefundOrder refundOrder) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new IllegalStateException("平台后端访问地址(backendBaseUrl)未配置, 无法生成杉德退款回调地址");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/sand/refund",
                base, refundOrder.getMchNo(), refundOrder.getChannelMchNo());
    }
}
