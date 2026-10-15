package cn.daxpay.open.channel.fuyou.result.direct;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 富友直连商户密钥配置结果
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "富友直连商户密钥配置")
public class FuyouDirectKeyConfigResult extends BaseResult {

    @Schema(description = "平台商户号")
    private String mchNo;

    @Schema(description = "通道商户号(FUYOU+雪花)")
    private String channelMchNo;

    @Schema(description = "所属支付产品")
    private String product;

    @Schema(description = "富友商户号(mchnt_cd)")
    private String mchntCd;

    @SensitiveInfo(front = 6, end = 4)
    @Schema(description = "商户密钥(mchnt_key, 加密存储)")
    private String mchntKey;

    @Schema(description = "终端号(term_id)")
    private String termNo;

    @Schema(description = "是否沙箱环境")
    private Boolean sandbox;
}
