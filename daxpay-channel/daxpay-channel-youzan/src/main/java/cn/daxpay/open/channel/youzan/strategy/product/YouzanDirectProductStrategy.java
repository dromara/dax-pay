package cn.daxpay.open.channel.youzan.strategy.product;

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
import java.util.Set;

/// # 有赞聚合支付产品策略
///
/// 有赞支付(高汇通)为聚合通道, 单产品(YOUZAN_PAY)承载统收单线的四种支付形态:
/// - 支付宝 JS 支付(pay_tool=ALIPAY_JS, ext 传 buyer_id)
/// - 微信 JS 支付(pay_tool=WX_JS, ext 传 open_id + app_id)
/// - 微信小程序支付(pay_tool=WX_APPLET, ext 传 open_id + app_id)
/// - 微信 H5 支付(pay_tool=WX_H5, ext 传 type=Wap, 返回唤醒链接)
///
/// 注: 有赞无聚合静态码接口(扫码形态在参考实现中经站点中转页落 JS 支付), 平台侧按 JS/H5 形态接入。
@Slf4j
@Service
@RequiredArgsConstructor
public class YouzanDirectProductStrategy extends AbsProductStrategy {

    /// 支付方式 → 支付能力映射
    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.ofEntries(
            Map.entry(PayMethodEnum.ALIPAY_JSAPI, List.of(PayCapabilityEnum.ALIPAY_JSAPI)),
            Map.entry(PayMethodEnum.WECHAT_JSAPI, List.of(PayCapabilityEnum.WECHAT_JSAPI)),
            Map.entry(PayMethodEnum.WECHAT_MINI, List.of(PayCapabilityEnum.WECHAT_MINI)),
            Map.entry(PayMethodEnum.WECHAT_H5, List.of(PayCapabilityEnum.WECHAT_H5)));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.YOUZAN_PAY;
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    /// 高汇通无独立沙箱网关
    @Override
    public boolean isSandbox() {
        return false;
    }

    @Override
    public List<PayProviderEnum> supportedPayProviders() {
        return List.of(PayProviderEnum.WECHAT, PayProviderEnum.ALIPAY);
    }

    @Override
    public Map<PayMethodEnum, List<PayCapabilityEnum>> methodCapabilityMapping() {
        return METHOD_CAP_MAP;
    }

    /// 有赞聚合直连: 微信侧仅 JSAPI/小程序支付需要 appid(拉起微信收银台),
    /// 收窄弹窗候选与支付链路的解析范围
    @Override
    public Set<PayCapabilityEnum> wxAppRequiredCapabilities() {
        return Set.of(PayCapabilityEnum.WECHAT_JSAPI, PayCapabilityEnum.WECHAT_MINI);
    }
}
