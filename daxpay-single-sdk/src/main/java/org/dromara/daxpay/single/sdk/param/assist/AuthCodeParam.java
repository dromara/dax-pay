package org.dromara.daxpay.single.sdk.param.assist;

import cn.hutool.core.lang.TypeReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.single.sdk.model.assist.AuthModel;
import org.dromara.daxpay.single.sdk.net.DaxPayRequest;
import org.dromara.daxpay.single.sdk.response.DaxPayResult;
import org.dromara.daxpay.single.sdk.util.JsonUtil;

/**
 * 获取认证信息请求参数
 * @author xxm
 * @since 2024/12/3
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class AuthCodeParam extends DaxPayRequest<AuthModel> {


    @Schema(description = "支付通道")
    private String channel;

    @Schema(description = "认证标识码")
    private String authCode;

    /** 用于查询Code值, 可以为空 */
    @Schema(description = "查询码")
    private String queryCode;

    /**
     * 方法请求路径
     *
     * @return 请求路径
     */
    @Override
    public String path() {
        return "/assist/channel/auth/auth";
    }

    /**
     * 将请求返回结果反序列化为实体类
     *
     * @param json json字符串
     * @return 反序列后的对象
     */
    @Override
    public DaxPayResult<AuthModel> toModel(String json) {
        return JsonUtil.toBean(json, new TypeReference<DaxPayResult<AuthModel>>() {});
    }
}
