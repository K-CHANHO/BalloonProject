package org.zerock.balloon.repository.free.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FreeSearchBoardRepository {
    Page<Object[]> searchPage(String subject,String type,String keyword, Pageable pageable);
}
