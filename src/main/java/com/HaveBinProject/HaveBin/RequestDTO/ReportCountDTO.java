package com.HaveBinProject.HaveBin.RequestDTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReportCountDTO {

    private String TrashcanId;

    public ReportCountDTO(String trashcanId) {
        TrashcanId = trashcanId;
    }
}

