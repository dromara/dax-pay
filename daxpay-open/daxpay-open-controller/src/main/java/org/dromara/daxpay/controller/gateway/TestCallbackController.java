package org.dromara.daxpay.controller.gateway;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.util.JsonUtil;
import org.dromara.daxpay.core.result.DaxNoticeResult;
import org.dromara.daxpay.core.util.PaySignUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 测试回调接受控制器
 * @author xxm
 * @since 2024/8/30
 */
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
        DaxNoticeResult<Map<String,Object>> bean = JsonUtil.toBean(data, new TypeReference<>() {});
        PaySignUtil.verifyHmacSha256Sign(bean,"123456",bean.getSign());
        ThreadUtil.sleep(RandomUtil.randomInt(2, 7) *1000L);
        return "success";
    }
}
