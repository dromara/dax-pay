package cn.bootx.platform.notice.core.wecom.entity.msg;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.util.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import me.chanjar.weixin.cp.bean.article.NewArticle;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;

import java.util.List;

/**
 * 暂未支持
 *
 * @author xxm
 * @since 2022/7/23
 */
@Data
@Accessors(chain = true)
@Schema(title = "图文消息")
public class WeComNewsMsg implements WeComMsg {

    @Schema(description = "图文内容列表")
    private List<NewArticle> articles;

    @Override
    public WxCpMessage toMsg() {
        if (CollUtil.isEmpty(articles)) {
            throw new BizException("图文内容列表不可为空");
        }
        NewArticle[] newArticles = ArrayUtil.toArray(articles, NewArticle.class);
        return WxCpMessage.NEWS().addArticle(newArticles).build();
    }

}
