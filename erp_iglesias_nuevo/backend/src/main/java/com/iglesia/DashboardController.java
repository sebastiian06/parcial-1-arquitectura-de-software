package com.iglesia;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final PersonRepository personRepository;
    private final CourseRepository courseRepository;
    private final OfferingRepository offeringRepository;
    private final ChurchRepository churchRepository;
    // private final PaymentRepository paymentRepository; // Comentado temporalmente

    public DashboardController(
            PersonRepository personRepository,
            CourseRepository courseRepository,
            OfferingRepository offeringRepository,
            ChurchRepository churchRepository) {
        this.personRepository = personRepository;
        this.courseRepository = courseRepository;
        this.offeringRepository = offeringRepository;
        this.churchRepository = churchRepository;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CLIENT')")
    @GetMapping
    public DashboardResponse getDashboard() {
        Church church = getCurrentChurch();
        Long churchId = church.getId();
        
        long totalPeople = personRepository.countByChurchId(churchId);
        long activeCourses = courseRepository.countByChurchIdAndActiveTrue(churchId);
        Double offeringsMonth = offeringRepository.sumAmountByChurchIdAndStatus(churchId, "COMPLETED");
        long pendingPayments = 0L; // Temporal mientras arreglamos PaymentRepository
        
        return new DashboardResponse(
            totalPeople,
            activeCourses,
            offeringsMonth != null ? offeringsMonth : 0.0,
            pendingPayments
        );
    }

    private Church getCurrentChurch() {
        return churchRepository.findAll()
            .stream()
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No hay iglesia registrada"));
    }

    public record DashboardResponse(
        long totalPeople,
        long activeCourses,
        double offeringsMonth,
        long pendingPayments
    ) {}
}