package cn.daxpay.open.payment.device.qrcode.entity;

import cn.daxpay.open.payment.device.qrcode.convert.DeviceQrCodeConvert;
import cn.daxpay.open.payment.device.qrcode.result.DeviceQrCodeResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付码牌
///
/// 记录码牌与商户/应用的绑定关系。
/// 扫码链接按 [programType] 分流: H5 为 `/h/{code}`, 小程序为 `/m/{code}`。
/// 支持运营端批量创建空白码牌后划拨给商户: mchNo 初始为空, 绑定后写入。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("device_qr_code")
public class DeviceQrCode extends MpBaseEntity implements ToResult<DeviceQrCodeResult> {

    /// 码牌编码(系统自动生成, 唯一, 二维码 :code 参数)
    private String code;

    /// 码牌名称
    private String name;

    /// 批次号(批量创建空白码时写入, 单条新增可空)
    private String batchNo;

    /// 所属商户号(划拨后设置, 初始为空表示空白码)
    private String mchNo;

    /// 关联应用号(可空, 空白码为空; 绑定后空则使用商户默认应用)
    private String appId;

    /// 落地程序类型: h5 / mini_app, 创建写入后不可改
    /// @see cn.daxpay.open.payment.device.enums.QrCodeProgramTypeEnum
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String programType;

    /// 金额类型: random-自定义金额 / fixed-固定金额
    /// @see cn.daxpay.open.payment.device.enums.QrCodeAmountTypeEnum
    private String amountType;

    /// 固定金额(分, amount_type=fixed 时必填)
    private Long fixedAmount;

    /// 状态: enabled-启用 / disabled-停用
    /// @see cn.daxpay.open.payment.device.enums.QrCodeStatusEnum
    private String status;

    /// 备注
    private String remark;

    /// 转换为返回对象
    @Override
    public DeviceQrCodeResult toResult() {
        return DeviceQrCodeConvert.CONVERT.toResult(this);
    }
}
