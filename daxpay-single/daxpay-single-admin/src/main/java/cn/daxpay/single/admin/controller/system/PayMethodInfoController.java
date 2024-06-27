package cn.daxpay.single.admin.controller.system;

import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.util.ValidationUtil;
import cn.daxpay.single.service.core.system.payinfo.service.PayMethodInfoService;
import cn.daxpay.single.service.dto.system.payinfo.PayMethodInfoDto;
import cn.daxpay.single.service.param.system.payinfo.PayWayInfoParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 支付方式管理
 * @author xxm
 * @since 2024/1/8
 */
@Tag(name = "支付方式管理")
@RestController
@RequestMapping("/pay/method/info")
@RequiredArgsConstructor
public class PayMethodInfoController {
    private final PayMethodInfoService payMethodInfoService;

    @Operation(summary = "获取全部")
    @GetMapping("/findAll")
    public ResResult<List<PayMethodInfoDto>> findAll(){
        return Res.ok(payMethodInfoService.findAll());
    }

    @Operation(summary = "根据ID获取")
    @GetMapping("/findById")
    public ResResult<PayMethodInfoDto> findById(Long id){
        return Res.ok(payMethodInfoService.findById(id));
    }

    @Operation(summary = "更新")
    @PostMapping("/update")
    public ResResult<Void> update(@RequestBody PayWayInfoParam param){
        ValidationUtil.validateParam(param);
        payMethodInfoService.update(param);
        return Res.ok();
    }
}
