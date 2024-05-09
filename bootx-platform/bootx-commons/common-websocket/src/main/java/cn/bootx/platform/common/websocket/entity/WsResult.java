package cn.bootx.platform.common.websocket.entity;

import cn.bootx.platform.common.core.code.CommonCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * websocket响应消息类
 *
 * @author xxm
 * @since 2022/6/9
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WsResult<T> implements Serializable {

    private static final long serialVersionUID = -3260481389997567266L;

    /** 类型编码 */
    private int type = CommonCode.SUCCESS_CODE;

    /** 数据体 */
    private T data;

    /** 事件编码 */
    private String eventCode;

    public WsResult(int type, T data) {
        this.type = type;
        this.data = data;
    }

}
