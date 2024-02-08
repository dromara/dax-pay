package cn.bootx.platform.daxpay.sdk.net;

import cn.bootx.platform.daxpay.sdk.code.SignTypeEnum;
import cn.bootx.platform.daxpay.sdk.response.DaxPayResult;
import cn.bootx.platform.daxpay.sdk.util.PaySignUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
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
    public <T extends DaxPayResponseModel> DaxPayResult<T> execute(DaxPayRequest<T> request){
        return execute(request, true);
    }

    /**
     * 支付请求执行类
     * @param request 请求参数
     * @param sign 是否进行签名
     * @return DaxPayResult 响应类
     * @param <T> 业务对象
     */
    public <T extends DaxPayResponseModel> DaxPayResult<T> execute(DaxPayRequest<T> request, boolean sign){
        // 判断是是否进行签名
        if (sign) {
            if (Objects.equals(SignTypeEnum.MD5, config.getSignType())){
                String md5Sign = PaySignUtil.md5Sign(request, config.getSignSecret());
                request.setSign(md5Sign);
            } else {
                String hmacSha256Sign = PaySignUtil.hmacSha256Sign(request, config.getSignSecret());
                request.setSign(hmacSha256Sign);
            }
        }
        String data = JSONUtil.toJsonStr(request);
        log.debug("请求参数:{}", data);
        String path = config.getServiceUrl() + request.path();
        HttpResponse execute = HttpUtil.createPost(path)
                .body(data, ContentType.JSON.getValue())
                .timeout(config.getReqTimeout())
                .execute();
        String body = execute.body();
        log.debug("响应参数:{}", body);
        return request.toModel(body);
    }
}
