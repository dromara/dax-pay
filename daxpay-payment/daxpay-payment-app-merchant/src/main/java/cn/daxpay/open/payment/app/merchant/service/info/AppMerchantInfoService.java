package cn.daxpay.open.payment.app.merchant.service.info;

import cn.daxpay.open.payment.merchant.param.info.MerchantInfoParam;
import cn.daxpay.open.payment.merchant.result.info.MerchantInfoResult;
import cn.daxpay.open.payment.merchant.service.info.MerchantInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 商户移动端-商户信息服务
///
/// 转发至 [MerchantInfoService]
@Service
@RequiredArgsConstructor
public class AppMerchantInfoService {

    private final MerchantInfoService merchantInfoService;

    /// 获取当前登录商户信息
    public MerchantInfoResult getMerchant() {
        return merchantInfoService.getMerchant();
    }

    /// 更新商户信息
    public void update(MerchantInfoParam param) {
        merchantInfoService.update(param);
    }
}
