package com.hdda.services.impl;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hdda.dto.request.TrafficReportRequestDTO;
import com.hdda.pojo.TrafficReport;
import com.hdda.pojo.User;
import com.hdda.repositories.TrafficReportRepository;
import com.hdda.repositories.UserRepository;
import com.hdda.services.TrafficReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TrafficReportServiceImpl implements TrafficReportService {
    
    @Autowired
    private TrafficReportRepository trafficReportRepository;
    
    @Autowired
    private UserRepository userRepo;
    
    @Override
    public TrafficReport saveReport(TrafficReportRequestDTO req) {
        // ① Lấy user từ SecurityContext
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        User currentUser = userRepo.getUserByUsername(username);
        
        // ② Tạo TrafficReport entity
        TrafficReport report = new TrafficReport();
        report.setLatitude(req.getLatitude());
        report.setLongitude(req.getLongitude());
        report.setDescription(req.getDescription());
        report.setImageUrl(req.getImageUrl());
        report.setStatus("PENDING");
        report.setCreatedAt(new Date());
        report.setUserId(currentUser);
        
        // ③ Save vào database trước
        trafficReportRepository.save(report);
        
        // ④ Push Firebase (sau khi có ID từ database)
//        pushToFirebase(report, currentUser);
        
        return report;
    }
    
    private void pushToFirebase(TrafficReport report, User user) {
        try {
            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference("reports")
                    .child("report_" + report.getId());
            
            // Tạo data map
            Map<String, Object> firebaseData = new HashMap<>();
            firebaseData.put("id", report.getId());
            firebaseData.put("lat", report.getLatitude());
            firebaseData.put("lng", report.getLongitude());
            firebaseData.put("description", report.getDescription());
            firebaseData.put("imageUrl", report.getImageUrl());
            firebaseData.put("status", report.getStatus());
            firebaseData.put("createdAt", report.getCreatedAt().getTime()); // Timestamp thay vì toString
            firebaseData.put("userId", user.getId());
            firebaseData.put("username", user.getUsername()); // Thêm username cho dễ debug
            
            // Push async
            ref.setValueAsync(firebaseData).addListener(() -> {
                System.out.println("✅ Firebase push SUCCESS for report_" + report.getId());
            }, Runnable::run);
            
            System.out.println("⏳ Firebase push QUEUED for report_" + report.getId());
            
        } catch (Exception ex) {
            System.err.println("🔥 Firebase push FAILED for report_" + report.getId());
            System.err.println("Error: " + ex.getMessage());
            ex.printStackTrace();
            // Không throw exception để không làm gián đoạn việc save vào DB
        }
    }

@Override
public List<TrafficReport> getAllReports() {
    return trafficReportRepository.getAllReports();
}

@Override
public void updateReportStatus(Long reportId, String status) {
    TrafficReport r = trafficReportRepository.getReportById(reportId);
    if (r != null) {
        r.setStatus(status);
        trafficReportRepository.save(r);  // dùng chung persist/merge
    }
}

@Override
public TrafficReport getReportById(Long id) {
    return trafficReportRepository.getReportById(id);
}
}