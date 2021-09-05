package hu.webuni.totinistvan.logistics.web;

import hu.webuni.totinistvan.logistics.mapper.TransportPlanMapper;
import hu.webuni.totinistvan.logistics.model.dto.TransportPlanDto;
import hu.webuni.totinistvan.logistics.model.entity.TransportPlan;
import hu.webuni.totinistvan.logistics.service.TransportPlanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
        transportPlanService.deleteById(id);
    }
}
