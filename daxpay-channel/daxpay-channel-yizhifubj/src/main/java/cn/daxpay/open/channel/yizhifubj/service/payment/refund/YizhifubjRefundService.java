package cn.daxpay.open.channel.yizhifubj.service.payment.refund;

import cn.daxpay.open.channel.yizhifubj.client.YizhifubjChannelClient;
import cn.daxpay.open.channel.yizhifubj.client.credential.YizhifubjSdkCredential;
import cn.daxpay.open.channel.yizhifubj.client.req.YizhifubjRefundReq;
import cn.daxpay.open.channel.yizhifubj.client.resp.YizhifubjRefundResp;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 一直付退款业务服务
///
/// 通过 [YizhifubjChannelClient] 调用子应用发起 5upay `/onlinePay/refund` 退款。
/// 一直付退款为同步受理: status=SUCCESS 即退款成功, 返回终态成功; 失败返回退款失败。
/// 另上送退款异步通知地址兜底(异常网络下的最终一致补偿)。
@Slf4j
@Service
@RequiredArgsConstructor
public class YizhifubjRefundService {

    private final YizhifubjChannelClient yizhifubjChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行一直付退款
    public RefundResultBo refund(RefundOrder refundOrder, YizhifubjSdkCredential credential) {
        YizhifubjRefundReq req = new YizhifubjRefundReq();
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        // 原支付订单的通道流水号(serialNumber); 为空时无法发起通道退款, 软失败等待回调补充
        req.setOriginOutOrderNo(refundOrder.getOutOrderNo());
        req.setAmount(refundOrder.getAmount());
        req.setNotifyUrl(this.buildRefundNotifyUrl(refundOrder));
        req.setCredential(credential);

        if (StrUtil.isBlank(req.getOriginOutOrderNo())) {
            log.error("一直付退款缺少原订单通道流水号: refundNo={}", refundOrder.getRefundNo());
            return new RefundResultBo()
                    .setComplete(false)
                    .setStatus(RefundOrderStatusEnum.PROGRESS)
                    .setSyncSuccess(false)
                    .setSyncErrorMsg("一直付通道流水号未知(等待回调补充), 暂无法退款");
        }

        DaxResult<YizhifubjRefundResp> result = yizhifubjChannelClient.refund(req);
        if (result.getCode() != 0) {
            log.error("一直付通道退款失败: refundNo={}, msg={}", refundOrder.getRefundNo(), result.getMsg());
            return new RefundResultBo()
                    .setComplete(false)
                    .setStatus(RefundOrderStatusEnum.FAIL)
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        // 同步受理即成功, 终态成功
        return new RefundResultBo()
                .setComplete(true)
                .setStatus(RefundOrderStatusEnum.SUCCESS);
    }

    /// 生成一直付退款异步通知地址(兜底通道)
    private String buildRefundNotifyUrl(RefundOrder refundOrder) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new IllegalStateException("平台后端访问地址(backendBaseUrl)未配置, 无法生成一直付退款回调地址");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/yizhifubj/refund",
                base, refundOrder.getMchNo(), refundOrder.getChannelMchNo());
    }
}
