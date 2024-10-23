package cn.daxpay.single.sdk.model.allocation;

import cn.daxpay.single.sdk.code.AllocReceiverTypeEnum;
import cn.daxpay.single.sdk.code.AllocRelationTypeEnum;
import cn.daxpay.single.sdk.code.ChannelEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 分账接收方
 * @author xxm
 * @since 2024/5/21
 */
@Data
public class AllocReceiverModel {

    /** 分账接收方列表 */
    private List<Receiver> receivers;

    /**
     * 分账接收方
     */
    @Data
    @Accessors(chain = true)
    public static class Receiver{
        /** 分账接收方编号, 需要保证唯一 */
        private String receiverNo;

        /**
         * 所属通道
         * @see ChannelEnum
         */
        private String channel;

        /**
         * 分账接收方类型
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

        /** 关系名称 */
        private String relationName;
    }

}
