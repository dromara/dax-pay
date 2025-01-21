package cn.bootx.platform.starter.file.result;

import cn.bootx.platform.core.result.BaseResult;
import cn.bootx.platform.starter.file.code.FilePlatformTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 文件存储平台
 * @author xxm
 * @since 2024/8/12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "文件存储平台")
public class FilePlatformResult extends BaseResult {

    /**
     * 平台类型
     * @see FilePlatformTypeEnum
     */
    @Schema(description = "平台类型")
    private String type;

    /** 名称 */
    @Schema(description = "名称")
    private String name;

    /** 默认存储平台 */
    @Schema(description = "默认存储平台")
    private Boolean defaultPlatform;

    /** 访问地址 */
    @Schema(description = "访问地址")
    private String url;
}
