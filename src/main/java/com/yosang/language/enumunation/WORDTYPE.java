package com.yosang.language.enumunation;

/**
 * @AUTHOR YoSang
 * @DATE 11/8/2019
 */
public enum WORDTYPE {

    EXCEPTION(0),WAGO(1),CHINESE(2),LOANWORD(3),LOANWORD_CHINESE(4);

    private Integer value;

    WORDTYPE(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
