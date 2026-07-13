package cn.daxpay.open.channel.dougong.service.payment;

import cn.daxpay.open.channel.dougong.client.DougongChannelClient;
import cn.daxpay.open.channel.dougong.client.credential.DougongSdkCredential;
import cn.daxpay.open.channel.dougong.client.req.DougongRefundReq;
import cn.daxpay.open.channel.dougong.client.resp.DougongRefundResp;
import cn.daxpay.open.payment.common.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayRefundOrder;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/// # 斗拱服务商退款执行业务服务
///
/// 通过 [DougongChannelClient] 调用子应用完成斗拱(汇付)退款。
@Slf4j
@Service
@RequiredArgsConstructor
public class DougongRefundService {

    /// 汇付纯日期格式(yyyyMMdd)
    private static final DateTimeFormatter PURE_DATE = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final DougongChannelClient dougongChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行退款
    public RefundResultBo refund(PayRefundOrder refundOrder, DougongSdkCredential credential) {
        DougongRefundReq req = new DougongRefundReq();
        req.setCredential(credential);
        // 退款单号(作为汇付 reqSeqId)
        req.setOutRefundNo(refundOrder.getRefundNo());
        // 原汇付支付流水号(原支付 hf_seq_id)
        req.setTradeNo(refundOrder.getOutOrderNo());
        // 原请求日期(取退款订单创建时间近似, 汇付主要靠 org_hf_seq_id 定位)
        req.setOrgReqDate(formatPureDate(refundOrder.getCreateTime()));
        req.setAmount(refundOrder.getAmount());
        req.setReason(refundOrder.getReason());
        req.setNotifyUrl(this.buildRefundNotifyUrl(refundOrder));

        DaxResult<DougongRefundResp> result = dougongChannelClient.refund(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "channel.error.dougongRefundFailed", result.getMsg());
        }

        DougongRefundResp resp = result.getData();
        boolean complete = Boolean.TRUE.equals(resp.getComplete());
        RefundResultBo bo = new RefundResultBo()
                .setOutRefundNo(resp.getTradeNo())
                .setFinishTime(resp.getFinishTime())
                .setComplete(complete);
        // 退款状态: 同步成功(S) 视为 SUCCESS, 否则处理中(待异步通知或同步确认)
        bo.setStatus(complete ? RefundOrderStatusEnum.SUCCESS : RefundOrderStatusEnum.PROGRESS);
        return bo;
    }

    /// 生成斗拱退款异步通知地址(汇付→平台)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{appId}/dougong/refund`
    private String buildRefundNotifyUrl(PayRefundOrder refundOrder) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            return null;
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/dougong/refund",
                base, refundOrder.getMchNo(), refundOrder.getAppId());
    }

    /// OffsetDateTime → yyyyMMdd(东八区)
    private String formatPureDate(OffsetDateTime time) {
        return time == null ? null : time.toLocalDate().format(PURE_DATE);
    }
}
