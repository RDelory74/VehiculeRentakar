package com.vehiculerentakar.web.controller;


import com.vehiculerentakar.web.model.Vehicule;
import com.vehiculerentakar.web.service.VehiculeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VehiculeController {

    private final VehiculeService vehiculeService;

    public VehiculeController(VehiculeService vehiculeService) {
        this.vehiculeService = vehiculeService;
    }
    @GetMapping("/Vehicules")
    public List<Vehicule> getAllVehicules(){
        return vehiculeService.getAllVehicules();
    }
    @GetMapping("/Vehicules/{id}")
    public Vehicule getVehiculeById(@PathVariable int id){
        return vehiculeService.getVehiculeById(id);
    }
    @GetMapping("/Vehicule/{registration}")
    public Vehicule getVehiculeByRegistration(@PathVariable String registration){
        return vehiculeService.getVehiculeByRegistration(registration);
    }
    @GetMapping("/Vehicule/available")
    public List<Vehicule> getVehiculeAvailable(){
        return vehiculeService.getByAvailability();
    }
    @GetMapping("/Vehicule/type/{type}")
    public List<Vehicule> getVehiculeByType(@PathVariable String type){
        return vehiculeService.findByType(type);
    }
    @PostMapping("/Vehicules")
    public Vehicule createVehicule(@RequestBody Vehicule vehicule){
        return vehiculeService.saveVehicule(vehicule);
    }
    @PutMapping("/Vehicules/{id}")
    public Vehicule updateVehiculeById(@PathVariable int id,@RequestBody Vehicule vehicule){
        return vehiculeService.updateVehiculeById(id, vehicule);
    }
    @DeleteMapping("/Vehicule/{id}")
    public void deleteVehicule(@PathVariable int id){
        vehiculeService.deleteVehicule(id);
    }
}
