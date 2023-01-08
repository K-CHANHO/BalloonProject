package org.zerock.balloon.service.walkmap;

import org.zerock.balloon.dto.walkmap.MapCourseDTO;
import org.zerock.balloon.entity.walkmap.MapCourse;

import java.util.List;

public interface MapCourseService {
    MapCourseDTO get(Long mno);

    List<MapCourseDTO> getPin();

    default MapCourse dtoToEntity(MapCourseDTO mapCourseDTO){
        MapCourse mapCourse = MapCourse.builder()
                .mno(mapCourseDTO.getMno())
                .pin(mapCourseDTO.getMpin())
                .mload(mapCourseDTO.getMload())
                .mname(mapCourseDTO.getMname())
                .build();

        return mapCourse;
    }

    default MapCourseDTO entityToDTo(MapCourse mapCourse){
        MapCourseDTO mapCourseDTO = MapCourseDTO.builder()
                .mno(mapCourse.getMno())
                .mpin(mapCourse.getPin())
                .mload(mapCourse.getMload())
                .mname(mapCourse.getMname())
                .build();
        System.out.println(mapCourseDTO+"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        return mapCourseDTO;
    }
}
