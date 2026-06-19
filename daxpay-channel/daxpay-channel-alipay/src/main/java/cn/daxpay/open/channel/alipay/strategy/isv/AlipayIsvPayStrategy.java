package cn.daxpay.open.channel.alipay.strategy.isv;

import cn.daxpay.open.channel.alipay.dao.isv.AlipayIsvAppManager;
import cn.daxpay.open.channel.alipay.dao.isv.AlipayIsvChannelMerchantManager;
import cn.daxpay.open.channel.alipay.entity.isv.AlipayIsvApp;
import cn.daxpay.open.channel.alipay.entity.isv.AlipayIsvAppKeyConfig;
import cn.daxpay.open.channel.alipay.entity.isv.AlipayIsvChannelMerchant;
import cn.daxpay.open.channel.alipay.service.isv.AlipayIsvAppKeyConfigService;
import cn.daxpay.open.channel.alipay.service.pay.AlipayPayService;
import cn.daxpay.open.payment.pay.bo.PayTradeResultBo;
import cn.daxpay.open.payment.strategy.pay.AbsPayStrategy;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/// # 支付宝服务商支付策略
///
/// 支付宝服务商模式(ProductEnum.ALIPAY_ISV)下发起支付的具体执行策略。
/// 负责服务商配置组装(含应用授权令牌), 支付执行委托给 [AlipayPayService]。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayIsvPayStrategy extends AbsPayStrategy {

    private final AlipayPayService alipayPayService;
    private final AlipayIsvChannelMerchantManager alipayIsvChannelMerchantManager;
    private final AlipayIsvAppManager alipayIsvAppManager;
    private final AlipayIsvAppKeyConfigService alipayIsvAppKeyConfigService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.ALIPAY_ISV;
    }

    @Override
    public void doBeforePayHandler() {
        mapMethod(getPayParam().getMethod());
    }

    @Override
    public PayTradeResultBo doPayHandler() {
        return alipayPayService.pay(getTrade(), getPayParam(), buildConfig());
    }

    private Map<String, Object> buildConfig() {
        String mchNo = getTrade().getMchNo();
        AlipayIsvChannelMerchant isvMerchant = alipayIsvChannelMerchantManager.findByMchNo(mchNo)
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.mchAppNotFound"));
        AlipayIsvApp isvApp = alipayIsvAppManager.findById(isvMerchant.getIsvAppId())
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.appNotFound"));
        AlipayIsvAppKeyConfig keyConfig = alipayIsvAppKeyConfigService.findByAlipayIsvAppId(isvMerchant.getIsvAppId());

        Map<String, Object> config = new HashMap<>();
        config.put("aliAppId", isvApp.getAliAppId());
        config.put("privateKey", keyConfig.getPrivateKey());
        config.put("alipayPublicKey", keyConfig.getAlipayPublicKey());
        config.put("authType", keyConfig.getAuthType());
        config.put("appCert", keyConfig.getAppCert());
        config.put("alipayCert", keyConfig.getAlipayCert());
        config.put("alipayRootCert", keyConfig.getAlipayRootCert());
        config.put("appAuthToken", isvMerchant.getAppAuthToken());
        return config;
    }

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
}
