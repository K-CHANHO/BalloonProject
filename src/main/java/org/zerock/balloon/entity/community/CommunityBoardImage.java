package org.zerock.balloon.entity.community;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="community_board_image")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "communityBoard")
public class CommunityBoardImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imgno;

    private String name;

    private String path;

    private String uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    private CommunityBoard communityBoard;
}
