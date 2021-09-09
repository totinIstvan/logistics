package hu.webuni.totinistvan.logistics.web;

import hu.webuni.totinistvan.logistics.mapper.TransportPlanMapper;
import hu.webuni.totinistvan.logistics.model.dto.DelayDto;
import hu.webuni.totinistvan.logistics.model.dto.TransportPlanDto;
import hu.webuni.totinistvan.logistics.model.entity.TransportPlan;
import hu.webuni.totinistvan.logistics.service.TransportPlanService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/transportPlans")
public class TransportPlanController {

    private final TransportPlanService transportPlanService;
    private final TransportPlanMapper transportPlanMapper;

    public TransportPlanController(TransportPlanService transportPlanService, TransportPlanMapper transportPlanMapper) {
        this.transportPlanService = transportPlanService;
        this.transportPlanMapper = transportPlanMapper;
    }

    @GetMapping
    public List<TransportPlanDto> getAll() {
        return transportPlanMapper.transportPlansToDtos(transportPlanService.findAll());
    }

    @GetMapping("/{id}")
    public TransportPlanDto getById(@PathVariable long id) {
        TransportPlan transportPlan = transportPlanService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return transportPlanMapper.transportPlanToDto(transportPlan);
    }

    @PostMapping
    public TransportPlanDto addNew(@RequestBody TransportPlanDto transportPlanDto) {
        if (transportPlanDto.getId() == null) {
            return transportPlanMapper.transportPlanToDto(transportPlanService.save(transportPlanMapper.dtoToTransportPlan(transportPlanDto)));
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransportPlanDto> updateTransportPlan(@PathVariable long id, @RequestBody TransportPlanDto transportPlanDto) {
        if (transportPlanDto.getId() == null || transportPlanDto.getId() == id) {
            TransportPlan transportPlan = transportPlanMapper.dtoToTransportPlan(transportPlanDto);
            transportPlan.setId(id);
            try {
                TransportPlanDto savedTransportPlan = transportPlanMapper.transportPlanToDto(transportPlanService.update(transportPlan));
                return ResponseEntity.ok(savedTransportPlan);
            } catch (NoSuchElementException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        try {
            transportPlanService.deleteById(id);
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transport plan with id " + id +
                    " cannot be deleted from the database because it still associated with existing sections");
        }
    }

    @PostMapping( "/{id}/delay")
    public void addDelayToMilestones(@PathVariable long id, @RequestBody DelayDto delayDto) {
        try {
            transportPlanService.addDelayToMilestones(id, delayDto.getMilestoneId(), delayDto.getMinutesOfDelay());
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transport plan or milestone not found");
        } catch (InvalidParameterException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The milestone does not belong to the requested transport plan");
        }
    }
}
