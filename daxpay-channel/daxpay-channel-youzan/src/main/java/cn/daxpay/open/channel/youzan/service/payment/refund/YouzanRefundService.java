package cn.daxpay.open.channel.youzan.service.payment.refund;

import cn.daxpay.open.channel.youzan.client.YouzanChannelClient;
import cn.daxpay.open.channel.youzan.client.credential.YouzanSdkCredential;
import cn.daxpay.open.channel.youzan.client.req.YouzanRefundReq;
import cn.daxpay.open.channel.youzan.client.resp.YouzanRefundResp;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 有赞退款业务服务
///
/// 通过 [YouzanChannelClient] 调用子应用发起有赞统一退款。
/// 受理成功即返回处理中(有赞无退款查询接口, 终态以 notify_url 异步通知为准)。
@Slf4j
@Service
@RequiredArgsConstructor
public class YouzanRefundService {

    private final YouzanChannelClient youzanChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行有赞退款
    public RefundResultBo refund(RefundOrder refundOrder, YouzanSdkCredential credential) {
        YouzanRefundReq req = new YouzanRefundReq();
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        // 原支付订单的有赞订单号(trade_no)
        req.setOriginOutOrderNo(refundOrder.getOutOrderNo());
        req.setAmount(refundOrder.getAmount());
        req.setNotifyUrl(this.buildRefundNotifyUrl(refundOrder));
        req.setCredential(credential);

        DaxResult<YouzanRefundResp> result = youzanChannelClient.refund(req);
        if (result.getCode() != 0) {
            log.error("有赞通道退款失败: refundNo={}, msg={}", refundOrder.getRefundNo(), result.getMsg());
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

    /// 生成有赞退款异步通知地址(notify_url)
    private String buildRefundNotifyUrl(RefundOrder refundOrder) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new IllegalStateException("平台后端访问地址(backendBaseUrl)未配置, 无法生成有赞退款回调地址");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/youzan/refund",
                base, refundOrder.getMchNo(), refundOrder.getChannelMchNo());
    }
}
