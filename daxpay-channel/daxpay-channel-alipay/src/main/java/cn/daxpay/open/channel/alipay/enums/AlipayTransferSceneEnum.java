package cn.daxpay.open.channel.alipay.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/// # 支付宝转账场景枚举
///
/// 对应支付宝资金转账(alipay.fund.trans.uni.transfer)的转账场景, 场景名称(transfer_scene_name)
/// 为支付宝协议固定的中文取值, 直传支付宝。每个场景要求不同的报备字段([reportInfoTypes]),
/// 字段值为支付宝协议固定的中文 [infoType], 不可更改。
///
/// 报备字段内容(infoContent)由商户在发起转账时填写。
/// [reportInfoDescriptions] 与 [reportInfoTypes] 一一平行, 描述每个字段含义和支付宝文档示例。
@Getter
@AllArgsConstructor
public enum AlipayTransferSceneEnum {

    /// 现金营销
    CASH_MARKETING("现金营销",
            List.of("活动名称", "奖励说明"),
            List.of("请描述收款方参与活动的名称", "请描述收款方因什么奖励获取这笔资金")),

    /// 企业退款
    ENTERPRISE_REFUND("企业退款",
            List.of("退款原因"),
            List.of("请描述退款原因,如商品质量问题退款")),

    /// 佣金报酬
    COMMISSION_REWARD("佣金报酬",
            List.of("佣金报酬说明"),
            List.of("请描述接收款项原因,如8月家政服务报酬")),

    /// 业务结算
    BUSINESS_SETTLEMENT("业务结算",
            List.of("结算款项名称"),
            List.of("请描述款项名称,如材料货款")),

    /// 二手回收
    SECOND_HAND_RECYCLING("二手回收",
            List.of("回收商品名称"),
            List.of("请描述回收商品名称,如衣服")),

    /// 公益补助
    PUBLIC_WELFARE_SUBSIDY("公益补助",
            List.of("公益活动名称"),
            List.of("请描述公益活动在民政部的备案名称")),

    /// 行政补贴和退款
    ADMINISTRATIVE_SUBSIDY("行政补贴和退款",
            List.of("补贴/退款类型"),
            List.of("请描述补贴/退款类型,如某地人才补贴")),

    /// 保险理赔
    INSURANCE_CLAIM("保险理赔",
            List.of("业务类型", "保险险种", "业务交易订单号"),
            List.of("请描述业务类型,如理赔、退保、其他",
                    "请描述保险险种及产品名称,如医疗险-某百万医疗保险",
                    "请描述这笔转账的业务内部交易订单号"));

    /// 转账场景名称(支付宝协议固定中文取值, 直传 transfer_scene_name)
    private final String sceneName;

    /// 报备字段定义(支付宝协议固定中文 infoType, 顺序即报备明细下标)
    private final List<String> reportInfoTypes;

    /// 报备字段说明(与 reportInfoTypes 平行, 描述字段含义和支付宝文档示例)
    private final List<String> reportInfoDescriptions;

    /// 根据场景名称查找枚举
    public static AlipayTransferSceneEnum findBySceneName(String sceneName) {
        for (AlipayTransferSceneEnum scene : values()) {
            if (scene.sceneName.equals(sceneName)) {
                return scene;
            }
        }
        return null;
    }
}
