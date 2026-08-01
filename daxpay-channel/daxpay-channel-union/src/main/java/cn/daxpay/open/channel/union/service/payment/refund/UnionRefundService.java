package cn.daxpay.open.channel.union.service.payment.refund;

import cn.daxpay.open.channel.union.client.UnionChannelClient;
import cn.daxpay.open.channel.union.client.credential.UnionSdkCredential;
import cn.daxpay.open.channel.union.client.enums.UnionPayMethod;
import cn.daxpay.open.channel.union.client.req.UnionRefundReq;
import cn.daxpay.open.channel.union.client.resp.UnionRefundResp;
import cn.daxpay.open.channel.union.code.UnionCode;
import cn.daxpay.open.channel.union.util.UnionDateUtil;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 云闪付退款业务服务
///
/// 通过 [UnionChannelClient] 调用子应用发起银联退货(交易类型 04)。
/// 银联退货必须传入原交易查询凭证 origQueryId, 取自原交易 [PayTrade#getOutOrderNo](queryId)。
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionRefundService {

    private final UnionChannelClient unionChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;
    private final PayTradeManager payTradeManager;

    /// 执行云闪付退款
    public RefundResultBo refund(RefundOrder refundOrder, UnionSdkCredential credential, UnionPayMethod method) {
        // 银联退货需 origQryId(原交易查询凭证), 从原交易 outOrderNo 取
        PayTrade trade = payTradeManager.findByTradeNo(refundOrder.getTradeNo()).orElse(null);
        String origQueryId = trade == null ? null : trade.getOutOrderNo();
        if (StrUtil.isBlank(origQueryId)) {
            log.error("云闪付退款未查到原交易凭证(origQryId), tradeNo={}", refundOrder.getTradeNo());
            return new RefundResultBo()
                    .setComplete(false)
                    .setStatus(RefundOrderStatusEnum.FAIL)
                    .setSyncSuccess(false)
                    .setSyncErrorMsg("原交易未完成或未同步, 缺少银联交易凭证 queryId");
        }

        UnionRefundReq req = new UnionRefundReq();
        req.setOutTradeNo(refundOrder.getTradeNo());
        req.setOrigQueryId(origQueryId);
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        req.setRefundAmount(refundOrder.getAmount());
        req.setNotifyUrl(this.buildRefundNotifyUrl(refundOrder));
        req.setMethod(method);
        req.setCredential(credential);

        DaxResult<UnionRefundResp> result = unionChannelClient.refund(req);
        if (result.getCode() != 0) {
            log.error("云闪付通道退款失败: refundNo={}, msg={}", refundOrder.getRefundNo(), result.getMsg());
            return new RefundResultBo()
                    .setComplete(false)
                    .setStatus(RefundOrderStatusEnum.FAIL)
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }
        return toRefundResult(result.getData());
    }

    /// 生成云闪付退款异步通知地址
    private String buildRefundNotifyUrl(RefundOrder refundOrder) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new IllegalStateException("平台后端访问地址(backendBaseUrl)未配置, 无法生成云闪付退款回调地址");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/union/refund",
                base, refundOrder.getMchNo(), refundOrder.getChannelMchNo());
    }

    /// 解析子应用响应
    private RefundResultBo toRefundResult(UnionRefundResp resp) {
        RefundResultBo bo = new RefundResultBo();
        bo.setOutRefundNo(resp.getOutRefundNo());
        bo.setFinishTime(UnionDateUtil.parseCst(resp.getFinishTime()));
        // SUCCESS 退款即时成功; 其他状态需同步查询确认
        if (UnionCode.REFUND_STATUS_SUCCESS.equals(resp.getRefundStatus())) {
            bo.setComplete(true).setStatus(RefundOrderStatusEnum.SUCCESS);
        } else {
            bo.setComplete(false).setStatus(RefundOrderStatusEnum.PROGRESS);
        }
        return bo;
    }
}
