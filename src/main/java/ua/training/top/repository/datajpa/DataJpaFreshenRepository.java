package ua.training.top.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Freshen;
import ua.training.top.repository.FreshenRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Repository
public class DataJpaFreshenRepository implements FreshenRepository {
    private static final Sort SORT_LANGUAGE = Sort.by(Sort.Direction.ASC, "language", "workplace");
    private final CrudFreshenRepository crudRepository;

    public DataJpaFreshenRepository(CrudFreshenRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Transactional
    @Override
    public Freshen save(Freshen freshen) {
        return crudRepository.save(freshen);
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        return crudRepository.delete(id) != 0;
    }

    @Override
    public Freshen get(int id) {
        return crudRepository.findById(id).orElse(null);
    }

    @Override
    public List<Freshen> getAll() {
        return crudRepository.findAll(SORT_LANGUAGE);
    }

    @Override
    public List <Freshen> getByDoubleString(String workplace, String language) {
        List<Freshen> freshens = null;
        try {
            freshens = crudRepository.getByDoubleString(workplace, language);
        } catch (Exception e) {}
        return freshens;
    }

    @Override
    public Freshen getLastAuth(int userId) {
        List<Freshen> freshens = Optional.of(crudRepository.getAllAuth(userId)).orElse(null);
        return freshens == null ? new Freshen(null, null, "", "", userId) : freshens.stream()
                .sorted((f1, f2) -> f1.getRecordedDate().compareTo(f2.getRecordedDate()))
                .collect(Collectors.toList()).get(freshens.size() - 1);
    }
}

