package cn.daxpay.open.channel.leshua.result.isv;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 乐刷服务商密钥配置
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "乐刷服务商密钥配置")
public class LeshuaIsvKeyConfigResult extends BaseResult {

    @Schema(description = "产品编码")
    private String product;

    @Schema(description = "乐刷商户号")
    private String lsMchNo;

    @SensitiveInfo(front = 8, end = 8)
    @Schema(description = "交易密钥(加密存储)")
    private String tradeKey;

    @SensitiveInfo(front = 8, end = 8)
    @Schema(description = "异步通知密钥(加密存储)")
    private String notifyKey;

    @Schema(description = "签名类型(MD5 / SM3)")
    private String signType;

    @Schema(description = "乐刷服务商号")
    private String lsIsvNo;

    @Schema(description = "是否沙箱环境")
    private Boolean sandbox;
}
