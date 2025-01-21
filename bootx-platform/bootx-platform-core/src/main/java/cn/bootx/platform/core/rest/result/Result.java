package cn.bootx.platform.core.rest.result;

import cn.bootx.platform.core.code.CommonCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 响应包装类
 *
 * @author xxm
 * @since 2020/1/22 15:26
 */
@Getter
@Setter
@ToString
public class Result<T> {

    @Schema(description = "响应提示")
    private String msg = "success";

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

    public Result(int code, String msg) {
        this(code);
        this.msg = msg;
    }

    public Result(int code, T data, String msg) {
        this(code, msg);
        this.data = data;
    }

}
