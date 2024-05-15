package cn.bootx.platform.starter.wechat.dto.article;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import me.chanjar.weixin.mp.bean.freepublish.WxMpFreePublishArticles;
import me.chanjar.weixin.mp.bean.freepublish.WxMpFreePublishItem;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * 微信文章
 *
 * @author xxm
 * @since 2022/8/12
 */
@Data
@Accessors(chain = true)
@Schema(title = "微信文章")
public class WeChatArticleDto {

    @Schema(description = "文章id")
    private String articleId;

    @Schema(description = "标题组")
    private String titles;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 构建
     */
    public static WeChatArticleDto init(WxMpFreePublishItem item) {
        DateTime date = DateUtil.date(Long.parseLong(item.getUpdateTime()));
        LocalDateTime localDateTime = DateUtil.toLocalDateTime(date);
        String titles = item.getContent()
            .getNewsItem()
            .stream()
            .map(WxMpFreePublishArticles::getTitle)
            .collect(Collectors.joining(","));
        return new WeChatArticleDto().setArticleId(item.getArticleId()).setUpdateTime(localDateTime).setTitles(titles);
    }

}
