package cn.daxpay.open.channel.xingyi.service.payment.refund;

import cn.daxpay.open.channel.xingyi.client.XingyiChannelClient;
import cn.daxpay.open.channel.xingyi.client.credential.XingyiSdkCredential;
import cn.daxpay.open.channel.xingyi.client.req.XingyiRefundReq;
import cn.daxpay.open.channel.xingyi.client.resp.XingyiRefundResp;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 星驿付退款业务服务
///
/// 通过 [XingyiChannelClient] 调用子应用发起星驿付退款。
/// 星驿付退款为同步受理即成功模式(对齐参考插件能力, 无退款异步通知), 受理即置终态成功。
///
/// reOrderNo 传原支付的星驿付订单号(RefundOrder.outOrderNo); 原单通道单号未知时
/// 通道侧将拒绝退款, 建议等待支付回调回填后再发起。
@Slf4j
@Service
@RequiredArgsConstructor
public class XingyiRefundService {

    private final XingyiChannelClient xingyiChannelClient;

    /// 执行星驿付退款
    public RefundResultBo refund(RefundOrder refundOrder, XingyiSdkCredential credential) {
        XingyiRefundReq req = new XingyiRefundReq();
        req.setCredential(credential);
        // 退款单号(作为星驿付 orderNo 上送)
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        // 原支付订单的星驿付订单号
        req.setOriginOutOrderNo(refundOrder.getOutOrderNo());
        req.setAmount(refundOrder.getAmount());
        // 渠道标记(对齐参考插件 tag 参数: 原支付渠道类型)
        req.setTag(this.mapTag(refundOrder.getCapability()));

        DaxResult<XingyiRefundResp> result = xingyiChannelClient.refund(req);
        if (result.getCode() != 0) {
            // 星驿付退款异常
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.xingyi.refundFailed", result.getMsg());
        }

        XingyiRefundResp resp = result.getData();
        RefundResultBo bo = new RefundResultBo()
                .setOutRefundNo(resp.getTradeNo())
                .setComplete(Boolean.TRUE.equals(resp.getComplete()))
                .setRefundAmount(resp.getRefundAmount());
        // 星驿付退款同步受理即成功
        bo.setStatus(Boolean.TRUE.equals(resp.getComplete())
                ? RefundOrderStatusEnum.SUCCESS
                : RefundOrderStatusEnum.PROGRESS);
        return bo;
    }

    /// 原支付能力编码 → 参考插件 tag 参数(渠道类型标记: wxpay/alipay/bank)
    private String mapTag(String capability) {
        if (capability == null) {
            return null;
        }
        if (capability.startsWith("wechat")) {
            return "wxpay";
        }
        if (capability.startsWith("alipay")) {
            return "alipay";
        }
        if (capability.startsWith("union")) {
            return "bank";
        }
        return null;
    }
}
