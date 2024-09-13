package cn.daxpay.multi.admin.controller.develop;

import cn.daxpay.multi.core.param.trade.pay.PayParam;
import cn.daxpay.multi.service.service.develop.DevelopTradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 开发调试服务商
 * @author xxm
 * @since 2024/9/6
 */
@Tag(name = "开发调试服务商")
@RestController
@RequestMapping("/develop/trade")
@RequiredArgsConstructor
public class TradeDevelopController {
    private final DevelopTradeService developTradeService;

    @Operation(summary = "支付参数签名")
    @PostMapping("/paySign")
    public String paySign(@RequestBody PayParam param) {
        return developTradeService.genSign(param);
    }
}
