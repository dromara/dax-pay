package cn.daxpay.single.service.handler;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.daxpay.single.exception.pay.PayFailureException;
import cn.daxpay.single.service.annotation.PaymentApi;
import cn.daxpay.single.service.core.system.config.dao.PayApiConfigManager;
import cn.daxpay.single.service.core.system.config.entity.PayApiConfig;
import cn.daxpay.single.service.core.system.config.service.PayApiConfigService;
import cn.bootx.platform.starter.auth.service.RouterCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.util.Objects;

/**
 * 支付接口请求检查器, 用于判断请求的支付接口是否允许被外部访问.
 * 同时如果检查的结果是放行, 同时初始化支付上下文线程对象
 * @author xxm
 * @since 2023/12/22
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PayApiCheckHandler implements RouterCheck {
    private final PayApiConfigManager apiConfigManager;
    private final PayApiConfigService apiConfigService;

    @Override
    public int sortNo() {
        return -1000;
    }

    @Override
    public boolean check(Object handler) {
        // 如果请求的接口未启用
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            PaymentApi paymentApi = handlerMethod.getMethodAnnotation(PaymentApi.class);
            if (Objects.isNull(paymentApi)){
                return false;
            }
            String code = paymentApi.value();
            PayApiConfig api = apiConfigManager.findByCode(code)
                    .orElseThrow(() -> new DataNotExistException("未找到接口信息"));
            if (!api.isEnable()){
                throw new PayFailureException("该接口权限未开放");
            }
            // 设置接口信息
            apiConfigService.initApiInfo(api);
            return true;
        }
        return false;
    }
}
