package cn.daxpay.open.platform.capability.file.param;

import cn.daxpay.open.platform.core.rest.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 平台文件记录分页查询参数
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "平台文件记录分页查询参数")
public class PlatformFileRecordPageParam extends PageParam {

    @Schema(description = "文件名称")
    private String filename;

    @Schema(description = "原始文件名")
    private String originalFilename;

    @Schema(description = "文件扩展名")
    private String ext;

    @Schema(description = "访问类型")
    private String accessType;

    @Schema(description = "业务分类")
    private String bizType;
}
