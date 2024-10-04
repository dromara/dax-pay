package cn.bootx.platform.starter.auth.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xxm
 * @since 2021/8/25
 */
@Data
@Accessors(chain = true)
@Schema(title = "登录方式")
public class AuthLoginType {

    /** 编码 */
    private String code;
}
