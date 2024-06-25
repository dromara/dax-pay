package cn.daxpay.multi.admin.controller.channel;

import cn.daxpay.multi.service.result.channel.ChannelConfigResult;
import cn.daxpay.multi.service.service.channel.ChannelConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 通道配置
 * @author xxm
 * @since 2024/6/25
 */
@Tag(name = "通道配置")
@RestController
@RequestMapping("/channel/config")
@RequiredArgsConstructor
public class ChannelConfigController {
    private final ChannelConfigService channelConfigService;

    @Operation(summary = "根据应用AppId查询配置列表")
    @GetMapping("/findAllByAppId")
    public List<ChannelConfigResult> findAllByAppId(String appId){
        return channelConfigService.findAllByAppId(appId);
    }

}
