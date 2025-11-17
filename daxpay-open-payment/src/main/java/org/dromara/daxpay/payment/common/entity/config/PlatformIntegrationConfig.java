package org.dromara.daxpay.payment.common.entity.config;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.payment.common.convert.PlatformConfigConvert;
import org.dromara.daxpay.payment.common.result.config.platform.PlatformIntegrationConfigResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 平台集成配置
 * @author xxm
 * @since 2025/9/13
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_platform_integration_config")
public class PlatformIntegrationConfig extends MpBaseEntity implements ToResult<PlatformIntegrationConfigResult> {

    /** 是否对请求进行验签 */
    private boolean reqSign;

    /** 是否验证请求时间是否超时 */
    private boolean reqTimeout;

    /**
     * 请求超时时间(秒)
     * 如果传输的请求时间与当前服务时间差值超过配置的时长, 将会请求失败
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private Integer apiReqTimeout;

    /**
     * 转换为结果对象
     */
    @Override
    public PlatformIntegrationConfigResult toResult() {
        return PlatformConfigConvert.CONVERT.toResult(this);
    }
}
