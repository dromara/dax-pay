package org.dromara.daxpay.payment.isv.entity.config;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.payment.isv.convert.config.IsvChannelConfigConvert;
import org.dromara.daxpay.payment.isv.result.config.IsvChannelConfigResult;
import org.dromara.daxpay.payment.pay.enums.ChannelEnum;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 服务商通道配置
 * @author xxm
 * @since 2024/10/30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_isv_channel_config")
public class IsvChannelConfig extends MpBaseEntity implements ToResult<IsvChannelConfigResult> {

    /**
     * 服务商通道
     * @see ChannelEnum
     */
    private String channel;

    /** 是否启用 */
    private Boolean enable;


    /** 服务商号 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String isvNo;

    /**
     * 转换
     */
    @Override
    public IsvChannelConfigResult toResult() {
        return IsvChannelConfigConvert.CONVERT.toResult(this);
    }
}
