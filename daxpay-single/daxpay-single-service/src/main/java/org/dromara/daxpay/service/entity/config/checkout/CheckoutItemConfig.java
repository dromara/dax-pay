package org.dromara.daxpay.service.entity.config.checkout;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.CheckoutCallTypeEnum;
import org.dromara.daxpay.core.enums.PayMethodEnum;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.convert.config.CheckoutItemConfigConvert;
import org.dromara.daxpay.service.param.config.checkout.CheckoutItemConfigParam;
import org.dromara.daxpay.service.result.config.checkout.CheckoutItemConfigVo;

/**
 * 收银台配置项
 * @author xxm
 * @since 2024/11/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "pay_checkout_item_config")
public class CheckoutItemConfig extends MchAppBaseEntity implements ToResult<CheckoutItemConfigVo> {

    /** 类目配置Id */
    private Long groupId;

    /** 名称 */
    private String name;

    /** 图标 */
    private String icon;

    /** 排序 */
    private Double sortNo;

    /**
     * 发起调用的类型
     * @see CheckoutCallTypeEnum
     */
    private String callType;

    /**
     * 支付通道
     * @see ChannelEnum
     */
    private String channel;

    /**
     * 支付方式
     * @see PayMethodEnum
     */
    private String payMethod;

    /**
     * 构造
     */
    public static CheckoutItemConfig init(CheckoutItemConfigParam param) {
        return CheckoutItemConfigConvert.CONVERT.toEntity(param);
    }

    /**
     * 转换
     */
    @Override
    public CheckoutItemConfigVo toResult() {
        return CheckoutItemConfigConvert.CONVERT.toVo(this);
    }
}
