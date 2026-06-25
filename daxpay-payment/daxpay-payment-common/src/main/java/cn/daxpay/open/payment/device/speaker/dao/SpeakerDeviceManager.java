package cn.daxpay.open.payment.device.speaker.dao;

import cn.daxpay.open.payment.device.speaker.entity.SpeakerDevice;
import cn.daxpay.open.payment.device.speaker.param.SpeakerDeviceQuery;
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
public class SpeakerDeviceManager extends BaseManager<SpeakerDeviceMapper, SpeakerDevice> {

    /// 根据设备SN查询
    public Optional<SpeakerDevice> findByDeviceSn(String deviceSn) {
        return findByField(SpeakerDevice::getDeviceSn, deviceSn);
    }

    /// 判断设备SN是否存在(排除指定id, excludeId 为 null 时不排除)
    public boolean existsByDeviceSn(String deviceSn, Long excludeId) {
        if (excludeId == null) {
            return existedByField(SpeakerDevice::getDeviceSn, deviceSn);
        }
        return existedByField(SpeakerDevice::getDeviceSn, deviceSn, excludeId);
    }

    /// 分页
    public Page<SpeakerDevice> page(PageParam pageParam, SpeakerDeviceQuery query) {
        Page<SpeakerDevice> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<SpeakerDevice> wrapper = QueryGenerator.generator(query);
        return this.page(mpPage, wrapper);
    }
}
