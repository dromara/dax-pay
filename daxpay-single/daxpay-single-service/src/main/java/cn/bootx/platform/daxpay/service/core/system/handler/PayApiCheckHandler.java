package cn.bootx.platform.daxpay.service.core.system.handler;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.daxpay.service.annotation.PaymentApi;
import cn.bootx.platform.daxpay.service.common.context.PaymentContext;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.system.dao.PayApiConfigManager;
import cn.bootx.platform.daxpay.service.core.system.entity.PayApiConfig;
import cn.bootx.platform.daxpay.service.core.system.service.PayApiConfigService;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
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
    private final PayApiConfigManager openApiInfoManager;
    private final PayApiConfigService openApiService;

    @Override
    public int sortNo() {
        return -1000;
    }

    @Override
    public boolean check(Object handler) {
        // 如果请求的接口未启用
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            PaymentApi ignoreAuth = handlerMethod.getMethodAnnotation(PaymentApi.class);
            if (Objects.isNull(ignoreAuth)){
                return false;
            }
            String code = ignoreAuth.value();
            PayApiConfig api = openApiInfoManager.findByCode(code)
                    .orElseThrow(() -> new DataNotExistException("未找到接口信息"));
            if (!api.isEnable()){
                throw new PayFailureException("该接口权限未开放");
            }
            // 初始化支付上下文
            PaymentContext paymentContext = new PaymentContext();
            PaymentContextLocal.set(paymentContext);
            // 设置接口信息
            openApiService.initApiInfo(api);
            return true;
        }
        return false;
    }
}
