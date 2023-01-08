package org.zerock.balloon.entity.walkmap;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="map_course")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class MapCourse{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    @Column(columnDefinition = "LONGTEXT")
    private String mload;

    private String mname;
    private String pin;
}
