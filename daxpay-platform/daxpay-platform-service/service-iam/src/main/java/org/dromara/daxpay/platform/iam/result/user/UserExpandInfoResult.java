package org.dromara.daxpay.platform.iam.result.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.OffsetDateTime;

/// # 用户扩展信息 whole
///
@Data
@Accessors(chain = true)
@Schema(title = "用户扩展信息")
public class UserExpandInfoResult {

    @Schema(description = "用户id")
    private Long id;

    @Schema(description = "性别")
    private String sex;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "生日")
    private LocalDate birthday;

    @Schema(description = "上次登录时间 (UTC)")
    private OffsetDateTime lastLoginTime;

    @Schema(description = "本次登录时间 (UTC)")
    private OffsetDateTime currentLoginTime;

    @Schema(description = "最后登录IP")
    private String lastLoginIp;

    @Schema(description = "登录次数")
    private Integer loginCount;

    @Schema(description = "注册来源")
    private String registerSource;

    @Schema(description = "注册渠道")
    private String registerChannel;

}
