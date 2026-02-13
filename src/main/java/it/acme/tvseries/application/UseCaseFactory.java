package it.acme.tvseries.application;

import it.acme.tvseries.application.usecase.*;
import it.acme.tvseries.domain.port.TvSeriesRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

@ApplicationScoped
public class UseCaseFactory {

    @Inject
    TvSeriesRepository repository;

    @Produces
    @ApplicationScoped
    public CreateTvSeriesUseCase createTvSeriesUseCase() {
        return new CreateTvSeriesUseCase(repository);
    }

    @Produces
    @ApplicationScoped
    public GetAllTvSeriesUseCase getAllTvSeriesUseCase() {
        return new GetAllTvSeriesUseCase(repository);
    }

    @Produces
    @ApplicationScoped
    public GetTvSeriesByIdUseCase getTvSeriesByIdUseCase() {
        return new GetTvSeriesByIdUseCase(repository);
    }

    @Produces
    @ApplicationScoped
    public UpdateTvSeriesUseCase updateTvSeriesUseCase() {
        return new UpdateTvSeriesUseCase(repository);
    }

    @Produces
    @ApplicationScoped
    public DeleteTvSeriesUseCase deleteTvSeriesUseCase() {
        return new DeleteTvSeriesUseCase(repository);
    }
}
