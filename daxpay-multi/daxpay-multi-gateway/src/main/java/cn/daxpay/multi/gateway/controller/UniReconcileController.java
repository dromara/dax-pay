package cn.daxpay.multi.gateway.controller;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 对账接口处理器
 * @author xxm
 * @since 2024/6/4
 */
@IgnoreAuth
@Tag(name = "对账接口处理器")
@RestController
@RequestMapping("/unipay/reconcile")
@RequiredArgsConstructor
public class UniReconcileController {
}
