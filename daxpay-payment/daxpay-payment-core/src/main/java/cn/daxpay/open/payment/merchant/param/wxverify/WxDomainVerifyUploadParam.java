package cn.daxpay.open.payment.merchant.param.wxverify;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 微信域名验证文件上传参数
///
/// 前端读取 .txt 文件内容后以 JSON 提交，避免 multipart 二进制流传输
@Data
@Accessors(chain = true)
@Schema(title = "微信域名验证文件上传参数")
public class WxDomainVerifyUploadParam {

    /// 文件名（微信校验文件，格式 MP_verify_xxx.txt）
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
