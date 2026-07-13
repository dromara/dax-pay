package cn.daxpay.open.channel.yeepay.service.payment.refund;

import cn.daxpay.open.channel.yeepay.client.YeepayChannelClient;
import cn.daxpay.open.channel.yeepay.client.credential.YeepaySdkCredential;
import cn.daxpay.open.channel.yeepay.client.req.YeepayRefundReq;
import cn.daxpay.open.channel.yeepay.client.resp.YeepayRefundResp;
import cn.daxpay.open.payment.common.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayRefundOrder;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 易宝退款业务服务
///
/// 通过 [YeepayChannelClient] 调用子应用发起易宝退款。
/// 退款可能同步成功(直接返回 SUCCESS)或处理中(需轮询查询)。
@Slf4j
@Service
@RequiredArgsConstructor
public class YeepayRefundService {

    private final YeepayChannelClient yeepayChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行易宝退款
    public RefundResultBo refund(PayRefundOrder refundOrder, YeepaySdkCredential credential) {
        YeepayRefundReq req = new YeepayRefundReq();
        req.setOriginOutTradeNo(refundOrder.getOrderNo());
        req.setOutRefundNo(refundOrder.getRefundNo());
        req.setAmount(refundOrder.getAmount());
        req.setReason(refundOrder.getReason());
        req.setNotifyUrl(this.buildRefundNotifyUrl(refundOrder));
        req.setCredential(credential);

        DaxResult<YeepayRefundResp> result = yeepayChannelClient.refund(req);
        if (result.getCode() != 0) {
            log.error("易宝通道退款失败: refundNo={}, msg={}", refundOrder.getRefundNo(), result.getMsg());
            return new RefundResultBo()
                    .setComplete(false)
                    .setStatus(RefundOrderStatusEnum.FAIL)
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        return toRefundResult(result.getData());
    }

    /// 生成易宝退款异步通知地址
    private String buildRefundNotifyUrl(PayRefundOrder refundOrder) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new IllegalStateException("平台后端访问地址(backendBaseUrl)未配置, 无法生成易宝退款回调地址");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/yeepay/{}/refund",
                base, refundOrder.getMchNo(), refundOrder.getAppId(), refundOrder.getChannelMchNo());
    }

    /// 解析子应用响应
    private RefundResultBo toRefundResult(YeepayRefundResp resp) {
        RefundResultBo bo = new RefundResultBo();
        bo.setOutRefundNo(resp.getTradeNo());
        bo.setFinishTime(resp.getFinishTime());
        // 同步完成 = 退款即时成功; 否则处理中
        if (resp.isComplete()) {
            bo.setComplete(true)
                    .setStatus(RefundOrderStatusEnum.SUCCESS);
        } else {
            bo.setComplete(false)
                    .setStatus(RefundOrderStatusEnum.PROGRESS);
        }
        return bo;
    }
}
