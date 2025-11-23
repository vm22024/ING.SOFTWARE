package com.ues.edu.servicios.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ues.edu.modelo.Naviera;
import com.ues.edu.repositorios.INavieraRepo;
import com.ues.edu.servicios.INavieraService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class NavieraServiceImpl implements INavieraService {

    private final INavieraRepo navieraRepo;

    public NavieraServiceImpl(INavieraRepo navieraRepo) {
        this.navieraRepo = navieraRepo;
    }

    @Override
    public Naviera guardar(Naviera obj) {
        return navieraRepo.save(obj);
    }

    @Override
    public List<Naviera> listar() {
        return navieraRepo.findAll();
    }

    @Override
    public Naviera leerPorId(Integer id) {
        return navieraRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Naviera no encontrada con ID: " + id));
    }

    @Override
    public boolean eliminar(Integer id) {
        if (navieraRepo.existsById(id)) {
            navieraRepo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
