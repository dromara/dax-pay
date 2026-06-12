package org.dromara.daxpay.platform.core.rest.result;

import org.dromara.daxpay.platform.core.code.CommonCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/// # 响应包装类
///
@Getter
@Setter
@ToString
public class Result<T> {

    @Schema(description = "响应提示")
    private String message = "success";

    @Schema(description = "响应码")
    private int code = CommonCode.SUCCESS_CODE;

    @Schema(description = "响应数据")
    private T data;

    public Result() {
        super();
    }

    public Result(int code) {
        this.code = code;
    }

    public Result(int code, T data) {
        this(code);
        this.data = data;
    }

    public Result(int code, String message) {
        this(code);
        this.message = message;
    }

    public Result(int code, T data, String message) {
        this(code, message);
        this.data = data;
    }

}
