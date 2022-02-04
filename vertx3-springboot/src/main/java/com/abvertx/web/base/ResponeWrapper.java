package com.abvertx.web.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ResponeWrapper<V> {

    private int code;
    private V data;
    private String message;

    public static final ResponeWrapper<String> RESPONE_SUCCESS = new ResponeWrapper<>(0, null, "操作成功");
    public static final ResponeWrapper<String> RESPONE_FAIL = new ResponeWrapper<>(10001, null, "操作失败");
}