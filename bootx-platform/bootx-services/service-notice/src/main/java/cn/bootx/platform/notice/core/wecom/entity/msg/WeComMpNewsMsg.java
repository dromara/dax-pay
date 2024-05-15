package cn.bootx.platform.notice.core.wecom.entity.msg;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.util.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import me.chanjar.weixin.cp.bean.article.MpnewsArticle;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;

import java.util.List;

/**
 * mpnews类型的图文消息，跟普通的图文消息一致，唯一的差异是图文内容存储在企业微信。 多次发送mpnews，会被认为是不同的图文，阅读、点赞的统计会被分开计算。
 *
 * @author xxm
 * @since 2022/7/23
 */
@Data
@Accessors(chain = true)
@Schema(title = "图文消息(mpnews)")
public class WeComMpNewsMsg implements WeComMsg {

    @Schema(description = "图文内容列表")
    private List<MpnewsArticle> articles;

    @Override
    public WxCpMessage toMsg() {
        if (CollUtil.isEmpty(articles)) {
            throw new BizException("图文内容列表不可为空");
        }
        MpnewsArticle[] newArticles = ArrayUtil.toArray(articles, MpnewsArticle.class);
        return WxCpMessage.MPNEWS().addArticle(newArticles).build();
    }

}
