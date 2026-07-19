package cn.daxpay.open.platform.core.rest.dto;

import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/// # 通道商户下拉选项
///
/// 继承 [LabelValue], 额外携带通道/产品编码, 供前端在下拉项中展示支付产品图标。
/// JSON 序列化输出 `{ label, value, channelMchNo, channelMerchantName, channel, product }`,
/// 旧消费端(只读 label/value) 无感知。
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Schema(title = "通道商户下拉选项")
public class ChannelMchOption extends LabelValue {

    @Schema(description = "通道商户号(等于 value)")
    private String channelMchNo;

    @Schema(description = "通道商户名称(空时前端可回退到 channelMchNo)")
    private String channelMerchantName;

    /// 所属支付通道编码(如 wechat / alipay / lakala_pay)
    @Schema(description = "所属支付通道编码")
    private String channel;

    /// 所属支付产品编码(如 wechat_pay / lakala_pay), 前端优先用此匹配产品级图标
    @Schema(description = "所属支付产品编码")
    private String product;

    public ChannelMchOption(String channelMchNo, String channelMerchantName, String channel, String product) {
        // 简称空时回退到通道商户号, 保证下拉 label 非空
        super(StrUtil.blankToDefault(channelMerchantName, channelMchNo), channelMchNo);
        this.channelMchNo = channelMchNo;
        this.channelMerchantName = channelMerchantName;
        this.channel = channel;
        this.product = product;
    }
}
