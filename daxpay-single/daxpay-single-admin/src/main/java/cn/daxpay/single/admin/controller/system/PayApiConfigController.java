package cn.daxpay.single.admin.controller.system;

import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.daxpay.single.service.core.system.config.service.PayApiConfigService;
import cn.daxpay.single.service.dto.system.config.PayApiConfigDto;
import cn.daxpay.single.service.param.system.config.PayApiConfigParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 支付接口配置
 * @author xxm
 * @since 2024/1/2
 */
@Tag(name = "支付接口配置")
@RestController
@RequestMapping("/pay/api/config")
@RequiredArgsConstructor
public class PayApiConfigController {
    private final PayApiConfigService payApiConfigService;

    @Operation(summary = "根据ID获取")
    @GetMapping("/findById")
    public ResResult<PayApiConfigDto> findById(Long id){
        return Res.ok(payApiConfigService.findById(id));
    }
    @Operation(summary = "获取全部")
    @GetMapping("/findAll")
    public ResResult<List<PayApiConfigDto>> findAll(){
        return Res.ok(payApiConfigService.findAll());
    }
    @Operation(summary = "更新")
    @PostMapping("/update")
    public ResResult<Void> update(@RequestBody PayApiConfigParam param){
        payApiConfigService.update(param);
        return Res.ok();
    }
}
