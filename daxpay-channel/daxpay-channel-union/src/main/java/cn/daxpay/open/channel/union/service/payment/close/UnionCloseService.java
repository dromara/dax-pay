package cn.daxpay.open.channel.union.service.payment.close;

import cn.daxpay.open.channel.union.client.UnionChannelClient;
import cn.daxpay.open.channel.union.client.credential.UnionSdkCredential;
import cn.daxpay.open.channel.union.client.enums.UnionPayMethod;
import cn.daxpay.open.channel.union.client.req.UnionCloseReq;
import cn.daxpay.open.channel.union.client.resp.UnionCloseResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 云闪付支付关闭业务服务
///
/// 通过 [UnionChannelClient] 调用子应用关闭银联订单。
/// 银联关单(交易类型 31)需原交易查询凭证 queryId, 取自 [PayTrade#getOutOrderNo](支付成功时存入)。
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionCloseService {

    private final UnionChannelClient unionChannelClient;

    /// 执行云闪付订单关闭
    public CloseTypeEnum close(PayTrade trade, UnionSdkCredential credential, boolean useCancel, UnionPayMethod method) {
        UnionCloseReq req = new UnionCloseReq();
        req.setOutTradeNo(trade.getTradeNo());
        // queryId 取自原交易通道订单号(支付成功/同步时存入 trade.outOrderNo)
        req.setQueryId(trade.getOutOrderNo());
        req.setMethod(method);
        req.setCredential(credential);

        DaxResult<UnionCloseResp> result = unionChannelClient.close(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.OPERATION_FAIL, "error.channel.union.closeFailed", result.getMsg());
        }
        return useCancel ? CloseTypeEnum.CANCEL : CloseTypeEnum.CLOSE;
    }
}
