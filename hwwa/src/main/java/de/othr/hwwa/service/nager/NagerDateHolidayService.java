package de.othr.hwwa.service.nager;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NagerDateHolidayService {

    private final RestTemplate restTemplate;
    private final Map<String, Map<Integer, List<NagerHolidayDto>>> cache = new ConcurrentHashMap<>();

    public NagerDateHolidayService() {
        this.restTemplate = new RestTemplate();
    }

    public List<NagerHolidayDto> getHolidaysForYear(int year, String countryCode) {
        String cc = countryCode == null ? "DE" : countryCode.toUpperCase(Locale.ROOT);

        Map<Integer, List<NagerHolidayDto>> byYear = cache.computeIfAbsent(cc, k -> new ConcurrentHashMap<>());

        return byYear.computeIfAbsent(year, y -> {
            String url = "https://date.nager.at/api/v3/PublicHolidays/" + y + "/" + cc;
            NagerHolidayDto[] arr = restTemplate.getForObject(url, NagerHolidayDto[].class);
            return arr == null ? List.of() : Arrays.asList(arr);
        });
    }

    public List<NagerHolidayDto> getHolidaysInRange(LocalDate startInclusive, LocalDate endExclusive, String countryCode) {
        if (startInclusive == null || endExclusive == null) {
            return List.of();
        }
        if (!startInclusive.isBefore(endExclusive)) {
            return List.of();
        }

        int startYear = startInclusive.getYear();
        int endYear = endExclusive.minusDays(1).getYear();

        List<NagerHolidayDto> all;
        if (startYear == endYear) {
            all = getHolidaysForYear(startYear, countryCode);
        } else {
            all = new ArrayList<>();
            all.addAll(getHolidaysForYear(startYear, countryCode));
            all.addAll(getHolidaysForYear(endYear, countryCode));
        }

        return all.stream()
                .filter(h -> h.date() != null)
                .filter(h -> !h.date().isBefore(startInclusive) && h.date().isBefore(endExclusive))
                .toList();
    }
}