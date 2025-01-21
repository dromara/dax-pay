package org.dromara.daxpay.single.sdk.net;

import org.dromara.daxpay.single.sdk.code.SignTypeEnum;
import org.dromara.daxpay.single.sdk.response.DaxPayResult;
import org.dromara.daxpay.single.sdk.util.JsonUtil;
import org.dromara.daxpay.single.sdk.util.PaySignUtil;
import cn.hutool.http.*;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * 支付发起工具包
 * @author xxm
 * @since 2024/2/2
 */
@Slf4j
@UtilityClass
public class DaxPayKit {

    private DaxPayConfig config;

    public void initConfig(DaxPayConfig config){
        log.debug("DaxPayKit初始化...");
        DaxPayKit.config = config;
    }

    /**
     * 支付请求执行类, 默认对请求参数进行签名
     * @param request 请求参数
     * @return DaxPayResult 响应类
     * @param <T> 业务对象
     */
    public <T> DaxPayResult<T> execute(DaxPayRequest<T> request){
        return execute(request, true);
    }

    /**
     * 支付请求执行类
     * @param request 请求参数
     * @param sign 是否进行签名
     * @return DaxPayResult 响应类
     * @param <T> 业务对象
     */
    public <T> DaxPayResult<T> execute(DaxPayRequest<T> request, boolean sign){
        // 判断是否需要填充和应用号
        if (Objects.isNull(request.getAppId())){
            request.setAppId(config.getAppId());
        }
        // 判断是是否进行签名
        if (sign) {
            if (Objects.equals(SignTypeEnum.MD5,config.getSignType())){
                request.setSign(PaySignUtil.md5Sign(request, config.getSignSecret()));
            } else if (Objects.equals(SignTypeEnum.HMAC_SHA256,config.getSignType())){
                request.setSign(PaySignUtil.hmacSha256Sign(request, config.getSignSecret()));
            } else if (Objects.equals(SignTypeEnum.SM3,config.getSignType())){
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
}
