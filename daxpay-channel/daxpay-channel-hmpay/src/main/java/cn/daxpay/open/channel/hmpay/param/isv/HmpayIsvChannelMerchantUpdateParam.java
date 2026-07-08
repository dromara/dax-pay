package cn.daxpay.open.channel.hmpay.param.isv;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 河马付通道商户配置更新参数
///
/// 创建时仅需核心识别字段(杉德商户号), 门店号/微信应用ID/通道渠道认证等可选配置
/// 在商户创建后通过本参数补充或修改, 对齐 lakala 等通道的"创建后编辑"惯例。
@Data
@Accessors(chain = true)
@Schema(title = "河马付通道商户配置更新参数")
public class HmpayIsvChannelMerchantUpdateParam {

    /// 通道商户号
    @Schema(description = "通道商户号")
    @NotBlank(message = "{validation.field.channelMchNo.notBlank}")
    private String channelMchNo;

    /// 门店号(storeId)
    @Schema(description = "门店号")
    private String storeId;
}
