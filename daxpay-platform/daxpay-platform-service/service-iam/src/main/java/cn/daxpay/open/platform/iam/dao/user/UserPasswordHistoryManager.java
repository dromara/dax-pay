package cn.daxpay.open.platform.iam.dao.user;

import cn.daxpay.open.platform.iam.entity.user.UserPasswordHistory;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

/// # 用户密码历史
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class UserPasswordHistoryManager {

    private final UserPasswordHistoryMapper passwordHistoryMapper;

    /// 添加密码历史记录
    public void addHistory(Long userId, String passwordHash) {
        var history = new UserPasswordHistory()
                .setUserId(userId)
                .setPassword(passwordHash);
        history.setCreateTime(OffsetDateTime.now(ZoneOffset.UTC));
        passwordHistoryMapper.insert(history);
    }

    /// 获取用户最近的密码历史记录
    /// @param userId 用户ID
    /// @param limit 限制条数
    public List<UserPasswordHistory> findRecentByUserId(Long userId, int limit) {
        LambdaQueryWrapper<UserPasswordHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserPasswordHistory::getUserId, userId)
                .orderByDesc(UserPasswordHistory::getCreateTime);
        // 分页插件按当前数据库方言生成 limit，避免硬编码 SQL 方言
        Page<UserPasswordHistory> page = new Page<>(1, limit);
        page.setSearchCount(false);
        return passwordHistoryMapper.selectPage(page, wrapper).getRecords();
    }

    /// 获取用户所有密码历史记录
    public List<UserPasswordHistory> findByUserId(Long userId) {
        LambdaQueryWrapper<UserPasswordHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserPasswordHistory::getUserId, userId)
                .orderByDesc(UserPasswordHistory::getCreateTime);
        return passwordHistoryMapper.selectList(wrapper);
    }

    /// 删除用户最早的密码历史记录
    /// @param userId 用户ID
    /// @param keepCount 保留条数
    public void deleteOldest(Long userId, int keepCount) {
        List<UserPasswordHistory> histories = findByUserId(userId);
        if (histories.size() > keepCount) {
            for (int i = keepCount; i < histories.size(); i++) {
                passwordHistoryMapper.deleteById(histories.get(i).getId());
            }
        }
    }

    /// 检查密码是否在历史记录中
    public boolean existsInHistory(Long userId, String passwordHash, int checkCount) {
        List<UserPasswordHistory> histories = findRecentByUserId(userId, checkCount);
        return histories.stream()
                .anyMatch(h -> h.getPassword().equals(passwordHash));
    }
}

