package org.dromara.daxpay.service.entity.config.checkout;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.enums.CheckoutTypeEnum;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.convert.config.CheckoutGroupConfigConvert;
import org.dromara.daxpay.service.param.config.checkout.CheckoutGroupConfigParam;
import org.dromara.daxpay.service.result.config.checkout.CheckoutGroupConfigVo;

/**
 * 收银台类目配置
 * @author xxm
 * @since 2024/11/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "pay_checkout_group_config")
public class CheckoutGroupConfig extends MchAppBaseEntity implements ToResult<CheckoutGroupConfigVo> {

    /**
     * 类型 web/h5/小程序 不包含聚合支付
     * @see CheckoutTypeEnum
     */
    private String type;

    /** 名称 */
    private String name;

    /** 图标 */
    private String icon;

    /** 排序 */
    private Double sortNo;

    public static CheckoutGroupConfig init(CheckoutGroupConfigParam param){
        return CheckoutGroupConfigConvert.CONVERT.toEntity(param);
    }

    /**
     * 转换
     */
    @Override
    public CheckoutGroupConfigVo toResult() {
        return CheckoutGroupConfigConvert.CONVERT.toVo(this);
    }
}
