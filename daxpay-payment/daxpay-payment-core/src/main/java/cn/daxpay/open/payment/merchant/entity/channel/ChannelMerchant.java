package cn.daxpay.open.payment.merchant.entity.channel;

import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.payment.merchant.convert.channel.ChannelMerchantConvert;
import cn.daxpay.open.platform.core.enums.channel.ChannelMerchantSourceEnum;
import cn.daxpay.open.payment.merchant.result.channel.ChannelMerchantResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.util.Objects;

/// # 通道商户信息
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@FieldNameConstants
@TableName("mch_channel_merchant")
public class ChannelMerchant extends MchBaseEntity implements ToResult<ChannelMerchantResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 商户名称
    private String channelMerchantName;

    /// 所属支付产品
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String product;

    /// 是否启用
    private Boolean enable;

    /// 通道商户创建来源
    /// @see ChannelMerchantSourceEnum
    private String source;

    /// 申请单ID
    private Long applyId;

    /// 是否沙箱环境商户
    private boolean sandbox;

    public Boolean getEnable() {
        return Objects.equals(enable, true);
    }

    /// 转换
    @Override
    public ChannelMerchantResult toResult() {
        return ChannelMerchantConvert.CONVERT.toResult(this);
    }
}
