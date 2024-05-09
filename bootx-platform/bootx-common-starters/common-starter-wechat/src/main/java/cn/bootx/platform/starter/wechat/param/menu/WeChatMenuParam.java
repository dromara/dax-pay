package cn.bootx.platform.starter.wechat.param.menu;

import cn.bootx.platform.starter.wechat.core.menu.domin.WeChatMenuInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 微信自定义菜单
 *
 * @author xxm
 * @since 2022-08-08
 */
@Data
@Schema(title = "微信自定义菜单")
@Accessors(chain = true)
public class WeChatMenuParam {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "菜单信息")
    private WeChatMenuInfo menuInfo = new WeChatMenuInfo();

    @Schema(description = "是否发布")
    private boolean publish;

    @Schema(description = "备注")
    private String remark;

}
