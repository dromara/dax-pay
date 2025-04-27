package org.dromara.daxpay.core.context;

import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.enums.MchAppStatusEnum;
import org.dromara.daxpay.core.enums.MerchantNotifyTypeEnum;
import org.dromara.daxpay.core.enums.MerchantStatusEnum;
import org.dromara.daxpay.core.enums.MerchantTypeEnum;

import java.math.BigDecimal;

/**
 * 商户应用及相关信息
 * @author xxm
 * @since 2024/6/27
 */
@Data
@Accessors(chain = true)
public class MchAppLocal {

    /** 支付网关地址 */
    private String gatewayServiceUrl;

    /** 网关移动端地址 */
    private String gatewayMobileUrl;

    /** 商户号 */
    private final String mchNo = "M001";

    /** 商户名称 */
    private final String mchName = "默认商户";

    /**
     * 商户类型
     * @see MerchantTypeEnum
     */
    private final String mchType = MerchantTypeEnum.COMMON.getCode();

    /**
     * 商户状态
     * @see MerchantStatusEnum
     */
    private final String mchStatus = MerchantStatusEnum.ENABLE.getCode();

    /** 应用号 */
    private String appId;

    /** 应用名称 */
    private String appName;

    /** 签名方式 */
    private String signType;

    /** 签名秘钥 */
    private String signSecret;

    /** 是否对请求进行验签 */
    private Boolean reqSign;

    /** 支付限额 */
    private BigDecimal limitAmount;

    /** 订单默认超时时间(分钟) */
    private Integer orderTimeout;

    /** 是否验证请求时间是否超时 */
    private Boolean reqTimeout;

    /**
     * 请求有效时长(秒)
     * 如果传输的请求时间与当前服务时间差值超过配置的时长, 将会请求失败
     */
    private Integer reqTimeoutSecond;

    /**
     * 应用状态
     * @see MchAppStatusEnum
     */
    private String status;

    /**
     * 异步消息通知类型, 当前只支持http方式
     * @see MerchantNotifyTypeEnum
     */
    private String notifyType;

    /**
     * 地址, http/WebSocket 需要配置
     */
    private String notifyUrl;

    public Boolean getReqSign() {
        return Boolean.TRUE.equals(reqSign);
    }

    public Boolean getReqTimeout() {
        return Boolean.TRUE.equals(reqTimeout);
    }
}
