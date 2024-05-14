package com.ezen.www.domain;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class BoardVO {
    private long bno;
    private String title;
    private String writer;
    private String content;
    private String regAt;
    private String modAt;
}
