package org.dromara.daxpay.payment.common.entity.config;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.payment.common.convert.PlatformConfigConvert;
import org.dromara.daxpay.payment.common.result.config.platform.PlatformUrlConfigResult;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 系统地址配置
 * @author xxm
 * @since 2025/6/28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_platform_url_config")
public class PlatformUrlConfig extends MpBaseEntity implements ToResult<PlatformUrlConfigResult> {
    /** 运营端网址 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String adminWebUrl;

    /** 商户端网址 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String mchWebUrl;

    /** 支付网关地址 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String gatewayServiceUrl;

    /** 网关h5地址 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String gatewayH5Url;

    public String getAdminWebUrl() {
        return StrUtil.removeSuffix(adminWebUrl, "/");
    }

    public String getMchWebUrl() {
        return StrUtil.removeSuffix(mchWebUrl, "/");
    }

    public String getGatewayServiceUrl() {
        return StrUtil.removeSuffix(gatewayServiceUrl, "/");
    }

    public String getGatewayH5Url() {
        return StrUtil.removeSuffix(gatewayH5Url, "/");
    }

    /**
     * 转换
     */
    @Override
    public PlatformUrlConfigResult toResult() {
        return PlatformConfigConvert.CONVERT.toResult(this);
    }
}
