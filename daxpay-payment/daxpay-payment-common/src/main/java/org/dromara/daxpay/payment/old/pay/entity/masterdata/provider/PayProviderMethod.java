package org.dromara.daxpay.payment.old.pay.entity.masterdata.provider;

import org.dromara.daxpay.platform.common.mybatisplus.base.MpBaseEntity;
import org.dromara.daxpay.platform.core.enums.pay.channel.PayMethodEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 渠道下支付方式目录项
///
/// pay_provider + method 关联 `pay_method.code` 主数据。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_provider_method")
public class PayProviderMethod extends MpBaseEntity {

    /// 支付渠道编码
    private String provider;

    /// 支付方式编码
    /// @see PayMethodEnum
    private String method;

    /// 渠道内排序
    private Integer sortNo;

    /// 目录项说明
    private String description;
}