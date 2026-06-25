package cn.daxpay.open.payment.admin.device.speaker.service;

import cn.daxpay.open.payment.device.enums.DeviceStatusEnum;
import cn.daxpay.open.payment.device.speaker.dao.SpeakerDeviceManager;
import cn.daxpay.open.payment.device.speaker.entity.SpeakerDevice;
import cn.daxpay.open.payment.device.speaker.param.SpeakerDeviceParam;
import cn.daxpay.open.payment.device.speaker.param.SpeakerDeviceQuery;
import cn.daxpay.open.payment.device.speaker.result.SpeakerDeviceResult;
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

/// # 云音响设备管理(运营端)
///
/// 首期仅维护本地设备台账, 绑定/解绑只更新本地状态; 真实商米对接由独立服务 dax-pay-iot 完成。
@Slf4j
@Service
@RequiredArgsConstructor
public class SpeakerDeviceAdminService {

    private final SpeakerDeviceManager speakerDeviceManager;

    /// 新增设备(默认未绑定状态)
    @Transactional(rollbackFor = Exception.class)
    public void add(SpeakerDeviceParam param) {
        // 校验设备SN唯一
        if (speakerDeviceManager.existsByDeviceSn(param.getDeviceSn(), null)) {
            // 云音响: 设备序列号已存在
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.speaker.deviceSnExists");
        }
        SpeakerDevice entity = new SpeakerDevice()
                .setMchNo(param.getMchNo())
                .setDeviceSn(param.getDeviceSn())
                .setImei(param.getImei())
                .setShopId(param.getShopId())
                .setDeviceName(param.getDeviceName())
                .setRemark(param.getRemark())
                // 新增默认未绑定
                .setStatus(DeviceStatusEnum.UNBOUND.getCode());
        speakerDeviceManager.save(entity);
    }

    /// 修改设备
    @Transactional(rollbackFor = Exception.class)
    public void update(SpeakerDeviceParam param) {
        SpeakerDevice entity = speakerDeviceManager.findById(param.getId())
                // 云音响: 设备不存在
                .orElseThrow(() -> new DataNotExistException("error.device.speaker.deviceNotFound"));
        // SN 变更时校验唯一
        if (!entity.getDeviceSn().equals(param.getDeviceSn())
                && speakerDeviceManager.existsByDeviceSn(param.getDeviceSn(), param.getId())) {
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.speaker.deviceSnExists");
        }
        entity.setMchNo(param.getMchNo())
                .setDeviceSn(param.getDeviceSn())
                .setImei(param.getImei())
                .setShopId(param.getShopId())
                .setDeviceName(param.getDeviceName())
                .setRemark(param.getRemark());
        speakerDeviceManager.updateById(entity);
    }

    /// 分页
    public PageResult<SpeakerDeviceResult> page(PageParam pageParam, SpeakerDeviceQuery query) {
        return MpUtil.toPageResult(speakerDeviceManager.page(pageParam, query));
    }

    /// 根据id查询
    public SpeakerDeviceResult findById(Long id) {
        return speakerDeviceManager.findById(id)
                // 云音响: 设备不存在
                .orElseThrow(() -> new DataNotExistException("error.device.speaker.deviceNotFound"))
                .toResult();
    }

    /// 删除
    public void delete(Long id) {
        speakerDeviceManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("error.device.speaker.deviceNotFound"));
        speakerDeviceManager.deleteById(id);
    }

    /// 绑定设备(首期仅更新本地状态为在线, 真实商米对接由独立服务完成)
    @Transactional(rollbackFor = Exception.class)
    public void bind(Long id) {
        SpeakerDevice entity = speakerDeviceManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("error.device.speaker.deviceNotFound"));
        entity.setStatus(DeviceStatusEnum.ONLINE.getCode())
                .setBindTime(OffsetDateTime.now());
        speakerDeviceManager.updateById(entity);
    }

    /// 解绑设备(首期仅更新本地状态为未绑定)
    @Transactional(rollbackFor = Exception.class)
    public void unbind(Long id) {
        SpeakerDevice entity = speakerDeviceManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("error.device.speaker.deviceNotFound"));
        entity.setStatus(DeviceStatusEnum.UNBOUND.getCode());
        speakerDeviceManager.updateById(entity);
    }
}
