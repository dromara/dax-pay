package org.dromara.daxpay.payment.pay.entity.masterdata.product;

import org.dromara.daxpay.platform.common.mybatisplus.base.MpBaseEntity;
import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.payment.pay.convert.product.PayProductConfigConvert;
import org.dromara.daxpay.payment.pay.result.masterdata.product.PayProductConfigResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付产品配置
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "pay_product_config", autoResultMap = true)
public class PayProductConfig extends MpBaseEntity implements ToResult<PayProductConfigResult> {

    /// 产品编码
    private String product;

    /// 通道编码
    private String channel;

    /// 生效环境: prod/sandbox
    private String activeEnv;

    /// 是否已配置参数
    private boolean configured;

    /// 备注
    private String remark;

    @Override
    public PayProductConfigResult toResult() {
        return PayProductConfigConvert.CONVERT.toResult(this);
    }
}
