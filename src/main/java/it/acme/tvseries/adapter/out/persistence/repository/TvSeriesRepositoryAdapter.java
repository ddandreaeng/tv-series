package it.acme.tvseries.adapter.out.persistence.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import it.acme.tvseries.adapter.out.persistence.entity.TvSeriesEntity;
import it.acme.tvseries.adapter.out.persistence.mapper.TvSeriesEntityMapper;
import it.acme.tvseries.domain.error.TvSeriesDuplicateException;
import it.acme.tvseries.domain.model.TvSeries;
import it.acme.tvseries.domain.port.PageRequest;
import it.acme.tvseries.domain.port.PageResult;
import it.acme.tvseries.domain.port.TvSeriesFilter;
import it.acme.tvseries.domain.port.TvSeriesRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class TvSeriesRepositoryAdapter implements TvSeriesRepository {

    @Inject
    TvSeriesEntityMapper mapper;

    @Inject
    TvSeriesPanacheRepository panacheRepository;

    @Override
    @Transactional
    public TvSeries save(TvSeries tvSeries) {
        try {
            TvSeriesEntity entity = mapper.toEntity(tvSeries);
            boolean exists = panacheRepository.findByIdOptional(entity.getId()).isPresent();
            
            if (exists) {
                entity = panacheRepository.getEntityManager().merge(entity);
            } else {
                panacheRepository.persist(entity);
            }
            
            // Force flush to catch constraint violations immediately
            panacheRepository.flush();
            
            return mapper.toDomain(entity);
        } catch (org.hibernate.exception.ConstraintViolationException e) {
            throw new TvSeriesDuplicateException(
                "A TV series with the same title, year, and director already exists", e
            );
        } catch (PersistenceException e) {
            // Check if it's a constraint violation as a cause
            Throwable cause = e.getCause();
            if (cause instanceof org.hibernate.exception.ConstraintViolationException ||
                cause instanceof ConstraintViolationException ||
                (cause != null && cause.getCause() instanceof ConstraintViolationException)) {
                throw new TvSeriesDuplicateException(
                    "A TV series with the same title, year, and director already exists", e
                );
            }
            throw e;
        }
    }

    @Override
    public Optional<TvSeries> findById(UUID id) {
        return panacheRepository.findByIdOptional(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<TvSeries> findAll() {
        return panacheRepository.listAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        panacheRepository.delete("id", id);
    }

    @Override
    public boolean existsById(UUID id) {
        return panacheRepository.count("id", id) > 0;
    }

    @Override
    public PageResult<TvSeries> findAll(TvSeriesFilter filter, PageRequest pageRequest) {
        var queryBuilder = new StringBuilder();
        var params = new ArrayList<>();
        
        if (filter.query() != null && !filter.query().isBlank()) {
            queryBuilder.append("lower(titolo) like ?").append(params.size() + 1);
            params.add("%" + filter.query().toLowerCase() + "%");
        }
        
        if (filter.genere() != null && !filter.genere().isBlank()) {
            if (!queryBuilder.isEmpty()) queryBuilder.append(" and ");
            queryBuilder.append("genere = ?").append(params.size() + 1);
            params.add(filter.genere());
        }
        
        if (filter.annoFrom() != null) {
            if (!queryBuilder.isEmpty()) queryBuilder.append(" and ");
            queryBuilder.append("anno >= ?").append(params.size() + 1);
            params.add(filter.annoFrom());
        }
        
        if (filter.annoTo() != null) {
            if (!queryBuilder.isEmpty()) queryBuilder.append(" and ");
            queryBuilder.append("anno <= ?").append(params.size() + 1);
            params.add(filter.annoTo());
        }
        
        String query = queryBuilder.isEmpty() ? "" : queryBuilder.toString();
        
        Sort sort = parseSort(pageRequest.sort());
        
        var panachePage = Page.of(pageRequest.page(), pageRequest.size());
        
        List<TvSeriesEntity> entities;
        long total;
        
        if (query.isEmpty()) {
            entities = panacheRepository.findAll(sort).page(panachePage).list();
            total = panacheRepository.count();
        } else {
            entities = panacheRepository.find(query, sort, params.toArray()).page(panachePage).list();
            total = panacheRepository.count(query, params.toArray());
        }
        
        List<TvSeries> items = entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
        
        return new PageResult<>(items, pageRequest.page(), pageRequest.size(), total);
    }
    
    private Sort parseSort(String sortParam) {
        if (sortParam == null || sortParam.isBlank()) {
            return Sort.by("titolo").ascending();
        }
        
        String[] parts = sortParam.split(",");
        String field = parts[0].trim();
        boolean ascending = parts.length < 2 || !parts[1].trim().equalsIgnoreCase("desc");
        
        return ascending ? Sort.by(field).ascending() : Sort.by(field).descending();
    }

    @ApplicationScoped
    static class TvSeriesPanacheRepository implements PanacheRepositoryBase<TvSeriesEntity, UUID> {
    }
}
