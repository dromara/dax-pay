package org.dromara.daxpay.service.entity.assist;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.ChannelTerminalStatusEnum;
import org.dromara.daxpay.core.enums.TerminalTypeEnum;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.convert.assist.TerminalDeviceConvert;
import org.dromara.daxpay.service.param.termina.ChannelTerminalParam;
import org.dromara.daxpay.service.result.termina.ChannelTerminalResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 通道终端设备上报记录
 * @author xxm
 * @since 2025/3/7
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_channel_terminal")
public class ChannelTerminal extends MchAppBaseEntity implements ToResult<ChannelTerminalResult> {

    /** 终端设备ID */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long terminalId;

    /** 终端设备编码 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String terminalNo;

    /**
     * 通道
     * @see ChannelEnum
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channel;

    /**
     * 终端报送类型
     * @see TerminalTypeEnum
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String type;

    /**
     * 状态
     * @see ChannelTerminalStatusEnum
     */
    private String status;

    /**
     * 扩展信息
     */
    private String extra;

    /** 通道终端号 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String outTerminalNo;

    /** 错误信息 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorMsg;

    /**
     * 获取上报类型 格式: 通道编码:上报类型
     */
    public String genUpType(){
        return this.channel + ":" + this.type;
    }

    /**
     * 初始化对象
     */
    public static ChannelTerminal init(ChannelTerminalParam param){
        return TerminalDeviceConvert.CONVERT.toEntity(param);
    }

    @Override
    public ChannelTerminalResult toResult() {
        return TerminalDeviceConvert.CONVERT.toResult(this);
    }

}
