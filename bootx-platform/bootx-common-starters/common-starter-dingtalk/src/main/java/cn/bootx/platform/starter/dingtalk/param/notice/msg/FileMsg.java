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
@Schema(title = "钉钉文件通知")
public class FileMsg extends Msg implements Serializable {

    private static final long serialVersionUID = -835679566138176L;

    @Schema(description = "钉钉文件")
    private DingMedia file;

    public FileMsg() {
        super(DingTalkNoticeCode.MSG_FILE);
    }

    public FileMsg(String mediaId) {
        super(DingTalkNoticeCode.MSG_FILE);
        this.file = new DingMedia(mediaId);
    }

}
