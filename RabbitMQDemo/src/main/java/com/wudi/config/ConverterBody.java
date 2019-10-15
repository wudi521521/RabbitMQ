package com.wudi.config;

import lombok.Data;

/**
 * @author Dillon Wu
 * @Title: ConverterBody
 * @Description: TODO
 * @date 2019/10/14 13:32
 */
@Data
public class ConverterBody {

    private byte[] body;

    public ConverterBody(){}

    public ConverterBody(byte[] body){
        this.body=body;
    }


}
