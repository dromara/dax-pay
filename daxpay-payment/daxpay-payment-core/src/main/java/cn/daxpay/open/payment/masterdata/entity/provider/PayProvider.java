package cn.daxpay.open.payment.masterdata.entity.provider;

import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付渠道（运营元数据）
///
/// code 对齐 `PayProviderEnum`；展示名走 enum i18n。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_md_provider")
public class PayProvider extends MpBaseEntity {

    /// 支付渠道编码
    /// @see PayProviderEnum
    private String code;

    /// 图标（可选）
    private String icon;

    /// 排序
    private Integer sortNo;

    /// 是否启用
    private boolean enabled;

    /// 描述
    private String description;
}