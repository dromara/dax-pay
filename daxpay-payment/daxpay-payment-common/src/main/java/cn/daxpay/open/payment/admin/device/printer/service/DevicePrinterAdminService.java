package cn.daxpay.open.payment.admin.device.printer.service;

import cn.daxpay.open.payment.device.enums.DeviceStatusEnum;
import cn.daxpay.open.payment.device.enums.DeviceVendorEnum;
import cn.daxpay.open.payment.device.printer.dao.DevicePrinterManager;
import cn.daxpay.open.payment.device.printer.entity.DevicePrinter;
import cn.daxpay.open.payment.device.printer.param.DevicePrinterParam;
import cn.daxpay.open.payment.device.printer.param.DevicePrinterQuery;
import cn.daxpay.open.payment.device.printer.result.DevicePrinterResult;
import cn.daxpay.open.payment.device.vendor.dao.DeviceVendorConfigManager;
import cn.daxpay.open.payment.device.vendor.entity.DeviceVendorConfig;
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

import java.time.OffsetDateTime;

/// # 云打印设备管理(运营端)
///
/// 首期仅维护本地设备台账, 绑定/解绑只更新本地状态; 真实设备对接由独立服务 dax-pay-iot 完成。
@Slf4j
@Service
@RequiredArgsConstructor
public class DevicePrinterAdminService {

    private final DevicePrinterManager devicePrinterManager;
    private final DeviceVendorConfigManager deviceVendorConfigManager;

    /// 新增设备(默认未绑定状态)
    @Transactional(rollbackFor = Exception.class)
    public void add(DevicePrinterParam param) {
        // 校验设备SN唯一
        if (devicePrinterManager.existsByDeviceSn(param.getDeviceSn(), null)) {
            // 云打印: 设备序列号已存在
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.printer.deviceSnExists");
        }
        // 校验厂商配置合法性
        validateVendorConfig(param.getVendorCode(), param.getVendorConfigId());
        DevicePrinter entity = new DevicePrinter()
                .setMchNo(param.getMchNo())
                .setVendorCode(param.getVendorCode())
                .setVendorConfigId(param.getVendorConfigId())
                .setDeviceSn(param.getDeviceSn())
                .setImei(param.getImei())
                .setShopId(param.getShopId())
                .setDeviceName(param.getDeviceName())
                .setRemark(param.getRemark())
                // 新增默认未绑定
                .setStatus(DeviceStatusEnum.UNBOUND.getCode());
        devicePrinterManager.save(entity);
    }

    /// 修改设备
    @Transactional(rollbackFor = Exception.class)
    public void update(DevicePrinterParam param) {
        DevicePrinter entity = devicePrinterManager.findById(param.getId())
                // 云打印: 设备不存在
                .orElseThrow(() -> new DataNotExistException("error.device.printer.deviceNotFound"));
        // SN 变更时校验唯一
        if (!entity.getDeviceSn().equals(param.getDeviceSn())
                && devicePrinterManager.existsByDeviceSn(param.getDeviceSn(), param.getId())) {
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.printer.deviceSnExists");
        }
        // 校验厂商配置合法性
        validateVendorConfig(param.getVendorCode(), param.getVendorConfigId());
        entity.setMchNo(param.getMchNo())
                .setVendorCode(param.getVendorCode())
                .setVendorConfigId(param.getVendorConfigId())
                .setDeviceSn(param.getDeviceSn())
                .setImei(param.getImei())
                .setShopId(param.getShopId())
                .setDeviceName(param.getDeviceName())
                .setRemark(param.getRemark());
        devicePrinterManager.updateById(entity);
    }

    /// 分页
    public PageResult<DevicePrinterResult> page(PageParam pageParam, DevicePrinterQuery query) {
        return MpUtil.toPageResult(devicePrinterManager.page(pageParam, query));
    }

    /// 根据id查询
    public DevicePrinterResult findById(Long id) {
        return devicePrinterManager.findById(id)
                // 云打印: 设备不存在
                .orElseThrow(() -> new DataNotExistException("error.device.printer.deviceNotFound"))
                .toResult();
    }

    /// 删除
    public void delete(Long id) {
        devicePrinterManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("error.device.printer.deviceNotFound"));
        devicePrinterManager.deleteById(id);
    }

    /// 绑定设备(首期仅更新本地状态为在线, 真实设备对接由独立服务完成)
    @Transactional(rollbackFor = Exception.class)
    public void bind(Long id) {
        DevicePrinter entity = devicePrinterManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("error.device.printer.deviceNotFound"));
        entity.setStatus(DeviceStatusEnum.ONLINE.getCode())
                .setBindTime(OffsetDateTime.now());
        devicePrinterManager.updateById(entity);
    }

    /// 解绑设备(首期仅更新本地状态为未绑定)
    @Transactional(rollbackFor = Exception.class)
    public void unbind(Long id) {
        DevicePrinter entity = devicePrinterManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("error.device.printer.deviceNotFound"));
        entity.setStatus(DeviceStatusEnum.UNBOUND.getCode());
        devicePrinterManager.updateById(entity);
    }

    /// 校验厂商代码合法且配置存在并匹配
    private void validateVendorConfig(String vendorCode, Long vendorConfigId) {
        // 校验厂商代码合法
        DeviceVendorEnum.findByCode(vendorCode);
        // 校验配置存在且厂商匹配
        DeviceVendorConfig config = deviceVendorConfigManager.findById(vendorConfigId)
                .orElseThrow(() -> new DataNotExistException("error.device.vendor.configNotFound"));
        if (!config.getVendorCode().equals(vendorCode)) {
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.vendor.vendorConfigNotMatch");
        }
    }
}
