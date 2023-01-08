package org.zerock.balloon.entity.walkmap;

import lombok.*;
import org.zerock.balloon.entity.BaseEntity;
import org.zerock.balloon.entity.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="map_review")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"member", "mapCourse"})
public class MapReview extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private MapCourse mapCourse;
}
