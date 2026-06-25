package cn.daxpay.open.payment.device.vendor.dao;

import cn.daxpay.open.payment.device.vendor.entity.DeviceVendorConfig;
import cn.daxpay.open.payment.device.vendor.param.DeviceVendorConfigQuery;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/// # 设备厂商配置管理
@Slf4j
@Repository
@RequiredArgsConstructor
public class DeviceVendorConfigManager extends BaseManager<DeviceVendorConfigMapper, DeviceVendorConfig> {

    /// 查询指定设备类型和厂商的启用配置列表
    public List<DeviceVendorConfig> listEnabledByVendor(String deviceType, String vendorCode) {
        return lambdaQuery()
                .eq(DeviceVendorConfig::getDeviceType, deviceType)
                .eq(DeviceVendorConfig::getVendorCode, vendorCode)
                .eq(DeviceVendorConfig::isEnable, true)
                .list();
    }

    /// 判断配置名称是否存在(排除指定id, excludeId 为 null 时不排除)
    public boolean existsByConfigName(String deviceType, String vendorCode, String configName, Long excludeId) {
        var query = lambdaQuery()
                .eq(DeviceVendorConfig::getDeviceType, deviceType)
                .eq(DeviceVendorConfig::getVendorCode, vendorCode)
                .eq(DeviceVendorConfig::getConfigName, configName);
        if (excludeId != null) {
            query.ne(DeviceVendorConfig::getId, excludeId);
        }
        return query.exists();
    }

    /// 分页
    public Page<DeviceVendorConfig> page(PageParam pageParam, DeviceVendorConfigQuery query) {
        Page<DeviceVendorConfig> mpPage = MpUtil.getMpPage(pageParam);
        return this.page(mpPage, QueryGenerator.generator(query));
    }
}
