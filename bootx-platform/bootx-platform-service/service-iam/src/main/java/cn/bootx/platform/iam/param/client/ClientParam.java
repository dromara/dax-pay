package cn.bootx.platform.iam.param.client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

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

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "编码")
    private String code;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "备注")
    private String remark;

}
