package org.dromara.daxpay.server.controller.gateway;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.util.JsonUtil;
import org.dromara.daxpay.core.result.DaxNoticeResult;
import org.dromara.daxpay.core.result.trade.pay.PayOrderResult;
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
        // 转换成实体类, 使用sdk中内置的json工具类转换
        DaxNoticeResult<PayOrderResult> x = JsonUtil.toBean(data, new TypeReference<>() {});
        // 替换成自己的密钥
        PaySignUtil.verifyHmacSha256Sign(x,"bc5b5d592cc34434a27fb57fe923dacc5374da52a4174ff5874768a8215e5fd3",x.getSign());
        // 使用map方式验签
        DaxNoticeResult<Map<String,Object>> bean = JsonUtil.toBean(data, new TypeReference<>() {});
        // 替换成自己的密钥
        PaySignUtil.verifyHmacSha256Sign(bean,"bc5b5d592cc34434a27fb57fe923dacc5374da52a4174ff5874768a8215e5fd3",bean.getSign());
        ThreadUtil.sleep(RandomUtil.randomInt(2, 7) *1000L);
        return "success";
    }
}
