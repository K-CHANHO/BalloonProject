package org.zerock.balloon.entity.community;

import lombok.*;
import org.zerock.balloon.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name="community_board_reply")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "communityBoard")
public class CommunityBoardReply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    private String nickname;

    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    private CommunityBoard communityBoard;
}
