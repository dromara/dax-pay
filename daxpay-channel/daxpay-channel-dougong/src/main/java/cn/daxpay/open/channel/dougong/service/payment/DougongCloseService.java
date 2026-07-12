package cn.daxpay.open.channel.dougong.service.payment;

import cn.daxpay.open.channel.dougong.client.DougongChannelClient;
import cn.daxpay.open.channel.dougong.client.credential.DougongSdkCredential;
import cn.daxpay.open.channel.dougong.client.req.DougongCloseReq;
import cn.daxpay.open.channel.dougong.client.resp.DougongCloseResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.core.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/// # 斗拱服务商关单业务服务
///
/// 通过 [DougongChannelClient] 调用子应用关闭斗拱(汇付)订单。
/// 汇付仅提供关单接口(V2TradePaymentScanpayCloseRequest), 无撤销接口, useCancel 参数忽略。
@Slf4j
@Service
@RequiredArgsConstructor
public class DougongCloseService {

    /// 汇付纯日期格式(yyyyMMdd)
    private static final DateTimeFormatter PURE_DATE = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final DougongChannelClient dougongChannelClient;

    /// 关闭订单
    ///
    /// @param order      支付订单
    /// @param credential 通道凭证
    /// @param useCancel  是否撤销(汇付不支持撤销, 忽略此参数)
    /// @param clientIp   客户端IP(汇付关单不强制, 保留参数)
    /// @return 关闭类型(恒为 CLOSE)
    public CloseTypeEnum close(PayTrade order, DougongSdkCredential credential, boolean useCancel, String clientIp) {
        DougongCloseReq req = new DougongCloseReq();
        req.setCredential(credential);
        // 商户订单号(原 reqSeqId)
        req.setOutTradeNo(order.getTradeNo());
        // 原汇付流水号
        req.setTradeNo(order.getOutOrderNo());
        // 原请求日期(取下单时间, 东八区 yyyyMMdd)
        req.setOrgReqDate(formatPureDate(order.getCreateTime()));

        DaxResult<DougongCloseResp> result = dougongChannelClient.close(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "channel.error.dougongCloseFailed", result.getMsg());
        }
        return CloseTypeEnum.CLOSE;
    }

    /// OffsetDateTime → yyyyMMdd(东八区)
    private String formatPureDate(OffsetDateTime time) {
        return time == null ? null : time.toLocalDate().format(PURE_DATE);
    }
}
