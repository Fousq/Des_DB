package kz.enu.students;

import java.util.Arrays;

public enum FilterSetType {
    NO_FILTER(0), FILTER_BY_GENDER(1), FILTER_SORTED(2), FILTER_BY_GENDER_SORTED(3);

    private int id;

    FilterSetType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static FilterSetType getFilterTypeById(int id) {
        return Arrays.stream(FilterSetType.values()).filter(filterSetType -> filterSetType.getId() == id)
                .findAny()
                .orElseThrow(() -> {throw new IllegalArgumentException("No filter set for id: " + id);});
    }
}
