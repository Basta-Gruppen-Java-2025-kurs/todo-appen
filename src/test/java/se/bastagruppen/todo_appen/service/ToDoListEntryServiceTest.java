package se.bastagruppen.todo_appen.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.bastagruppen.todo_appen.dto.ToDoListEntryRequestDto;
import se.bastagruppen.todo_appen.dto.ToDoListEntryResponseDto;
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


    @Test
    void createToDoListEntry_shouldCreateEntry_whenUserOwnsList() {
        Long listId = 1L;
        Long userId = 42L;

        ToDoListEntryRequestDto dto = new ToDoListEntryRequestDto();
        dto.setParentId(null);

        ToDoList list = new ToDoList();
        list.setId(listId);

        ToDoListEntry entry = new ToDoListEntry();
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
        Long listId = 1L;
        Long userId = 42L;
        ToDoListEntryRequestDto dto = new ToDoListEntryRequestDto();

        when(listRepository.findByIdAndOwnerId(listId, userId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.createToDoListEntry(listId, dto, userId));

        verify(repository, never()).save(any());
    }

    @Test
    void createToDoListEntry_shouldThrowBadRequest_whenParentInDifferentList() {
        Long listId = 1L;
        Long userId = 42L;

        ToDoListEntryRequestDto dto = new ToDoListEntryRequestDto();
        dto.setParentId(99L);

        ToDoList list = new ToDoList();
        list.setId(listId);

        ToDoList otherList = new ToDoList();
        otherList.setId(2L);

        ToDoListEntry parent = new ToDoListEntry();
        parent.setList(otherList);

        ToDoListEntry entry = new ToDoListEntry();

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
        Long listId = 1L;
        Long userId = 10L;

        ToDoList list = new ToDoList();
        list.setId(listId);

        ToDoListEntry entry = new ToDoListEntry();
        entry.setId(5L);

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
        Long listId = 1L;
        Long userId = 42L;

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
        Long entryId = 10L;
        Long userId = 42L;

        ToDoListEntry entry = new ToDoListEntry();
        entry.setId(entryId);

        when(repository.findByIdWithOwner(entryId, userId))
                .thenReturn(Optional.of(entry));

        service.deleteEntryById(entryId, userId);

        verify(repository).findByIdWithOwner(entryId, userId);
        verify(repository).deleteById(entryId);
    }

    @Test
    void deleteEntryById_shouldThrowNotFound_whenEntryNotFoundOrNotOwned() {
        Long entryId = 10L;
        Long userId = 42L;

        when(repository.findByIdWithOwner(entryId, userId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.deleteEntryById(entryId, userId));

        verify(repository).findByIdWithOwner(entryId, userId);
        verify(repository, never()).deleteById(anyLong());
    }
}
