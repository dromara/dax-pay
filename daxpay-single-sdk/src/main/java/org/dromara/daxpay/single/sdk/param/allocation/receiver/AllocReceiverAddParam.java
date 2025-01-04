package org.dromara.daxpay.single.sdk.param.allocation.receiver;

import cn.hutool.core.lang.TypeReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.single.sdk.code.AllocReceiverTypeEnum;
import org.dromara.daxpay.single.sdk.code.AllocRelationTypeEnum;
import org.dromara.daxpay.single.sdk.code.ChannelEnum;
import org.dromara.daxpay.single.sdk.model.allocation.receiver.AllocReceiverAddModel;
import org.dromara.daxpay.single.sdk.net.DaxPayRequest;
import org.dromara.daxpay.single.sdk.response.DaxPayResult;
import org.dromara.daxpay.single.sdk.util.JsonUtil;

/**
 * 分账接收者添加参数
 * @author xxm
 * @since 2024/5/20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class AllocReceiverAddParam extends DaxPayRequest<AllocReceiverAddModel> {

    /** 接收者编号 */
    private String receiverNo;

    /**
     * 所属通道
     * @see ChannelEnum
     */
    private String channel;

    /**
     * 分账接收方类型 根据不同类型的通道进行传输
     * @see AllocReceiverTypeEnum
     */
    private String receiverType;

    /** 接收方账号 */
    private String receiverAccount;

    /** 接收方姓名 */
    private String receiverName;

    /**
     * 分账关系类型
     * @see AllocRelationTypeEnum
     */
    private String relationType;

    /** 关系名称 关系类型为自定义是填写 */
    private String relationName;

    /**
     * 方法请求路径
     */
    @Override
    public String path() {
        return "/unipay/alloc/receiver/add";
    }

    /**
     * 将请求返回结果反序列化为实体类
     */
    @Override
    public DaxPayResult<AllocReceiverAddModel> toModel(String json) {
        return JsonUtil.toBean(json, new TypeReference<DaxPayResult<AllocReceiverAddModel>>() {});
    }
}
