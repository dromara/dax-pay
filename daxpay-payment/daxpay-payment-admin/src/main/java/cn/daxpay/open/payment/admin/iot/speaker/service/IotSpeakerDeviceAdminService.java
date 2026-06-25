package cn.daxpay.open.payment.admin.iot.speaker.service;

import cn.daxpay.open.payment.iot.speaker.dao.IotSpeakerDeviceManager;
import cn.daxpay.open.payment.iot.speaker.entity.IotSpeakerDevice;
import cn.daxpay.open.payment.iot.speaker.enums.IotDeviceStatusEnum;
import cn.daxpay.open.payment.iot.speaker.param.IotSpeakerDeviceParam;
import cn.daxpay.open.payment.iot.speaker.param.IotSpeakerDeviceQuery;
import cn.daxpay.open.payment.iot.speaker.result.IotSpeakerDeviceResult;
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
public class IotSpeakerDeviceAdminService {

    private final IotSpeakerDeviceManager iotSpeakerDeviceManager;

    /// 新增设备(默认未绑定状态)
    @Transactional(rollbackFor = Exception.class)
    public void add(IotSpeakerDeviceParam param) {
        // 校验设备SN唯一
        if (iotSpeakerDeviceManager.existsByDeviceSn(param.getDeviceSn(), null)) {
            // 云音响: 设备序列号已存在
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.iot.speaker.deviceSnExists");
        }
        IotSpeakerDevice entity = new IotSpeakerDevice()
                .setMchNo(param.getMchNo())
                .setDeviceSn(param.getDeviceSn())
                .setImei(param.getImei())
                .setShopId(param.getShopId())
                .setDeviceName(param.getDeviceName())
                .setRemark(param.getRemark())
                // 新增默认未绑定
                .setStatus(IotDeviceStatusEnum.UNBOUND.getCode());
        iotSpeakerDeviceManager.save(entity);
    }

    /// 修改设备
    @Transactional(rollbackFor = Exception.class)
    public void update(IotSpeakerDeviceParam param) {
        IotSpeakerDevice entity = iotSpeakerDeviceManager.findById(param.getId())
                // 云音响: 设备不存在
                .orElseThrow(() -> new DataNotExistException("error.iot.speaker.deviceNotFound"));
        // SN 变更时校验唯一
        if (!entity.getDeviceSn().equals(param.getDeviceSn())
                && iotSpeakerDeviceManager.existsByDeviceSn(param.getDeviceSn(), param.getId())) {
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.iot.speaker.deviceSnExists");
        }
        entity.setMchNo(param.getMchNo())
                .setDeviceSn(param.getDeviceSn())
                .setImei(param.getImei())
                .setShopId(param.getShopId())
                .setDeviceName(param.getDeviceName())
                .setRemark(param.getRemark());
        iotSpeakerDeviceManager.updateById(entity);
    }

    /// 分页
    public PageResult<IotSpeakerDeviceResult> page(PageParam pageParam, IotSpeakerDeviceQuery query) {
        return MpUtil.toPageResult(iotSpeakerDeviceManager.page(pageParam, query));
    }

    /// 根据id查询
    public IotSpeakerDeviceResult findById(Long id) {
        return iotSpeakerDeviceManager.findById(id)
                // 云音响: 设备不存在
                .orElseThrow(() -> new DataNotExistException("error.iot.speaker.deviceNotFound"))
                .toResult();
    }

    /// 删除
    public void delete(Long id) {
        iotSpeakerDeviceManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("error.iot.speaker.deviceNotFound"));
        iotSpeakerDeviceManager.deleteById(id);
    }

    /// 绑定设备(首期仅更新本地状态为在线, 真实商米对接由独立服务完成)
    @Transactional(rollbackFor = Exception.class)
    public void bind(Long id) {
        IotSpeakerDevice entity = iotSpeakerDeviceManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("error.iot.speaker.deviceNotFound"));
        entity.setStatus(IotDeviceStatusEnum.ONLINE.getCode())
                .setBindTime(OffsetDateTime.now());
        iotSpeakerDeviceManager.updateById(entity);
    }

    /// 解绑设备(首期仅更新本地状态为未绑定)
    @Transactional(rollbackFor = Exception.class)
    public void unbind(Long id) {
        IotSpeakerDevice entity = iotSpeakerDeviceManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("error.iot.speaker.deviceNotFound"));
        entity.setStatus(IotDeviceStatusEnum.UNBOUND.getCode());
        iotSpeakerDeviceManager.updateById(entity);
    }
}
