package com.horstmann.violet.framework.userpreferences;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.horstmann.violet.framework.file.IFile;

class UserPreferencesServiceTest {

    private UserPreferencesService service;

    @BeforeEach
    void setUp() throws Exception {
        service = new UserPreferencesService();
        Field daoField = UserPreferencesService.class.getDeclaredField("dao");
        daoField.setAccessible(true);
        daoField.set(service, new InMemoryUserPreferencesDao());
    }

    @Test
    void preferredFilesWithSameNameInDifferentDirectoriesAreDistinct() {
        PreferredFile inStaging = new PreferredFile("/str", "a.class.violet.html");
        PreferredFile inStorage = new PreferredFile("/files", "a.class.violet.html");

        assertNotEquals(inStaging, inStorage);
        assertEquals(new PreferredFile("/files", "a.class.violet.html"), inStorage);
        assertEquals(new PreferredFile("/files", "a.class.violet.html").hashCode(), inStorage.hashCode());
    }

    @Test
    void addRecentFileMovesExistingEntryToTopWithoutDuplicate() {
        service.addRecentFile(new PreferredFile("/files", "a.class.violet.html"));
        service.addRecentFile(new PreferredFile("/files", "b.class.violet.html"));
        service.addRecentFile(new PreferredFile("/files", "a.class.violet.html"));

        List<IFile> recent = service.getRecentFiles();
        assertEquals(2, recent.size());
        assertEquals("a.class.violet.html", recent.get(0).getFilename());
        assertEquals("b.class.violet.html", recent.get(1).getFilename());
    }

    @Test
    void addRecentFileKeepsSameNameInDifferentDirectories() {
        service.addRecentFile(new PreferredFile("/home/a", "diagram.class.violet.html"));
        service.addRecentFile(new PreferredFile("/home/b", "diagram.class.violet.html"));

        assertEquals(2, service.getRecentFiles().size());
    }

    @Test
    void removeRecentFileRemovesOnlyMatchingEntry() {
        service.addRecentFile(new PreferredFile("/home/a", "diagram.class.violet.html"));
        service.addRecentFile(new PreferredFile("/home/b", "diagram.class.violet.html"));

        service.removeRecentFile(new PreferredFile("/home/a", "diagram.class.violet.html"));

        List<IFile> recent = service.getRecentFiles();
        assertEquals(1, recent.size());
        assertEquals("/home/b", recent.get(0).getDirectory());
    }

    private static class InMemoryUserPreferencesDao implements IUserPreferencesDao {

        private final Map<PreferencesConstant, String> values = new HashMap<>();

        @Override
        public String get(PreferencesConstant key, String defval) {
            return values.getOrDefault(key, defval);
        }

        @Override
        public void put(PreferencesConstant key, String value) {
            values.put(key, value);
        }

        @Override
        public void reset() {
            values.clear();
        }
    }
}
