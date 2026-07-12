package cn.daxpay.open.channel.alipay.service.payment.sync;

import cn.daxpay.open.channel.alipay.client.AlipayChannelClient;
import cn.daxpay.open.channel.alipay.client.credential.AlipaySdkCredential;
import cn.daxpay.open.channel.alipay.client.req.AlipaySyncReq;
import cn.daxpay.open.channel.alipay.client.resp.AlipaySyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.common.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.core.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.core.trade.order.entity.PayTrade;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 支付宝支付同步业务服务
///
/// 通过 [AlipayChannelClient] 调用子应用 dax-pay-channel-one 查询支付宝订单状态,
/// 将支付宝 trade_status 映射为平台 [PayFundStatusEnum]。
///
/// 映射规则(参照商业版 AlipaySyncService):
/// - TRADE_SUCCESS / TRADE_FINISHED → SUCCESS
/// - WAIT_BUYER_PAY → PROCESSING
/// - TRADE_CLOSED + 有付款时间 → SUCCESS(已退款)
/// - TRADE_CLOSED + 无付款时间 → CLOSE
/// - ACQ.TRADE_NOT_EXIST → PROCESSING(客户未操作, 订单未创建)
/// - 其他查询失败 → syncSuccess=false
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipaySyncService {

    private final AlipayChannelClient alipayChannelClient;

    /// 支付宝交易状态: 交易支付成功
    private static final String TRADE_SUCCESS = "TRADE_SUCCESS";
    /// 支付宝交易状态: 交易结束(不可退款)
    private static final String TRADE_FINISHED = "TRADE_FINISHED";
    /// 支付宝交易状态: 等待买家付款
    private static final String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
    /// 支付宝交易状态: 未付款超时关闭, 或支付完成后全额退款
    private static final String TRADE_CLOSED = "TRADE_CLOSED";
    /// 支付宝业务码: 交易不存在
    private static final String ACQ_TRADE_NOT_EXIST = "ACQ.TRADE_NOT_EXIST";

    /// 执行支付宝支付同步
    ///
    /// @param trade      支付订单(tradeNo 作为 out_trade_no, outOrderNo 作为 trade_no)
    /// @param credential 通道调用凭证
    /// @return 同步结果(含映射后的资金状态)
    public PaySyncResultBo sync(PayTrade trade, AlipaySdkCredential credential) {
        // 构建请求
        AlipaySyncReq req = new AlipaySyncReq();
        req.setOutTradeNo(trade.getTradeNo());
        req.setTradeNo(trade.getOutOrderNo());
        req.setCredential(credential);

        // 调用子应用
        DaxResult<AlipaySyncResp> result = alipayChannelClient.sync(req);
        if (result.getCode() != 0) {
            log.error("支付宝通道同步失败: outTradeNo={}, msg={}", trade.getTradeNo(), result.getMsg());
            PaySyncResultBo bo = new PaySyncResultBo();
            bo.setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
            return bo;
        }

        return toSyncResult(result.getData());
    }

    /// 解析子应用响应, 映射 trade_status → [PayFundStatusEnum]
    private PaySyncResultBo toSyncResult(AlipaySyncResp resp) {
        PaySyncResultBo bo = new PaySyncResultBo();
        bo.setOutOrderNo(resp.getTradeNo());

        String tradeStatus = resp.getTradeStatus();

        // 支付完成(TRADE_SUCCESS / TRADE_FINISHED)
        if (TRADE_SUCCESS.equals(tradeStatus) || TRADE_FINISHED.equals(tradeStatus)) {
            return bo.setPayStatus(PayFundStatusEnum.SUCCESS)
                    .setFinishTime(resp.getSendPayDate())
                    .setRealAmount(resp.getBuyerPayAmount())
                    .setBuyerId(StrUtil.blankToDefault(resp.getBuyerOpenId(), resp.getBuyerUserId()));
        }

        // 待支付(等待买家付款)
        if (WAIT_BUYER_PAY.equals(tradeStatus)) {
            return bo.setPayStatus(PayFundStatusEnum.PROCESSING);
        }

        // 已关闭或支付完成后全额退款
        if (TRADE_CLOSED.equals(tradeStatus)) {
            // 有付款时间说明是支付后全额退款 → SUCCESS; 无付款时间 → CLOSE
            if (resp.getSendPayDate() != null) {
                return bo.setPayStatus(PayFundStatusEnum.SUCCESS)
                        .setFinishTime(resp.getSendPayDate());
            }
            return bo.setPayStatus(PayFundStatusEnum.CLOSE);
        }

        // 交易不存在(客户未操作, 订单未创建) → PROCESSING
        if (ACQ_TRADE_NOT_EXIST.equals(resp.getSubCode())) {
            return bo.setPayStatus(PayFundStatusEnum.PROCESSING);
        }

        // 查询失败
        return bo.setSyncSuccess(false)
                .setSyncErrorCode(resp.getSubCode())
                .setSyncErrorMsg(StrUtil.blankToDefault(resp.getSubMsg(), "支付宝同步查询失败"));
    }
}
