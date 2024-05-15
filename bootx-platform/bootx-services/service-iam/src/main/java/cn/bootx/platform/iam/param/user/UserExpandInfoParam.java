package cn.bootx.platform.iam.param.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * @author xxm
 * @since 2022/1/8
 */
@Data
@Accessors(chain = true)
@Schema(title = "用户扩展信息参数")
public class UserExpandInfoParam {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "性别")
    private Integer sex;

    @Schema(description = "生日")
    private LocalDate birthday;

}
