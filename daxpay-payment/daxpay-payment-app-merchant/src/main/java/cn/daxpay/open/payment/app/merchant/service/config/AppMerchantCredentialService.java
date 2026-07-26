package cn.daxpay.open.payment.app.merchant.service.config;

import cn.daxpay.open.payment.merchant.param.config.MerchantCredentialParam;
import cn.daxpay.open.payment.merchant.result.config.MerchantCredentialResult;
import cn.daxpay.open.payment.merchant.service.config.MerchantCredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 商户移动端-对接凭证服务
///
/// 转发至 core [MerchantCredentialService]
@Service
@RequiredArgsConstructor
public class AppMerchantCredentialService {

    private final MerchantCredentialService credentialService;

    /// 根据商户号查询
    public MerchantCredentialResult findByMchNo(String mchNo) {
        return credentialService.findByMchNo(mchNo);
    }

    /// 更新商户API配置
    public void update(MerchantCredentialParam param) {
        credentialService.update(param);
    }
}
