package com.yosang.language.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * @AUTHOR YoSang
 * @DATE 11/14/2019
 */

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DuplicateWords {
    private Word firstWord;
    private Word secondWord;
}
