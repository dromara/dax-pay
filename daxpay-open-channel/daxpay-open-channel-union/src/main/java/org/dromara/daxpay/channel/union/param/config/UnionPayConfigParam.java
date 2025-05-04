package org.dromara.daxpay.channel.union.param.config;

import org.dromara.daxpay.channel.union.code.UnionPayCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 云闪付支付配置参数
 * @author xxm
 * @since 2024/3/9
 */
@Data
@Accessors(chain = true)
@Schema(title = "云闪付支付配置参数")
public class UnionPayConfigParam {

    /** 主键 */
    @Schema(description = "主键")
    private Long id;

    /** 银联商户号 */
    @Schema(description = "银联商户号")
    private String unionMachId;

    /** 是否启用, 只影响支付和退款操作 */
    @Schema(description = "是否启用")
    private Boolean enable;

    @Schema(description = "支付限额")
    private Integer limitAmount;

    /**
     * 商户收款账号
     */
    @Schema(description = "商户收款账号")
    private String seller;

    /**
     * 签名类型
     * @see UnionPayCode.SignType
     */
    @Schema(description = "签名类型")
    public String signType;

    /**
     * 是否为证书签名
     */
    @Schema(description = "是否为证书签名")
    private boolean certSign;

    /**
     * 应用私钥证书
     */
    @Schema(description = "应用私钥证书")
    private String keyPrivateCert;
    /**
     * 私钥证书对应的密码
     */
    @Schema(description = "私钥证书对应的密码")
    private String keyPrivateCertPwd;

    /**
     * 中级证书
     */
    @Schema(description = "中级证书")
    private String acpMiddleCert;
    /**
     * 根证书
     */
    @Schema(description = "根证书")
    private String acpRootCert;

    /** 是否沙箱环境 */
    @Schema(description = "是否沙箱环境")
    private boolean sandbox;

    /** 商户AppId */
    @Schema(description = "商户AppId")
    @NotBlank(message = "商户AppId不可为空")
    private String appId;
}
