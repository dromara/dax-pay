package cn.daxpay.multi.service.entity.config;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
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
public class PlatformConfig extends MpBaseEntity {
    /** 管理平台访问地址 */
    private String adminServiceUrl;
    /** 支付网关地址 */
    private String gatewayServiceUrl;
    /** 商户平台地址 */
    private String merchantServiceUrl;
}
