package cn.daxpay.open.channel.douyin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/// # 抖音转账场景枚举
///
/// 对应抖音商家转账(/v1/fund_trade/mch-transfer/transfer-bills)的转账场景,
/// 场景ID 为固定枚举值(1001-1007),商户在抖音商户平台「产品中心-商家转账到抖音零钱」开通。
/// 每个场景要求不同的报备字段([reportInfoTypes]), 字段值为抖音协议固定的中文 [infoType], 不可更改。
///
/// 报备字段内容(infoContent)由商户在发起转账时填写。
/// [reportInfoDescriptions] 与 [reportInfoTypes] 一一平行, 描述每个字段的含义和抖音文档示例。
/// [userRecvPerceptionOptions] 为收款人在抖音中看到的感知文案可选值, 不传时抖音按场景取默认(第一个)。
@Getter
@AllArgsConstructor
public enum DouyinTransferSceneEnum {

    /// 现金营销
    CASH_MARKETING("1001", "现金营销",
            List.of("活动名称", "奖励说明"),
            List.of("商户自定义内容，如「新会员有礼」", "商户自定义内容，如「注册会员抽奖一等奖」"),
            List.of("活动奖励", "现金奖励")),

    /// 企业赔付
    ENTERPRISE_COMPENSATION("1002", "企业赔付",
            List.of("赔付原因"),
            List.of("商户自定义内容，如「商品质量问题退款」"),
            List.of("退款", "商家赔付")),

    /// 佣金报酬
    COMMISSION_REWARD("1003", "佣金报酬",
            List.of("岗位类型", "报酬说明"),
            List.of("商户自定义内容，如「外卖员」", "商户自定义内容，如「7月份配送费」"),
            List.of("劳务报酬", "报销款", "企业补贴", "开工利是")),

    /// 采购货款
    PURCHASE_PAYMENT("1004", "采购货款",
            List.of("采购商品名称"),
            List.of("商户自定义内容，如「戴尔笔记本电脑」"),
            List.of("货款")),

    /// 二手回收
    SECOND_HAND_RECYCLING("1005", "二手回收",
            List.of("回收商品名称"),
            List.of("商户自定义内容，如「塑料瓶」"),
            List.of("二手回收货款")),

    /// 公益补助
    PUBLIC_WELFARE_SUBSIDY("1006", "公益补助",
            List.of("公益活动名称", "公益活动备案编号"),
            List.of("请填写在民政部的备案名称", "请填写在民政部的备案编号"),
            List.of("公益补助金")),

    /// 行政补贴
    ADMINISTRATIVE_SUBSIDY("1007", "行政补贴",
            List.of("补贴类型"),
            List.of("商户自定义内容，如「购车补贴」"),
            List.of("行政补贴", "行政奖励"));

    /// 转账场景ID
    private final String code;

    /// 场景名称
    private final String name;

    /// 报备字段定义(抖音协议固定中文 infoType, 顺序即报备明细下标)
    private final List<String> reportInfoTypes;

    /// 报备字段说明(与 reportInfoTypes 平行, 描述字段含义和抖音文档示例)
    private final List<String> reportInfoDescriptions;

    /// 用户收款感知可选值(收款人在抖音中看到的文案, 不传时取第一个为默认)
    private final List<String> userRecvPerceptionOptions;

    /// 根据场景ID 查找枚举
    public static DouyinTransferSceneEnum findByCode(String code) {
        for (DouyinTransferSceneEnum scene : values()) {
            if (scene.code.equals(code)) {
                return scene;
            }
        }
        return null;
    }
}
