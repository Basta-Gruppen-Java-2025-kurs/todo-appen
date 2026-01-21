package se.bastagruppen.todo_appen.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.bastagruppen.todo_appen.dto.ToDoListCatalogResponseDto;
import se.bastagruppen.todo_appen.service.ToDoListCatalogService;

import java.util.List;

@RestController
@RequestMapping("/catalogs")
public class ToDoListCatalogController {

    private final ToDoListCatalogService catalogService;

    public ToDoListCatalogController(ToDoListCatalogService catalogService){
        this.catalogService = catalogService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ToDoListCatalogResponseDto createCatalog(@RequestParam Long userId, @RequestParam String name) {
        return catalogService.createCatalog(userId, name);
    }

    @GetMapping
    public List<ToDoListCatalogResponseDto> getCatalogs(@RequestParam Long userId) {
        return catalogService.getCatalogsForUser(userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCatalog(@PathVariable Long id) {
        catalogService.deleteCatalog(id);
        return ResponseEntity.noContent().build();
    }
}
