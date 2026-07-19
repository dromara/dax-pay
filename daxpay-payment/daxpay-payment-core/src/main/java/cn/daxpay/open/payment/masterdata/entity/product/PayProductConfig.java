package cn.daxpay.open.payment.masterdata.entity.product;

import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.payment.masterdata.convert.product.PayProductConfigConvert;
import cn.daxpay.open.payment.masterdata.result.product.PayProductConfigResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付产品配置
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "pay_md_product_config", autoResultMap = true)
public class PayProductConfig extends MpBaseEntity implements ToResult<PayProductConfigResult> {

    /// 产品编码
    private String product;

    /// 通道编码
    private String channel;

    /// 生效环境: prod/sandbox
    private String activeEnv;

    /// 备注
    private String remark;

    /// 转换
    @Override
    public PayProductConfigResult toResult() {
        return PayProductConfigConvert.CONVERT.toResult(this);
    }
}
