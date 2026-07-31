package cn.daxpay.open.channel.ums.service.payment.close;

import cn.daxpay.open.channel.ums.client.UmsChannelClient;
import cn.daxpay.open.channel.ums.client.credential.UmsSdkCredential;
import cn.daxpay.open.channel.ums.client.enums.UmsPayMethod;
import cn.daxpay.open.channel.ums.client.req.UmsCloseReq;
import cn.daxpay.open.channel.ums.client.resp.UmsCloseResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 银联商务支付关闭业务服务
///
/// 通过 [UmsChannelClient] 调用子应用关闭银联商务订单。
/// 支付方式(method)由产品执行策略按所属产品决定: 扫码类(UMS_QRCODE/UMS_BARCODE)走 bills 关单,
/// H5 类(UMS_H5/UMS_JSAPI/UMS_MINI/UMS_APP)走 netpay 关单, 子应用按 method 二分接口。
@Slf4j
@Service
@RequiredArgsConstructor
public class UmsCloseService {

    private final UmsChannelClient umsChannelClient;

    /// 执行银联商务订单关闭
    public CloseTypeEnum close(PayTrade trade, UmsSdkCredential credential, boolean useCancel, UmsPayMethod method) {
        UmsCloseReq req = new UmsCloseReq();
        req.setOutTradeNo(trade.getTradeNo());
        // 支付方式由产品策略决定(扫码类走 bills 关单, H5 类走 netpay 关单)
        req.setMethod(method);
        req.setCredential(credential);

        DaxResult<UmsCloseResp> result = umsChannelClient.close(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.OPERATION_FAIL, "error.channel.ums.closeFailed", result.getMsg());
        }

        return useCancel ? CloseTypeEnum.CANCEL : CloseTypeEnum.CLOSE;
    }
}
