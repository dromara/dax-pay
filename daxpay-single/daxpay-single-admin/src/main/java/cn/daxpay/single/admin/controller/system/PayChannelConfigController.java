package cn.daxpay.single.admin.controller.system;

import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.daxpay.single.service.core.system.config.service.PayChannelConfigService;
import cn.daxpay.single.service.dto.system.config.PayChannelConfigDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 支付通道信息
 * @author xxm
 * @since 2024/1/8
 */
@Tag(name = "支付通道信息")
@RestController
@RequestMapping("/pay/channel/config")
@RequiredArgsConstructor
public class PayChannelConfigController {
    private final PayChannelConfigService payChannelConfigService;

    @Operation(summary = "查询全部")
    @GetMapping("/findAll")
    public ResResult<List<PayChannelConfigDto>> findAll(){
        List<PayChannelConfigDto> all = payChannelConfigService.findAll();
        return Res.ok(all);
    }

    @Operation(summary = "根据ID获取")
    @GetMapping("/findById")
    public ResResult<PayChannelConfigDto> findById(Long id){
        return Res.ok(payChannelConfigService.findById(id));
    }
}
