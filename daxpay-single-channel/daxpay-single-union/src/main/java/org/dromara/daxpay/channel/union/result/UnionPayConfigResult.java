package org.dromara.daxpay.channel.union.result;

import cn.bootx.platform.common.jackson.sensitive.SensitiveInfo;
import org.dromara.daxpay.core.result.MchAppResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author xxm
 * @since 2022/3/11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "云闪付配置")
public class UnionPayConfigResult extends MchAppResult {

    /** 商户号 */
    @Schema(description = "商户号")
    private String unionMachId;

    /** 是否启用, 只影响支付和退款操作 */
    @Schema(description = "是否启用")
    private Boolean enable;

    /** 支付限额 */
    @Schema(description = "支付限额")
    private BigDecimal limitAmount;

    /**
     * 商户收款账号
     */
    @Schema(description = "商户收款账号")
    private String seller;

    /**
     * 签名类型
     */
    @Schema(description = "签名类型")
    public String signType;

    /**
     * 是否为证书签名
     */
    @Schema(description = "是否为证书签名")
    private boolean certSign;

    /**
     * 应用私钥证书 字符串
     */
    @SensitiveInfo(value = SensitiveInfo.SensitiveType.OTHER, front = 15)
    @Schema(description = "应用私钥证书")
    private String keyPrivateCert;
    /**
     * 私钥证书对应的密码
     */
    @SensitiveInfo(value = SensitiveInfo.SensitiveType.PASSWORD)
    @Schema(description = "私钥证书对应的密码")
    private String keyPrivateCertPwd;

    /**
     * 中级证书
     */
    @SensitiveInfo(value = SensitiveInfo.SensitiveType.OTHER, front = 15)
    @Schema(description = "中级证书")
    private String acpMiddleCert;
    /**
     * 根证书
     */
    @SensitiveInfo(value = SensitiveInfo.SensitiveType.OTHER, front = 15)
    @Schema(description = "根证书")
    private String acpRootCert;

    /** 是否沙箱环境 */
    @Schema(description = "是否沙箱环境")
    private boolean sandbox;
}
