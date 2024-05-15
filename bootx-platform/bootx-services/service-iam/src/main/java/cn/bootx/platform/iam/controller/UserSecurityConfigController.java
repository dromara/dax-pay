package cn.bootx.platform.iam.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户安全配置管理
 * @author xxm
 * @since 2023/9/25
 */
@Tag(name = "用户安全配置管理")
@RestController
@RequestMapping("/user/security")
@RequiredArgsConstructor
public class UserSecurityConfigController {
}
