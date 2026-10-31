package cn.daxpay.open.channel.mock.service;

import cn.daxpay.open.channel.mock.client.MockChannelClient;
import cn.daxpay.open.channel.mock.client.req.MockCloseReq;
import cn.daxpay.open.channel.mock.client.resp.MockCloseResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Mock 通道支付关闭业务服务
///
/// 通过 [MockChannelClient] 调用独立 mock 子应用关闭 mock 订单;
/// 已支付订单关闭、订单不存在等由 mock 侧返回业务失败(DaxResult 透传)。
/// mock 侧不区分关闭/撤销, useCancel 仅透传决定返回的关闭类型。
@Slf4j
@Service
@RequiredArgsConstructor
public class MockCloseService {

    private final MockChannelClient mockChannelClient;
    private final MockCredentialSupport mockCredentialSupport;

    /// 执行 mock 订单关闭
    ///
    /// @param trade     支付订单(tradeNo 作为商户订单号透传)
    /// @param useCancel 是否使用撤销方式关闭(mock 不区分, 仅透传决定返回的关闭类型)
    /// @return 实际使用的关闭方式([CloseTypeEnum#CLOSE] / [CloseTypeEnum#CANCEL])
    public CloseTypeEnum close(PayTrade trade, boolean useCancel) {
        // 构建请求(支付单无 product, 凭证走通道商户号反查商户级两件套)
        MockCloseReq req = new MockCloseReq();
        req.setOutTradeNo(trade.getTradeNo());
        req.setCredential(mockCredentialSupport.build(trade.getChannelMchNo()));

        // 调用 mock 子应用
        DaxResult<MockCloseResp> result = mockChannelClient.close(req);
        if (result.getCode() != 0) {
            // 模拟通道关闭订单异常
            throw new BizInfoException(DaxPayErrorCode.OPERATION_FAIL, "error.channel.mock.closeFailed", result.getMsg());
        }

        return useCancel ? CloseTypeEnum.CANCEL : CloseTypeEnum.CLOSE;
    }
}
