package org.dromara.daxpay.sdk.net;

import org.dromara.daxpay.sdk.response.DaxResult;
import org.dromara.daxpay.sdk.util.JsonUtil;
import org.dromara.daxpay.sdk.util.PaySignUtil;
import cn.hutool.http.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * 支付发起工具包
 *
 * @author xxm
 * @since 2024/2/2
 */
@Slf4j
public class DaxPayKit {

    private final DaxPayConfig config;

    public DaxPayKit(DaxPayConfig config) {
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
    public <T> DaxResult<T> execute(DaxPayRequest<T> request) {
        // 判断是否需要填充商户号和应用号
        if (Objects.isNull(request.getMchNo())) {
            request.setMchNo(config.getMchNo());
        }
        if (Objects.isNull(request.getAppId())) {
            request.setAppId(config.getAppId());
        }
        // 进行签名
        request.setSign(PaySignUtil.sign(request, config.getPrivateKey()));
        // 参数序列化
        String data = JsonUtil.toJsonStr(request);
        log.debug("请求参数:{}", data);

        String path = config.getServiceUrl() + request.path();
        String body;
        try (HttpResponse execute = HttpUtil.createPost(path)
                .body(data, ContentType.JSON.getValue())
                .timeout(config.getReqTimeout())
                .execute()) {
            // 响应码只有200 才可以进行支付
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
        if (Objects.isNull(sign)) {
            return true;
        }
        return PaySignUtil.verify(result, config.getPublicKey());
    }
}
