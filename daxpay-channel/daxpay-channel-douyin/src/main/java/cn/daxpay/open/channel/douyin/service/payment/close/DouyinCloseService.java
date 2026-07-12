package cn.daxpay.open.channel.douyin.service.payment.close;

import cn.daxpay.open.channel.douyin.client.DouyinChannelClient;
import cn.daxpay.open.channel.douyin.client.credential.DouyinSdkCredential;
import cn.daxpay.open.channel.douyin.client.req.DouyinCloseReq;
import cn.daxpay.open.channel.douyin.client.resp.DouyinCloseResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.core.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 抖音支付关闭业务服务
///
/// 通过 [DouyinChannelClient] 调用子应用 dax-pay-channel-one 关闭抖音订单。
/// 抖音关单接口不区分撤销/关闭, 统一为 CLOSE。
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinCloseService {

    private final DouyinChannelClient douyinChannelClient;

    /// 执行抖音订单关闭
    public CloseTypeEnum close(PayTrade trade, DouyinSdkCredential credential, boolean useCancel) {
        DouyinCloseReq req = new DouyinCloseReq();
        req.setOutTradeNo(trade.getTradeNo());
        req.setCredential(credential);

        DaxResult<DouyinCloseResp> result = douyinChannelClient.close(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.OPERATION_FAIL, "error.channel.douyin.closeFailed", result.getMsg());
        }

        // 抖音只有关单接口, 不区分撤销, 统一返回 CLOSE
        return useCancel ? CloseTypeEnum.CANCEL : CloseTypeEnum.CLOSE;
    }
}
