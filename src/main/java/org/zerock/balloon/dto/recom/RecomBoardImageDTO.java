package org.zerock.balloon.dto.recom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecomBoardImageDTO {
    private Long imgno;

    private String imgname;
    private String uuid;

    private String path;

    public String getImageURL(){
        try{
            return URLEncoder.encode(path + "/" + uuid + "_" + imgname, "UTF-8");
        }catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
    public String getThumbnailURL(){
        try{
            return URLEncoder.encode(path + "/s_" + uuid + "_" + imgname, "UTF-8");
        }catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
