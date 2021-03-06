package com.yosang.language.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @AUTHOR YoSang
 * @DATE 11/7/2019
 */
@Data
@Accessors(chain = true)
@TableName("LANGUAGE."+"WORD")
public class Word {
    @TableId(value = "word_id", type = IdType.AUTO)
    private Integer wordId;
    private String wordOriginal;
    private String wordPronunciation;
    private Integer wordType; // 0异常 1和语 2汉语 3外来语 4外来语+汉语
    private Integer wordMistakeNum;
    private Integer wordRightNum;
    private String wordCreateTime;
    private String wordChineseIds; // |52|45|75|
    private String wordUpdateTime;
    private String wordMeaning;
    private String wordProperty; //词性
    private Integer wordViewCount; //查看次数
}
