package cn.bootx.platform.iam.param.client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

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

    @Schema(description = "是否系统内置")
    private Boolean system;

    @Schema(description = "是否可用")
    private Boolean enable;

    @Schema(description = "新注册的用户是否默认赋予该终端")
    private Boolean defaultEndow;

    @Schema(description = "关联终端")
    private List<String> loginTypeIdList;

    @Schema(description = "描述")
    private String description;

}
