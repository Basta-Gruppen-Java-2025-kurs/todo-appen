package se.bastagruppen.todo_appen.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.bastagruppen.todo_appen.dto.ToDoListEntryRequestDto;
import se.bastagruppen.todo_appen.dto.ToDoListEntryResponseDto;
import se.bastagruppen.todo_appen.dto.ToDoListEntryUpdateDoneDto;
import se.bastagruppen.todo_appen.exception.BadRequestException;
import se.bastagruppen.todo_appen.exception.NotFoundException;
import se.bastagruppen.todo_appen.mapper.ToDoListEntryMapper;
import se.bastagruppen.todo_appen.model.ToDoList;
import se.bastagruppen.todo_appen.model.ToDoListEntry;
import se.bastagruppen.todo_appen.repository.ToDoListEntryRepository;
import se.bastagruppen.todo_appen.repository.ToDoListRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ToDoListEntryServiceTest {

    @Mock
    private ToDoListRepository listRepository;

    @Mock
    private ToDoListEntryRepository repository;

    @Mock
    private ToDoListEntryMapper mapper;

    @InjectMocks
    private ToDoListEntryService service;

    Long entryId = 12L;
    Long listId = 1L;
    Long userId = 123L;

    private ToDoList list;
    private ToDoListEntry entry;

    @BeforeEach
    void setUp() {
        list = new ToDoList();
        list.setId(listId);
        entry = new ToDoListEntry();
        entry.setId(entryId);
        entry.setDone(false);
    }


    @Test
    void createToDoListEntry_shouldCreateEntry_whenUserOwnsList() {
        ToDoListEntryRequestDto dto = new ToDoListEntryRequestDto();
        dto.setParentId(null);

        ToDoListEntry saved = new ToDoListEntry();
        ToDoListEntryResponseDto responseDto = new ToDoListEntryResponseDto();

        when(listRepository.findByIdAndOwnerId(listId, userId))
                .thenReturn(Optional.of(list));
        when(mapper.toEntity(dto)).thenReturn(entry);
        when(repository.save(entry)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(responseDto);

        ToDoListEntryResponseDto result =
                service.createToDoListEntry(listId, dto, userId);

        assertNotNull(result);
        assertEquals(responseDto, result);

        verify(repository).save(entry);
    }

    @Test
    void createToDoListEntry_shouldThrowNotFound_whenListNotOwned() {
        ToDoListEntryRequestDto dto = new ToDoListEntryRequestDto();

        when(listRepository.findByIdAndOwnerId(listId, userId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.createToDoListEntry(listId, dto, userId));

        verify(repository, never()).save(any());
    }

    @Test
    void createToDoListEntry_shouldThrowBadRequest_whenParentInDifferentList() {
        ToDoListEntryRequestDto dto = new ToDoListEntryRequestDto();
        dto.setParentId(99L);

        ToDoList otherList = new ToDoList();
        otherList.setId(2L);

        ToDoListEntry parent = new ToDoListEntry();
        parent.setList(otherList);

        when(listRepository.findByIdAndOwnerId(listId, userId))
                .thenReturn(Optional.of(list));
        when(mapper.toEntity(dto)).thenReturn(entry);
        when(repository.findById(99L)).thenReturn(Optional.of(parent));

        assertThrows(BadRequestException.class,
                () -> service.createToDoListEntry(listId, dto, userId));

        verify(repository, never()).save(any());
    }

    @Test
    void getAllEntriesOfAList_shouldReturnEntries() {
        ToDoListEntryResponseDto responseDto = new ToDoListEntryResponseDto();

        when(listRepository.findByIdAndOwnerId(listId, userId))
                .thenReturn(Optional.of(list));
        when(repository.findByListIdAndParentIsNull(listId))
                .thenReturn(List.of(entry));
        when(mapper.toDto(entry)).thenReturn(responseDto);

        List<ToDoListEntryResponseDto> result = service.getAllEntriesOfAList(listId, userId);

        assertEquals(1, result.size());
        assertTrue(result.contains(responseDto));

        verify(listRepository).findByIdAndOwnerId(listId, userId);
        verify(repository).findByListIdAndParentIsNull(listId);
        verify(mapper).toDto(entry);
    }

    @Test
    void getAllEntriesOfAList_shouldThrowNotFound_whenListNotOwned() {
        when(listRepository.findByIdAndOwnerId(listId, userId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.getAllEntriesOfAList(listId, userId));

        verify(listRepository).findByIdAndOwnerId(listId, userId);
        verifyNoInteractions(repository);
        verifyNoInteractions(mapper);
    }

    @Test
    void deleteEntryById_shouldDelete_whenUserOwnsEntry() {
        when(repository.findByIdWithOwner(entryId, userId))
                .thenReturn(Optional.of(entry));

        service.deleteEntryById(entryId, userId);

        verify(repository).findByIdWithOwner(entryId, userId);
        verify(repository).deleteById(entryId);
    }

    @Test
    void deleteEntryById_shouldThrowNotFound_whenEntryNotFoundOrNotOwned() {
        when(repository.findByIdWithOwner(entryId, userId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.deleteEntryById(entryId, userId));

        verify(repository).findByIdWithOwner(entryId, userId);
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    void updateDone_happyPath_shouldSetDoneTrue() {
        ToDoListEntryUpdateDoneDto dto = new ToDoListEntryUpdateDoneDto(true);

        when(repository.findByIdWithOwner(entryId, userId))
                .thenReturn(Optional.of(entry));
       when(repository.save(entry)).thenReturn(entry);

        service.updateDone(entryId, userId, dto);

        assertTrue(entry.getDone());
        verify(repository).save(entry);
    }

    @Test
    void updateDone_entryNotFound_shouldThrowNotFoundException() {
        ToDoListEntryUpdateDoneDto dto = new ToDoListEntryUpdateDoneDto(true);
        when(repository.findByIdWithOwner(entryId, userId))
                .thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> service.updateDone(entryId, userId, dto));
        assertEquals("Entry not found or you do not have permission", ex.getMessage());

        verify(repository, never()).save(any());
    }

    @Test
    void deleteEntryById_entryExists_shouldCallRepositoryDelete() {
        when(repository.findByIdWithOwner(entryId, userId))
                .thenReturn(Optional.of(entry));

        service.deleteEntryById(entryId, userId);

        verify(repository).deleteById(entryId);
    }

    @Test
    void deleteEntryById_entryNotFound_shouldThrowNotFoundException() {
        when(repository.findByIdWithOwner(entryId, userId))
                .thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> service.deleteEntryById(entryId, userId));

        assertEquals("Entry not found or you do not have permission", ex.getMessage());
        verify(repository, never()).deleteById(any());
    }
}
