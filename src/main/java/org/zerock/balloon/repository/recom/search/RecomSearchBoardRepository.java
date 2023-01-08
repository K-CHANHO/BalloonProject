package org.zerock.balloon.repository.recom.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecomSearchBoardRepository {
    Page<Object[]> searchPage(String subject, String type, String keyword, Pageable pageable);
}
