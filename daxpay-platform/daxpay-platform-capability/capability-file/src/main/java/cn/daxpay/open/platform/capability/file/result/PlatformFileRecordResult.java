package cn.daxpay.open.platform.capability.file.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.OffsetDateTime;

/// # 平台文件记录返回结果
///
@Data
@Accessors(chain = true)
@Schema(title = "平台文件记录返回结果")
public class PlatformFileRecordResult {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "文件大小，单位字节")
    private Long size;

    @Schema(description = "文件名称（不含路径）")
    private String filename;

    @Schema(description = "原始文件名")
    private String originalFilename;

    @Schema(description = "存储路径（以/开头，不含文件名）")
    private String path;

    @Schema(description = "文件扩展名")
    private String ext;

    @Schema(description = "MIME类型")
    private String contentType;

    @Schema(description = "访问类型")
    private String accessType;

    @Schema(description = "业务分类")
    private String bizType;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private OffsetDateTime createTime;
}
