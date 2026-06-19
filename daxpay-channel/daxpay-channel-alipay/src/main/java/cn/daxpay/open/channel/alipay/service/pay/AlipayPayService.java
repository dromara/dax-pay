package cn.daxpay.open.channel.alipay.service.pay;

import cn.daxpay.open.payment.pay.bo.PayTradeResultBo;
import cn.daxpay.open.payment.pay.order.entity.PayTrade;
import cn.daxpay.open.payment.unipay.param.trade.pay.PayParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/// # 支付宝支付执行业务服务
///
/// 支付方式映射、通道请求组装、通道适配服务调用、支付结果转换。
/// TODO Service 当前为占空实现, 后续补全支付执行流程逻辑。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayPayService {

    /// 执行支付宝支付
    ///
    /// @param order    支付订单
    /// @param payParam 支付参数
    /// @param config   通道调用配置(密钥/证书/授权令牌等)
    /// @return 支付结果
    public PayTradeResultBo pay(PayTrade order, PayParam payParam, Map<String, Object> config) {
        // TODO 后续实现: method映射 → 组装ChannelPayReq → channelPayClient.pay → 结果转换
        throw new UnsupportedOperationException("AlipayPayService.pay 尚未实现");
    }
}
