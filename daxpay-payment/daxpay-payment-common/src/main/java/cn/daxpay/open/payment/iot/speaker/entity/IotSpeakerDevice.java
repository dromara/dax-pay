package cn.daxpay.open.payment.iot.speaker.entity;

import cn.daxpay.open.payment.iot.speaker.enums.IotDeviceStatusEnum;
import cn.daxpay.open.payment.iot.speaker.result.IotSpeakerDeviceResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 云音响设备
///
/// 记录商米云音响设备与商户/门店的绑定关系, 真实播报对接由独立服务 dax-pay-iot 完成。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("iot_speaker_device")
public class IotSpeakerDevice extends MpBaseEntity implements ToResult<IotSpeakerDeviceResult> {

    /// 商户号
    private String mchNo;

    /// 商米设备序列号(SN)
    private String deviceSn;

    /// 设备IMEI
    private String imei;

    /// 商米门店ID
    private String shopId;

    /// 设备名称
    private String deviceName;

    /// 设备状态
    /// @see IotDeviceStatusEnum
    private String status;

    /// 绑定时间
    private OffsetDateTime bindTime;

    /// 最后在线时间
    private OffsetDateTime lastOnlineTime;

    /// 备注
    private String remark;

    /// 转换为返回对象
    @Override
    public IotSpeakerDeviceResult toResult() {
        IotSpeakerDeviceResult result = new IotSpeakerDeviceResult()
                .setMchNo(mchNo)
                .setDeviceSn(deviceSn)
                .setImei(imei)
                .setShopId(shopId)
                .setDeviceName(deviceName)
                .setStatus(status)
                .setBindTime(bindTime)
                .setLastOnlineTime(lastOnlineTime)
                .setRemark(remark);
        result.setId(getId());
        result.setCreateTime(getCreateTime());
        return result;
    }
}
