package cn.daxpay.open.payment.device.vendor.service;

import cn.daxpay.open.payment.device.vendor.dao.DeviceVendorConfigManager;
import cn.daxpay.open.payment.device.vendor.entity.DeviceVendorConfig;
import cn.daxpay.open.payment.device.vendor.param.DeviceVendorConfigParam;
import cn.daxpay.open.payment.device.vendor.param.DeviceVendorConfigQuery;
import cn.daxpay.open.payment.device.vendor.result.DeviceVendorConfigResult;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/// # 设备厂商配置管理(运营端)
@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceVendorConfigAdminService {

    private final DeviceVendorConfigManager deviceVendorConfigManager;

    /// 新增配置
    @Transactional(rollbackFor = Exception.class)
    public void add(DeviceVendorConfigParam param) {
        // 校验配置名称唯一(同设备类型+厂商下)
        if (deviceVendorConfigManager.existsByConfigName(
                param.getDeviceType(), param.getVendorCode(), param.getConfigName(), null)) {
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.vendor.configNameExists");
        }
        var entity = new DeviceVendorConfig()
                .setDeviceType(param.getDeviceType())
                .setVendorCode(param.getVendorCode())
                .setConfigName(param.getConfigName())
                .setAppId(param.getAppId())
                .setAppSecret(param.getAppSecret())
                .setEnable(param.isEnable())
                .setExtParam(param.getExtParam())
                .setRemark(param.getRemark());
        deviceVendorConfigManager.save(entity);
    }

    /// 修改配置
    @Transactional(rollbackFor = Exception.class)
    public void update(DeviceVendorConfigParam param) {
        DeviceVendorConfig entity = deviceVendorConfigManager.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("error.device.vendor.configNotFound"));
        // 配置名称变更时校验唯一
        if (!entity.getConfigName().equals(param.getConfigName())
                && deviceVendorConfigManager.existsByConfigName(
                        param.getDeviceType(), param.getVendorCode(), param.getConfigName(), param.getId())) {
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.vendor.configNameExists");
        }
        entity.setConfigName(param.getConfigName())
                .setAppId(param.getAppId())
                .setEnable(param.isEnable())
                .setExtParam(param.getExtParam())
                .setRemark(param.getRemark());
        // appSecret 前端不传时跳过更新
        if (param.getAppSecret() != null && !param.getAppSecret().isEmpty()) {
            entity.setAppSecret(param.getAppSecret());
        }
        deviceVendorConfigManager.updateById(entity);
    }

    /// 分页
    public PageResult<DeviceVendorConfigResult> page(PageParam pageParam, DeviceVendorConfigQuery query) {
        return MpUtil.toPageResult(deviceVendorConfigManager.page(pageParam, query));
    }

    /// 根据id查询
    public DeviceVendorConfigResult findById(Long id) {
        return deviceVendorConfigManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("error.device.vendor.configNotFound"))
                .toResult();
    }

    /// 删除
    public void delete(Long id) {
        deviceVendorConfigManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("error.device.vendor.configNotFound"));
        deviceVendorConfigManager.deleteById(id);
    }

    /// 查询指定设备类型和厂商的启用配置列表(给设备下拉用)
    public List<DeviceVendorConfigResult> listEnabledByVendor(String deviceType, String vendorCode) {
        return deviceVendorConfigManager.listEnabledByVendor(deviceType, vendorCode)
                .stream()
                .map(DeviceVendorConfig::toResult)
                .toList();
    }
}
