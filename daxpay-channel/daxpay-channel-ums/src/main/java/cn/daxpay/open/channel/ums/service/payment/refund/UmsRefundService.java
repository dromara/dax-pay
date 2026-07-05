package cn.daxpay.open.channel.ums.service.payment.refund;

import cn.daxpay.open.channel.ums.client.UmsChannelClient;
import cn.daxpay.open.channel.ums.client.credential.UmsSdkCredential;
import cn.daxpay.open.channel.ums.client.enums.UmsPayMethod;
import cn.daxpay.open.channel.ums.client.req.UmsRefundReq;
import cn.daxpay.open.channel.ums.client.resp.UmsRefundResp;
import cn.daxpay.open.channel.ums.code.UmsCode;
import cn.daxpay.open.payment.common.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.core.trade.bo.RefundResultBo;
import cn.daxpay.open.payment.core.trade.entity.PayRefundOrder;
import cn.daxpay.open.platform.system.service.config.PlatformUrlConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/// # 银联商务退款业务服务
///
/// 通过 [UmsChannelClient] 调用子应用发起银联商务退款。
/// 首期默认按扫码模式退款(QRCODE)。
@Slf4j
@Service
@RequiredArgsConstructor
public class UmsRefundService {

    private final UmsChannelClient umsChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 银联商务退款时间格式
    private static final DateTimeFormatter REFUND_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /// 执行银联商务退款
    public RefundResultBo refund(PayRefundOrder refundOrder, UmsSdkCredential credential) {
        UmsRefundReq req = new UmsRefundReq();
        req.setOutTradeNo(refundOrder.getOrderNo());
        req.setOutRefundNo(refundOrder.getRefundNo());
        req.setRefundAmount(refundOrder.getAmount());
        req.setReason(refundOrder.getReason());
        req.setNotifyUrl(this.buildRefundNotifyUrl(refundOrder));
        // 首期默认扫码退款
        req.setMethod(UmsPayMethod.QRCODE);
        req.setCredential(credential);

        DaxResult<UmsRefundResp> result = umsChannelClient.refund(req);
        if (result.getCode() != 0) {
            log.error("银联商务通道退款失败: refundNo={}, msg={}", refundOrder.getRefundNo(), result.getMsg());
            return new RefundResultBo()
                    .setComplete(false)
                    .setStatus(RefundOrderStatusEnum.FAIL)
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        return toRefundResult(result.getData());
    }

    /// 生成银联商务退款异步通知地址
    private String buildRefundNotifyUrl(PayRefundOrder refundOrder) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new IllegalStateException("平台后端访问地址(backendBaseUrl)未配置, 无法生成银联商务退款回调地址");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/ums/{}/refund",
                base, refundOrder.getMchNo(), refundOrder.getAppId(), refundOrder.getChannelMchNo());
    }

    /// 解析子应用响应
    private RefundResultBo toRefundResult(UmsRefundResp resp) {
        RefundResultBo bo = new RefundResultBo();
        bo.setOutRefundNo(resp.getOutRefundNo());
        // 退款完成时间(yyyy-MM-dd HH:mm:ss → OffsetDateTime)
        if (StrUtil.isNotBlank(resp.getFinishTime())) {
            bo.setFinishTime(OffsetDateTime.parse(resp.getFinishTime(), REFUND_TIME_FORMAT));
        }
        // SUCCESS 退款即时成功; 其他状态需同步查询确认
        if (UmsCode.REFUND_STATUS_SUCCESS.equals(resp.getRefundStatus())) {
            bo.setComplete(true)
                    .setStatus(RefundOrderStatusEnum.SUCCESS);
        } else {
            bo.setComplete(false)
                    .setStatus(RefundOrderStatusEnum.PROGRESS);
        }
        return bo;
    }
}
