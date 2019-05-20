package fr.leroymerlin.demodevfest.repository;

import fr.leroymerlin.demodevfest.model.TvShow;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TvShowRepository extends ReactiveMongoRepository<TvShow, String> {
}
