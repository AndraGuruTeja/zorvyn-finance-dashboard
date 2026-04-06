package com.zorvyn.finance.config;

import com.zorvyn.finance.entity.*;
import com.zorvyn.finance.repository.FinancialRecordRepository;
import com.zorvyn.finance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Seeds the database with default users and sample records on first run.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final FinancialRecordRepository recordRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Database already seeded — skipping.");
            return;
        }

        log.info("Seeding database with default users and sample records...");

        // ── Default Users ──
        User admin = userRepository.save(User.builder()
                .name("Admin User")
                .email("admin@zorvyn.com")
                .password(passwordEncoder.encode("admin123"))
                .role(Role.ADMIN)
                .status(UserStatus.ACTIVE)
                .build());

        User analyst = userRepository.save(User.builder()
                .name("Jane Analyst")
                .email("jane@zorvyn.com")
                .password(passwordEncoder.encode("analyst123"))
                .role(Role.ANALYST)
                .status(UserStatus.ACTIVE)
                .build());

        userRepository.save(User.builder()
                .name("Bob Viewer")
                .email("bob@zorvyn.com")
                .password(passwordEncoder.encode("viewer123"))
                .role(Role.VIEWER)
                .status(UserStatus.ACTIVE)
                .build());

        // ── Sample Financial Records ──
        List<FinancialRecord> records = List.of(
                record(admin, "5000.00", RecordType.INCOME, "salary", "Monthly salary", -1),
                record(admin, "5000.00", RecordType.INCOME, "salary", "Monthly salary", -2),
                record(admin, "5000.00", RecordType.INCOME, "salary", "Monthly salary", -3),
                record(admin, "1500.00", RecordType.EXPENSE, "rent", "Apartment rent", -1),
                record(admin, "1500.00", RecordType.EXPENSE, "rent", "Apartment rent", -2),
                record(admin, "1500.00", RecordType.EXPENSE, "rent", "Apartment rent", -3),
                record(analyst, "2500.00", RecordType.INCOME, "freelance", "Consulting project", -1),
                record(analyst, "350.00", RecordType.EXPENSE, "food", "Groceries", -1),
                record(analyst, "120.00", RecordType.EXPENSE, "transport", "Fuel", -1),
                record(admin, "800.00", RecordType.INCOME, "investment", "Dividend payout", -2),
                record(admin, "200.00", RecordType.EXPENSE, "utilities", "Electric bill", -1),
                record(admin, "150.00", RecordType.EXPENSE, "entertainment", "Concert tickets", -1),
                record(analyst, "3000.00", RecordType.INCOME, "freelance", "Web dev project", -2),
                record(analyst, "450.00", RecordType.EXPENSE, "shopping", "Electronics", -2),
                record(admin, "75.00", RecordType.EXPENSE, "healthcare", "Pharmacy", -1)
        );
        recordRepository.saveAll(records);

        log.info("Seeded {} users and {} records.", 3, records.size());
    }

    private FinancialRecord record(User user, String amount, RecordType type,
                                    String category, String notes, int monthsAgo) {
        return FinancialRecord.builder()
                .createdBy(user)
                .amount(new BigDecimal(amount))
                .type(type)
                .category(category)
                .notes(notes)
                .date(LocalDate.now().plusMonths(monthsAgo))
                .build();
    }
}
