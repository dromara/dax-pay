package cn.daxpay.open.channel.vbill.entity.isv;

import cn.daxpay.open.channel.vbill.convert.isv.VbillIsvChannelMerchantConvert;
import cn.daxpay.open.channel.vbill.result.isv.VbillIsvChannelMerchantResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 随行付通道商户绑定
///
/// 随行付(天阙科技)服务商模式下, 子商户绑定天阙商户号(mno),
/// 机构号/密钥由服务商全局配置([VbillIsvKeyConfig])提供。
/// 同一商户号下天阙商户号不重复。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "vbill_isv_channel_merchant", autoResultMap = true)
public class VbillIsvChannelMerchant extends MchBaseEntity implements ToResult<VbillIsvChannelMerchantResult> {

    /// 通道商户号(VBILL+雪花)
    private String channelMchNo;

    /// 所属支付产品
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    private String product;

    /// 是否沙箱环境商户
    private boolean sandbox;

    /// 天阙商户号(mno)
    private String vbillMchNo;

    /// 转换
    @Override
    public VbillIsvChannelMerchantResult toResult() {
        return VbillIsvChannelMerchantConvert.CONVERT.toResult(this);
    }
}
