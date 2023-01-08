package org.zerock.balloon.service.walkmap;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.zerock.balloon.dto.walkmap.MapCourseDTO;
import org.zerock.balloon.entity.walkmap.MapCourse;
import org.zerock.balloon.repository.walkmap.MapCourseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class MapCourseServiceImpl implements MapCourseService{
    private final MapCourseRepository mapCourseRepository;

    @Override
    public MapCourseDTO get(Long mno) {
        Optional<MapCourse> mapCourse = mapCourseRepository.findById(mno);
        log.info(mapCourse+"^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        MapCourse result = mapCourse.get();
        log.info(result+"&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        return entityToDTo(result);
    }

    @Override
    public List<MapCourseDTO> getPin(){
        List<MapCourseDTO> pin = new ArrayList<>();
        List<MapCourse> result = mapCourseRepository.findAll();
        result.forEach(i -> {
            pin.add(entityToDTo(i));
        });
        return pin;
    }
}
