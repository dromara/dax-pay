package org.dromara.daxpay.service.entity.config.checkout;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.convert.config.CheckoutConfigConvert;
import org.dromara.daxpay.service.param.config.checkout.CheckoutConfigParam;
import org.dromara.daxpay.service.result.config.checkout.CheckoutConfigVo;

/**
 * 收银台配置
 * @author xxm
 * @since 2024/11/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "pay_checkout_config")
public class CheckoutConfig extends MchAppBaseEntity implements ToResult<CheckoutConfigVo> {

    /** 收银台名称 */
    private String name;

    /** PC收银台是否同时显示聚合收银码 */
    private boolean aggregateShow;

    /** h5收银台自动升级聚合支付 */
    private boolean h5AutoUpgrade;

    public static CheckoutConfig init(CheckoutConfigParam param) {
        return CheckoutConfigConvert.CONVERT.toEntity(param);
    }

    @Override
    public CheckoutConfigVo toResult() {
        return CheckoutConfigConvert.CONVERT.toVo(this);
    }
}
