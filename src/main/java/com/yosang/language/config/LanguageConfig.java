package com.yosang.language.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yosang.language.dao.ChineseWordDao;
import com.yosang.language.dao.WordDao;
import com.yosang.language.enumunation.WORDTYPE;
import com.yosang.language.exception.LanguageException;
import com.yosang.language.pojo.ChineseWord;
import com.yosang.language.pojo.Word;
import com.yosang.language.utils.TimeUtils;

import static com.yosang.language.enumunation.WORDTYPE.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @AUTHOR YoSang
 * @DATE 11/7/2019
 */
public class LanguageConfig {

    private static String[] hiragana = {
            "あ", "い", "う", "え", "お", "か", "き", "く", "け", "こ",
            "さ", "し", "す", "せ", "そ", "た", "ち", "つ", "て", "と",
            "な", "に", "ぬ", "ね", "の", "は", "ひ", "ふ", "へ", "ほ",
            "ま", "み", "む", "め", "も", "や", "い", "ゆ", "え", "よ",
            "ら", "り", "る", "れ", "ろ", "わ", "い", "う", "え", "を",

            "が", "ぎ", "ぐ", "げ", "ご", "ざ", "じ", "ず", "ぜ", "ぞ",
            "だ", "ぢ", "づ", "で", "ど", "ば", "び", "ぶ", "べ", "ぼ",
            "ぱ", "ぴ", "ぷ", "ぺ"
    };

    private static String[] katakana = {
            "ア", "イ", "ウ", "エ", "オ", "カ", "キ", "ク", "ケ", "コ",
            "サ", "シ", "ス", "セ", "ソ", "タ", "チ", "ツ", "テ", "ト",
            "ナ", "ニ", "ヌ", "ネ", "ノ", "ハ", "ヒ", "フ", "ヘ", "ホ",
            "マ", "ミ", "ム", "メ", "モ", "ヤ", "イ", "ユ", "エ", "ヨ",
            "ラ", "リ", "ル", "レ", "ロ", "ワ", "イ", "ウ", "エ", "ヲ",

            "ガ", "ギ", "グ", "ゲ", "ゴ", "ザ", "ジ", "ズ", "ゼ", "ゾ",
            "ダ", "ヂ", "ヅ", "デ", "ド", "バ", "ビ", "ブ", "ベ", "ボ",
            "パ", "ピ", "プ", "ペ",
    };

    private static List<String> hiraganaList = Arrays.asList(hiragana);

    private static List<String> katakanaList = Arrays.asList(katakana);

    public static boolean containLoanword(String word) {
        for (int i = 0; i < word.length(); i++) {
            if (katakanaList.contains(word.substring(i, i + 1)))
                return true;
        }
        return false;
    }

    public static boolean isWago(String word) {
        for (int i = 0; i < word.length(); i++) {
            if (!hiraganaList.contains(word.substring(i, i + 1)))
                return true;
        }
        return false;
    }

    public static boolean containChinese(String word) {
        for (int i = 0; i < word.length(); i++) {
            if (!hiraganaList.contains(word.substring(i, i + 1)) && !katakanaList.contains(word.substring(i, i + 1)))
                return true;
        }
        return false;
    }

    public static WORDTYPE getWordType(String singleWord) {
        if (containLoanword(singleWord)) {
            if (containChinese(singleWord)) {
                return LOANWORD_CHINESE;
            } else {
                return LOANWORD;
            }
        } else if (containChinese(singleWord)) {
            return CHINESE;
        } else if (isWago(singleWord)) {
            return WAGO;
        } else {
            return EXCEPTION;
        }
    }

    public static String getChineseIds(String word, ChineseWordDao chineseWordDao) {
        List<String> chineseWords = filterChineseWords(word);
        StringBuilder builder = new StringBuilder("|");
        if (chineseWords.size() > 0) {
            for (String chineseWord : chineseWords) {
                Integer chineseWordId = getChineseWordIdByWord(chineseWord, chineseWordDao);
                builder.append(chineseWordId).append("|");
            }
            return builder.toString();
        } else {
            return null;
        }
    }

    public static List<String> filterChineseWords(String word) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            String singleWord = word.substring(i, i + 1);
            Integer wordType = getWordType(singleWord).getValue();
            if (wordType == 4 || wordType == 2) {
                result.add(singleWord);
            }
        }
        return result;
    }

    public static Integer getChineseWordIdByWord(String singleWord, ChineseWordDao chineseWordDao) {
        String time = TimeUtils.now();
        QueryWrapper<ChineseWord> con = new QueryWrapper<>();
        con.eq("CHINESE_WORD_ORIGINAL", singleWord);
        ChineseWord chineseWord = chineseWordDao.selectOne(con);
        if (null != chineseWord) {
            chineseWord.setChineseWordLinkNum(chineseWord.getChineseWordLinkNum() + 1)
                    .setChineseWordUpdateTime(time);
            chineseWordDao.updateById(chineseWord);
            return chineseWord.getChineseWordId();
        } else {
            ChineseWord newChineseWord = new ChineseWord();
            newChineseWord.setChineseWordOriginal(singleWord).setChineseWordLinkNum(1)
                    .setChineseWordCreateTime(time).setChineseWordLinkNum(1);
            chineseWordDao.insert(newChineseWord);
            return newChineseWord.getChineseWordId();
        }
    }

    public static void filterDuplicateWord(String wordOriginal, WordDao wordDao) throws LanguageException {
        QueryWrapper<Word> con = new QueryWrapper<>();
        con.eq("WORD_ORIGINAL", wordOriginal);
        if (wordDao.selectOne(con) != null) {
            throw new LanguageException("duplicate word in database");
        }
    }

    public static List<Word> getWordsBySingleWord(String singleWord, ChineseWordDao chineseWordDao, WordDao wordDao) {
        QueryWrapper<ChineseWord> con = new QueryWrapper<>();
        con.eq("CHINESE_WORD_ORIGINAL", singleWord);
        ChineseWord chineseWord = chineseWordDao.selectOne(con);
        if (null != chineseWord) {
            QueryWrapper<Word> con1 = new QueryWrapper<>();
            con1.like("WORD_CHINESE_IDS", "|" + chineseWord.getChineseWordId() + "|");
            return wordDao.selectList(con1);

        } else {
            return null;
        }
    }

    public static List<Word> getWordAndRelation(Integer wordId,WordDao wordDao) {
        List<Word> result=new ArrayList<>();
        Word word = wordDao.selectById(wordId);
        String chineseIds = word.getWordChineseIds();
        if(chineseIds !=null){
            String[] ids = chineseIds.substring(1, chineseIds.length() - 2).split("|");
            for (String id : ids) {
                QueryWrapper<Word> con=new QueryWrapper<>();
                con.like("WORD_CHINESE_IDS","|"+id+"|");
                List<Word> words = wordDao.selectList(con);
                result.addAll(words);
            }
        }
        return result;
    }
}
