package hu.webuni.totinistvan.logistics.service;

import hu.webuni.totinistvan.logistics.model.entity.Milestone;
import hu.webuni.totinistvan.logistics.repository.MilestoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class MilestoneService {
    
    @Autowired
    private MilestoneRepository milestoneRepository;

    public List<Milestone> findAll() {
        return milestoneRepository.findAll();
    }

    public Optional<Milestone> findById(long id) {
        return milestoneRepository.findById(id);
    }

    @Transactional
    public Milestone save(Milestone milestone) {
        return milestoneRepository.save(milestone);
    }

    @Transactional
    public Milestone update(Milestone milestone) {
        if (milestoneRepository.existsById(milestone.getId())) {
            return save(milestone);
        }
        throw new NoSuchElementException();
    }

    @Transactional
    public void deleteById(long id) {
        if (milestoneRepository.existsById(id)) {
            milestoneRepository.deleteById(id);
        }
    }
}
