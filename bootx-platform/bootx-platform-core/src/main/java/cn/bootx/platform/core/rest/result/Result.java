package cn.bootx.platform.core.rest.result;

import cn.bootx.platform.core.code.CommonCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

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

    private String msg = "success";

    private int code = CommonCode.SUCCESS_CODE;

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
