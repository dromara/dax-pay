package cn.bootx.platform.notice.dto.mail;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * touser 必需时使用的参数
 *
 * @author xxm
 * @since 2020/5/2 20:32
 */
@Data
@Accessors(chain = true)
public abstract class ToUserRequiredMailParam implements Serializable {

    private static final long serialVersionUID = 248630938901130468L;

}
