package com.ezen.www.domain;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AuthVO {
    private String email;
    private String auth;
}
