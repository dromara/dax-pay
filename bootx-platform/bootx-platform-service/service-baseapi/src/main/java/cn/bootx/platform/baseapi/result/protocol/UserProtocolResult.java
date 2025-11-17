package cn.bootx.platform.baseapi.result.protocol;

import cn.bootx.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户协议管理
 * @author xxm
 * @since 2025/5/9
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "用户协议管理")
public class UserProtocolResult extends BaseResult {

    /** 名称 */
    @Schema(description = "名称")
    private String name;

    /** 显示名称 */
    @Schema(description = "显示名称")
    private String showName;

    /** 类型 */
    @Schema(description = "类型")
    private String type;

    /** 默认协议 */
    @Schema(description = "默认协议")
    private Boolean defaultProtocol;

    /** 内容 */
    @Schema(description = "内容")
    private String content;
}
