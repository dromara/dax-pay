package cn.daxpay.multi.service.common.context;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 平台配置
 * @author xxm
 * @since 2023/12/24
 */
@Data
@Accessors(chain = true)
public class PlatformLocal {

    /** 管理平台访问地址 */
    private String adminServiceUrl;
    /** 支付网关地址 */
    private String gatewayServiceUrl;
    /** 商户平台地址 */
    private String merchantServiceUrl;

}
