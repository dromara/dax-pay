package cn.daxpay.open.channel.hkrt.entity.isv;

import cn.daxpay.open.channel.hkrt.convert.isv.HkrtIsvChannelMerchantConvert;
import cn.daxpay.open.channel.hkrt.result.isv.HkrtIsvChannelMerchantResult;
import cn.daxpay.open.payment.common.entity.merchant.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 海科融通通道商户绑定
///
/// 海科融通服务商模式下, 子商户绑定海科商户号(merchNo) + SAAS 终端号(pn),
/// 密钥(agentNo/accessId/accessKey)由服务商全局配置([HkrtIsvKeyConfig])提供。
/// 同一商户下海科商户号不重复。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "hkrt_isv_channel_merchant", autoResultMap = true)
public class HkrtIsvChannelMerchant extends MchBaseEntity implements ToResult<HkrtIsvChannelMerchantResult> {

    /// 通道商户号(HKRT+雪花)
    private String channelMchNo;

    /// 所属支付产品
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    private String product;

    /// 海科商户号(merch_no)
    private String merchNo;

    /// SAAS 终端号(pn)
    private String pn;

    /// 转换
    @Override
    public HkrtIsvChannelMerchantResult toResult() {
        return HkrtIsvChannelMerchantConvert.CONVERT.toResult(this);
    }
}
