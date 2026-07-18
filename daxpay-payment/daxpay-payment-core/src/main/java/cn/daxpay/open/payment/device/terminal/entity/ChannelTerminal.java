package cn.daxpay.open.payment.device.terminal.entity;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.payment.device.enums.TerminalTypeEnum;
import cn.daxpay.open.payment.device.terminal.convert.ChannelTerminalConvert;
import cn.daxpay.open.payment.device.terminal.result.ChannelTerminalResult;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelTerminalStatusEnum;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/// # 通道终端台账
///
/// 挂在通道商户 [channelMchNo] 下, 人工登记通道侧终端号与状态。
/// 不调用通道报备 API; 状态由运营人工维护。
/// 与系统终端通过 [TerminalChannelBind] 多对多绑定。
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@TableName("pay_channel_terminal")
public class ChannelTerminal extends MchBaseEntity implements ToResult<ChannelTerminalResult> {

    /// 通道商户号(平台侧, 对应 [cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant#channelMchNo])
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 所属支付产品(冗余自通道商户, 便于列表)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String product;

    /// 所属通道编码(冗余自支付产品)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channel;

    /// 报送类型
    /// @see TerminalTypeEnum
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String type;

    /// 终端名称
    private String name;

    /// 通道侧终端号(人工录入, 支付二期解析用)
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String outTerminalNo;

    /// 登记状态(人工维护, 复用通道终端状态枚举)
    /// @see ChannelTerminalStatusEnum
    private String status;

    /// 错误/异常说明
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorMsg;

    /// 备注
    private String remark;

    @Override
    public ChannelTerminalResult toResult() {
        return ChannelTerminalConvert.CONVERT.toResult(this);
    }
}
