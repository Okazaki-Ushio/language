package com.yosang.language.service;

import com.alibaba.fastjson.JSONObject;
import com.yosang.language.pojo.Word;

/**
 * @AUTHOR YoSang
 * @DATE 11/7/2019
 */
public interface WordService {

    JSONObject addWord(Word word);

    JSONObject getWordAndRelation(Integer wordId);

    JSONObject getWordBySingleWord(String singleWord);
}
