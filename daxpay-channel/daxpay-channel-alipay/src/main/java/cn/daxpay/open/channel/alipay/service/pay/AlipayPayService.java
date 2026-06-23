package cn.daxpay.open.channel.alipay.service.pay;

import cn.daxpay.open.channel.alipay.client.AlipayChannelClient;
import cn.daxpay.open.channel.alipay.dto.AlipayPayReq;
import cn.daxpay.open.channel.alipay.dto.AlipayPayResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.pay.bo.PayTradeResultBo;
import cn.daxpay.open.payment.pay.order.entity.PayTrade;
import cn.daxpay.open.payment.unipay.param.trade.pay.PayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.unipay.PayBodyTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/// # 支付宝支付执行业务服务
///
/// 通过 [AlipayChannelClient] 调用子应用 dax-pay-channel-one 完成支付宝支付下单。
/// 请求构建、响应解析全部在本类中完成。
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayPayService {

    private static final BigDecimal HUNDRED = new BigDecimal("100");

    private final AlipayChannelClient alipayChannelClient;

    /// 执行支付宝支付
    ///
    /// @param order    支付订单
    /// @param payParam 支付参数
    /// @param config   通道调用配置(密钥/证书/回调地址等)
    /// @return 支付结果
    public PayTradeResultBo pay(PayTrade order, PayParam payParam, Map<String, Object> config) {
        // 构建请求
        AlipayPayReq req = new AlipayPayReq();
        req.setChannel("alipay");
        // 使用支付交易号作为商户订单号透传给支付宝, 回调时凭此反查 PayTrade
        req.setBizOrderNo(order.getTradeNo());
        req.setAmount(payParam.getAmount().multiply(HUNDRED).longValue());
        req.setSubject(payParam.getTitle());
        req.setDescription(payParam.getDescription());
        // 将平台支付方式(PayMethodEnum code)映射为支付宝通道识别码
        req.setMethod(mapMethod(payParam.getMethod()));
        req.setConfig(config);

        // 调用子应用
        DaxResult<AlipayPayResp> result = alipayChannelClient.pay(req);
        if (result.getCode() != 0) {
            throw new IllegalStateException("支付宝通道支付失败: " + result.getMsg());
        }

        return toPayResult(result.getData());
    }

    /// 平台支付方式([PayMethodEnum] code) -> 支付宝通道识别码
    private static String mapMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            case ALIPAY_PC -> "alipay_page";
            case ALIPAY_H5 -> "alipay_wap";
            case ALIPAY_APP -> "alipay_app";
            case ALIPAY_QR, ALIPAY_ORDER_QR -> "alipay_qr";
            default -> throw new UnsupportedOperationException(
                    "暂不支持的支付宝支付方式: " + methodCode);
        };
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(AlipayPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                .setOutOrderNo(resp.getOutOrderNo())
                .setComplete(Boolean.TRUE.equals(resp.getComplete()))
                .setPayBody(resp.getPayBody())
                .setTransOrderNo(resp.getTransOrderNo());

        // 支付参数体类型
        String payBodyType = resp.getPayBodyType();
        if (payBodyType != null) {
            for (PayBodyTypeEnum type : PayBodyTypeEnum.values()) {
                if (type.getCode().equals(payBodyType)) {
                    bo.setPayBodyType(type);
                    break;
                }
            }
        }

        // 完成时间
        String finishTime = resp.getFinishTime();
        if (finishTime != null && !finishTime.isBlank()) {
            bo.setFinishTime(OffsetDateTime.parse(finishTime, DateTimeFormatter.ISO_DATE_TIME));
        }

        return bo;
    }
}
