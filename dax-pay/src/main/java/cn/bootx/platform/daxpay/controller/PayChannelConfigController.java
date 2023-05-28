package cn.bootx.platform.daxpay.controller;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.daxpay.core.channel.config.service.PayChannelConfigService;
import cn.bootx.platform.daxpay.dto.channel.config.PayChannelConfigDto;
import cn.bootx.platform.daxpay.param.channel.config.PayChannelConfigParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 支付通道配置
 *
 * @author xxm
 * @date 2023-05-24
 */
@Tag(name = "支付通道配置")
@RestController
@RequestMapping("/channel")
@RequiredArgsConstructor
public class PayChannelConfigController {

    private final PayChannelConfigService channelConfigService;

    @Operation(summary = "添加")
    @PostMapping(value = "/add")
    public ResResult<Void> add(@RequestBody PayChannelConfigParam param) {
        channelConfigService.add(param);
        return Res.ok();
    }

    @Operation(summary = "修改")
    @PostMapping(value = "/update")
    public ResResult<Void> update(@RequestBody PayChannelConfigParam param) {
        channelConfigService.update(param);
        return Res.ok();
    }

    @Operation(summary = "删除")
    @DeleteMapping(value = "/delete")
    public ResResult<Void> delete(Long id) {
        channelConfigService.delete(id);
        return Res.ok();
    }

    @Operation(summary = "通过ID查询")
    @GetMapping(value = "/findById")
    public ResResult<PayChannelConfigDto> findById(Long id) {
        return Res.ok(channelConfigService.findById(id));
    }

    @Operation(summary = "分页查询")
    @GetMapping(value = "/page")
    public ResResult<PageResult<PayChannelConfigDto>> page(PageParam pageParam,
            PayChannelConfigParam payChannelConfigParam) {
        return Res.ok(channelConfigService.page(pageParam, payChannelConfigParam));
    }

    @Operation(summary = "编码是否被使用")
    @GetMapping("/existsByCode")
    public ResResult<Boolean> existsByCode(String code) {
        return Res.ok(channelConfigService.existsByCode(code));
    }

    @Operation(summary = "编码是否被使用(不包含自己)")
    @GetMapping("/existsByCodeNotId")
    public ResResult<Boolean> existsByCode(String code, Long id) {
        return Res.ok(channelConfigService.existsByCode(code, id));
    }

}
