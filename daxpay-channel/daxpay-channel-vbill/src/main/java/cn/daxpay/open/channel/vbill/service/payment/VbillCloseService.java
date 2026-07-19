package cn.daxpay.open.channel.vbill.service.payment;

import cn.daxpay.open.channel.vbill.client.VbillChannelClient;
import cn.daxpay.open.channel.vbill.client.credential.VbillSdkCredential;
import cn.daxpay.open.channel.vbill.client.req.VbillCloseReq;
import cn.daxpay.open.channel.vbill.client.resp.VbillCloseResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 随行付服务商关单业务服务
///
/// 通过 [VbillChannelClient] 调用子应用关闭随行付订单(`/query/close`)。
/// 随行付仅提供关单接口, 无撤销接口, useCancel 参数忽略。
@Slf4j
@Service
@RequiredArgsConstructor
public class VbillCloseService {

    private final VbillChannelClient vbillChannelClient;

    /// 关闭订单
    ///
    /// @param order      支付订单
    /// @param credential 通道凭证
    /// @param useCancel  是否撤销(随行付不支持撤销, 忽略此参数)
    /// @return 关闭类型(恒为 CLOSE)
    public CloseTypeEnum close(PayTrade order, VbillSdkCredential credential, boolean useCancel) {
        VbillCloseReq req = new VbillCloseReq();
        req.setCredential(credential);
        // 随行付关单凭网关订单号(uuid)
        req.setOutOrderNo(order.getOutOrderNo());

        DaxResult<VbillCloseResp> result = vbillChannelClient.close(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.vbill.closeFailed", result.getMsg());
        }
        return CloseTypeEnum.CLOSE;
    }
}
