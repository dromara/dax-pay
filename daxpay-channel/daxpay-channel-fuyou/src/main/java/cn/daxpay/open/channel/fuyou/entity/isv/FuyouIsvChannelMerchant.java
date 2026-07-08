package cn.daxpay.open.channel.fuyou.entity.isv;

import cn.daxpay.open.channel.fuyou.convert.isv.FuyouIsvChannelMerchantConvert;
import cn.daxpay.open.channel.fuyou.result.isv.FuyouIsvChannelMerchantResult;
import cn.daxpay.open.payment.common.entity.merchant.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 富友通道商户绑定
///
/// 富友服务商模式下, 子商户绑定富友商户号(merchantNo/mchnt_cd) + 终端号(termNo),
/// 机构号/密钥由服务商全局配置([FuyouIsvKeyConfig])提供。
/// 同一商户号下富友商户号不重复。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "fuyou_isv_channel_merchant", autoResultMap = true)
public class FuyouIsvChannelMerchant extends MchBaseEntity implements ToResult<FuyouIsvChannelMerchantResult> {

    /// 通道商户号(FUYOU+雪花)
    private String channelMchNo;

    /// 所属支付产品
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    private String product;

    /// 富友商户号(mchnt_cd)
    private String fuyouMchNo;

    /// 终端号(term_id)
    private String termNo;

    /// 转换
    @Override
    public FuyouIsvChannelMerchantResult toResult() {
        return FuyouIsvChannelMerchantConvert.CONVERT.toResult(this);
    }
}
