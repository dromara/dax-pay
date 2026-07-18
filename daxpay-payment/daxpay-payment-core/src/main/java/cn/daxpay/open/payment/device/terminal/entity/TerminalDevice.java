package cn.daxpay.open.payment.device.terminal.entity;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.payment.device.terminal.convert.TerminalDeviceConvert;
import cn.daxpay.open.payment.device.terminal.result.TerminalDeviceResult;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/// # 系统终端(逻辑终端)
///
/// 系统商户下的公共逻辑设备编号, 对接方下单主要传 [terminalNo]。
/// 可归属一门店([storeNo] 可空, 门店 1:N 终端); 与通道终端通过中间表多对多绑定。
/// 本期仅系统内台账, 不调通道报备接口。
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@TableName("pay_terminal_device")
public class TerminalDevice extends MchBaseEntity implements ToResult<TerminalDeviceResult> {

    /// 系统终端编码(平台生成, 创建后不可改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String terminalNo;

    /// 终端名称
    private String name;

    /// 绑定门店号(可空; 对应 [cn.daxpay.open.payment.merchant.entity.store.MchStoreInfo#storeNo])
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String storeNo;

    /// 是否启用
    private Boolean enable;

    /// 备注
    private String remark;

    @Override
    public TerminalDeviceResult toResult() {
        return TerminalDeviceConvert.CONVERT.toResult(this);
    }
}
