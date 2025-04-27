package org.dromara.daxpay.service.entity.config;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.service.convert.config.PlatformConfigConvert;
import org.dromara.daxpay.service.result.config.PlatformConfigResult;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

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

    /** 网关移动端地址 */
    private String gatewayMobileUrl;

    /** 全局单笔限额 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private BigDecimal limitAmount;

    public String getGatewayServiceUrl() {
        return StrUtil.removeSuffix(gatewayServiceUrl, "/");
    }

    public String getGatewayMobileUrl() {
        return StrUtil.removeSuffix(gatewayMobileUrl, "/");
    }

    /**
     * 转换
     */
    @Override
    public PlatformConfigResult toResult() {
        return PlatformConfigConvert.CONVERT.toResult(this);
    }
}
