package com.vehiculerentakar.web.repository;


import com.vehiculerentakar.web.model.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface VehiculeRepository extends JpaRepository<Vehicule, Integer> {

    @Query("Select v From Vehicule v where v.registration=:registration")
    Optional<Vehicule> findByRegistration(String registration);

    @Query("Select v From Vehicule v Where v.isAvailable = true")
    List<Vehicule> findByIsAvailable();

    @Query("Select v From Vehicule v where v.type=:type")
    List<Vehicule> findbyType(String type);

    @Modifying
    @Query("UPDATE Vehicule v SET v.kilometer = :kilometer WHERE v.id = :id")
    int updateKilometersById(@Param("id") int id, @Param("kilometer") int kilometer);

    @Query("SELECT u.displacement FROM Vehicule u WHERE u.id = :id")
    int getDisplacementByid(int id);

    @Query("SELECT u.horsePower FROM Vehicule u WHERE u.id = :id")
    int getHPById(int id);

    @Query ("Select t.type From Vehicule t Where t.id =:id")
    String getTypeById(int id);

    @Query ("Select c.cargo From Vehicule c Where c.id=:id")
    int getCargoById(int id);
}

