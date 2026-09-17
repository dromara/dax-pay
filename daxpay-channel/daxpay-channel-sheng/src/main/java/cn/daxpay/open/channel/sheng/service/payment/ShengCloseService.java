package cn.daxpay.open.channel.sheng.service.payment;

import cn.daxpay.open.channel.sheng.client.ShengChannelClient;
import cn.daxpay.open.channel.sheng.client.credential.ShengSdkCredential;
import cn.daxpay.open.channel.sheng.client.req.ShengCloseReq;
import cn.daxpay.open.channel.sheng.client.resp.ShengCloseResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 盛付通关单业务服务
///
/// 通过 [ShengChannelClient] 调用子应用关闭/撤销盛付通订单。
/// useCancel 语义透传给子应用: true=当日撤单(reverseOrder), false=关单(closeOrder)。
@Slf4j
@Service
@RequiredArgsConstructor
public class ShengCloseService {

    private final ShengChannelClient shengChannelClient;

    /// 关闭订单
    ///
    /// @param order     支付订单
    /// @param credential 通道凭证
    /// @param useCancel 是否撤单(true=当日撤销 reverseOrder, false=关单 closeOrder)
    /// @return 关闭类型(撤单返回 CANCEL, 关单返回 CLOSE)
    public CloseTypeEnum close(PayTrade order, ShengSdkCredential credential, boolean useCancel) {
        ShengCloseReq req = new ShengCloseReq();
        req.setCredential(credential);
        req.setUseCancel(useCancel);
        req.setOriginOutTradeNo(order.getTradeNo());
        req.setOriginTradeNo(order.getOutOrderNo());

        DaxResult<ShengCloseResp> result = shengChannelClient.close(req);
        if (result.getCode() != 0) {
            // 盛付通关闭/撤销订单异常
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.sheng.closeFailed", result.getMsg());
        }
        // 撤单返回 CANCEL, 关单返回 CLOSE
        return useCancel ? CloseTypeEnum.CANCEL : CloseTypeEnum.CLOSE;
    }
}
