package com.iglesia.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.iglesia.Church;
import com.iglesia.ChurchRepository;
import com.iglesia.Person;
import com.iglesia.PersonRepository;
import com.iglesia.dto.request.PersonRequest;
import com.iglesia.dto.response.PersonResponse;
import com.iglesia.validation.ValidationService;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final ChurchRepository churchRepository;
    private final ValidationService validationService;  // Nuevo

    public PersonService(PersonRepository personRepository, 
                        ChurchRepository churchRepository,
                        ValidationService validationService) {  // Inyectar
        this.personRepository = personRepository;
        this.churchRepository = churchRepository;
        this.validationService = validationService;
    }

    @Transactional
    public PersonResponse createPerson(PersonRequest request) {
        // PASO 1: Validar formato con Strategy Pattern
        validationService.validatePerson(request);

        // PASO 2: Validar unicidad
        validateUniqueDocument(request.document(), null);
        validateUniqueEmail(request.email(), null);

        // PASO 3: Crear persona
        Church church = getCurrentChurch();
        Person person = new Person();
        person.setFirstName(request.firstName());
        person.setLastName(request.lastName());
        person.setDocument(request.document());
        person.setPhone(request.phone());
        person.setEmail(request.email());
        person.setChurch(church);
        
        Person savedPerson = personRepository.save(person);
        return PersonResponse.from(savedPerson);
    }

    @Transactional
    public PersonResponse updatePerson(Long id, PersonRequest request) {
        // Validar formato
        validationService.validatePerson(request);

        Person person = personRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                "Persona no encontrada con ID: " + id));
        
        // Validar documento único si cambió
        validateUniqueDocument(request.document(), id);
        // Validar email único si cambió
        validateUniqueEmail(request.email(), id);

        person.setFirstName(request.firstName());
        person.setLastName(request.lastName());
        person.setDocument(request.document());
        person.setPhone(request.phone());
        person.setEmail(request.email());
        
        Person updatedPerson = personRepository.save(person);
        return PersonResponse.from(updatedPerson);
    }

    // ... resto de métodos (getAllPeople, getPersonById, deletePerson) igual que antes

    private void validateUniqueDocument(String document, Long excludeId) {
        if (document != null && !document.isEmpty()) {
            personRepository.findByDocument(document)
                .filter(p -> excludeId == null || !p.getId().equals(excludeId))
                .ifPresent(p -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                        "Ya existe una persona con el documento: " + document);
                });
        }
    }

    private void validateUniqueEmail(String email, Long excludeId) {
        if (email != null && !email.isEmpty()) {
            personRepository.findByEmail(email)
                .filter(p -> excludeId == null || !p.getId().equals(excludeId))
                .ifPresent(p -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                        "Ya existe una persona con el email: " + email);
                });
        }
    }

    private Church getCurrentChurch() {
        return churchRepository.findAll()
            .stream()
            .findFirst()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Debe registrar una iglesia primero"));
    }

    @Transactional(readOnly = true)
    public List<PersonResponse> getAllPeople() {
        Church church = getCurrentChurch();
        return personRepository.findAllByChurchId(church.getId())
            .stream()
            .map(PersonResponse::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public PersonResponse getPersonById(Long id) {
        Person person = personRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                "Persona no encontrada con ID: " + id));
        return PersonResponse.from(person);
    }

    @Transactional
    public void deletePerson(Long id) {
        if (!personRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                "Persona no encontrada con ID: " + id);
        }
        personRepository.deleteById(id);
    }
}