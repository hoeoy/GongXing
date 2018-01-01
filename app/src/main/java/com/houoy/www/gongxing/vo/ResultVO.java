package com.houoy.www.gongxing.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by andyzhao on 2017/12/23.
 */
@Data
@NoArgsConstructor
public class ResultVO<T> {
    private String code;
    private String message;
    private T data;
}
