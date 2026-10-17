package cn.daxpay.open.channel.ysep.service.payment;

import cn.daxpay.open.channel.ysep.client.YsepChannelClient;
import cn.daxpay.open.channel.ysep.client.credential.YsepSdkCredential;
import cn.daxpay.open.channel.ysep.client.req.YsepCloseReq;
import cn.daxpay.open.channel.ysep.client.resp.YsepCloseResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/// # 银盛关单业务服务(两产品共用)
///
/// 通过 [YsepChannelClient] 调用子应用关闭银盛订单:
/// - ysep_pay 走 ysepay.online.trade.close(openapi 网关)
/// - yseqt_pay 走 serviceNo=closeOrder
@Slf4j
@Service
@RequiredArgsConstructor
public class YsepCloseService {

    private final YsepChannelClient ysepChannelClient;

    /// 关闭订单
    public CloseTypeEnum close(PayTrade order, YsepSdkCredential credential, ProductEnum product) {
        YsepCloseReq req = new YsepCloseReq();
        req.setCredential(credential);
        req.setProduct(product.getCode());
        req.setOutTradeNo(order.getTradeNo());
        req.setTradeNo(order.getOutOrderNo());
        // 开放平台族 shopdate 须与下单时一致(订单创建日, 东八区)
        if (Objects.nonNull(order.getCreateTime())) {
            req.setTradeDate(order.getCreateTime()
                    .atZoneSameInstant(ZoneOffset.ofHours(8))
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        }

        DaxResult<YsepCloseResp> result = ysepChannelClient.close(req);
        if (result.getCode() != 0) {
            // 银盛关闭订单异常
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.ysep.closeFailed", result.getMsg());
        }
        return CloseTypeEnum.CLOSE;
    }
}
