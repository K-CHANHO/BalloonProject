package org.zerock.balloon.service.community;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.balloon.repository.community.CommunityBoardImageRepository;

@Service
public class CommunityBoardImageServiceImpl implements CommunityBoardImageService {

    @Autowired
    private CommunityBoardImageRepository communityBoardImageRepository;

    @Override
    public void remove(Long imgno) {
        communityBoardImageRepository.deleteById(imgno);
    }
}
