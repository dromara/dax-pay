package cn.daxpay.open.payment.device.speaker.entity;

import cn.daxpay.open.payment.device.enums.DeviceStatusEnum;
import cn.daxpay.open.payment.device.speaker.convert.DeviceSpeakerConvert;
import cn.daxpay.open.payment.device.speaker.result.DeviceSpeakerResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 云音箱设备
///
/// 记录云音箱设备与商户/门店的绑定关系, 真实播报对接由独立服务 dax-pay-iot 完成。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("device_speaker")
public class DeviceSpeaker extends MpBaseEntity implements ToResult<DeviceSpeakerResult> {

    /// 商户号
    private String mchNo;

    /// 厂商代码
    /// @see cn.daxpay.open.payment.device.enums.DeviceVendorEnum
    private String vendorCode;

    /// 厂商配置ID
    private Long vendorConfigId;

    /// 设备序列号(SN)
    private String deviceSn;

    /// 设备IMEI
    private String imei;

    /// 厂商门店ID
    private String shopId;

    /// 设备名称
    private String deviceName;

    /// 设备状态
    /// @see DeviceStatusEnum
    private String status;

    /// 绑定时间
    private OffsetDateTime bindTime;

    /// 最后在线时间
    private OffsetDateTime lastOnlineTime;

    /// 备注
    private String remark;

    /// 转换为返回对象
    @Override
    public DeviceSpeakerResult toResult() {
        return DeviceSpeakerConvert.CONVERT.toResult(this);
    }
}
