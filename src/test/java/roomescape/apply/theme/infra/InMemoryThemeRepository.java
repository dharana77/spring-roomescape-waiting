package roomescape.apply.theme.infra;

import roomescape.apply.theme.domain.Theme;
import roomescape.apply.theme.domain.ThemeRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;


public class InMemoryThemeRepository implements ThemeRepository {

    private final Map<Long, Theme> map = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong();

    @Override
    public Theme save(Theme theme) {
        Long id = theme.getId();
        if (id == null) {
            id = idCounter.incrementAndGet();
            theme.changeId(id);
        }
        map.put(id, theme);
        return theme;
    }

    @Override
    public List<Theme> findAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public void deleteById(Long id) {
        map.remove(id);
    }

    @Override
    public Optional<Long> findIdById(long id) {
        return map.containsKey(id) ? Optional.of(id) : Optional.empty();
    }

    @Override
    public Optional<Theme> findOneById(long themeId) {
        return Optional.ofNullable(map.get(themeId));
    }
}
