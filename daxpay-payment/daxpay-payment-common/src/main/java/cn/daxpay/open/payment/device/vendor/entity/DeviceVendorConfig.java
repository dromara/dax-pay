package cn.daxpay.open.payment.device.vendor.entity;

import cn.daxpay.open.payment.device.vendor.convert.DeviceVendorConfigConvert;
import cn.daxpay.open.payment.device.vendor.result.DeviceVendorConfigResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import cn.daxpay.open.platform.common.mybatisplus.handler.type.JsonbStringTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 设备厂商配置
///
/// 平台级存储各设备厂商的对接凭证(如 appId/appSecret), 每厂商可配多套, 敏感字段加密。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "device_vendor_config", autoResultMap = true)
public class DeviceVendorConfig extends MpBaseEntity implements ToResult<DeviceVendorConfigResult> {

    /// 设备类型
    /// @see cn.daxpay.open.payment.device.enums.DeviceTypeEnum
    private String deviceType;

    /// 厂商代码
    /// @see cn.daxpay.open.payment.device.enums.DeviceVendorEnum
    private String vendorCode;

    /// 配置名称
    private String configName;

    /// 厂商应用ID
    private String appId;

    /// 厂商应用密钥(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String appSecret;

    /// 是否启用
    private boolean enable;

    /// 扩展参数(JSON, 厂商特有字段, 以 jsonb 存储)
    @TableField(typeHandler = JsonbStringTypeHandler.class)
    private String extParam;

    /// 备注
    private String remark;

    /// 转换为返回对象
    @Override
    public DeviceVendorConfigResult toResult() {
        return DeviceVendorConfigConvert.CONVERT.toResult(this);
    }
}
