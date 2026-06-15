package org.dromara.daxpay.channel.douyin.entity.direct;

import org.dromara.daxpay.channel.douyin.convert.direct.DouyinDirectChannelMerchantConvert;
import org.dromara.daxpay.channel.douyin.result.direct.DouyinDirectChannelMerchantResult;
import org.dromara.daxpay.payment.common.entity.merchant.MchBaseEntity;
import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 抖音直连通道商户绑定
///
/// 一个抖音商户号(dyMchId)对应一个 channelMchNo, 商户的多个应用共享此绑定。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "douyin_direct_channel_merchant", autoResultMap = true)
public class DouyinDirectChannelMerchant extends MchBaseEntity implements ToResult<DouyinDirectChannelMerchantResult> {

    /// 通道商户号(系统生成雪花号)
    private String channelMchNo;

    /// 所属支付产品
    /// @see org.dromara.daxpay.platform.core.enums.pay.channel.ProductEnum
    private String product;

    /// 抖音商户号(MCHID)
    private String dyMchId;

    /// 转换
    @Override
    public DouyinDirectChannelMerchantResult toResult() {
        return DouyinDirectChannelMerchantConvert.CONVERT.toResult(this);
    }
}
