package cn.daxpay.open.payment.auth;

import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import cn.daxpay.open.payment.strategy.PaymentStrategyFactory;
import cn.daxpay.open.payment.strategy.product.AbsProductStrategy;
import cn.daxpay.open.payment.strategy.ProductStrategySupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 通道基础数据获取
///
@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelBasicService {

    /// 获取通道产品支持的支付能力列表
    public List<LabelValue> payMethodList(String channel) {
        var productStrategy = PaymentStrategyFactory.create(channel, AbsProductStrategy.class);
        return ProductStrategySupport.supportedPayCapabilities(productStrategy).stream()
                .map(capability -> new LabelValue(I18nUtil.getEnumName(capability), capability.getCode()))
                .toList();
    }

}
