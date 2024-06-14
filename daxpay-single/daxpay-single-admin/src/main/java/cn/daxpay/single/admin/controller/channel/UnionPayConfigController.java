package cn.daxpay.single.admin.controller.channel;

import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.dto.LabelValue;
import cn.daxpay.single.service.core.channel.union.service.UnionPayConfigService;
import cn.daxpay.single.service.dto.channel.union.UnionPayConfigDto;
import cn.daxpay.single.service.param.channel.union.UnionPayConfigParam;
import cn.hutool.core.codec.Base64;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 云闪付配置
 * @author xxm
 * @since 2024/3/9
 */
@Tag(name = "云闪付配置")
@RestController
@RequestMapping("/union/pay/config")
@RequiredArgsConstructor
public class UnionPayConfigController {

    private final UnionPayConfigService unionPayConfigService;

    @Operation(summary = "获取配置")
    @GetMapping("/getConfig")
    public ResResult<UnionPayConfigDto> getConfig() {
        return Res.ok(unionPayConfigService.getConfig().toDto());
    }

    @Operation(summary = "更新")
    @PostMapping("/update")
    public ResResult<Void> update(@RequestBody UnionPayConfigParam param) {
        unionPayConfigService.update(param);
        return Res.ok();
    }

    @Operation(summary = "支持的支付方式")
    @GetMapping("/findPayWays")
    public ResResult<List<LabelValue>> findPayWays() {
        return Res.ok(unionPayConfigService.findPayWays());
    }

    @SneakyThrows
    @Operation(summary = "读取证书文件内容")
    @PostMapping("/toBase64")
    public ResResult<String> toBase64(MultipartFile file){
        return Res.ok(Base64.encode(file.getBytes()));
    }

    @Operation(summary = "生成异步通知地址")
    @GetMapping("/generateNotifyUrl")
    public ResResult<String> generateNotifyUrl() {
        return Res.ok(unionPayConfigService.generateNotifyUrl());
    }

    @Operation(summary = "生成同步通知地址")
    @GetMapping("/generateReturnUrl")
    public ResResult<String> generateReturnUrl() {
        return Res.ok(unionPayConfigService.generateReturnUrl());
    }
}
