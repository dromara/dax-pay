package cn.daxpay.open.channel.lakala.result.isv;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 拉卡拉服务商密钥配置
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "拉卡拉服务商密钥配置")
public class LakalaIsvKeyConfigResult extends BaseResult {

    @Schema(description = "产品编码")
    private String product;

    @Schema(description = "拉卡拉应用编号")
    private String lklAppId;

    @Schema(description = "商户证书序列号")
    private String mchSerialNo;

    @SensitiveInfo(front = 24, end = 24)
    @Schema(description = "商户RSA私钥(加密存储)")
    private String privateKey;

    @SensitiveInfo(front = 24, end = 24)
    @Schema(description = "拉卡拉RSA公钥(加密存储)")
    private String publicKey;

    @SensitiveInfo(front = 6, end = 6)
    @Schema(description = "SM4密钥(加密存储)")
    private String sm4Key;

    @Schema(description = "机构代码")
    private String orgCode;

    @Schema(description = "是否沙箱环境")
    private Boolean sandbox;
}
