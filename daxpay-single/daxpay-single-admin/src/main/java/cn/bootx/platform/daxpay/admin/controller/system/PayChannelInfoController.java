package cn.bootx.platform.daxpay.admin.controller.system;

import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.daxpay.service.core.system.payinfo.service.PayChannelInfoService;
import cn.bootx.platform.daxpay.service.dto.system.payinfo.PayChannelInfoDto;
import cn.bootx.platform.daxpay.service.param.system.payinfo.PayChannelInfoParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 支付通道信息
 * @author xxm
 * @since 2024/1/8
 */
@Tag(name = "支付通道信息")
@RestController
@RequestMapping("/pay/channel/info")
@RequiredArgsConstructor
public class PayChannelInfoController {
    private final PayChannelInfoService payChannelInfoService;

    @Operation(summary = "查询全部")
    @GetMapping("/findAll")
    public ResResult<List<PayChannelInfoDto>> findAll(){
        return Res.ok(payChannelInfoService.findAll());
    }

    @Operation(summary = "根据ID获取")
    @GetMapping("/findById")
    public ResResult<PayChannelInfoDto> findById(Long id){
        return Res.ok(payChannelInfoService.findById(id));
    }

    @Operation(summary = "更新")
    @PostMapping("/update")
    public ResResult<Void> update(@RequestBody PayChannelInfoParam param){
        payChannelInfoService.update(param);
        return Res.ok();
    }
}
