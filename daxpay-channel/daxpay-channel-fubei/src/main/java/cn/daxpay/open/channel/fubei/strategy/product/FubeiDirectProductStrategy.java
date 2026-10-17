package cn.daxpay.open.channel.fubei.strategy.product;

import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.product.AbsProductStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/// # 付呗聚合支付产品策略
///
/// 付呗为聚合支付通道, 单产品(FUBEI_PAY)承载云闪付扫码/微信JSAPI/支付宝JSAPI/支付宝H5:
/// 微信/支付宝的 C扫B 台牌码与付款码被扫为门店台牌/收银机能力, 参考插件未走开放平台接口, 一期未接。
///
/// 付呗无独立沙箱网关。
@Slf4j
@Service
@RequiredArgsConstructor
public class FubeiDirectProductStrategy extends AbsProductStrategy {

    /// 支付方式 → 支付能力映射
    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.ofEntries(
            // 扫码族(云闪付)
            Map.entry(PayMethodEnum.UNION_QR, List.of(PayCapabilityEnum.UNION_QR)),
            // JSAPI 族(微信=openid+sub_appid / 支付宝=买家用户ID)
            Map.entry(PayMethodEnum.WECHAT_JSAPI, List.of(PayCapabilityEnum.WECHAT_JSAPI)),
            Map.entry(PayMethodEnum.WECHAT_MINI, List.of(PayCapabilityEnum.WECHAT_MINI)),
            Map.entry(PayMethodEnum.ALIPAY_JSAPI, List.of(PayCapabilityEnum.ALIPAY_JSAPI)),
            // H5 族(支付宝)
            Map.entry(PayMethodEnum.ALIPAY_H5, List.of(PayCapabilityEnum.ALIPAY_H5)));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.FUBEI_PAY;
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    /// 付呗无独立沙箱网关
    @Override
    public boolean isSandbox() {
        return false;
    }

    @Override
    public List<PayProviderEnum> supportedPayProviders() {
        return List.of(PayProviderEnum.AGGREGATE_PAY, PayProviderEnum.WECHAT, PayProviderEnum.ALIPAY,
                PayProviderEnum.UNION_PAY);
    }

    @Override
    public Map<PayMethodEnum, List<PayCapabilityEnum>> methodCapabilityMapping() {
        return METHOD_CAP_MAP;
    }
}
