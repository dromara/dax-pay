package cn.daxpay.open.payment.iot.speaker.dao;

import cn.daxpay.open.payment.iot.speaker.entity.IotSpeakerDevice;
import cn.daxpay.open.payment.iot.speaker.param.IotSpeakerDeviceQuery;
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

/// # 云音响设备管理
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class IotSpeakerDeviceManager extends BaseManager<IotSpeakerDeviceMapper, IotSpeakerDevice> {

    /// 根据设备SN查询
    public Optional<IotSpeakerDevice> findByDeviceSn(String deviceSn) {
        return findByField(IotSpeakerDevice::getDeviceSn, deviceSn);
    }

    /// 判断设备SN是否存在(排除指定id, excludeId 为 null 时不排除)
    public boolean existsByDeviceSn(String deviceSn, Long excludeId) {
        if (excludeId == null) {
            return existedByField(IotSpeakerDevice::getDeviceSn, deviceSn);
        }
        return existedByField(IotSpeakerDevice::getDeviceSn, deviceSn, excludeId);
    }

    /// 分页
    public Page<IotSpeakerDevice> page(PageParam pageParam, IotSpeakerDeviceQuery query) {
        Page<IotSpeakerDevice> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<IotSpeakerDevice> wrapper = QueryGenerator.generator(query);
        return this.page(mpPage, wrapper);
    }
}
