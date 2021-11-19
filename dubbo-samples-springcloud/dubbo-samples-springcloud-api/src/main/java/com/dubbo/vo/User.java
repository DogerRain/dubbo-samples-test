package com.dubbo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author huangyongwen
 * @date 2021/11/17
 * @Description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = -4294369157631410325L;
    Long userId;
    String userName;
    String responseInfo;
}
