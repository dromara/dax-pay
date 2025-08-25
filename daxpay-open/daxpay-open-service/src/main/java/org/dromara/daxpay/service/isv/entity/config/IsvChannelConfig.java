package org.dromara.daxpay.service.isv.entity.config;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.bootx.platform.core.annotation.BigField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.isv.convert.config.IsvChannelConfigConvert;
import org.dromara.daxpay.service.isv.result.config.IsvChannelConfigResult;

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
    private boolean enable;

    /** 扩展存储 */
    @BigField
    private String ext;

    /**
     * 转换
     */
    @Override
    public IsvChannelConfigResult toResult() {
        return IsvChannelConfigConvert.CONVERT.toResult(this);
    }
}
