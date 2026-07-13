package cn.daxpay.open.channel.wechat.service.payment.isv;

import cn.daxpay.open.channel.wechat.client.WechatChannelClient;
import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.client.req.WechatSyncReq;
import cn.daxpay.open.channel.wechat.client.resp.WechatSyncResp;
import cn.daxpay.open.payment.common.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 微信服务商支付同步业务服务
///
/// 通过 [WechatChannelClient] 调用子应用 dax-pay-channel-one 服务商端点查询微信订单状态,
/// 将微信 trade_state 映射为平台 [PayFundStatusEnum]。映射规则与直连模式一致。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatIsvSyncService {

    private final WechatChannelClient wechatChannelClient;

    /// 微信交易状态
    private static final String STATE_SUCCESS = "SUCCESS";
    private static final String STATE_REFUND = "REFUND";
    private static final String STATE_NOTPAY = "NOTPAY";
    private static final String STATE_USERPAYING = "USERPAYING";
    private static final String STATE_CLOSED = "CLOSED";
    private static final String STATE_REVOKED = "REVOKED";
    private static final String STATE_PAYERROR = "PAYERROR";
    private static final String STATE_ACCEPT = "ACCEPT";

    /// 执行微信服务商支付同步
    public PaySyncResultBo sync(PayTrade trade, WechatSdkCredential credential) {
        // 构建请求(与直连一致)
        WechatSyncReq req = new WechatSyncReq();
        req.setOutTradeNo(trade.getTradeNo());
        req.setTransactionId(trade.getOutOrderNo());
        req.setCredential(credential);

        // 调用子应用服务商端点
        DaxResult<WechatSyncResp> result = wechatChannelClient.isvSync(req);
        if (result.getCode() != 0) {
            log.error("微信服务商通道同步失败: outTradeNo={}, msg={}", trade.getTradeNo(), result.getMsg());
            PaySyncResultBo bo = new PaySyncResultBo();
            bo.setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
            return bo;
        }

        return toSyncResult(result.getData());
    }

    /// 解析子应用响应, 映射 trade_state → [PayFundStatusEnum]
    private PaySyncResultBo toSyncResult(WechatSyncResp resp) {
        PaySyncResultBo bo = new PaySyncResultBo();
        bo.setOutOrderNo(resp.getTransactionId());

        String tradeState = resp.getTradeState();

        // 支付成功(REFUND 表示原订单已支付, 后续发起退款, 资金状态视为 SUCCESS)
        if (STATE_SUCCESS.equals(tradeState) || STATE_REFUND.equals(tradeState)) {
            return bo.setPayStatus(PayFundStatusEnum.SUCCESS)
                    .setFinishTime(resp.getSuccessTime())
                    .setRealAmount(resp.getPayerTotal())
                    .setAmount(resp.getTotalAmount())
                    .setBuyerId(resp.getOpenId());
        }

        // 待支付 / 用户支付中 / 已接收(等待扣款)
        if (STATE_NOTPAY.equals(tradeState) || STATE_USERPAYING.equals(tradeState) || STATE_ACCEPT.equals(tradeState)) {
            return bo.setPayStatus(PayFundStatusEnum.PROCESSING);
        }

        // 已关闭
        if (STATE_CLOSED.equals(tradeState)) {
            return bo.setPayStatus(PayFundStatusEnum.CLOSE);
        }

        // 已撤销(付款码支付撤销)
        if (STATE_REVOKED.equals(tradeState)) {
            return bo.setPayStatus(PayFundStatusEnum.CANCEL);
        }

        // 支付失败
        if (STATE_PAYERROR.equals(tradeState)) {
            return bo.setPayStatus(PayFundStatusEnum.FAIL);
        }

        // 未知状态
        return bo.setSyncSuccess(false)
                .setSyncErrorMsg(StrUtil.blankToDefault(resp.getTradeStateDesc(), "微信服务商同步查询未知状态: " + tradeState));
    }
}
