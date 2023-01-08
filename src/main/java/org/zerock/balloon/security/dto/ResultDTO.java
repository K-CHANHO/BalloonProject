package org.zerock.balloon.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResultDTO {
    private boolean result;
    private String message;
    private String changedInfo;
}
