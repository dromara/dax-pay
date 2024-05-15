package cn.bootx.platform.starter.wechat.dto.media;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialFileBatchGetResult.WxMaterialFileBatchGetNewsItem;

import java.time.LocalDateTime;

/**
 * @author xxm
 * @since 2022/8/12
 */
@Data
@Accessors(chain = true)
@Schema(title = "微信素材")
public class WeChatMediaDto {

    @Schema(description = "媒体id")
    private String mediaId;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "链接地址")
    private String url;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    public static WeChatMediaDto init(WxMaterialFileBatchGetNewsItem item) {
        return new WeChatMediaDto().setMediaId(item.getMediaId())
            .setName(item.getName())
            .setUrl(item.getUrl())
            .setUpdateTime(LocalDateTimeUtil.of(item.getUpdateTime()));
    }

}
