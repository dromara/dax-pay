package cn.bootx.platform.iam.param.client;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.annotations.Update;

/**
 * 认证应用
 *
 * @author xxm
 * @since 2022-06-27
 */
@Data
@Schema(title = "认证应用")
@Accessors(chain = true)
public class ClientParam {

    @NotNull(message = "主键不可为空", groups = {Update.class})
    @Schema(description = "主键")
    private Long id;

    @NotNull(message = "编码不可为空")
    @Schema(description = "编码")
    private String code;

    @NotNull(message = "名称不可为空")
    @Schema(description = "名称")
    private String name;

    @Schema(description = "备注")
    private String remark;

}
