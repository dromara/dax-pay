package cn.bootx.platform.notice.controller;

import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.notice.core.sms.service.SmsChannelConfigService;
import cn.bootx.platform.notice.dto.sms.SmsChannelConfigDto;
import cn.bootx.platform.notice.param.sms.SmsChannelConfigParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 *
 * @author xxm
 * @since 2023/8/5
 */
@Tag(name = "短信渠道配置")
@RestController
@RequestMapping("/sms/config")
@RequiredArgsConstructor
public class SmsChannelConfigController {
    private final SmsChannelConfigService configService;

    @Operation(summary = "添加")
    @PostMapping(value = "/add")
    public ResResult<Void> add(@RequestBody Map<String,Object> map) {
        configService.add(map);
        return Res.ok();
    }

    @Operation(summary = "修改")
    @PostMapping(value = "/update")
    public ResResult<Void> update(@RequestBody Map<String,Object> map) {
        configService.update(map);
        return Res.ok();
    }

    @Operation(summary = "通过ID查询")
    @GetMapping(value = "/findById")
    public ResResult<SmsChannelConfigDto> findById(Long id) {
        return Res.ok(configService.findById(id));
    }

    @Operation(summary = "通过Code查询")
    @GetMapping(value = "/findByCode")
    public ResResult<SmsChannelConfigDto> findByCode(String code) {
        return Res.ok(configService.findByCode(code));
    }

    @Operation(summary = "查询所有")
    @GetMapping(value = "/findAll")
    public ResResult<List<SmsChannelConfigDto>> findAll() {
        return Res.ok(configService.findAll());
    }

}
