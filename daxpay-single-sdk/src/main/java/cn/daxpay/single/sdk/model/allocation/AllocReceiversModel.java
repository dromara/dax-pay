package cn.daxpay.single.sdk.model.allocation;

import lombok.Data;

import java.util.List;

/**
 * 分账接收方返回结果
 * @author xxm
 * @since 2024/5/21
 */
@Data
public class AllocReceiversModel{

    /** 接收方列表 */
    private List<AllocReceiverModel> receivers;

}
