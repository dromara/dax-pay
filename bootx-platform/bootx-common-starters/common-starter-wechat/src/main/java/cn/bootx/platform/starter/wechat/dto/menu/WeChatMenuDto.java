package cn.bootx.platform.starter.wechat.dto.menu;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.platform.starter.wechat.core.menu.domin.WeChatMenuInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 微信自定义菜单
 *
 * @author xxm
 * @since 2022-08-08
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "微信自定义菜单")
@Accessors(chain = true)
public class WeChatMenuDto extends BaseDto {

    @Schema(description = "名称")
    private String name;

    @Schema(description = "菜单信息")
    private WeChatMenuInfo menuInfo;

    @Schema(description = "是否发布")
    private boolean publish;

    @Schema(description = "备注")
    private String remark;

}
