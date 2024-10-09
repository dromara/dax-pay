package org.dromara.daxpay.service.common.context;

import org.dromara.daxpay.core.enums.MerchantNotifyTypeEnum;
import org.dromara.daxpay.service.enums.MchAppStautsEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 商户应用
 * @author xxm
 * @since 2024/6/27
 */
@Data
@Accessors(chain = true)
public class MchAppLocal {

    /** 应用号 */
    private String appId;

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

    /**
     * 请求有效时长(秒)
     * 如果传输的请求时间早于当前服务时间, 而且差值超过配置的时长, 将会请求失败
     * 如果传输的请求时间比服务时间大于配置的时长(超过一分钟), 将会请求失败
     */
    private Integer reqTimeout;

    /**
     * 应用状态
     * @see MchAppStautsEnum
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
}
