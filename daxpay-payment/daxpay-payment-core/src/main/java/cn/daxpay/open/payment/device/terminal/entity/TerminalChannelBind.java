package cn.daxpay.open.payment.device.terminal.entity;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/// # 系统终端与通道终端绑定(多对多)
///
/// 同一系统终端可绑多个通道终端; 同一通道终端也可绑多个系统终端。
/// 两端必须同属 [mchNo]。
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@TableName("pay_terminal_channel_bind")
public class TerminalChannelBind extends MchBaseEntity {

    /// 系统终端编码
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String systemTerminalNo;

    /// 通道终端主键
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long channelTerminalId;
}
