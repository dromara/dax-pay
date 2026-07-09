package cn.daxpay.open.channel.lakala.entity.isv;

import cn.daxpay.open.channel.lakala.convert.isv.LakalaIsvChannelMerchantConvert;
import cn.daxpay.open.channel.lakala.result.isv.LakalaIsvChannelMerchantResult;
import cn.daxpay.open.payment.common.entity.merchant.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 拉卡拉通道商户绑定
///
/// 拉卡拉服务商模式下, 子商户绑定商户号(merchantNo) + 终端号(termNo),
/// 密钥/证书由服务商全局配置([LakalaIsvKeyConfig])提供。
/// 同一商户下拉卡拉商户号不重复。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "lakala_isv_channel_merchant", autoResultMap = true)
public class LakalaIsvChannelMerchant extends MchBaseEntity implements ToResult<LakalaIsvChannelMerchantResult> {

    /// 通道商户号(LAKALA+雪花)
    private String channelMchNo;

    /// 所属支付产品
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    private String product;

    /// 是否沙箱环境商户
    private boolean sandbox;

    /// 拉卡拉商户编号(merchantNo)
    private String lakalaMchNo;

    /// 终端号
    private String termNo;

    /// 转换
    @Override
    public LakalaIsvChannelMerchantResult toResult() {
        return LakalaIsvChannelMerchantConvert.CONVERT.toResult(this);
    }
}
