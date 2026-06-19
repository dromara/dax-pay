package cn.daxpay.open.channel.alipay.strategy.direct;

import cn.daxpay.open.channel.alipay.dao.direct.AlipayDirectAppManager;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectApp;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectAppKeyConfig;
import cn.daxpay.open.channel.alipay.service.direct.AlipayDirectAppKeyConfigService;
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

/// # 支付宝直连支付策略
///
/// 支付宝直连模式(ProductEnum.ALIPAY)下发起支付的具体执行策略。
/// 负责直连配置组装, 支付执行委托给 [AlipayPayService]。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayDirectPayStrategy extends AbsPayStrategy {

    private final AlipayPayService alipayPayService;
    private final AlipayDirectAppManager alipayDirectAppManager;
    private final AlipayDirectAppKeyConfigService alipayDirectAppKeyConfigService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.ALIPAY;
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
        AlipayDirectApp app = alipayDirectAppManager.findFirstByMchNo(mchNo)
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.mchAppNotFound"));
        AlipayDirectAppKeyConfig keyConfig = alipayDirectAppKeyConfigService.findByAlipayDirectAppId(app.getId());

        Map<String, Object> config = new HashMap<>();
        config.put("aliAppId", app.getAliAppId());
        config.put("privateKey", keyConfig.getPrivateKey());
        config.put("alipayPublicKey", keyConfig.getAlipayPublicKey());
        config.put("authType", keyConfig.getAuthType());
        config.put("appCert", keyConfig.getAppCert());
        config.put("alipayCert", keyConfig.getAlipayCert());
        config.put("alipayRootCert", keyConfig.getAlipayRootCert());
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
