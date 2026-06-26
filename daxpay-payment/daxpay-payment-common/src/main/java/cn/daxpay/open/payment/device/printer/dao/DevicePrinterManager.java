package cn.daxpay.open.payment.device.printer.dao;

import cn.daxpay.open.payment.device.printer.entity.DevicePrinter;
import cn.daxpay.open.payment.device.printer.param.DevicePrinterQuery;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 云打印设备管理
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class DevicePrinterManager extends BaseManager<DevicePrinterMapper, DevicePrinter> {

    /// 根据设备SN查询
    public Optional<DevicePrinter> findByDeviceSn(String deviceSn) {
        return findByField(DevicePrinter::getDeviceSn, deviceSn);
    }

    /// 判断设备SN是否存在(排除指定id, excludeId 为 null 时不排除)
    public boolean existsByDeviceSn(String deviceSn, Long excludeId) {
        if (excludeId == null) {
            return existedByField(DevicePrinter::getDeviceSn, deviceSn);
        }
        return existedByField(DevicePrinter::getDeviceSn, deviceSn, excludeId);
    }

    /// 分页
    public Page<DevicePrinter> page(PageParam pageParam, DevicePrinterQuery query) {
        Page<DevicePrinter> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<DevicePrinter> wrapper = QueryGenerator.generator(query);
        return this.page(mpPage, wrapper);
    }
}
