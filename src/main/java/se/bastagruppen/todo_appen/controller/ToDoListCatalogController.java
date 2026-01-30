package se.bastagruppen.todo_appen.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import se.bastagruppen.todo_appen.dto.ToDoListCatalogResponse;
import se.bastagruppen.todo_appen.security.CustomPrincipal;
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
    public ToDoListCatalogResponse createCatalog(@RequestParam Long userId, @RequestParam String name) {
        return catalogService.createCatalog(userId, name);
    }

    @GetMapping
    public List<ToDoListCatalogResponse> getCatalogs(@RequestParam Long userId) {
        return catalogService.getCatalogsForUser(userId);
    }

    @DeleteMapping("/{catalogId}")
    public ResponseEntity<Void> deleteCatalog(@PathVariable Long catalogId,
                                              @AuthenticationPrincipal CustomPrincipal user) {
        catalogService.deleteCatalog(catalogId, user.getUserId());

        return ResponseEntity.noContent().build();
    }
}
