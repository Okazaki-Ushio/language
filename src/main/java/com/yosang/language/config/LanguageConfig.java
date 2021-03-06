package com.yosang.language.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yosang.language.dao.ChineseWordDao;
import com.yosang.language.dao.WordDao;
import com.yosang.language.enumunation.WORDTYPE;
import com.yosang.language.exception.LanguageException;
import com.yosang.language.forkjoin.UpdateWordsForkJoin;
import com.yosang.language.pojo.ChineseWord;
import com.yosang.language.pojo.Word;
import com.yosang.language.utils.TimeUtils;

import static com.yosang.language.enumunation.WORDTYPE.*;

import java.util.*;
import java.util.concurrent.ForkJoinPool;


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
            "ぱ", "ぴ", "ぷ", "ぺ", "ん", "っ", "ゃ", "ょ", "ッ", "～",
            "ぽ", "."
    };

    private static String[] katakana = {
            "ア", "イ", "ウ", "エ", "オ", "カ", "キ", "ク", "ケ", "コ",
            "サ", "シ", "ス", "セ", "ソ", "タ", "チ", "ツ", "テ", "ト",
            "ナ", "ニ", "ヌ", "ネ", "ノ", "ハ", "ヒ", "フ", "ヘ", "ホ",
            "マ", "ミ", "ム", "メ", "モ", "ヤ", "イ", "ュ", "エ", "ョ",
            "ラ", "リ", "ル", "レ", "ロ", "ワ", "イ", "ウ", "エ", "ヲ",

            "ガ", "ギ", "グ", "ゲ", "ゴ", "ザ", "ジ", "ズ", "ゼ", "ゾ",
            "ダ", "ヂ", "ヅ", "デ", "ド", "バ", "ビ", "ブ", "ベ", "ボ",
            "パ", "ピ", "プ", "ペ", "ァ", "ー", "ン", "ポ", "ェ", "ィ",
            "ヨ", "ャ", "ユ"
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
                return false;
        }
        return true;
    }

    public static boolean containChinese(String word) {
        for (int i = 0; i < word.length(); i++) {
            if (!hiraganaList.contains(word.substring(i, i + 1)) && !katakanaList.contains(word.substring(i, i + 1)))
                return true;
        }
        return false;
    }

    public static WORDTYPE getWordType(String word) {
        if (containLoanword(word)) {
            if (containChinese(word)) {
                return LOANWORD_CHINESE;
            } else {
                return LOANWORD;
            }
        } else if (containChinese(word)) {
            return CHINESE;
        } else if (isWago(word)) {
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
        String time = TimeUtils.nowSimpleDate();
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

    public static boolean filterDuplicateWord(String wordOriginal, WordDao wordDao){
        QueryWrapper<Word> con = new QueryWrapper<>();
        con.eq("WORD_ORIGINAL", wordOriginal);
        return wordDao.selectOne(con) != null;
    }

    public static List<Word> getWordsBySingleWord(String singleWord, ChineseWordDao chineseWordDao, WordDao wordDao) {
        QueryWrapper<ChineseWord> con = new QueryWrapper<>();
        con.eq("CHINESE_WORD_ORIGINAL", singleWord);
        ChineseWord chineseWord = chineseWordDao.selectOne(con);
        if (null != chineseWord) {
            QueryWrapper<Word> con1 = new QueryWrapper<>();
            con1.like("WORD_CHINESE_IDS", "|" + chineseWord.getChineseWordId() + "|");
            List<Word> words = wordDao.selectList(con1);
            return words;
        } else {
            return null;
        }
    }

    public static Map<String, Object> getWordAndRelation(Integer wordId, WordDao wordDao) {
        List<Word> result = new ArrayList<>();
        Word word = wordDao.selectById(wordId);
        String chineseIds = word.getWordChineseIds();
        if (chineseIds != null) {
            String[] ids = chineseIds.substring(1, chineseIds.length() - 1).split("\\|");
            for (String id : ids) {
                QueryWrapper<Word> con = new QueryWrapper<>();
                con.like("WORD_CHINESE_IDS", "|" + id + "|")/*.notIn("WORD_ID",wordId)*/;
                List<Word> words = wordDao.selectList(con);
                result.addAll(words);
            }
        }
        updateListViewCount(result,wordDao);
        Map<String, Object> map = new HashMap<>();
        map.put("size", result.size());
        map.put("list", result);
        map.put("target", word);
        return map;
    }

    public static void updateListViewCount(List<Word> list, WordDao wordDao){
        for (Word updateWord : list) {
            updateWord.setWordViewCount(updateWord.getWordViewCount()+1);
            updateWord.setWordUpdateTime(TimeUtils.nowSimpleDate());
            //wordDao.updateById(updateWord);
        }
        //wordDao.updateBatch(list);
        ForkJoinPool pool = new ForkJoinPool();
        UpdateWordsForkJoin forkJoin=new UpdateWordsForkJoin(0,list.size(),list,wordDao);
        pool.invoke(forkJoin);
    }
}
