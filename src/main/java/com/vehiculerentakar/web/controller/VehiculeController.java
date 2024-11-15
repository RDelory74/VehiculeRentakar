package com.vehiculerentakar.web.controller;


import com.vehiculerentakar.web.model.Vehicule;
import com.vehiculerentakar.web.service.VehiculeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/vehicules")
@CrossOrigin
public class VehiculeController {

    private final VehiculeService vehiculeService;

    public VehiculeController(VehiculeService vehiculeService) {
        this.vehiculeService = vehiculeService;
    }

    @GetMapping
    public List<Vehicule> getAllVehicules() {
        return vehiculeService.getAllVehicules();
    }

    @GetMapping("/id/{id}")
    public Vehicule getVehiculeById(@PathVariable int id) {
        return vehiculeService.getVehiculeById(id);
    }

    @GetMapping("/registration/{registration}")
    public Vehicule getVehiculeByRegistration(@PathVariable String registration) {
        return vehiculeService.getVehiculeByRegistration(registration);
    }

    @GetMapping("/displacement/{id}")
    public int getdisplacement(@PathVariable int id) {
        return vehiculeService.getDisplacementByid(id);
    }

    @GetMapping("/horsepower/{id}")
    public int getHorsepowerById(@PathVariable int id) {
        return vehiculeService.getHorsePowerByid(id);
    }

    @GetMapping("/cargo/{id}")
    public int getVehiculeCargoById(@PathVariable int id) {
        return vehiculeService.getVehiculeCargoById(id);
    }

    @GetMapping("/available")
    public List<Vehicule> getVehiculeAvailable() {
        return vehiculeService.getByAvailability();
    }

    @GetMapping("/typeName/{type}")
    public List<Vehicule> getVehiculeByType(@PathVariable String type) {
        return vehiculeService.findByType(type);
    }

    @GetMapping("/type/{id}")
    public String getVehiculeTypeById(@PathVariable int id) {
        return vehiculeService.getTypeById(id);
    }

    @PostMapping
    public Vehicule createVehicule(@RequestBody Vehicule vehicule) {
        return vehiculeService.saveVehicule(vehicule);
    }

    @PutMapping("/{id}")
    public Vehicule updateVehiculeById(@PathVariable int id, @RequestBody Vehicule vehicule) {
        return vehiculeService.updateVehiculeById(id, vehicule);
    }

    @DeleteMapping("/{id}")
    public void deleteVehicule(@PathVariable int id) {
        vehiculeService.deleteVehicule(id);
    }

    @GetMapping("/available/{startDate},{endDate}")
    public ResponseEntity<List<Vehicule>> getVehiculeByDate(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        // Validation des dates
        if (startDate == null || endDate == null) {
            return ResponseEntity.badRequest().build();
        }

        if (startDate.isAfter(endDate)) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            List<Vehicule> availableVehicles = vehiculeService.getAvailableVehicles(startDate, endDate);
            return ResponseEntity.ok(availableVehicles);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/pricecatalog/{id}/{startDate}/{endDate}/{estimateKm}")
    public double getSimulatedPrice (
            @PathVariable int id,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate startDate,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate endDate,
            @PathVariable int estimateKm){
        return vehiculeService.calculateAmountCatalog(id,startDate,endDate,estimateKm);
    }

    @PutMapping("/upOrderedVehicule/{id}")
    public Vehicule updateOrderedById(@PathVariable int id,@RequestBody Vehicule vehicule) {
        return vehiculeService.updateOrderedById(id, vehicule);
    }
}
