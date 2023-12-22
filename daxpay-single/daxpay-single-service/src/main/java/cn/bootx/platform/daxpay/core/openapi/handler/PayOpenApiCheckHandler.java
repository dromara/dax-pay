package cn.bootx.platform.daxpay.core.openapi.handler;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.daxpay.annotation.PaymentApi;
import cn.bootx.platform.daxpay.common.context.PaymentContext;
import cn.bootx.platform.daxpay.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.core.openapi.dao.PayOpenApiInfoManager;
import cn.bootx.platform.daxpay.core.openapi.entity.PayOpenApiInfo;
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
public class PayOpenApiCheckHandler implements RouterCheck {
    private final PayOpenApiInfoManager openApiInfoManager;

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
            String code = ignoreAuth.code();
            PayOpenApiInfo openApiInfo = openApiInfoManager.findByCode(code)
                    .orElseThrow(() -> new DataNotExistException("未找到接口信息"));
            if (!openApiInfo.isEnable()){
                throw new PayFailureException("该接口权限未开放");
            }
            // 初始化支付上下文
            PaymentContext paymentContext = new PaymentContext()
                    .setApiCode(code)
                    .setReqSign(openApiInfo.isReqSign())
                    .setResSign(openApiInfo.isResSign())
                    .setNotice(openApiInfo.isNotice())
                    .setNoticeSign(openApiInfo.isNoticeSign())
                    .setRecord(openApiInfo.isRecord());
            PaymentContextLocal.set(paymentContext);
            return true;
        }
        return false;
    }
}
