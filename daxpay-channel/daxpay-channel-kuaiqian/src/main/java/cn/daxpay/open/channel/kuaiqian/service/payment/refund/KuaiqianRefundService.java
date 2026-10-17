package cn.daxpay.open.channel.kuaiqian.service.payment.refund;

import cn.daxpay.open.channel.kuaiqian.client.KuaiqianChannelClient;
import cn.daxpay.open.channel.kuaiqian.client.credential.KuaiqianSdkCredential;
import cn.daxpay.open.channel.kuaiqian.client.req.KuaiqianRefundReq;
import cn.daxpay.open.channel.kuaiqian.client.resp.KuaiqianRefundResp;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 快钱退款业务服务
///
/// 通过 [KuaiqianChannelClient] 调用子应用发起快钱 A7003 当面付退款。
/// 受理成功即返回处理中(快钱无退款查询接口, 终态以 tr3Url 异步通知为准)。
@Slf4j
@Service
@RequiredArgsConstructor
public class KuaiqianRefundService {

    private final KuaiqianChannelClient kuaiqianChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行快钱退款
    public RefundResultBo refund(RefundOrder refundOrder, KuaiqianSdkCredential credential) {
        KuaiqianRefundReq req = new KuaiqianRefundReq();
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        req.setOriginOutTradeNo(refundOrder.getTradeNo());
        // 原支付订单的通道订单控制号(快钱 idOrderCtrl)
        req.setOriginOutOrderNo(refundOrder.getOutOrderNo());
        req.setAmount(refundOrder.getAmount());
        req.setReason(refundOrder.getReason());
        req.setNotifyUrl(this.buildRefundNotifyUrl(refundOrder));
        req.setCredential(credential);

        DaxResult<KuaiqianRefundResp> result = kuaiqianChannelClient.refund(req);
        if (result.getCode() != 0) {
            log.error("快钱通道退款失败: refundNo={}, msg={}", refundOrder.getRefundNo(), result.getMsg());
            return new RefundResultBo()
                    .setComplete(false)
                    .setStatus(RefundOrderStatusEnum.FAIL)
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        // A7003 受理成功恒为处理中, 终态经异步通知
        return new RefundResultBo()
                .setComplete(false)
                .setStatus(RefundOrderStatusEnum.PROGRESS);
    }

    /// 生成快钱退款异步通知地址(tr3Url)
    private String buildRefundNotifyUrl(RefundOrder refundOrder) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new IllegalStateException("平台后端访问地址(backendBaseUrl)未配置, 无法生成快钱退款回调地址");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/kuaiqian/refund",
                base, refundOrder.getMchNo(), refundOrder.getChannelMchNo());
    }
}
