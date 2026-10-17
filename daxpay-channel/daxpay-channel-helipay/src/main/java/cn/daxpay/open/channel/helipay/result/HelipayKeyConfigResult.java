package cn.daxpay.open.channel.helipay.result;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 合利宝通道密钥配置返回结果
@Data
@Accessors(chain = true)
@Schema(title = "合利宝通道密钥配置返回结果")
public class HelipayKeyConfigResult {

    /// 通道商户号
    private String channelMchNo;

    /// 合利宝商户编号(customerNumber)
    private String customerNumber;

    /// 子商户号
    private String subCustomerNumber;

    /// 扫码产品签名密钥(脱敏返回)
    @SensitiveInfo(front = 4, end = 4)
    private String signKey;

    /// 公众号/生活号支付 AppId
    private String publicAppId;

    /// H5 支付 AppId
    private String h5AppId;

    /// 扫码支付报备ID
    private String reportId;

    /// 签名密钥是否已配置
    private boolean signKeyConfigured;
}
