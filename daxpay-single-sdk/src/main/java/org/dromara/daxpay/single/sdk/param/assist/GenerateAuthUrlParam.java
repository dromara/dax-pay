package org.dromara.daxpay.single.sdk.param.assist;

import cn.hutool.core.lang.TypeReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.single.sdk.model.assist.AuthUrlModel;
import org.dromara.daxpay.single.sdk.net.DaxPayRequest;
import org.dromara.daxpay.single.sdk.response.DaxPayResult;
import org.dromara.daxpay.single.sdk.util.JsonUtil;

/**
 * 生成授权链接参数
 * @author xxm
 * @since 2024/12/3
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class GenerateAuthUrlParam extends DaxPayRequest<AuthUrlModel> {

    /**
     * 支付通道
     */
    @Schema(description = "支付通道")
    private String channel;

    /**
     * 自定义授权重定向地址, 如果不传, 使用系统提供的默认地址
     */
    @Schema(description = "授权重定向地址")
    private String authRedirectUrl;

    /**
     * 方法请求路径
     *
     * @return 请求路径
     */
    @Override
    public String path() {
        return "/assist/channel/auth/generateAuthUrl";
    }

    /**
     * 将请求返回结果反序列化为实体类
     *
     * @param json json字符串
     * @return 反序列后的对象
     */
    @Override
    public DaxPayResult<AuthUrlModel> toModel(String json) {
        return JsonUtil.toBean(json, new TypeReference<DaxPayResult<AuthUrlModel>>() {});
    }
}
