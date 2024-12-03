package org.dromara.daxpay.single.sdk.param.trade.pay;

import cn.hutool.core.lang.TypeReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.single.sdk.code.CheckoutTypeEnum;
import org.dromara.daxpay.single.sdk.model.trade.pay.CheckoutUrlModel;
import org.dromara.daxpay.single.sdk.net.DaxPayRequest;
import org.dromara.daxpay.single.sdk.response.DaxPayResult;
import org.dromara.daxpay.single.sdk.util.JsonUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 收银台创建参数
 * @author xxm
 * @since 2024/12/3
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class CheckoutCreatParam extends DaxPayRequest<CheckoutUrlModel> {

    /**
     * 收银台类型
     * @see CheckoutTypeEnum
     */
    @Size(max = 100, message = "收银台类型不可超过32位")
    private String checkoutType;

    /** 商户订单号 */
    @Schema(description = "商户订单号")
    private String bizOrderNo;

    /** 支付标题 */
    @Schema(description = "支付标题")
    private String title;

    /** 支付描述 */
    @Schema(description = "支付描述")
    private String description;

    /** 是否开启分账 */
    @Schema(description = "是否开启分账")
    private Boolean allocation;

    /** 自动分账 */
    @Schema(description = "自动分账")
    private Boolean autoAllocation;

    /** 过期时间 */
    @Schema(description = "过期时间")
    private LocalDateTime expiredTime;

    /** 支付金额 */
    @Schema(description = "支付金额")
    private BigDecimal amount;

    /**
     * 支付扩展参数
     */
    @Schema(description = "支付扩展参数")
    private String extraParam;

    /** 商户扩展参数,回调时会原样返回 */
    @Schema(description = "商户扩展参数")
    private String attach;

    /** 同步跳转地址, 支付完毕后用户浏览器返回到该地址, 不传输跳转到默认地址 */
    @Schema(description = "同步通知URL")
    private String returnUrl;

    /** 异步通知地址 */
    @Schema(description = "异步通知地址")
    private String notifyUrl;

    /**
     * 方法请求路径
     *
     * @return 请求路径
     */
    @Override
    public String path() {
        return "/unipay/checkout/creat";
    }

    /**
     * 将请求返回结果反序列化为实体类
     *
     * @param json json字符串
     * @return 反序列后的对象
     */
    @Override
    public DaxPayResult<CheckoutUrlModel> toModel(String json) {
        return JsonUtil.toBean(json, new TypeReference<DaxPayResult<CheckoutUrlModel>>() {});
    }
}
