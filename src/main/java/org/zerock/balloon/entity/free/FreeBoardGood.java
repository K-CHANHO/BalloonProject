package org.zerock.balloon.entity.free;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.zerock.balloon.entity.Member;

import javax.persistence.*;

@DynamicInsert
@Table(name = "free_board_good")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"member", "freeBoard"})
public class FreeBoardGood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gno;

    @ColumnDefault("1")
    private Long count;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private FreeBoard freeBoard;
}
