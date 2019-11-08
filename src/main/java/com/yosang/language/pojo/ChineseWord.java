package com.yosang.language.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @AUTHOR YoSang
 * @DATE 11/7/2019
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("LANGUAGE." + "CHINESE_WORD")
public class ChineseWord {
    @TableId(value = "chinese_word_id", type = IdType.AUTO)
    private Integer chineseWordId;
    private String chineseWordOriginal;
    private Integer chineseWordLinkNum;
    private String chineseWordCreateTime;
    private String chineseWordUpdateTime;


}
