package cn.bootx.platform.daxpay.controller;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.dto.KeyValue;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.daxpay.core.channel.wechat.service.WeChatPayConfigService;
import cn.bootx.platform.daxpay.dto.paymodel.wechat.WeChatPayConfigDto;
import cn.bootx.platform.daxpay.param.paymodel.wechat.WeChatPayConfigParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xxm
 * @date 2021/3/19
 */
@Tag(name = "微信支付配置")
@RestController
@RequestMapping("/wechat/pay")
@AllArgsConstructor
public class WeChatPayConfigController {

    private final WeChatPayConfigService weChatPayConfigService;

    @Operation(summary = "添加微信支付配置")
    @PostMapping("/add")
    public ResResult<Void> add(@RequestBody WeChatPayConfigParam param) {
        weChatPayConfigService.add(param);
        return Res.ok();
    }

    @Operation(summary = "更新")
    @PostMapping("/update")
    public ResResult<Void> update(@RequestBody WeChatPayConfigParam param) {
        weChatPayConfigService.update(param);
        return Res.ok();
    }

    @Operation(summary = "设置启用的微信支付配置")
    @PostMapping("/setUpActivity")
    public ResResult<Void> setUpActivity(Long id) {
        weChatPayConfigService.setUpActivity(id);
        return Res.ok();
    }

    @Operation(summary = "清除指定的微信支付配置")
    @PostMapping("/clearActivity")
    public ResResult<Void> clearActivity(Long id) {
        weChatPayConfigService.clearActivity(id);
        return Res.ok();
    }

    @Operation(summary = "分页")
    @GetMapping("/page")
    public ResResult<PageResult<WeChatPayConfigDto>> page(PageParam pageParam, WeChatPayConfigParam param) {
        return Res.ok(weChatPayConfigService.page(pageParam, param));
    }

    @Operation(summary = "根据Id查询")
    @GetMapping("/findById")
    public ResResult<WeChatPayConfigDto> findById(Long id) {
        return Res.ok(weChatPayConfigService.findById(id));
    }

    @Operation(summary = "微信支持支付方式")
    @GetMapping("/findPayWayList")
    public ResResult<List<KeyValue>> findPayWayList() {
        return Res.ok(weChatPayConfigService.findPayWayList());
    }

}
