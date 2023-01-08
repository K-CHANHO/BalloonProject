package org.zerock.balloon.dto.walkmap;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MapCourseDTO {
    private Long mno;

    private String mload;

    private String mpin;

    private String mname;
}
