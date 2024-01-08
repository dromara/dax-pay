package cn.bootx.platform.daxpay.admin.controller.system;

import cn.bootx.platform.daxpay.service.core.system.payinfo.service.PayWayInfoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author xxm
 * @since 2024/1/8
 */
@Tag(name = "")
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class PayWayInfoController {
    private final PayWayInfoService payWayInfoService;
}
