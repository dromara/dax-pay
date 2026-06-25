package cn.daxpay.open.platform.iam.auth.service.twofactor;

import cn.hutool.crypto.SecureUtil;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/// # 备用验证码服务
///
/// 负责备用验证码的生成与哈希计算。备用码为 8 位字母数字(去除易混淆字符 I/O/0/1),
/// 中间以分隔符呈现为 4-4 形式(如 K7MQ-AB3X)。仅存储 SHA-256 哈希, 明文仅在生成时
/// 一次性返回给前端展示/下载。
///
/// 校验与消费(置 used)逻辑由 [UserTwoFactorService] 配合持久层完成, 本服务只提供无状态算法。
///
@Service
public class BackupCodeService {

    /// 备用码字符集(去除易混淆字符 I / O / 0 / 1)
    private static final char[] CHARSET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789".toCharArray();

    /// 备用码有效长度(不含分隔符)
    private static final int CODE_LENGTH = 8;

    /// 分隔位置(前 4 位与后 4 位之间)
    private static final int SPLIT_POS = 4;

    private static final String SEPARATOR = "-";

    private final SecureRandom random = new SecureRandom();

    /// 生成指定数量的备用验证码
    ///
    /// @param count 备用码数量
    /// @return 生成结果, 含明文列表(供前端一次性展示)与哈希条目(供持久化)
    public GeneratedBackupCode generate(int count) {
        List<String> plaintextCodes = new ArrayList<>(count);
        List<BackupCodeEntry> entries = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            String plain = generateOne();
            plaintextCodes.add(plain);
            entries.add(new BackupCodeEntry(hash(plain), false));
        }
        return new GeneratedBackupCode(plaintextCodes, entries);
    }

    /// 计算备用码明文的 SHA-256 哈希(十六进制小写)
    public String hash(String plaintext) {
        // 备用码比对时统一去除分隔符并转大写后哈希, 容忍用户输入是否带分隔符及大小写差异
        return SecureUtil.sha256(normalize(plaintext));
    }

    /// 规范化: 去除分隔符 + 转大写
    public String normalize(String input) {
        if (input == null) {
            return "";
        }
        return input.replace(SEPARATOR, "").trim().toUpperCase();
    }

    /// 生成单个备用码明文(4-4 分隔格式)
    private String generateOne() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH + SEPARATOR.length());
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(CHARSET[random.nextInt(CHARSET.length)]);
            if (i == SPLIT_POS - 1) {
                sb.append(SEPARATOR);
            }
        }
        return sb.toString();
    }

    /// 备用码生成结果
    public record GeneratedBackupCode(List<String> plaintextCodes, List<BackupCodeEntry> entries) {
    }
}
