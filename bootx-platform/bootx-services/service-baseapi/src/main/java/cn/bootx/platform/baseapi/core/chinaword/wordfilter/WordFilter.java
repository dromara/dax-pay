package cn.bootx.platform.baseapi.core.chinaword.wordfilter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 敏感词过滤器
 *
 * @author minghu.zhang
 */
@Component
@RequiredArgsConstructor
@SuppressWarnings("rawtypes")
public class WordFilter {
    private final WordContext context;
    /**
     * 替换敏感词
     *
     * @param text 输入文本
     */
    public String replace(final String text) {
        return replace(text, 0, '*');
    }

    /**
     * 替换敏感词
     * 查找敏感词，距离越长，过滤越严格，效率越低，开发者可以根据具体需求设置，
     * 这里以“紧急”为敏感词举例，以此类推:
     * 0 匹配紧急
     * 1 匹配不紧不急，紧x急
     * 2 匹配紧急，紧x急，紧xx急
     *
     * @param text   输入文本
     * @param symbol 替换符号
     */
    public String replace(final String text, final char symbol) {
        return replace(text, 0, symbol);
    }

    /**
     * 替换敏感词
     *
     * @param text   输入文本
     * @param skip   文本距离
     * @param symbol 替换符号
     */
    public String replace(final String text, final int skip, final char symbol) {
        char[] charset = text.toCharArray();
        for (int i = 0; i < charset.length; i++) {
            FlagIndex fi = getFlagIndex(charset, i, skip);
            if (fi.isFlag()) {
                if (!fi.isWhiteWord()) {
                    for (int j : fi.getIndex()) {
                        charset[j] = symbol;
                    }
                } else {
                    i += fi.getIndex().size() - 1;
                }
            }
        }
        return new String(charset);
    }

    /**
     * 是否包含敏感词
     *
     * @param text 输入文本
     */
    public boolean include(final String text) {
        return include(text, 0);
    }

    /**
     * 是否包含敏感词,  程序会跳过不同的距离，
     * 查找敏感词，距离越长，过滤越严格，效率越低，开发者可以根据具体需求设置，
     * 这里以“紧急”为敏感词举例，以此类推:
     * 0 匹配紧急
     * 1 匹配不紧不急，紧x急
     * 2 匹配紧急，紧x急，紧xx急
     *
     * @param text 输入文本
     * @param skip 文本距离
     */
    public boolean include(final String text, final int skip) {
        boolean include = false;
        char[] charset = text.toCharArray();
        for (int i = 0; i < charset.length; i++) {
            FlagIndex fi = getFlagIndex(charset, i, skip);
            if(fi.isFlag()) {
                if (fi.isWhiteWord()) {
                    i += fi.getIndex().size() - 1;
                } else {
                    include = true;
                    break;
                }
            }
        }
        return include;
    }

    /**
     * 获取敏感词数量
     *
     * @param text 输入文本
     */
    public int wordCount(final String text) {
        return wordCount(text, 0);
    }

    /**
     * 获取敏感词数量
     *
     * @param text 输入文本
     * @param skip 文本距离
     */
    public int wordCount(final String text, final int skip) {
        int count = 0;
        char[] charset = text.toCharArray();
        for (int i = 0; i < charset.length; i++) {
            FlagIndex fi = getFlagIndex(charset, i, skip);
            if (fi.isFlag()) {
                if(fi.isWhiteWord()) {
                    i += fi.getIndex().size() - 1;
                } else {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 获取敏感词列表
     *
     * @param text 输入文本
     */
    public Set<String> wordList(String text) {
        return wordList(text, 0);
    }

    /**
     * 获取敏感词列表
     *
     * @param text 输入文本
     * @param skip 文本距离
     */
    public Set<String> wordList(final String text, final int skip) {
        Set<String> wordSet = new LinkedHashSet<>();
        char[] charset = text.toCharArray();
        for (int i = 0; i < charset.length; i++) {
            FlagIndex fi = getFlagIndex(charset, i, skip);
            if (fi.isFlag()) {
                if(fi.isWhiteWord()) {
                    i += fi.getIndex().size() - 1;
                } else {
                    StringBuilder builder = new StringBuilder();
                    for (int j : fi.getIndex()) {
                        char word = text.charAt(j);
                        builder.append(word);
                    }
                    wordSet.add(builder.toString());
                }
            }
        }
        return wordSet;
    }

    /**
     * 获取标记索引
     *
     * @param charset 输入文本
     * @param begin   检测起始
     * @param skip    文本距离
     */
    private FlagIndex getFlagIndex(final char[] charset, final int begin, final int skip) {
        FlagIndex fi = new FlagIndex();

        Map current = context.getWordMap();
        boolean flag = false;
        int count = 0;
        List<Integer> index = new ArrayList<>();
        for (int i = begin; i < charset.length; i++) {
            char word = charset[i];
            Map mapTree = (Map) current.get(word);
            if (count > skip || (i == begin && Objects.isNull(mapTree))) {
                break;
            }
            if (Objects.nonNull(mapTree)) {
                current = mapTree;
                count = 0;
                index.add(i);
            } else {
                count++;
                if (flag && count > skip) {
                    break;
                }
            }
            if ("1".equals(current.get("isEnd"))) {
                flag = true;
            }
            if ("1".equals(current.get("isWhiteWord"))) {
                fi.setWhiteWord(true);
                break;
            }
        }

        fi.setFlag(flag);
        fi.setIndex(index);

        return fi;
    }
}
