package org.zerock.balloon.entity.free;

import lombok.*;
import org.zerock.balloon.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "free_reply")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "freeBoard")
public class FreeBoardReply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    private String nickname;

    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    private FreeBoard freeBoard;
}
