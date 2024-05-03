package com.HaveBinProject.HaveBin.RequestDTO;

import com.HaveBinProject.HaveBin.Trashcan.Unknown_Trashcan;
import lombok.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Getter @Setter
public class RegisterTrashcanDTO {

    private double latitude;
    private double longitude;
    private String categories;
    private String detailAddress;

    public Unknown_Trashcan toEntity(RegisterTrashcanDTO registerTrashcanDTO, Long userid) {
        Unknown_Trashcan trashcan = new Unknown_Trashcan();

        trashcan.setUserId(userid);
        trashcan.setLatitude(registerTrashcanDTO.getLatitude());
        trashcan.setLongitude(registerTrashcanDTO.getLongitude());
        trashcan.setCategories(registerTrashcanDTO.getCategories());
        trashcan.setState("impossible");
        trashcan.setDetailAddress(registerTrashcanDTO.getDetailAddress());

        Date currentDate = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        String formattedDate = dateFormat.format(currentDate);

        trashcan.setDate(formattedDate);
        //trashcan.setRoadviewImgpath(imageService.uploadImageToS3(image, "Unknown_Trashcan"));
//        try {
//            trashcan.setRoadviewImgpath(imageService.uploadImageToS3(registerTrashcanDTO.getImage(), "Unknown_Trashcan"));
//        } catch (Exception e) {
//            System.out.println("이미지 업로드 실패");
//        }

        return trashcan;
    }
}
