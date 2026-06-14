package org.dromara.daxpay.channel.alipay.entity.direct;

import org.dromara.daxpay.channel.alipay.convert.direct.AlipayDirectChannelMerchantConvert;
import org.dromara.daxpay.channel.alipay.result.direct.AlipayDirectChannelMerchantResult;
import org.dromara.daxpay.payment.common.entity.merchant.MchBaseEntity;
import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付宝直连通道商户绑定
///
/// 一个商户PID对应一个channelMchNo, 商户的多个应用共享此绑定。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "mch_alipay_direct_channel_merchant", autoResultMap = true)
public class AlipayDirectChannelMerchant extends MchBaseEntity implements ToResult<AlipayDirectChannelMerchantResult> {

    /// 通道商户号(系统生成雪花号)
    private String channelMchNo;

    /// 所属支付产品
    /// @see org.dromara.daxpay.platform.core.enums.pay.channel.ProductEnum
    private String product;

    /// 支付宝商家唯一识别码(2088开头的16位数字)
    private String alipayUserId;

    /// 转换
    @Override
    public AlipayDirectChannelMerchantResult toResult() {
        return AlipayDirectChannelMerchantConvert.CONVERT.toResult(this);
    }
}
