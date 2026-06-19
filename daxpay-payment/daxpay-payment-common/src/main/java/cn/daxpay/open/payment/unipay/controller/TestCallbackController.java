package cn.daxpay.open.payment.unipay.controller;

import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.common.config.properties.PlatformConfigProperties;
import cn.daxpay.open.payment.common.util.PaySignUtil;
import cn.hutool.extra.spring.SpringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 测试回调接受控制器
///
@Slf4j
@Tag(name = "测试商户回调接收控制器")
@RestController
@RequestMapping("/test/callback")
@RequiredArgsConstructor
@IgnoreAuth
public class TestCallbackController {

    @Operation(summary = "notify")
    @PostMapping("/notify")
    public String notify(@RequestBody String data){
        log.info("notify:{}",data);
        var licenseKey = SpringUtil.getBean(PlatformConfigProperties.class).getKeyConfig();
        boolean verify = PaySignUtil.verify(data, licenseKey.getPublicKey());
        log.info("回调签名认证: :{}",verify);
        return "success";
    }
}
