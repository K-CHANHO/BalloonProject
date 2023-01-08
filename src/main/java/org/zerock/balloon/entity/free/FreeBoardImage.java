package org.zerock.balloon.entity.free;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="free_board_image")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "freeBoard")
public class FreeBoardImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imgno;

    private String name;

    private String path;

    private String uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    private FreeBoard freeBoard;
}
