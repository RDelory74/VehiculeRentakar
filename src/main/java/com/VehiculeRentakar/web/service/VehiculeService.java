package com.VehiculeRentakar.web.service;


import com.VehiculeRentakar.web.controller.VehiculeController;
import com.VehiculeRentakar.web.model.Vehicule;
import com.VehiculeRentakar.web.repository.VehiculeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class VehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final String orderServiceUrl = "http://localhost:9092/orders";
    private final String userServiceUrl = "http://localhost:9093/Users";
    private  RestTemplate restTemplate;

    public VehiculeService(VehiculeRepository vehiculeRepository) {
        this.vehiculeRepository = vehiculeRepository;
    }

    public List<Vehicule> getAllVehicules(){
        System.out.print("Fetching all Vehiculs ||");
        return vehiculeRepository.findAll();
    }

    public Vehicule getVehiculeById(int id){
        Vehicule vehicule = vehiculeRepository.findById(id).orElse(null);
        if(vehicule == null){
            System.out.println("Vehicule not found");
        }
        System.out.print("Fetching Vehicule by id ||");
        System.out.println("Vehicul with id: " + id + " found  ||");
        System.out.println(vehicule);
        return vehicule;
    }

    public Vehicule saveVehicule(Vehicule vehicule){
        System.out.print("Vehicule " + vehicule.getBrand() + vehicule.getModel() + "with"+ vehicule.getRegistration() + " created ||");
        return vehiculeRepository.save(vehicule);
    }

    public Vehicule getVehiculeByRegistration(String registration){
        System.out.print("Fetching Vehicule by registration ||");
        System.out.println("Vehicule with type: " + registration + " found  ||");
        return vehiculeRepository.findByRegistration(registration).orElse(null);
    }

    public List<Vehicule> findByType(String type){
        System.out.print("Fetching Vehicule by type ||");
        System.out.println("Vehicule with type: " + type + " found  ||");
        return vehiculeRepository.findbyType(type);
    }

    public Vehicule updateVehiculeById(int id, Vehicule vehicule){
        Vehicule vehiculeToUpdate = vehiculeRepository.findById(id).orElse(null);
        if (vehiculeToUpdate == null){
            System.out.println("Vehicule not found");
        } else {
            System.out.print("Vehicule " + vehicule.getBrand() + vehicule.getRegistration()+ " updated");
        return vehiculeRepository.save(vehicule);
    }
    return null;
    }

    public List<Vehicule> getByAvailability(){
        return vehiculeRepository.findByIsAvailable();
    }

    public void deleteVehicule(int id){
        vehiculeRepository.deleteById(id);
    }



    public boolean isDateAvailable(int id, LocalDate startDate, LocalDate endDate) {


        List<Map<String, Object>> orders = restTemplate.getForObject(orderServiceUrl + "/" + id, List.class);

        for (Map<String,Object> order : orders) {
           LocalDate reservedStart = LocalDate.parse(order.get("startDate").toString());
            LocalDate reservedEnd = LocalDate.parse(order.get("endDate").toString());


            if ((startDate.isBefore(reservedEnd) && endDate.isAfter(reservedStart)) ||
                    startDate.equals(reservedStart) || endDate.equals(reservedEnd)) {
                return false;  // Date indisponible
            }
        }
        return true;  // Date disponible
    }



}
