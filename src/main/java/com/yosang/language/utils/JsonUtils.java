package com.yosang.language.utils;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

public class JsonUtils {

    public static JSONObject success(Object data){
        JSONObject map = new JSONObject();
        map.put("errcode",0);
        map.put("errmsg","ok");
        map.put("data",data);
        return map;
    }

    public static JSONObject fail(Integer code, String msg){
        JSONObject map = new JSONObject();
        map.put("errcode",code);
        map.put("errmsg",msg);
        map.put("data","");
        return map;
    }

    public static JSONObject successList(List<?> list){
        JSONObject map = new JSONObject();
        map.put("data",list);
        map.put("size",list.size());
        map.put("errcode",0);
        return map;
    }

    public static JSONObject successPage(IPage iPage){
        JSONObject map = new JSONObject();
        map.put("errcode",0);
        map.put("errmsg","ok");
        map.put("list",iPage.getRecords());
        map.put("total",iPage.getTotal());
        map.put("size",iPage.getSize());
        return map;
    }

    public static JSONObject successPage(IPage iPage,JSONObject map){
        map.put("errcode",0);
        map.put("errmsg","ok");
        map.put("list",iPage.getRecords());
        map.put("total",iPage.getTotal());
        map.put("size",iPage.getSize());
        return map;
    }
}
