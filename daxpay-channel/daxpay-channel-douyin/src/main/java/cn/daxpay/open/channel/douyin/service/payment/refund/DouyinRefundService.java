package cn.daxpay.open.channel.douyin.service.payment.refund;

import cn.daxpay.open.channel.douyin.client.DouyinChannelClient;
import cn.daxpay.open.channel.douyin.client.credential.DouyinSdkCredential;
import cn.daxpay.open.channel.douyin.client.req.DouyinRefundReq;
import cn.daxpay.open.channel.douyin.client.resp.DouyinRefundResp;
import cn.daxpay.open.channel.douyin.code.DouyinPayCode;
import cn.daxpay.open.payment.common.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayRefundOrder;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

/// # 抖音退款业务服务
///
/// 通过 [DouyinChannelClient] 调用子应用 dax-pay-channel-one 发起抖音退款。
///
/// 资金变动判定:
/// - 退款状态 SUCCESS → 退款即时成功
/// - PROCESSING / CLOSED / ABNORMAL → 退款中, 需同步查询确认最终状态
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinRefundService {

    private final DouyinChannelClient douyinChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行抖音退款
    public RefundResultBo refund(PayRefundOrder refundOrder, DouyinSdkCredential credential) {
        DouyinRefundReq req = new DouyinRefundReq();
        req.setOutTradeNo(refundOrder.getOrderNo());
        req.setOutRefundNo(refundOrder.getRefundNo());
        req.setRefundAmount(refundOrder.getAmount());
        req.setTotalAmount(refundOrder.getOrderAmount());
        req.setNotifyUrl(this.buildRefundNotifyUrl(refundOrder));
        req.setCredential(credential);

        DaxResult<DouyinRefundResp> result = douyinChannelClient.refund(req);
        if (result.getCode() != 0) {
            log.error("抖音通道退款失败: refundNo={}, msg={}", refundOrder.getRefundNo(), result.getMsg());
            return new RefundResultBo()
                    .setComplete(false)
                    .setStatus(RefundOrderStatusEnum.FAIL)
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        return toRefundResult(result.getData());
    }

    /// 生成抖音退款异步通知地址(带 channelMchNo 供回调组装凭证验签)
    private String buildRefundNotifyUrl(PayRefundOrder refundOrder) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new IllegalStateException("平台后端访问地址(backendBaseUrl)未配置, 无法生成抖音退款回调地址");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/douyin/{}/refund",
                base, refundOrder.getMchNo(), refundOrder.getAppId(), refundOrder.getChannelMchNo());
    }

    /// 解析子应用响应
    private RefundResultBo toRefundResult(DouyinRefundResp resp) {
        RefundResultBo bo = new RefundResultBo();
        bo.setOutRefundNo(resp.getOutRefundNo());
        // 退款完成时间(RFC3339 → OffsetDateTime)
        if (StrUtil.isNotBlank(resp.getFinishTime())) {
            bo.setFinishTime(OffsetDateTime.parse(resp.getFinishTime()));
        }
        // SUCCESS 退款即时成功; 其他状态需同步查询确认
        if (DouyinPayCode.REFUND_STATUS_SUCCESS.equals(resp.getRefundStatus())) {
            bo.setComplete(true)
                    .setStatus(RefundOrderStatusEnum.SUCCESS);
        } else {
            bo.setComplete(false)
                    .setStatus(RefundOrderStatusEnum.PROGRESS);
        }
        return bo;
    }
}
