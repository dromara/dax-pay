package org.dromara.daxpay.sdk.net;

import org.dromara.daxpay.sdk.code.SignTypeEnum;
import org.dromara.daxpay.sdk.response.DaxResult;
import org.dromara.daxpay.sdk.util.JsonUtil;
import org.dromara.daxpay.sdk.util.PaySignUtil;
import cn.hutool.http.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * 支付发起工具包
 * @author xxm
 * @since 2024/2/2
 */
@Slf4j
public class DaxPayAgentKit {

    private final DaxPayAgentConfig config;

    public DaxPayAgentKit(DaxPayAgentConfig config) {
        log.debug("DaxPayKit初始化...");
        this.config = config;
    }

    /**
     * 支付请求执行类, 默认对请求参数进行签名
     *
     * @param request 请求参数
     * @param <T>     业务对象
     * @return DaxResult 响应类
     */
    public <T> DaxResult<T> execute(DaxPayAgentRequest<T> request) {
        return execute(request, true);
    }

    /**
     * 支付请求执行类
     *
     * @param request 请求参数
     * @param sign    是否进行签名
     * @param <T>     业务对象
     * @return DaxResult 响应类
     */
    public <T> DaxResult<T> execute(DaxPayAgentRequest<T> request, boolean sign) {
        // 判断是否需要填充商户号和应用号
        if (Objects.isNull(request.getAgentNo())) {
            request.setAgentNo(config.getAgentNo());
        }
        // 判断是是否进行签名
        if (sign) {
            if (Objects.equals(SignTypeEnum.MD5, config.getSignType())) {
                request.setSign(PaySignUtil.md5Sign(request, config.getSignSecret()));
            } else if (Objects.equals(SignTypeEnum.HMAC_SHA256, config.getSignType())) {
                request.setSign(PaySignUtil.hmacSha256Sign(request, config.getSignSecret()));
            } else if (Objects.equals(SignTypeEnum.SM3, config.getSignType())) {
                request.setSign(PaySignUtil.sm3Sign(request, config.getSignSecret()));
            }
        }
        // 参数序列化
        String data = JsonUtil.toJsonStr(request);
        log.debug("请求参数:{}", data);

        String path = config.getServiceUrl() + request.path();
        String body;
        try (HttpResponse execute = HttpUtil.createPost(path)
                .body(data, ContentType.JSON.getValue())
                .timeout(config.getReqTimeout())
                .execute()) {
            // 响应码只有200 正常
            if (execute.getStatus() != HttpStatus.HTTP_OK) {
                log.error("请求第支付网关失败，请排查配置的支付网关地址是否正常");
                throw new HttpException("请求失败，内部异常");
            }
            body = execute.body();
        }
        log.debug("响应参数:{}", body);
        return request.toModel(body);
    }

    /**
     * 验签
     */
    public boolean verifySign(DaxResult<?> result) {
        String sign = result.getSign();
        // 如果签名值为空, 不需要进行验签
        if (Objects.isNull(sign)){
            return true;
        }
        if (Objects.equals(SignTypeEnum.MD5, config.getSignType())) {
            return PaySignUtil.verifyMd5Sign(result, config.getSignSecret(), sign);
        } else if (Objects.equals(SignTypeEnum.HMAC_SHA256, config.getSignType())) {
            return PaySignUtil.verifyHmacSha256Sign(result, config.getSignSecret(), sign);
        } else if (Objects.equals(SignTypeEnum.SM3, config.getSignType())) {
            return PaySignUtil.verifySm3Sign(result, config.getSignSecret(), sign);
        }
        throw new IllegalArgumentException("未获取到签名方式，请检查");
    }
}
