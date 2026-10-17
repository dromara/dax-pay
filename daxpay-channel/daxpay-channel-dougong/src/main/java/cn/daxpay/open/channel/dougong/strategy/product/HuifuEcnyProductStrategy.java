package cn.daxpay.open.channel.dougong.strategy.product;

import cn.daxpay.open.platform.core.enums.pay.channel.ChannelApiCallMode;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelPayIdType;
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

/// # 汇付数字人民币支付产品策略
///
/// 汇付数字人民币(huifu_ecny)经斗拱平台收款, 与 [DougongProductStrategy](DOUGONG_PAY)同通道(HUIFU)、
/// 同服务商密钥、同子应用接口(jspay), 仅 tradeType 不同(D_NATIVE), 故独立为产品而复用斗拱服务族。
///
/// 付款方限数字人民币APP钱包(扫码模式无需上送用户/钱包标识), 仅支持扫码支付一种能力。
@Slf4j
@Service
@RequiredArgsConstructor
public class HuifuEcnyProductStrategy extends AbsProductStrategy {

    /// 支付方式 → 支付能力映射(数币仅扫码一种)
    private static final Map<PayMethodEnum, List<PayCapabilityEnum>> METHOD_CAP_MAP = Map.of(
            // 数字人民币扫码
            PayMethodEnum.ECNY_QR, List.of(PayCapabilityEnum.ECNY_QR));

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.HUIFU_ECNY;
    }

    @Override
    public boolean isIsv() {
        return true;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public boolean isSandbox() {
        return false;
    }

    @Override
    public ChannelApiCallMode getApiCallMode() {
        return ChannelApiCallMode.ISV;
    }

    @Override
    public ChannelPayIdType getPayIdType() {
        return ChannelPayIdType.MCH;
    }

    @Override
    public List<PayProviderEnum> supportedPayProviders() {
        return List.of(PayProviderEnum.ECNY);
    }

    @Override
    public Map<PayMethodEnum, List<PayCapabilityEnum>> methodCapabilityMapping() {
        return METHOD_CAP_MAP;
    }

    /// 数字人民币无微信支付能力, 无需绑定微信应用(返回空集收窄弹窗候选与解析范围)
    @Override
    public Set<PayCapabilityEnum> wxAppRequiredCapabilities() {
        return Set.of();
    }
}
