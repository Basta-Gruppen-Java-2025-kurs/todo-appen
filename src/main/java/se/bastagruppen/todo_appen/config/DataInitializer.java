package se.bastagruppen.todo_appen.config;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import se.bastagruppen.todo_appen.model.ToDoList;
import se.bastagruppen.todo_appen.model.ToDoListCatalog;
import se.bastagruppen.todo_appen.model.ToDoListEntry;
import se.bastagruppen.todo_appen.model.User;
import se.bastagruppen.todo_appen.repository.ToDoListCatalogRepository;
import se.bastagruppen.todo_appen.repository.ToDoListEntryRepository;
import se.bastagruppen.todo_appen.repository.ToDoListRepository;
import se.bastagruppen.todo_appen.repository.UserRepository;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final ToDoListCatalogRepository catalogRepository;
    private final ToDoListRepository listRepository;
    private final ToDoListEntryRepository entryRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    public void init() {
        if (userRepository.count() > 0) return;

        /* ======== USERS ======== */

        User alice = new User();
        alice.setUsername("Alice");
        alice.setPassword(passwordEncoder.encode("password123"));
        alice = userRepository.save(alice);

        User bob = new User();
        bob.setUsername("Bob");
        bob.setPassword(passwordEncoder.encode("password123"));
        bob = userRepository.save(bob);

        /* ======== CATALOGS ======== */

        ToDoListCatalog workAlice = createCatalog("Work", alice);
        ToDoListCatalog privateAlice = createCatalog("Private", alice);

        ToDoListCatalog workBob = createCatalog("Work", bob);

        /* ======== LISTS ======== */

        ToDoList aliceBackend = createList("Backend tasks", workAlice, alice);
        ToDoList aliceGroceries = createList("Groceries", privateAlice, alice);

        ToDoList bobPlanning = createList("Sprint planning", workBob, bob);

        /* ======== ENTRIES ======== */

        ToDoListEntry e1 = createEntry(
                "Implement ToDoList API",
                "Create controller + service",
                false,
                LocalDate.now().plusDays(3),
                aliceBackend,
                null
        );

        ToDoListEntry e2 = createEntry(
                "Write tests",
                null,
                false,
                LocalDate.now().plusDays(5),
                aliceBackend,
                e1
        );

        createEntry(
                "Milk",
                null,
                true,
                null,
                aliceGroceries,
                null
        );

        createEntry(
                "Prepare sprint backlog",
                null,
                false,
                LocalDate.now().plusDays(1),
                bobPlanning,
                null
        );
    }

    /* ======== HELPERS ======== */

    private ToDoListCatalog createCatalog(String name, User user) {
        ToDoListCatalog catalog = new ToDoListCatalog();
        catalog.setName(name);
        catalog.setUser(user);
        return catalogRepository.save(catalog);
    }

    private ToDoList createList(String name, ToDoListCatalog catalog, User owner) {
        ToDoList list = new ToDoList();
        list.setName(name);
        list.setCatalog(catalog);
        list.setCatalogId(catalog.getId());
        list.setOwner(owner);
        list.setOwnerId(owner.getId());
        return listRepository.save(list);
    }

    private ToDoListEntry createEntry(
            String summary,
            String details,
            boolean done,
            LocalDate deadline,
            ToDoList list,
            ToDoListEntry parent
    ) {
        ToDoListEntry entry = new ToDoListEntry();
        entry.setSummary(summary);
        entry.setDetails(details);
        entry.setDone(done);
        entry.setDeadline(deadline);
        entry.setList(list);
        entry.setParent(parent);
        return entryRepository.save(entry);
    }
}
