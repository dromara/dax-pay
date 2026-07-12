package cn.daxpay.open.channel.hmpay.entity.isv;

import cn.daxpay.open.channel.hmpay.convert.isv.HmpayIsvChannelMerchantConvert;
import cn.daxpay.open.channel.hmpay.result.isv.HmpayIsvChannelMerchantResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 河马付通道商户绑定
///
/// 河马付服务商模式下, 子商户绑定杉德商户号 + 门店号 + 微信 appId,
/// 密钥由服务商全局配置([HmpayIsvKeyConfig])提供。
/// 同一商户下杉德商户号不重复。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "hmpay_isv_channel_merchant", autoResultMap = true)
public class HmpayIsvChannelMerchant extends MchBaseEntity implements ToResult<HmpayIsvChannelMerchantResult> {

    /// 通道商户号(HMPAY+雪花)
    private String channelMchNo;

    /// 所属支付产品
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    private String product;

    /// 是否沙箱环境商户
    private boolean sandbox;

    /// 杉德商户编号
    private String merchantNo;

    /// 门店号(storeId)
    private String storeId;

    /// 转换
    @Override
    public HmpayIsvChannelMerchantResult toResult() {
        return HmpayIsvChannelMerchantConvert.CONVERT.toResult(this);
    }
}
