package cn.bootx.platform.daxpay.sdk.model.divide;

import cn.bootx.platform.daxpay.sdk.net.DaxPayResponseModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 分账结果(目前未支持)
 * @author xxm
 * @since 2024/2/7
 */
@Getter
@Setter
@ToString
public class DivideOrderModel extends DaxPayResponseModel {

    /** 分账状态 */
    private String status;
}
