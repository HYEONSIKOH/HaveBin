package com.HaveBinProject.HaveBin.admin;

import com.HaveBinProject.HaveBin.RequestDTO.*;
import com.HaveBinProject.HaveBin.Trashcan.ShowReportTrashcan;
import com.HaveBinProject.HaveBin.Trashcan.Unknown_Trashcan;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/findAllUnknownTrashcans")
    public List<Unknown_Trashcan> adminPage(){
        return adminService.findAll();
    }

    //새로운 쓰레기통으로 등록
    @PostMapping("/acceptNewTrashcan")
    public ResponseEntity<?> acceptNewTrashcan(@RequestBody String unknown_trashcan_id) {
        System.out.println("unknown_trashcan_id = " + unknown_trashcan_id);
        return adminService.acceptNewTrashcan(Long.parseLong(unknown_trashcan_id));
    }

    // 새로운 쓰레기통 등록 후 임시 쓰레기통 데이터 삭제
    @PostMapping("/deleteUnknownTrashcan")
    public ResponseEntity<?> deleteUnknownTrashcan(@RequestBody Long unknown_trashcan_id) {
        System.out.println("unknown_trashcan_id = " + unknown_trashcan_id);
        return adminService.deleteUnknownTrashcan(unknown_trashcan_id);
    }

    //기존에 있는 쓰레기통 삭제
    // 삭제 하기 전에 다른 유저들의 해당 쓰레기통 신고 내역 삭제
    // 삭제 되면 ShowReportTrashcan에 modifyStatus에 반영
    @PostMapping("/deleteTrashcan")
    public ResponseEntity<?> deleteTrashcan(@RequestBody ReportTrashcanDTO reportTrashcanDTO) {


        Long trashcanId = Long.parseLong(reportTrashcanDTO.getTrashcanId());
        String category = reportTrashcanDTO.getReportCategory();

        try {
            adminService.deleteReportTrashcans(trashcanId, category);
        } catch (Exception e) {
            logger.error("deleteTrashcan - 해당 Trashcan을 같은 신고항목으로 신고한 다른 신고내역들 삭제 실패");
            return ResponseEntity.badRequest().body("13");
        }

        try {
            adminService.modifyStatus(trashcanId,category, 1);
        } catch (Exception e) {
            logger.error("deleteTrashcan - 신고를 처리한 신고내역에 대해 조회용 신고내역의 modifyStatus를 1로 변경 실패");
            return ResponseEntity.badRequest().body("14");
        }

        return adminService.deleteTrashcan(trashcanId);
    }

    //잘못된 신고 삭제
    @PostMapping("/cancelReport")
    public ResponseEntity<?> cancelReport(@RequestBody ReportCancelDTO reportCancelDTO){

        Long trashcanId = Long.parseLong(reportCancelDTO.getTrashcanId());
        String category = reportCancelDTO.getReportCategory();

        return adminService.cancelReport(trashcanId,category, reportCancelDTO.getReasonReportCancel());
    }

    //신고한 쓰레기통 목록 조회
    @GetMapping("/findAllReportTrashcan")
    public List<SendReportTrashcanDTO> findAllReportTrashcan(){
        return adminService.findAllReportTrashcan();
    }


    //reportTrashcan에 있는 쓰레기통 데이터 수정
    @PostMapping("/modifyTrashcan")
    public ResponseEntity<?> modifyTrashcan(@RequestBody ReportDTO reportDTO){
        return adminService.modifyTrashcan(reportDTO);
    }

}
