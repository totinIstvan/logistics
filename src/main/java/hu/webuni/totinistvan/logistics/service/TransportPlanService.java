package hu.webuni.totinistvan.logistics.service;

import hu.webuni.totinistvan.logistics.model.entity.TransportPlan;
import hu.webuni.totinistvan.logistics.repository.TransportPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TransportPlanService {

    @Autowired
    private TransportPlanRepository transportPlanRepository;

    public List<TransportPlan> findAll() {
        return transportPlanRepository.findAll();
    }

    public Optional<TransportPlan> findById(long id) {
        return transportPlanRepository.findById(id);
    }

    @Transactional
    public TransportPlan save(TransportPlan transportPlan) {
        return transportPlanRepository.save(transportPlan);
    }

    @Transactional
    public TransportPlan update(TransportPlan transportPlan) {
        if (transportPlanRepository.existsById(transportPlan.getId())) {
            return save(transportPlan);
        }
        throw new NoSuchElementException();
    }

    @Transactional
    public void deleteById(long id) {
        if (transportPlanRepository.existsById(id)) {
            transportPlanRepository.deleteById(id);
        }
    }
}
