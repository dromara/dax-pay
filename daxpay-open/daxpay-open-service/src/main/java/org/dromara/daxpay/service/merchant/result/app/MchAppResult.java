package org.dromara.daxpay.service.merchant.result.app;

import cn.bootx.platform.core.result.BaseResult;
import org.dromara.daxpay.core.enums.MchAppStatusEnum;
import org.dromara.daxpay.core.enums.MerchantNotifyTypeEnum;
import org.dromara.daxpay.core.enums.SignTypeEnum;
import org.dromara.daxpay.service.merchant.entity.info.Merchant;
import org.dromara.core.trans.anno.Trans;
import org.dromara.core.trans.constant.TransType;
import org.dromara.core.trans.vo.TransPojo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;

/**
 * 商户应用
 * @author xxm
 * @since 2024/6/24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@Schema(title = "商户应用")
public class MchAppResult extends BaseResult implements TransPojo {

    @Schema(description = "商户号")
    @Trans(type = TransType.SIMPLE, target = Merchant.class, fields = Merchant.Fields.mchName, ref = MchAppResult.Fields.mchName, uniqueField=Merchant.Fields.mchNo)
    private String mchNo;

    @Schema(description = "商户名称")
    private String mchName;

    /** 应用号 */
    @Schema(description = "应用号")
    private String appId;

    /** 应用名称 */
    @Schema(description = "应用名称")
    private String appName;

    /**
     * 签名方式
     * @see SignTypeEnum
     */
    @Schema(description = "签名方式")
    private String signType;

    /** 签名秘钥 */
    @Schema(description = "签名秘钥")
    private String signSecret;

    /** 是否对请求进行验签 */
    @Schema(description = "是否对请求进行验签")
    private Boolean reqSign;

    /** 是否验证请求时间是否超时 */
    @Schema(description = "是否验证请求时间是否超时")
    private Boolean reqTimeout;

    /**
     * 请求超时时间(秒)
     * 如果传输的请求时间与当前服务时间差值超过配置的时长, 将会请求失败
     */
    @Schema(description = "请求超时时间(秒)")
    private Integer reqTimeoutSecond;


    /** 支付限额 */
    @Schema(description = "支付限额")
    private BigDecimal limitAmount;

    /** 订单默认超时时间(分钟) */
    @Schema(description = "订单默认超时时间(分钟)")
    private Integer orderTimeout;

    /**
     * 状态
     * @see MchAppStatusEnum
     */
    @Schema(description = "状态")
    private String status;

    /**
     * 异步消息通知类型, 当前只支持http方式
     * @see MerchantNotifyTypeEnum
     */
    @Schema(description = "异步消息通知类型")
    private String notifyType;

    /**
     * 通知地址, http/WebSocket 需要配置
     */
    @Schema(description = "通知地址")
    private String notifyUrl;

}
