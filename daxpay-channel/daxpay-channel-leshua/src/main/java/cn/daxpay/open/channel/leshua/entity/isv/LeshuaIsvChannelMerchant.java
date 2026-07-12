package cn.daxpay.open.channel.leshua.entity.isv;

import cn.daxpay.open.channel.leshua.convert.isv.LeshuaIsvChannelMerchantConvert;
import cn.daxpay.open.channel.leshua.result.isv.LeshuaIsvChannelMerchantResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 乐刷通道商户绑定
///
/// 乐刷服务商模式下, 子商户绑定乐刷商户号(merchant_id),
/// 密钥由服务商全局配置([LeshuaIsvKeyConfig])提供。
/// 同一商户下乐刷商户号不重复。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "leshua_isv_channel_merchant", autoResultMap = true)
public class LeshuaIsvChannelMerchant extends MchBaseEntity implements ToResult<LeshuaIsvChannelMerchantResult> {

    /// 通道商户号(LESHUA+雪花)
    private String channelMchNo;

    /// 所属支付产品
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    private String product;

    /// 是否沙箱环境商户
    private boolean sandbox;

    /// 乐刷商户编号(merchant_id)
    private String lsMchNo;

    /// 转换
    @Override
    public LeshuaIsvChannelMerchantResult toResult() {
        return LeshuaIsvChannelMerchantConvert.CONVERT.toResult(this);
    }
}
