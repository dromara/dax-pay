package cn.bootx.platform.daxpay.admin.controller.allocation;

import cn.bootx.platform.daxpay.service.core.payment.allocation.service.AllocationReceiverService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 对账接收方控制器
 * @author xxm
 * @since 2024/3/28
 */
@Tag(name = "对账接收方控制器")
@RestController
@RequestMapping("/allocation/receiver")
@RequiredArgsConstructor
public class AllocationReceiverController {

    private final AllocationReceiverService allocationReceiverService;
}
