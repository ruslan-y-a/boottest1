package home.boottest1.service;

import home.boottest1.entities.Files;
import home.boottest1.entities.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public interface TasksService  {
           public List<Tasks> findAll();
        public Tasks findById(Long id);
        public Tasks save(Tasks tasks);
        public void deleteById(Long id);

}
