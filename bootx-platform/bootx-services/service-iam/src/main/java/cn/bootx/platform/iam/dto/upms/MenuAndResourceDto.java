package cn.bootx.platform.iam.dto.upms;

import cn.bootx.platform.iam.dto.permission.PermMenuDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 用户菜单及资源权限返回类
 *
 * @author xxm
 * @since 2021/8/25
 */
@Data
@Accessors(chain = true)
@Schema(title = "菜单及资源权限数据类")
public class MenuAndResourceDto {

    @Schema(description = "资源权限码集合")
    private List<String> resourcePerms;

    @Schema(description = "菜单")
    private List<PermMenuDto> menus;

}
