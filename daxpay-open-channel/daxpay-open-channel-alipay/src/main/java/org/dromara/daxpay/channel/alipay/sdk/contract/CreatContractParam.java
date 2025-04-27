package org.dromara.daxpay.channel.alipay.sdk.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 开启代商户签约事务
 * @author xxm
 * @since 2024/10/31
 */
@Data
@Accessors(chain = true)
public class CreatContractParam {

    /**
     * 【描述】isv代操作的商户账号，可以是支付宝账号，也可以是pid（2088开头）
     * 【示例值】test@alipay.com
     */
    private String account;

    /**
     * 【描述】商户联系人信息，包含联系人名称、手机、邮箱信息。联系人信息将用于接受签约后的重要通知，如确认协议、到期提醒等。
     */
    @JsonProperty("contact_info")
    private ContactInfo contactInfo;

    /**
     * 【描述】订单授权凭证。若传入本参数，则对应事务提交后进入预授权模式。
     * 【示例值】00ee2d475f374ad097ee0f1ac223fX00
     */
    @JsonProperty("order_ticket")
    private String orderTicket;

    /**
     * 商户联系人信息
     * @author xxm
     * @since 2024/10/31
     */
    @Data
    @Accessors(chain = true)
    public static class ContactInfo{
        /**
         * 【描述】联系人名称
         * 【示例值】张三
         */
        @JsonProperty("contact_name")
        private String contactName;
        /**
         * 【描述】联系人手机号码
         * 【示例值】18866668888
         */
        @JsonProperty("contact_mobile")
        private String contactMobile;
        /**
         * 【描述】联系人邮箱
         * 【示例值】zhangsan@alipy.com
         */
        @JsonProperty("contact_email")
        private String contactEmail;

    }
}
