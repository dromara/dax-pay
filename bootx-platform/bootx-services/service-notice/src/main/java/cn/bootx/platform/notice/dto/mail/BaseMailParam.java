package cn.bootx.platform.notice.dto.mail;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 基础通知参数
 *
 * @author xxm
 * @since 2020/5/2 20:32
 */
@Data
@Accessors(chain = true)
public class BaseMailParam implements Serializable {

    private static final long serialVersionUID = 5270841506064102447L;

}
