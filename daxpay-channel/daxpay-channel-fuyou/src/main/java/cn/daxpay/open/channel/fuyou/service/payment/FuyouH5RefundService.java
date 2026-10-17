package cn.daxpay.open.channel.fuyou.service.payment;

import cn.daxpay.open.channel.fuyou.client.FuyouH5ChannelClient;
import cn.daxpay.open.channel.fuyou.client.credential.FuyouRsaSdkCredential;
import cn.daxpay.open.channel.fuyou.client.req.FuyouH5RefundReq;
import cn.daxpay.open.channel.fuyou.client.resp.FuyouRefundResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.runtime.service.PayTradeContainerFields;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 富友 H5 聚合收银台退款执行业务服务
///
/// 通过 [FuyouH5ChannelClient] 调用子应用完成富友收银台退款(aggwapRefund)。
/// 受理成功即返回退款金额(参考实现按同步终态处理), 本服务将受理成功直接映射为退款成功(complete=true);
/// 退款结果异步通知(back_notify_url)仅作兜底核对, 无独立退款查询接口、无退款同步策略。
///
/// 退款需原支付下单日期(pay_order_date)+ 原商户订单号(pay_order_id),
/// 通过 [PayTradeManager] 凭 refundOrder.orderNo(tradeNo) 反查原支付订单。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouH5RefundService {

    private final FuyouH5ChannelClient fuyouH5ChannelClient;
    private final PayTradeManager payTradeManager;
    private final PayTradeContainerFields payTradeContainerFields;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行退款
    public RefundResultBo refund(RefundOrder refundOrder, FuyouRsaSdkCredential credential) {
        // 反查原支付订单(富友退款需原订单商户订单号 + 原下单日期)
        PayTrade originOrder = payTradeManager.findByTradeNo(refundOrder.getTradeNo())
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.originOrderNotExist"));

        FuyouH5RefundReq req = new FuyouH5RefundReq();
        req.setCredential(credential);
        var fields = payTradeContainerFields.resolve(originOrder);
        // 原支付订单的商户订单号(富友 pay_order_id = 平台交易号)
        req.setRelationOrderNo(fields.relationOrderNo());
        // 退款单号(平台退款关联号, 作为富友 refund_order_id)
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        // 退款金额(单位: 分)
        req.setRefundAmount(refundOrder.getAmount());
        // 原支付下单时间(富友 pay_order_date)
        req.setOriginOrderTime(originOrder.getCreateTime());
        // 退款结果异步通知地址(富友→平台)
        req.setNotifyUrl(this.buildRefundNotifyUrl(refundOrder));

        DaxResult<FuyouRefundResp> result = fuyouH5ChannelClient.refund(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.fuyou.refundFailed", result.getMsg());
        }

        FuyouRefundResp resp = result.getData();
        // 富友收银台退款为同步终态, 受理成功即退款成功
        return new RefundResultBo()
                .setOutRefundNo(resp.getOutRefundId())
                .setStatus(RefundOrderStatusEnum.SUCCESS)
                .setComplete(true)
                .setRefundAmount(refundOrder.getAmount());
    }

    /// 生成富友 H5 收银台退款异步通知地址(富友→平台)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/fuyou/h5/refund`
    private String buildRefundNotifyUrl(RefundOrder refundOrder) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/fuyou/h5/refund",
                base, refundOrder.getMchNo(), refundOrder.getChannelMchNo());
    }
}
