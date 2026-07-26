package cn.daxpay.open.payment.app.merchant.service.config;

import cn.daxpay.open.payment.admin.service.merchant.config.MchAppNotifyConfigService;
import cn.daxpay.open.payment.merchant.param.config.MchAppNotifyConfigParam;
import cn.daxpay.open.payment.merchant.result.config.MchAppNotifyConfigResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 商户移动端-应用事件通知配置服务
///
/// 转发至 [MchAppNotifyConfigService]
@Service
@RequiredArgsConstructor
public class AppMerchantAppNotifyConfigService {

    private final MchAppNotifyConfigService mchAppNotifyConfigService;

    /// 根据应用ID查询通知配置
    public MchAppNotifyConfigResult findByAppId(String appId) {
        return mchAppNotifyConfigService.findByAppId(appId);
    }

    /// 保存或更新通知配置
    public void saveOrUpdate(MchAppNotifyConfigParam param) {
        mchAppNotifyConfigService.saveOrUpdate(param);
    }
}
