package com.barry.netty.codec;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable {

    private static final long serialVersionUID = 2563808302014047931L;

    private String userId;
    private String userName;
    private Integer age;
    private String mobile;
    private String address;
    private Date birthDay;


}
