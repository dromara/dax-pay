package cn.daxpay.multi.service.entity.config;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.multi.service.convert.config.PlatformConfigConvert;
import cn.daxpay.multi.service.result.config.PlatformConfigResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 管理平台配置
 * @author xxm
 * @since 2024/6/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_platform_config")
public class PlatformConfig extends MpBaseEntity implements ToResult<PlatformConfigResult> {
    /** 支付网关地址 */
    private String gatewayServiceUrl;

    /**
     * 转换
     */
    @Override
    public PlatformConfigResult toResult() {
        return PlatformConfigConvert.CONVERT.toResult(this);
    }
}
