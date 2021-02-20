package home.boottest1.service;

import home.boottest1.entities.Files;
import home.boottest1.repos.FilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public interface FilesService {

	public List<Files> findAll();
	public Files findById(Long id);
	public Files save(Files files);
	public void deleteById(Long id);
}