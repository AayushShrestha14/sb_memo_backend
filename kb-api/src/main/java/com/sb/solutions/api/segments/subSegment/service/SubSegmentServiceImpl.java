package com.sb.solutions.api.segments.subSegment.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.segments.subSegment.entity.SubSegment;
import com.sb.solutions.api.segments.subSegment.repository.SubSegmentRepository;
import com.sb.solutions.core.dto.SearchDto;
import com.sb.solutions.core.enums.Status;

@Service
@AllArgsConstructor
public class SubSegmentServiceImpl implements SubSegmentService {

    private SubSegmentRepository subSegmentRepository;

    @Override
    public List<SubSegment> findAll() {
        return subSegmentRepository.findAll();
    }

    @Override
    public SubSegment findOne(Long id) {
        return subSegmentRepository.getOne(id);
    }

    @Override
    public SubSegment save(SubSegment subSegment) {
        subSegment.setLastModifiedAt(new Date());
        if (subSegment.getId() == null) {
            subSegment.setStatus(Status.ACTIVE);
        }
        return subSegmentRepository.save(subSegment);
    }

    @Override
    public Page<SubSegment> findAllPageable(Object object, Pageable pageable) {
        ObjectMapper objectMapper = new ObjectMapper();
        SearchDto s = objectMapper.convertValue(object, SearchDto.class);
        return subSegmentRepository
            .subSegmentFilter(s.getName() == null ? "" : s.getName(), pageable);
    }

    @Override
    public List<SubSegment> saveAll(List<SubSegment> list) {
        return subSegmentRepository.saveAll(list);
    }

    @Override
    public Map<Object, Object> subSegmentStatusCount() {
        return subSegmentRepository.subSegmentStatusCount();
    }
}
