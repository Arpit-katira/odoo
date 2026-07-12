package com.odoo.features.fuel.service;
import com.odoo.features.fuel.dto.*;
import java.util.List;

public interface FuelService {

    FuelResponseDTO create(FuelRequestDTO dto);

    List<FuelResponseDTO> getAll();

    FuelResponseDTO getById(Long id);

    FuelResponseDTO update(Long id, FuelRequestDTO dto);

    void delete(Long id);
}

