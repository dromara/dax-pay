package cn.daxpay.open.platform.capability.sensitiveword.engine;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.dfa.WordTree;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/// # 敏感词匹配器（Hutool WordTree / AC）
///
@Component
public class SensitiveWordMatcher {

    private final AtomicReference<WordTree> treeRef = new AtomicReference<>(new WordTree());
    private final AtomicReference<String> versionRef = new AtomicReference<>("");

    /// 按启用词列表重建（version 变化时）
    public void rebuild(List<String> words, String version) {
        String ver = StrUtil.nullToEmpty(version);
        if (ver.equals(versionRef.get()) && treeRef.get() != null && CollUtil.isNotEmpty(words)) {
            return;
        }
        WordTree tree = new WordTree();
        if (CollUtil.isNotEmpty(words)) {
            for (String w : words) {
                if (StrUtil.isNotBlank(w)) {
                    tree.addWord(w.trim());
                }
            }
        }
        treeRef.set(tree);
        versionRef.set(ver);
    }

    /// 查找全部命中（最长匹配）
    public List<String> findHits(String normalizedText) {
        if (StrUtil.isBlank(normalizedText)) {
            return Collections.emptyList();
        }
        WordTree tree = treeRef.get();
        if (tree == null) {
            return Collections.emptyList();
        }
        List<String> hits = tree.matchAll(normalizedText, -1, false, false);
        return hits == null ? Collections.emptyList() : hits;
    }

    public boolean contains(String normalizedText) {
        return CollUtil.isNotEmpty(findHits(normalizedText));
    }
}

