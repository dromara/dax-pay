package cn.daxpay.open.device.linkage.dao;

import cn.daxpay.open.device.linkage.entity.DeviceMchAppConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.core.annotation.IgnoreTenant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/// # 商户应用设备联动配置管理
///
/// 行不存在=默认全开, 由调用方解释默认值(appId 唯一索引兜底一应用一行)。
@Slf4j
@Repository
@RequiredArgsConstructor
public class MchAppDeviceConfigManager extends BaseManager<MchAppDeviceConfigMapper, DeviceMchAppConfig> {

    /// 按应用号查配置行(忽略租户: JMS 消费线程/免登录客户端无商户上下文, 消费侧配置读取入口)
    @IgnoreTenant
    public Optional<DeviceMchAppConfig> findByAppIdNotTenant(String appId) {
        return lambdaQuery()
                .eq(DeviceMchAppConfig::getAppId, appId)
                .oneOpt();
    }

    /// 保存配置(存在则全量覆盖, 不存在则新建)
    ///
    /// insert 分支的 mchNo 已由调用方显式赋值(运营端无商户上下文不能依赖 Fill);
    /// update 分支显式 set mchNo 防应用换绑商户后的归属残留。
    /// 忽略租户: 本方法内部对 [findByAppIdNotTenant] 的调用是 this 自调用,
    /// 不走 Spring 代理, 内层方法上的 @IgnoreTenant 切面不生效(运营端无商户上下文时
    /// 租户拦截器抛 mchContextMissing, 2026-10-24 联调实踩); 本方法由外部经代理调用,
    /// 注解在本方法上可可靠生效, 且可重入计数会同时覆盖内部查询与写入。
    @IgnoreTenant
    @Transactional(rollbackFor = Exception.class)
    public void saveConfig(DeviceMchAppConfig config) {
        DeviceMchAppConfig existing = findByAppIdNotTenant(config.getAppId()).orElse(null);
        if (existing == null) {
            this.save(config);
            return;
        }
        lambdaUpdate()
                .set(DeviceMchAppConfig::getMchNo, config.getMchNo())
                .set(DeviceMchAppConfig::getBroadcastEnable, config.getBroadcastEnable())
                .set(DeviceMchAppConfig::getPrintEnable, config.getPrintEnable())
                .set(DeviceMchAppConfig::getStoreStrictMatch, config.getStoreStrictMatch())
                .set(DeviceMchAppConfig::getTemplateCode, config.getTemplateCode())
                .eq(DeviceMchAppConfig::getId, existing.getId())
                .update();
    }
}
