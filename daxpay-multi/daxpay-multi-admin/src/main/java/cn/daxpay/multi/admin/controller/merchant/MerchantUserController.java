package cn.daxpay.multi.admin.controller.merchant;

import cn.bootx.platform.core.annotation.RequestGroup;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商户用户
 * @author xxm
 * @since 2024/8/23
 */
@RequestGroup(groupCode = "merchant", moduleCode = "PayConfig")
@Tag(name = "")
@RestController
@RequestMapping("/merchant/user")
@RequiredArgsConstructor
public class MerchantUserController {
}
