package cn.daxpay.single.sdk.model.allocation;

import cn.daxpay.single.sdk.net.DaxPayResponseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 分账接收方删除返回结果
 * @author xxm
 * @since 2024/5/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class AllocReceiverRemoveModel extends DaxPayResponseModel {
}
