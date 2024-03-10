package cn.bootx.platform.daxpay.admin.controller.channel;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 云闪付控制器
 * @author xxm
 * @since 2024/3/9
 */
@Tag(name = "云闪付控制器")
@RestController
@RequestMapping("/union/pay")
@RequiredArgsConstructor
public class UnionPayController {
}
