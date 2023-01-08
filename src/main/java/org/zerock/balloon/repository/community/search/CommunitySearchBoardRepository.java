package org.zerock.balloon.repository.community.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommunitySearchBoardRepository {
    Page<Object[]> searchPage(String subject, String type, String keyword, Pageable pageable);
}
