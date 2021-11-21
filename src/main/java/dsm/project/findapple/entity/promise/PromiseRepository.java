package dsm.project.findapple.entity.promise;

import dsm.project.findapple.entity.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromiseRepository extends JpaRepository<Promise, Long> {
    Optional<Promise> findByMessage(Message message);
    void deleteByMessage(Message message);
}
