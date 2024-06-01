package cn.daxpay.single.sdk.model.allocation;

import cn.daxpay.single.sdk.net.DaxPayResponseModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 分账接收方返回结果
 * @author xxm
 * @since 2024/5/21
 */
@Getter
@Setter
@ToString(callSuper = true)
public class AllocReceiversModel extends DaxPayResponseModel {

    /** 接收方列表 */
    private List<AllocReceiverModel> receivers;

}
