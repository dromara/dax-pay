package cn.daxpay.open.payment.merchant.param.wxverify;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 域名校验文件上传参数
///
/// 前端读取文件内容后以 JSON 提交，避免 multipart 二进制流传输；
/// 商户级仅支持 MP_verify_*.txt（微信授权回调），平台级另支持随机名 .txt 与 32位hex .html
@Data
@Accessors(chain = true)
@Schema(title = "域名校验文件上传参数")
public class WxDomainVerifyUploadParam {

    /// 文件名（微信域名验证为 MP_verify_xxx.txt；平台级另支持微信扫普通链接随机名 .txt 与支付宝 .html）
    @Schema(description = "文件名")
    @NotBlank(message = "{validation.field.fileName.notBlank}")
    private String fileName;

    /// 文件内容（验证文件纯文本内容）
    @Schema(description = "文件内容")
    @NotBlank(message = "{validation.field.fileContent.notBlank}")
    private String fileContent;

    /// 备注
    @Schema(description = "备注")
    private String remark;

}
