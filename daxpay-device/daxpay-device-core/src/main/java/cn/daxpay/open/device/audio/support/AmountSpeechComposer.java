package cn.daxpay.open.device.audio.support;

import java.util.ArrayList;
import java.util.List;

/// # 金额 → 播报词元序列(平台侧镜像实现)
///
/// 与终端 APP 的 `core/AmountSpeechComposer.kt` **逐行同源**(后者移植自 open pos 的
/// `AmountSpeechComposer.cs`), 用于运营端/商户端在下发试播前展示「预计播报」文案,
/// 让"改了金额/渠道到底会念什么"在点击前可见, 也顺带暴露词元素材缺失。
///
/// ## 与 APP 的一致性约束
///
/// 规则照抄不改: 四位分组跨级补零、组首十位为 1 省「一」、数字+位权输出融合词元、
/// 小数读角分、整数读「元整」、越界退化为「收款成功」。
/// 任一侧改规则必须同步另一侧, 否则播报口径漂移。
///
/// ## 为什么不给终端下发文案
///
/// 词元是终端本地资产(顺序播放 + 交叉淡化), 平台只镜像"会念什么", 不下发播放指令。
///
/// 移植自 plus `daxpay-device-core`(快照 773134af), 无业务改动。
public final class AmountSpeechComposer {

    /// 可播金额上限(分): 99,999,999.99 元(与 APP MAX_CENTS 一致)
    public static final long MAX_CENTS = 9_999_999_999L;

    private static final String[] DIGITS = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};

    private static final String[] WEIGHTS = {"千", "百", "十"};

    private static final String FALLBACK = "收款成功";

    private AmountSpeechComposer() {
    }

    /// 渠道 code → 前缀词(先归一化为五键闭集, 与终端词元表一一对应)
    public static String channelText(String channel) {
        return switch (normalize(channel)) {
            case "wechat" -> "微信收款";
            case "alipay" -> "支付宝到账";
            case "douyin" -> "抖音支付到账";
            case "union" -> "银联收款";
            default -> "收款到账";
        };
    }

    /// 完整播报词元: 渠道前缀 + 金额(越界时只输出兜底词)
    public static List<String> compose(String channel, long amountCents) {
        List<String> amount = amountTokens(amountCents);
        if (amount.size() == 1 && FALLBACK.equals(amount.get(0))) {
            return amount;
        }
        List<String> tokens = new ArrayList<>(amount.size() + 1);
        tokens.add(channelText(channel));
        tokens.addAll(amount);
        return tokens;
    }

    /// 金额词元(不含渠道前缀)
    public static List<String> amountTokens(long amountCents) {
        List<String> tokens = new ArrayList<>(16);
        if (amountCents <= 0 || amountCents > MAX_CENTS) {
            tokens.add(FALLBACK);
            return tokens;
        }
        long integer = amountCents / 100;
        int jiao = (int) ((amountCents % 100) / 10);
        int fen = (int) (amountCents % 10);

        if (integer == 0L) {
            tokens.add("零");
        } else {
            composeInteger(tokens, integer);
        }

        if (jiao > 0 || fen > 0) {
            tokens.add("点");
            if (jiao > 0) {
                tokens.add(digit(jiao));
            }
            if (fen > 0) {
                if (jiao == 0) {
                    tokens.add("零");
                }
                tokens.add(digit(fen));
            }
            tokens.add("元");
        } else {
            tokens.add("元整");
        }
        return tokens;
    }

    /// 词元拼成完整中文串(展示用)
    public static String fullText(List<String> tokens) {
        return String.join("", tokens);
    }

    private static void composeInteger(List<String> out, long value) {
        long wan = value / 10_000;
        long ge = value % 10_000;
        if (wan > 0) {
            composeGroup4(out, wan, "万");
            if (ge > 0) {
                if (ge < 1000) {
                    out.add("零");
                }
                composeGroup4(out, ge, null);
            }
        } else {
            composeGroup4(out, ge, null);
        }
    }

    /// 四位组读数(1~9999): 零只补一个、级尾零不读、组首十位为 1 省「一」;
    /// 数字+位权(含个位接 trailingUnit 的「万」)输出融合词元整词连读;
    /// 个位为零时「万」退回独立词元(十万/一百万/一千二百三十万)
    private static void composeGroup4(List<String> out, long group, String trailingUnit) {
        int[] digits = {
                (int) (group / 1000),
                (int) (group / 100 % 10),
                (int) (group / 10 % 10),
                (int) (group % 10),
        };
        boolean hasOut = false;
        boolean pendingZero = false;
        for (int i = 0; i <= 3; i++) {
            int d = digits[i];
            if (d == 0) {
                if (hasOut) {
                    pendingZero = true;
                }
                continue;
            }
            if (pendingZero) {
                out.add("零");
                pendingZero = false;
            }
            if (i == 2 && d == 1 && !hasOut) {
                out.add("十");
            } else {
                String suffix = i < 3 ? WEIGHTS[i] : trailingUnit;
                if (suffix != null) {
                    out.add(digit(d) + suffix);
                } else {
                    out.add(digit(d));
                }
            }
            hasOut = true;
        }
        if (trailingUnit != null && hasOut && digits[3] == 0) {
            out.add(trailingUnit);
        }
    }

    private static String digit(int d) {
        return d >= 0 && d <= 9 ? DIGITS[d] : "";
    }

    /// 渠道别名归一化(与终端 `ChannelNames.normalize` 同表, 多收 `union_pay`)
    private static String normalize(String channel) {
        String code = channel == null ? "" : channel.trim().toLowerCase();
        return switch (code) {
            case "wechat", "wx", "微信" -> "wechat";
            case "alipay", "ali", "支付宝" -> "alipay";
            case "douyin", "tt", "抖音" -> "douyin";
            case "union", "union_pay", "银联" -> "union";
            default -> code;
        };
    }
}
