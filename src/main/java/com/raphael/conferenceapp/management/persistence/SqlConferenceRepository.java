package com.raphael.conferenceapp.management.persistence;

import com.raphael.conferenceapp.management.entity.Conference;
import com.raphael.conferenceapp.management.entity.ConferenceQuery;
import com.raphael.conferenceapp.management.entity.PaginatedItems;
import com.raphael.conferenceapp.management.usecase.ConferenceRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class SqlConferenceRepository implements ConferenceRepository {
    private final JpaConferenceRepository jpaRepository;

    @Override
    public PaginatedItems<Conference> findByQuery(final ConferenceQuery query) {
        Specification<ConferenceEntity> specification = (root, q, criteriaBuilder) -> {
            List<Predicate> conditions = new LinkedList<>();

            if (StringUtils.isNotBlank(query.title())) {
                conditions.add(criteriaBuilder.like(root.get("title"), "%" + query.title() + "%"));
            }

            if (query.startTime() != null) {
                conditions.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startTime"), query.startTime()));
            }

            if (query.endTime() != null) {
                conditions.add(criteriaBuilder.lessThanOrEqualTo(root.get("endTime"), query.endTime()));
            }

            if (query.userId() != null) {
                conditions.add(criteriaBuilder.equal(root.get("userId"), query.userId()));
            }

            return criteriaBuilder.and(conditions.toArray(new Predicate[0]));
        };

        PageRequest request = PageRequest.of(
                Math.toIntExact(query.currentPage()) - 1,
                Math.toIntExact(query.limit()),
                Sort.by("startTime").descending()
        );

        Page<ConferenceEntity> page = jpaRepository.findAll(specification, request);

        return new PaginatedItems<>(
                page.get().map(ConferenceEntity::toDomain).toList(),
                query.limit(),
                page.getTotalElements(),
                query.offset()
        );
    }

    @Override
    public Optional<Conference> findById(final Long conferenceId) {
        return jpaRepository.findById(conferenceId).map(ConferenceEntity::toDomain);
    }

    @Override
    public Conference save(final Conference conference) {
        return jpaRepository.save(ConferenceEntity.fromDomain(conference)).toDomain();
    }

    @Override
    public void delete(final Long conferenceId) {
        jpaRepository.deleteById(conferenceId);
    }
}
