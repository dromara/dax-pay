package cn.daxpay.open.platform.capability.auth.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
@Schema(title = "登录方式")
public class AuthLoginType {

    /// 编码
    private String code;
}
