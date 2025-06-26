package com.mathmastery.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProgressDTO {
    private Long questionId;
    private boolean correct;
}
