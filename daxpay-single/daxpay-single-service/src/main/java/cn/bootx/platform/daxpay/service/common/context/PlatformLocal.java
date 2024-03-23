package cn.bootx.platform.daxpay.service.common.context;

import cn.bootx.platform.daxpay.code.PaySignTypeEnum;
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

    /** 网站地址 */
    private String websiteUrl;

    /**
     * 签名方式
     * @see PaySignTypeEnum
     */
    private String signType;

    /** 签名秘钥 */
    private String signSecret;

    /** 支付通知地址 */
    private String notifyUrl;

    /** 支付同步跳转地址 */
    private String returnUrl;

    /** 订单支付超时(分钟) */
    private Integer orderTimeout;

    /** 支付限额 */
    private Integer singleLimit;
}
