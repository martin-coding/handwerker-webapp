package de.othr.hwwa.service.nager;

import java.util.Map;

public final class GermanStates {
    private GermanStates() {}

    public static final Map<String, String> NAME_BY_ISO = Map.ofEntries(
            Map.entry("DE-BW", "Baden-Württemberg"),
            Map.entry("DE-BY", "Bayern"),
            Map.entry("DE-BE", "Berlin"),
            Map.entry("DE-BB", "Brandenburg"),
            Map.entry("DE-HB", "Bremen"),
            Map.entry("DE-HH", "Hamburg"),
            Map.entry("DE-HE", "Hessen"),
            Map.entry("DE-MV", "Mecklenburg-Vorpommern"),
            Map.entry("DE-NI", "Niedersachsen"),
            Map.entry("DE-NW", "Nordrhein-Westfalen"),
            Map.entry("DE-RP", "Rheinland-Pfalz"),
            Map.entry("DE-SL", "Saarland"),
            Map.entry("DE-SN", "Sachsen"),
            Map.entry("DE-ST", "Sachsen-Anhalt"),
            Map.entry("DE-SH", "Schleswig-Holstein"),
            Map.entry("DE-TH", "Thüringen")
    );
}