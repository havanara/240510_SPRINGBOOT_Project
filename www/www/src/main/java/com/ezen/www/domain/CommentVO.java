package com.ezen.www.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class CommentVO {
    private long cno;
    private long bno;
    private String writer;
    private String content;
    private String regAt;
    private String modAt;
}
