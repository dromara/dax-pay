package org.dromara.daxpay.single.sdk.param.assist;

import cn.hutool.core.lang.TypeReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.single.sdk.net.DaxPayRequest;
import org.dromara.daxpay.single.sdk.response.DaxPayResult;
import org.dromara.daxpay.single.sdk.util.JsonUtil;

/**
 * 设置认证信息请求参数, 次参数不需要进行签名验证, 通过查询码进行认证信息的查询, 主要拥有内部系统使用的场景
 * @author xxm
 * @since 2024/12/3
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class AuthAndSetParam extends DaxPayRequest<Void> {

    @Schema(description = "支付通道")
    private String channel;

    @Schema(description = "认证标识码")
    private String authCode;

    /** 用于查询Code值, 不可为空 */
    @Schema(description = "查询码")
    private String queryCode;

    /**
     * 方法请求路径
     *
     * @return 请求路径
     */
    @Override
    public String path() {
        return "/assist/channel/auth/authAndSet";
    }

    /**
     * 将请求返回结果反序列化为实体类
     *
     * @param json json字符串
     * @return 反序列后的对象
     */
    @Override
    public DaxPayResult<Void> toModel(String json) {
        return JsonUtil.toBean(json, new TypeReference<DaxPayResult<Void>>() {});
    }
}
