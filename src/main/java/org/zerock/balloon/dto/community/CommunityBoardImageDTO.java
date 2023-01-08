package org.zerock.balloon.dto.community;


import lombok.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommunityBoardImageDTO {

//    private Long bno; // 게시물 번호
//
    private Long imgno; // 이미지 번호

    private String name; // 사진 파일명

    private String uuid; // uuid

    private String path; // 사진 파일 경로

    public String getImageURL(){
        try{
            return URLEncoder.encode(path + "/" + uuid + "_" + name, "UTF-8");
        }catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getThumbnailURL(){
        try{
            return URLEncoder.encode(path + "/s_" + uuid + "_" + name, "UTF-8");
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return "";
    }

}
