package com.yosang.language.controller;

import com.alibaba.fastjson.JSONObject;
import com.yosang.language.annotation.NotNullProps;
import com.yosang.language.pojo.Word;
import com.yosang.language.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @AUTHOR YoSang
 * @DATE 11/7/2019
 */
@Controller
//@RequestMapping("language")
public class WordController {

    @Autowired
    private WordService wordService;

    @RequestMapping({"/","word"})
    public String index(){
        return "word";
    }

    @ResponseBody
    @NotNullProps({"wordOriginal","wordPronunciation"})
    @RequestMapping("addWord")
    public JSONObject addWord(Word word){
        return wordService.addWord(word);
    }

    @ResponseBody
    @NotNullProps({"wordId"})
    @RequestMapping("getWordAndRelation")
    public JSONObject getWordAndRelation(Integer wordId){
        return wordService.getWordAndRelation(wordId);
    }

    @ResponseBody
    @NotNullProps({"singleWord"})
    @RequestMapping("getWordBySingleWord")
    public JSONObject getWordBySingleWord(String singleWord){
        return wordService.getWordBySingleWord(singleWord);
    }

    @ResponseBody
    @RequestMapping("checkAndUpdateWordNum")
    public JSONObject checkAndUpdateWordNum(@RequestBody List<Word> words){
        return wordService.checkAndUpdateWordNum(words);
    }

    @ResponseBody
    @RequestMapping({"randomStart"})
    public JSONObject randomStart(){
        return wordService.randomStart();
    }

    @ResponseBody
    @NotNullProps({"wordId"})
    @RequestMapping("deleteWordByWordId")
    public JSONObject deleteWordByWordId(Integer wordId){
        return wordService.deleteWordByWordId(wordId);
    }

    @NotNullProps({"wordId"})
    @ResponseBody
    @RequestMapping("updateWordByWordId")
    public JSONObject updateWordByWordId(Word word){
        return wordService.updateWordByWordId(word);
    }
}
