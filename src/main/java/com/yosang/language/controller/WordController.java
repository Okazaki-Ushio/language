package com.yosang.language.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yosang.language.annotation.NotNullProps;
import com.yosang.language.pojo.Word;
import com.yosang.language.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping({"/"})
    public String index(){
        return "chinese";
    }

    @RequestMapping("{page}")
    public String page(@PathVariable String page){
        return page;
    }

    @RequestMapping({"gotoAddWordPage"})
    public String gotoAddWordPage(){
        return "addWord";
    }

    @ResponseBody
    @NotNullProps({"wordOriginal","wordPronunciation","wordMeaning"})
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
        return wordService.randomStart(0);
    }

    @ResponseBody
    @NotNullProps({"wordId"})
    @RequestMapping("deleteWordByWordId")
    public JSONObject deleteWordByWordId(Integer wordId){
        return wordService.deleteWordByWordId(wordId);
    }

    @ResponseBody
    @RequestMapping("updateWordByWordId")
    @NotNullProps({"wordId","wordOriginal","wordMeaning"})
    public JSONObject updateWordByWordId(Word word,String searchWord){
        JSONObject jsonObject = wordService.updateWordByWordId(word);
        jsonObject.put("searchWord",searchWord);
        return jsonObject;
    }

    @NotNullProps("wordId")
    @RequestMapping("getWordByWordId")
    public String getWordByWordId(Integer wordId,String searchWord,Model model){
        model.addAttribute("word",wordService.getWordByWordId(wordId));
        model.addAttribute("searchWord",searchWord);
        return "editWord";
    }

    @ResponseBody
    @RequestMapping("getAllWords")
    public JSONObject getAllWords(){
        return wordService.getAllWords();
    }

    @NotNullProps({"wordType"})
    @ResponseBody
    @RequestMapping({"getWordsByWordType"})
    public JSONObject getWordsByWordType(Word word,Page<Word> page){
        return wordService.getWordsByWordType(page,word);
    }
}
