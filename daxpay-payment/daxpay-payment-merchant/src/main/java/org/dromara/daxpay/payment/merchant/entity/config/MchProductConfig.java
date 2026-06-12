package org.dromara.daxpay.payment.merchant.entity.config;

import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.payment.common.entity.merchant.MchBaseEntity;
import org.dromara.daxpay.payment.merchant.convert.config.MchProductConfigConvert;
import org.dromara.daxpay.payment.merchant.result.config.MchProductConfigResult;
import org.dromara.daxpay.platform.core.enums.pay.channel.ProductEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 商户产品配置
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("mch_product_config")
public class MchProductConfig extends MchBaseEntity implements ToResult<MchProductConfigResult> {

    /// 产品编码
    /// @see ProductEnum
    private String product;

    /// 通道编码
    private String channel;

    /// 是否启用
    private boolean enable;

    @Override
    public MchProductConfigResult toResult() {
        return MchProductConfigConvert.CONVERT.toResult(this);
    }
}

