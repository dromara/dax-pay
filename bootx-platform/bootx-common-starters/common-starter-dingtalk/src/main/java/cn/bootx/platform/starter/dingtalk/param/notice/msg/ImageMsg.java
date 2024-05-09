package cn.bootx.platform.starter.dingtalk.param.notice.msg;

import cn.bootx.platform.starter.dingtalk.code.DingTalkNoticeCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 钉钉图片通知
 *
 * @author xxm
 * @since 2022/7/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "钉钉图片通知")
public class ImageMsg extends Msg implements Serializable {

    private static final long serialVersionUID = -835679566138176L;

    @Schema(description = "钉钉图片")
    private DingMedia image;

    public ImageMsg() {
        super(DingTalkNoticeCode.MSG_IMAGE);
    }

    public ImageMsg(String mediaId) {
        super(DingTalkNoticeCode.MSG_IMAGE);
        this.image = new DingMedia(mediaId);
    }

}
