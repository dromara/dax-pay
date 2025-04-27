package org.dromara.daxpay.service.entity.merchant;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.core.enums.MerchantTypeEnum;
import org.dromara.daxpay.core.enums.SignTypeEnum;
import org.dromara.daxpay.core.enums.MerchantNotifyTypeEnum;
import org.dromara.daxpay.service.convert.merchant.MchAppConvert;
import org.dromara.daxpay.core.enums.MchAppStatusEnum;
import org.dromara.daxpay.service.result.merchant.MchAppResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;

/**
 * 商户应用
 * @author xxm
 * @since 2024/5/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@TableName("pay_mch_app")
public class MchApp extends MpBaseEntity implements ToResult<MchAppResult> {

    /** 商户号 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String mchNo;

    /** 应用号 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String appId;

    /** 应用名称 */
    private String appName;

    /**
     * 签名方式
     * @see SignTypeEnum
     */
    private String signType;

    /** 签名秘钥 */
    private String signSecret;

    /** 是否对请求进行验签 */
    private Boolean reqSign;

    /** 支付限额 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private BigDecimal limitAmount;

    /** 订单默认超时时间(分钟) */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private Integer orderTimeout;

    /** 是否验证请求时间是否超时 */
    private Boolean reqTimeout;

    /**
     * 请求超时时间(秒)
     * 如果传输的请求时间与当前服务时间差值超过配置的时长, 将会请求失败
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
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
     * 通知地址, http/WebSocket 需要配置
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String notifyUrl;

    /**
     * 所属用户类型
     * @see MerchantTypeEnum
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String merchantType;


    @Override
    public MchAppResult toResult() {
        return MchAppConvert.CONVERT.toResult(this);
    }
}
