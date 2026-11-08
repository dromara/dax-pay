package cn.daxpay.open.payment.merchant.param.channel;

import cn.daxpay.open.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 通道商户信息查询参数
///
/// 字段名即查询列(QueryGenerator 按下划线转换生成条件), 必须与实体
/// [cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant] 字段同名, 否则生成不存在的列。
@Data
@Accessors(chain = true)
@Schema(title = "通道商户信息查询参数")
public class ChannelMerchantQuery {

    /// 通道商户号(对应 channel_mch_no 列)
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "通道商户号")
    private String channelMchNo;

    /// 通道商户名称
    @Schema(description = "通道商户名称")
    private String channelMerchantName;

    /// 所属商户
    @Schema(description = "所属商户")
    private String mchNo;

    /// 是否沙箱环境商户
    @Schema(description = "是否沙箱环境商户")
    private Boolean sandbox;

}
