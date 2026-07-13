package cn.daxpay.open.payment.app.admin.service.merchant.config;

import cn.daxpay.open.payment.merchant.param.config.MerchantCredentialParam;
import cn.daxpay.open.payment.merchant.result.config.MerchantCredentialResult;
import cn.daxpay.open.payment.merchant.service.config.MerchantCredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 运营移动端-商户对接配置服务
///
/// 转发至 [MerchantCredentialService]
@Service
@RequiredArgsConstructor
public class AppAdminMerchantCredentialService {

    private final MerchantCredentialService credentialService;

    /// 根据商户号查询
    public MerchantCredentialResult findByMchNo(String mchNo) {
        return credentialService.findByMchNo(mchNo);
    }

    /// 更新对接配置
    public void update(MerchantCredentialParam param) {
        credentialService.update(param);
    }
}
