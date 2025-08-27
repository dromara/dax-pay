package org.dromara.daxpay.sdk.net;

import org.dromara.daxpay.sdk.response.DaxResult;
import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 请求接口
 * @author xxm
 * @since 2024/2/2
 */
@Getter
@Setter
public abstract class DaxPayRequest<T> {

    /** 商户号 */
    @Schema(description = "商户号")
    @NotBlank(message = "商户号不可为空")
    @Size(max = 32, message = "商户号不可超过32位")
    private String mchNo;

    /** 应用号 */
    @Schema(description = "应用号")
    @NotBlank(message = "应用号不可为空")
    @Size(max = 32, message = "应用号不可超过32位")
    private String appId;

    /** 客户端ip */
    @Schema(description = "客户端ip")
    @Size(max=64, message = "客户端ip不可超过64位")
    private String clientIp;

    /** 签名 */
    @Schema(description = "签名")
    @Size(max = 64, message = "签名不可超过64位")
    private String sign;

    /** 请求时间 格式yyyy-MM-dd HH:mm:ss */
    @Schema(description = "请求时间, 格式yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "请求时间必填")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime reqTime = LocalDateTime.now();

    /** 随机数 */
    @Schema(description = "随机数")
    @Size(max = 32, message = "随机数不可超过32位")
    private String nonceStr;

    /**
     * 方法请求路径
     * @return 请求路径
     */
    public abstract String path();

    /**
     * 将请求返回结果反序列化为实体类
     * @param json json字符串
     * @return 反序列后的对象
     */
    public abstract DaxResult<T> toModel(String json);

}
