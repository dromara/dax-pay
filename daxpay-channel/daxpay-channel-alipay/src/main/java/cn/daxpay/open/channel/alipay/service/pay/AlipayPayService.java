package cn.daxpay.open.channel.alipay.service.pay;

import cn.daxpay.open.channel.alipay.client.AlipayChannelClient;
import cn.daxpay.open.channel.alipay.client.credential.AlipaySdkCredential;
import cn.daxpay.open.channel.alipay.client.enums.AlipayPayBodyType;
import cn.daxpay.open.channel.alipay.client.enums.AlipayPayMethod;
import cn.daxpay.open.channel.alipay.client.req.AlipayPayReq;
import cn.daxpay.open.channel.alipay.client.resp.AlipayPayResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.pay.bo.PayTradeResultBo;
import cn.daxpay.open.payment.pay.order.entity.PayTrade;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.unipay.PayBodyTypeEnum;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 支付宝支付执行业务服务
///
/// 通过 [AlipayChannelClient] 调用子应用 dax-pay-channel-one 完成支付宝支付下单。
/// 请求构建、响应解析全部在本类中完成。
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayPayService {

    private final AlipayChannelClient alipayChannelClient;

    /// 执行支付宝支付
    ///
    /// @param order      支付订单
    /// @param payParam   支付参数
    /// @param credential 通道调用凭证(密钥/证书)
    /// @return 支付结果
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, AlipaySdkCredential credential) {
        // 构建请求
        AlipayPayReq req = new AlipayPayReq();
        // 使用支付交易号作为商户订单号透传给支付宝, 回调时凭此反查 PayTrade
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setSubject(payParam.getTitle());
        req.setBody(payParam.getDescription());
        // 将平台支付方式(PayMethodEnum code)映射为支付宝通道支付方式
        req.setMethod(mapMethod(payParam.getMethod()));
        // 付款码(BARCODE) / 买家标识(JSAPI) 通道专属参数透传
        req.setAuthCode(payParam.getAuthCode());
        req.setOpenId(payParam.getOpenId());
        req.setCredential(credential);

        // 调用子应用
        DaxResult<AlipayPayResp> result = alipayChannelClient.pay(req);
        if (result.getCode() != 0) {
            throw new IllegalStateException("支付宝通道支付失败: " + result.getMsg());
        }

        return toPayResult(result.getData());
    }

    /// 平台支付方式([PayMethodEnum] code) -> 支付宝通道支付方式
    private static AlipayPayMethod mapMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            case ALIPAY_PC -> AlipayPayMethod.PC;
            case ALIPAY_H5 -> AlipayPayMethod.WAP;
            case ALIPAY_APP -> AlipayPayMethod.APP;
            case ALIPAY_QR, ALIPAY_ORDER_QR -> AlipayPayMethod.QR;
            case ALIPAY_BARCODE -> AlipayPayMethod.BARCODE;
            case ALIPAY_JSAPI, ALIPAY_MINI -> AlipayPayMethod.JSAPI;
            default -> throw new UnsupportedOperationException(
                    "暂不支持的支付宝支付方式: " + methodCode);
        };
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(AlipayPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                .setOutOrderNo(resp.getTradeNo())
                .setComplete(Boolean.TRUE.equals(resp.getComplete()))
                .setPayBody(resp.getPayBody());

        // 支付内容类型映射(子应用 AlipayPayBodyType -> 平台 PayBodyTypeEnum)
        AlipayPayBodyType bodyType = resp.getPayBodyType();
        if (bodyType != null) {
            switch (bodyType) {
                case LINK -> bo.setPayBodyType(PayBodyTypeEnum.LINK);
                case QR_CODE -> bo.setPayBodyType(PayBodyTypeEnum.QR_CODE);
                case ORDER_STR, IDENTIFIER -> bo.setPayBodyType(PayBodyTypeEnum.IDENTIFIER);
            }
        }

        // 完成时间
        if (resp.getFinishTime() != null) {
            bo.setFinishTime(resp.getFinishTime());
        }
        // 实付金额(BARCODE 付款码同步成功时返回)
        if (resp.getRealAmount() != null) {
            bo.setRealAmount(resp.getRealAmount());
        }
        // 买家标识(BARCODE 付款码同步成功时返回)
        if (StrUtil.isNotBlank(resp.getBuyerId())) {
            bo.setBuyerId(resp.getBuyerId());
        }

        return bo;
    }
}
