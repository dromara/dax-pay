package cn.bootx.platform.daxpay.admin.controller.channel;

import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.dto.LabelValue;
import cn.bootx.platform.daxpay.service.core.channel.alipay.service.AliPayConfigService;
import cn.bootx.platform.daxpay.service.dto.channel.alipay.AliPayConfigDto;
import cn.bootx.platform.daxpay.service.param.channel.alipay.AliPayConfigParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author xxm
 * @since 2021/2/26
 */
@Tag(name = "支付宝配置")
@RestController
@RequestMapping("/alipay/config")
@AllArgsConstructor
public class AlipayConfigController {

    private final AliPayConfigService alipayConfigService;

    @Operation(summary = "获取配置")
    @GetMapping("/getConfig")
    public ResResult<AliPayConfigDto> getConfig() {
        return Res.ok(alipayConfigService.getConfig().toDto());
    }

    @Operation(summary = "更新")
    @PostMapping("/update")
    public ResResult<Void> update(@RequestBody AliPayConfigParam param) {
        alipayConfigService.update(param);
        return Res.ok();
    }

    @Operation(summary = "支付宝支持支付方式")
    @GetMapping("/findPayWays")
    public ResResult<List<LabelValue>> findPayWays() {
        return Res.ok(alipayConfigService.findPayWays());
    }

    @SneakyThrows
    @Operation(summary = "读取证书文件内容")
    @PostMapping("/readPem")
    public ResResult<String> readPem(MultipartFile file){
        return Res.ok(new String(file.getBytes(), StandardCharsets.UTF_8));
    }
}
