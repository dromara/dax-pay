package cn.bootx.platform.iam.result.client;

import cn.bootx.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 认证应用
 *
 * @author xxm
 * @since 2022-06-27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "认证应用")
@Accessors(chain = true)
public class ClientResult extends BaseResult {

    @Schema(description = "编码")
    private String code;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "是否系统内置")
    private boolean internal;

    @Schema(description = "描述")
    private String remark;

}
