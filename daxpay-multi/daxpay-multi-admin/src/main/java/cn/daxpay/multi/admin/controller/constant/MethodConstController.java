package cn.daxpay.multi.admin.controller.constant;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付方式常量控制器
 * @author xxm
 * @since 2024/7/14
 */
@Tag(name = "支付方式常量控制器")
@RestController
@RequestMapping("/const/method")
@RequiredArgsConstructor
public class MethodConstController {
}
