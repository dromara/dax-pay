package org.dromara.daxpay.payment.old.pay.entity.masterdata.method;

import org.dromara.daxpay.platform.common.mybatisplus.base.MpBaseEntity;
import org.dromara.daxpay.platform.core.enums.pay.channel.PayMethodEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付方式主数据
///
/// code 对齐 `PayMethodEnum`；展示名走 enum i18n。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_method")
public class PayMethod extends MpBaseEntity {

    /// 支付方式编码
    /// @see PayMethodEnum
    private String code;

    /// 全局排序
    private Integer sortNo;

    /// 说明
    private String description;
}