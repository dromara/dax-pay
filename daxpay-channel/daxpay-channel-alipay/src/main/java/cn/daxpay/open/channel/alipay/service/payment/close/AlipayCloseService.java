package cn.daxpay.open.channel.alipay.service.payment.close;

import cn.daxpay.open.channel.alipay.client.AlipayChannelClient;
import cn.daxpay.open.channel.alipay.client.credential.AlipaySdkCredential;
import cn.daxpay.open.channel.alipay.client.req.AlipayCloseReq;
import cn.daxpay.open.channel.alipay.client.resp.AlipayCloseResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.core.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 支付宝支付关闭业务服务
///
/// 通过 [AlipayChannelClient] 调用子应用 dax-pay-channel-one 关闭/撤销支付宝订单。
/// 请求构建、结果判定在本类中完成; 关闭失败的网关状态兜底由子应用内部处理。
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayCloseService {

    private final AlipayChannelClient alipayChannelClient;

    /// 执行支付宝订单关闭/撤销
    ///
    /// @param trade      支付订单(tradeNo 作为 out_trade_no, outOrderNo 作为 trade_no)
    /// @param credential 通道调用凭证
    /// @param useCancel  是否使用撤销方式关闭
    /// @return 实际使用的关闭方式([CloseTypeEnum.CLOSE] / [CloseTypeEnum.CANCEL])
    public CloseTypeEnum close(PayTrade trade, AlipaySdkCredential credential, boolean useCancel) {
        // 构建请求
        AlipayCloseReq req = new AlipayCloseReq();
        req.setOutTradeNo(trade.getTradeNo());
        req.setTradeNo(trade.getOutOrderNo());
        req.setUseCancel(useCancel);
        req.setCredential(credential);

        // 调用子应用
        DaxResult<AlipayCloseResp> result = alipayChannelClient.close(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.OPERATION_FAIL, "error.channel.alipay.closeFailed", result.getMsg());
        }

        return useCancel ? CloseTypeEnum.CANCEL : CloseTypeEnum.CLOSE;
    }
}
