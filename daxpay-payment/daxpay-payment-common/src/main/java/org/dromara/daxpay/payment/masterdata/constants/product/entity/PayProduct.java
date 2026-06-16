package org.dromara.daxpay.payment.masterdata.constants.product.entity;

import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.platform.common.mybatisplus.base.MpBaseEntity;
import org.dromara.daxpay.platform.common.mybatisplus.handler.type.StringListTypeHandler;
import org.dromara.daxpay.payment.masterdata.constants.product.convert.PayProductConvert;
import org.dromara.daxpay.payment.masterdata.constants.product.result.PayProductResult;
import org.dromara.daxpay.platform.core.enums.pay.channel.ProductEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/// # 支付产品
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "pay_product", autoResultMap = true)
public class PayProduct extends MpBaseEntity implements ToResult<PayProductResult> {

    /// 产品编码
    /// @see ProductEnum
    private String code;

    /// 关联通道编码
    private String channel;

    /// 产品介绍
    private String description;

    /// 图标
    private String icon;

    /// 支持的结算周期列表
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> settlePeriods;

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