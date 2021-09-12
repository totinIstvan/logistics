package hu.webuni.totinistvan.logistics.service;

import hu.webuni.totinistvan.logistics.model.entity.Section;
import hu.webuni.totinistvan.logistics.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class SectionService {
    
    @Autowired
    private SectionRepository sectionRepository;

    public List<Section> findAll() {
        return sectionRepository.findAll();
    }

    public Optional<Section> findById(long id) {
        return sectionRepository.findById(id);
    }

    @Transactional
    public Section save(Section section) {
        return sectionRepository.save(section);
    }

    @Transactional
    public Section update(Section section) {
        if (sectionRepository.existsById(section.getId())) {
            return save(section);
        }
        throw new NoSuchElementException();
    }

    @Transactional
    public void deleteById(long id) {
        if (sectionRepository.existsById(id)) {
            sectionRepository.deleteById(id);
        }
    }
}
