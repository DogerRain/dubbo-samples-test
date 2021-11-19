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
public class Order implements Serializable {

    private static final long serialVersionUID = -3757842094691885448L;
    Long orderId;
    String orderName;

}
