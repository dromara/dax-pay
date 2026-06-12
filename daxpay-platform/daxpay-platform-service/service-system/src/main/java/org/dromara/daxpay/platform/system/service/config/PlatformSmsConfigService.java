package org.dromara.daxpay.platform.system.service.config;

import org.dromara.daxpay.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.platform.system.convert.PlatformConfigConvert;
import org.dromara.daxpay.platform.system.dao.config.PlatformSmsConfigManager;
import org.dromara.daxpay.platform.system.entity.config.sms.PlatformSmsConfig;
import org.dromara.daxpay.platform.core.exception.config.ConfigNotExistException;
import org.dromara.daxpay.platform.system.param.config.PlatformSmsConfigParam;
import org.dromara.daxpay.platform.system.result.config.platform.PlatformSmsConfigResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 平台短信配置服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class PlatformSmsConfigService {
    private final PlatformSmsConfigManager smsConfigManager;

    /// 添加短信配置
    public void add(PlatformSmsConfigParam param) {
        PlatformSmsConfig config = PlatformConfigConvert.CONVERT.convert(param);
        smsConfigManager.save(config);
    }

    /// 更新短信配置
    public void update(PlatformSmsConfigParam param) {
        var config = smsConfigManager.findById(param.getId())
                .orElseThrow(() -> new ConfigNotExistException("error.system.sms.configNotExist"));
        PlatformConfigConvert.CONVERT.copy(param, config);
        smsConfigManager.updateById(config);
    }

    /// 设置默认配置
    public void setupEnable(Long id) {
        // 先将所有配置设置为非默认
        smsConfigManager.clearEnable();
        // 查询
        var config = smsConfigManager.findById(id)
                .orElseThrow(() -> new ConfigNotExistException("error.system.sms.configNotExist"));
        // 设置当前配置为默认
        config.setEnable(true);
        smsConfigManager.updateById(config);
    }

    /// 清除默认配置
    public void clearEnable(Long id) {
        // 查询
        var config = smsConfigManager.findById(id)
                .orElseThrow(() -> new ConfigNotExistException("error.system.sms.configNotExist"));
        // 设置当前配置为默认
        config.setEnable(false);
        smsConfigManager.updateById(config);
    }

    /// 删除短信配置
    public void delete(Long id) {
        smsConfigManager.deleteById(id);
    }

    /// 获取短信配置
    public PlatformSmsConfigResult findById(Long id) {
        return smsConfigManager.findById(id).map(PlatformSmsConfig::toResult)
                .orElseThrow(() -> new ConfigNotExistException("error.system.sms.configNotExist"));
    }

    /// 获取默认短信配置
    public PlatformSmsConfig findDefault() {
        return smsConfigManager.findDefault()
                .orElseThrow(() -> new ConfigNotExistException("error.system.sms.defaultConfigNotExist"));
    }

    /// 获取所有短信配置
    public List<PlatformSmsConfigResult> findAll() {
        return MpUtil.toListResult(smsConfigManager.findAll());
    }

}
