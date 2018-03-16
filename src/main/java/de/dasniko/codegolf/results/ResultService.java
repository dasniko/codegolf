package de.dasniko.codegolf.results;

import de.dasniko.codegolf.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Niko Köbler, http://www.n-k.de, @dasniko
 */
@Service
@RequiredArgsConstructor
public class ResultService {

    private final ResultEntryRepository resultEntryRepository;

    List<ResultEntry> getResultlist() {
        List<ResultEntry> entries = (List<ResultEntry>) resultEntryRepository.findAll();
        return entries.stream().sorted(Comparator.comparingInt(ResultEntry::getCountChars)).collect(Collectors.toList());
    }

    ResultEntry getResultEntry(String username) {
        return resultEntryRepository.findOne(username);
    }

    public void updateResultlist(User user, int count, String sourceCode) {
        // only save/update result, if user has no result or result with higher char count than current request
        ResultEntry existingEntry = resultEntryRepository.findOne(user.getUsername());
        if (null == existingEntry || existingEntry.getCountChars() >= count) {
            ResultEntry entry = ResultEntry.builder()
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .countChars(count)
                    .sourceCode(sourceCode)
                    .timestamp(new Date())
                    .build();
            resultEntryRepository.save(entry);
        }
    }

}
