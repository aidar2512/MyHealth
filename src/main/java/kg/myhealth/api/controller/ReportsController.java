package kg.myhealth.api.controller;

import kg.myhealth.api.dto.report.ReportDto;
import kg.myhealth.domain.User;
import kg.myhealth.service.ReportService;
import kg.myhealth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportsController {

    private final UserService userService;
    private final ReportService reportService;

    @GetMapping("/summary")
    public ReportDto summary(Authentication auth) {
        User u = userService.findByEmailOrThrow(auth.getName());
        return reportService.summary(u);
    }
}
