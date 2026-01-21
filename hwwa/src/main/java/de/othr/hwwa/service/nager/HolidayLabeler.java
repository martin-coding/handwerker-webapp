package de.othr.hwwa.service.nager;

public final class HolidayLabeler {
    private HolidayLabeler() {}

    public static String label(NagerHolidayDto h) {
        if (h.global() || h.counties() == null || h.counties().isEmpty()) {
            return h.localName();
        }

        var states = h.counties().stream()
                .map(code -> GermanStates.NAME_BY_ISO.getOrDefault(code, code))
                .sorted()
                .toList();

        return h.localName() + " (nur in " + String.join(", ", states) + ")";
    }
}