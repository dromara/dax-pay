package cn.daxpay.open.payment.app.admin.service.merchant.config;

import cn.daxpay.open.payment.admin.service.merchant.config.MchAppNotifyConfigService;
import cn.daxpay.open.payment.merchant.param.config.MchAppNotifyConfigParam;
import cn.daxpay.open.payment.merchant.result.config.MchAppNotifyConfigResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 运营移动端-应用事件通知配置服务
///
/// 转发至 [MchAppNotifyConfigService]
@Service
@RequiredArgsConstructor
public class AppAdminMchAppNotifyConfigService {

    private final MchAppNotifyConfigService notifyConfigService;

    /// 根据应用 ID 查询
    public MchAppNotifyConfigResult findByAppId(String appId) {
        return notifyConfigService.findByAppId(appId);
    }

    /// 保存或更新
    public void saveOrUpdate(MchAppNotifyConfigParam param) {
        notifyConfigService.saveOrUpdate(param);
    }
}
