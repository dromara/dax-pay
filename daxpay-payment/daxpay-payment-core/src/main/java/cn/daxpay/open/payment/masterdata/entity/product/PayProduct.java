package cn.daxpay.open.payment.masterdata.entity.product;

import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.payment.masterdata.convert.product.PayProductConvert;
import cn.daxpay.open.payment.masterdata.result.product.PayProductResult;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付产品
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "pay_md_product", autoResultMap = true)
public class PayProduct extends MpBaseEntity implements ToResult<PayProductResult> {

    /// 产品编码
    /// @see ProductEnum
    private String code;

    /// 关联通道编码
    private String channel;

    /// 是否启用
    private boolean enabled;

    /// 产品介绍
    private String description;

    /// 排序
    private Integer sortNo;

    /// 是否支持沙箱环境
    private Boolean sandbox;

    /// 转换
    @Override
    public PayProductResult toResult() {
        return PayProductConvert.CONVERT.toResult(this);
    }
}