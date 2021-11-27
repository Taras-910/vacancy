package ua.training.top.repository.datajpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Freshen;
import ua.training.top.repository.FreshenRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Repository
public class DataJpaFreshenRepository implements FreshenRepository {
    protected final Logger log = LoggerFactory.getLogger(getClass());
//    private static final Sort RECORDED_DATE = Sort.by(Sort.Direction.ASC, "recordedDate");
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
        Freshen freshen = crudRepository.findById(id).orElse(null);
            assert freshen != null;
            freshen.setVacancies(null);
            return freshen;
    }

    @Override
    public List<Freshen> getAll() {
        return crudRepository.findAll().stream().map(f -> {
            f.setVacancies(null);
            return f;
        }).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteList(List<Freshen> listToDelete) {
        crudRepository.deleteAll(listToDelete);
    }

    @Transactional
    @Override
    public void deleteOutDated(LocalDateTime reasonLocalDateTime) {
        deleteList(crudRepository.getOutDated(reasonLocalDateTime));
    }

    @Transactional
    @Override
    public void deleteExceedLimit(int limitFreshen) {
        if (getAll().size() > limitFreshen) {
            deleteList(crudRepository.findExceeded(limitFreshen));
        }
    }

}

