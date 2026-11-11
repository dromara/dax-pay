package cn.daxpay.open.payment.device.terminal.entity;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/// # 商户终端与通道终端绑定
///
/// 一条通道终端只归属一个商户终端; 同一商户终端在同一通道商户下按报送类型唯一。
/// 两端必须同属 [mchNo]。冗余 [channelMchNo]/[channel]/[type] 自通道终端,
/// 唯一校验与支付终端解析均可单表完成。
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@TableName("device_terminal_channel_bind")
public class DeviceTerminalChannelBind extends MchBaseEntity {

    /// 商户终端编码
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String systemTerminalNo;

    /// 通道终端主键
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long channelTerminalId;

    /// 通道商户号(冗余自通道终端, 支付解析与筛选用)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 通道编码(冗余自通道终端)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channel;

    /// 报送类型(冗余自通道终端, 唯一校验用)
    /// @see cn.daxpay.open.payment.device.enums.TerminalTypeEnum
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String type;
}
