package com.raphael.conferenceapp.management.persistence;

import com.raphael.conferenceapp.management.entity.PaginatedItems;
import com.raphael.conferenceapp.management.entity.Speaker;
import com.raphael.conferenceapp.management.entity.SpeakerQuery;
import com.raphael.conferenceapp.management.usecase.SpeakerRepository;
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

@AllArgsConstructor
@Component
public class SqlSpeakerRepository implements SpeakerRepository {
    private final JpaSpeakerRepository jpaRepository;

    @Override
    public PaginatedItems<Speaker> findByQuery(final SpeakerQuery query) {
        Specification<SpeakerEntity> specification = (root, q, criteriaBuilder) -> {
            List<Predicate> conditions = new LinkedList<>();

            if (StringUtils.isNotBlank(query.name())) {
                Predicate predicate = criteriaBuilder.or(
                        criteriaBuilder.like(root.get("firstName"), "%" + query.name() + "%"),
                        criteriaBuilder.like(root.get("lastName"), "%" + query.name() + "%")
                );

                conditions.add(predicate);
            }

            if (StringUtils.isNotBlank(query.email())) {
                conditions.add(criteriaBuilder.equal(root.get("email"), query.email()));
            }

            return criteriaBuilder.and(conditions.toArray(new Predicate[0]));
        };

        PageRequest request = PageRequest.of(
                Math.toIntExact(query.currentPage()) - 1,
                Math.toIntExact(query.limit()),
                Sort.by("firstName").ascending()
        );

        Page<SpeakerEntity> page = jpaRepository.findAll(specification, request);

        return new PaginatedItems<>(
                page.get().map(SpeakerEntity::toDomain).toList(),
                query.limit(),
                page.getTotalElements(),
                query.offset()
        );
    }

    @Override
    public Optional<Speaker> findById(final Long id) {
        return jpaRepository.findById(id).map(SpeakerEntity::toDomain);
    }

    @Override
    public Speaker save(final Speaker speaker) {
        return jpaRepository.save(SpeakerEntity.fromDomain(speaker)).toDomain();
    }

    @Override
    public void delete(final Long id) {
        jpaRepository.deleteById(id);
    }
}
