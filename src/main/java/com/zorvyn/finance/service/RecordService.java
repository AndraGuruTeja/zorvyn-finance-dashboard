package com.zorvyn.finance.service;

import com.zorvyn.finance.dto.request.CreateRecordRequest;
import com.zorvyn.finance.dto.request.UpdateRecordRequest;
import com.zorvyn.finance.dto.response.RecordResponse;
import com.zorvyn.finance.entity.FinancialRecord;
import com.zorvyn.finance.entity.RecordType;
import com.zorvyn.finance.entity.User;
import com.zorvyn.finance.exception.BadRequestException;
import com.zorvyn.finance.exception.ResourceNotFoundException;
import com.zorvyn.finance.repository.FinancialRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final FinancialRecordRepository recordRepository;

    public RecordResponse createRecord(CreateRecordRequest request, User currentUser) {
        FinancialRecord record = FinancialRecord.builder()
                .amount(request.getAmount())
                .type(request.getType())
                .category(request.getCategory())
                .date(request.getDate())
                .notes(request.getNotes())
                .createdBy(currentUser)
                .build();

        return RecordResponse.from(recordRepository.save(record));
    }

    public Page<RecordResponse> listRecords(
            RecordType type, String category,
            LocalDate startDate, LocalDate endDate,
            Pageable pageable) {

        Specification<FinancialRecord> spec =
                RecordSpecification.withFilters(type, category, startDate, endDate);

        return recordRepository.findAll(spec, pageable).map(RecordResponse::from);
    }

    public RecordResponse getRecordById(Long id) {
        return RecordResponse.from(findRecordOrThrow(id));
    }

    public RecordResponse updateRecord(Long id, UpdateRecordRequest request) {
        FinancialRecord record = findRecordOrThrow(id);

        boolean updated = false;

        if (request.getAmount() != null) { record.setAmount(request.getAmount()); updated = true; }
        if (request.getType() != null)   { record.setType(request.getType());     updated = true; }
        if (request.getCategory() != null) { record.setCategory(request.getCategory()); updated = true; }
        if (request.getDate() != null)   { record.setDate(request.getDate());     updated = true; }
        if (request.getNotes() != null)  { record.setNotes(request.getNotes());   updated = true; }

        if (!updated) {
            throw new BadRequestException("No valid fields provided for update.");
        }

        return RecordResponse.from(recordRepository.save(record));
    }

    public void deleteRecord(Long id) {
        FinancialRecord record = findRecordOrThrow(id);
        recordRepository.delete(record);
    }

    private FinancialRecord findRecordOrThrow(Long id) {
        return recordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Financial record not found with id: " + id));
    }
}
