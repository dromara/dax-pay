package org.dromara.daxpay.service.entity.config.checkout;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.convert.config.CheckoutGroupConfigConvert;
import org.dromara.daxpay.service.param.config.checkout.CheckoutGroupConfigParam;
import org.dromara.daxpay.service.result.config.checkout.CheckoutGroupConfigResult;

/**
 * 收银台类目配置
 * @author xxm
 * @since 2024/11/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class CheckoutGroupConfig extends MchAppBaseEntity implements ToResult<CheckoutGroupConfigResult> {

    /** 类型 web/h5/小程序 */
    private String type;

    /** 名称 */
    private String name;

    /** 排序 */
    private Double sort;

    public static CheckoutGroupConfig init(CheckoutGroupConfigParam param){
        return CheckoutGroupConfigConvert.CONVERT.toEntity(param);
    }

    /**
     * 转换
     */
    @Override
    public CheckoutGroupConfigResult toResult() {
        return CheckoutGroupConfigConvert.CONVERT.toResult(this);
    }
}
