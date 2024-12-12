package org.jala.university.application.serviceImpl;

import org.jala.university.domain.entity.CreditCardType;
import org.jala.university.domain.repository.CreditCardTypeRepository;
import org.jala.university.application.dto.CreditCardTypeDTO;
import org.jala.university.application.mapper.CreditCardTypeMapper;
import org.jala.university.application.service.CreditCardTypeService;

import java.util.List;
import java.util.UUID;

public class CreditCardTypeServiceImpl implements CreditCardTypeService {
    private final CreditCardTypeRepository typeRepository; 
    private final CreditCardTypeMapper typeMapper; 

    public CreditCardTypeServiceImpl(CreditCardTypeRepository typeRepository, CreditCardTypeMapper typeMapper) {
        this.typeRepository = typeRepository; 
        this.typeMapper = typeMapper;
    }

    @Override
    public CreditCardTypeDTO save(CreditCardTypeDTO creditCardType) {
        try {
            CreditCardType type = typeRepository.save(typeMapper.mapFrom(creditCardType));
            return typeMapper.mapTo(type); 
        } catch (Exception e) {
            throw new RuntimeException("Error saving credit card type: " + e.getMessage());
        }
    }
    
    @Override
    public CreditCardTypeDTO update(CreditCardTypeDTO creditCardType) {
        try {
            CreditCardType type = typeRepository.save(typeMapper.mapFrom(creditCardType));
            return typeMapper.mapTo(type); 
        } catch (Exception e) {
            throw new RuntimeException("Error updating credit card type: " + e.getMessage());
        }
    }

    @Override
    public void delete(CreditCardTypeDTO typeDto) {
        try {
            typeRepository.delete(typeMapper.mapFrom(typeDto));
        } catch (Exception e) {
            throw new RuntimeException("Error deleting credit card type: " + e.getMessage());
        }
    }

    @Override
    public void deleteById(UUID id) {
        try {
            typeRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting credit card type: " + e.getMessage());
        }
    }

    @Override
    public CreditCardTypeDTO findById(UUID id) {
        try {
            CreditCardType type = typeRepository.findById(id); 
            return typeMapper.mapTo(type);
        } catch (Exception e) {
            throw new RuntimeException("Error finding credit card type: " + e.getMessage());
        }
    }

    @Override
    public List<CreditCardTypeDTO> findAll() {
        try {
            List<CreditCardType> types = typeRepository.findAll();
            return types.stream().map(typeMapper::mapTo).toList(); 
        } catch (Exception e) {
            throw new RuntimeException("Error finding all credit card types: " + e.getMessage());
        }
    }

    @Override
    public CreditCardTypeDTO findLast() {
        try {
            return typeMapper.mapTo(typeRepository.findLast()); 
        } catch (Exception e) {
            throw new RuntimeException("Error finding last added credit card type: " + e.getMessage());
        }
    }
}
