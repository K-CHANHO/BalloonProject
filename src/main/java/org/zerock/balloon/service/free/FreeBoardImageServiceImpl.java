package org.zerock.balloon.service.free;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.balloon.repository. free. FreeBoardImageRepository;

@Service
public class FreeBoardImageServiceImpl implements  FreeBoardImageService {

    @Autowired
    private  FreeBoardImageRepository  freeBoardImageRepository;

    @Override
    public void remove(Long imgno) {freeBoardImageRepository.deleteById(imgno);}
}
