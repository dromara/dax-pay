package cn.bootx.platform.notice.dto.mail;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 邮件附件参数
 *
 * @author xxm
 * @since 2020/5/2 20:33
 */
@Data
@Accessors(chain = true)
public class MailFileParam {

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件后缀
     */
    private String fileExtend;

    /**
     * 文件大小
     */
    private int fileSize;

    /**
     * 文件流
     */
    private byte[] fileInputStream;

}
