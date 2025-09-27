package udpm.hn.server.utils;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import udpm.hn.server.entity.HistoryImport;
import udpm.hn.server.infrastructure.core.constant.LogFileType;
import udpm.hn.server.infrastructure.core.constant.LogType;
import udpm.hn.server.repository.FacilityRepository;
import udpm.hn.server.repository.HistoryImportRepository;
import udpm.hn.server.repository.RoleRepository;
import udpm.hn.server.repository.StaffRepository;

import java.util.List;

@Component
public class HistoryLogUtils {

    @Setter(onMethod_ = {@Autowired})
    private HistoryImportRepository historyImportRepository;

    @Setter(onMethod_ = {@Autowired})
    private FacilityRepository facilityRepository;

    @Setter(onMethod_ = {@Autowired})
    private RoleRepository roleRepository;

    @Setter(onMethod_ = {@Autowired})
    private StaffRepository staffRepository;

//    @Setter(onMethod_ = {@Autowired})
//    private SessionHelper sessionHelper;

    @Setter(onMethod_ = {@Autowired})
    private UserContextHelper userContextHelper;

    public void logErrorRecord(String message, String fileName) {
        try {
            String staffId = userContextHelper.getCurrentUserId();
            String facilityId = userContextHelper.getCurrentIdFacility();

            if (staffId == null || facilityId == null) {
                System.err.println("⚠️ Không thể ghi log lỗi: thiếu thông tin staff hoặc facility");
                return;
            }

            HistoryImport historyImport = new HistoryImport();
            historyImport.setFileName(fileName);
            historyImport.setFacility(facilityRepository.getReferenceById(facilityId));
            historyImport.setMessage(message);
            historyImport.setStaff(staffRepository.getReferenceById(staffId));

            historyImportRepository.save(historyImport);
            System.out.println("✅ Ghi log lỗi thành công: " + message);
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi ghi log lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logErrorRecord(
            String message,
            String fileName,
            String staffId,
            LogFileType logFileType
    ) {
        HistoryImport historyImport = new HistoryImport();
        historyImport.setFileName(fileName);
        historyImport.setMessage(message);
        historyImport.setStaff(staffRepository.getReferenceById(staffId));

//        List<Role> roles = roleRepository.findRoleByStaff(staffId);
//        if (!roles.isEmpty()) {
//            historyImport.setRole(roles.get(0)); // Lưu 1 role chính
//        }

        historyImportRepository.save(historyImport);
    }

    public void logErrorRecord1(
            String message,
            String fileName,
            String staffId,
            String facilityId,
            LogFileType logFileType
    ) {
        HistoryImport historyImport = new HistoryImport();
        historyImport.setFileName(fileName);
        historyImport.setMessage(message);
        historyImport.setType(LogType.ERROR);
        historyImport.setLogFileType(logFileType);

        if (staffId != null) {
            historyImport.setStaff(staffRepository.getReferenceById(staffId));
        }

        if (facilityId != null) {
            historyImport.setFacility(facilityRepository.getReferenceById(facilityId));
        }

        historyImportRepository.save(historyImport);
    }


    public void logSuccessRecord(String message, String fileName) {
        HistoryImport historyImport = new HistoryImport();
        historyImport.setFileName(fileName);
        historyImport.setFacility(facilityRepository.getReferenceById(userContextHelper.getCurrentIdFacility()));
        historyImport.setMessage(message);
        historyImport.setStaff(staffRepository.getReferenceById(userContextHelper.getCurrentUserId()));
        historyImportRepository.save(historyImport);
    }

    public List<HistoryImport> getHistoryImport() {
        return historyImportRepository.findAll();
    }

}
