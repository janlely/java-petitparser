package org.petitparser;

import lombok.Builder;
import lombok.Data;
import org.junit.Test;
import org.petitparser.context.Result;
import org.petitparser.parser.Parser;
import org.petitparser.parser.primitive.CharacterParser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class DemoParser {

    @Test
    public void testCsv() {
        Parser filedParser = CharacterParser.noneOf(",").plus().flatten();
        Parser csvParser = filedParser.separatedBy(CharacterParser.of(','))
                .map((List<String> values) -> CsvExample.builder()
                        .name(values.get(0))
                        .age(Integer.parseInt(values.get(1)))
                        .hobby(values.get(2))
                        .build());
        Result result = csvParser.parse("jay,35,badminton");
        Object res = result.get();
        System.out.println(res);
    }


    @Test
    public void testCustom() {
        Parser filedParser = CharacterParser.noneOf(",;{}").plus().flatten();
        Parser csvParser = filedParser.separatedBy(CharacterParser.of(','))
                .map((List<String> values) -> CsvExample.builder()
                        .name(values.get(0))
                        .age(Integer.parseInt(values.get(1)))
                        .hobby(values.get(2))
                        .build());

        Parser nameParser = CharacterParser.noneOf("{").plus().flatten();
        Parser customParser = nameParser.seq(CharacterParser.of('{'),
                        csvParser.separatedBy(CharacterParser.of(';')),
                        CharacterParser.of('}'))
                .map((List values) -> CustomExample.builder()
                        .className((String) values.get(0))
                        .people((List<CsvExample>) values.get(2))
                        .build());
        Result res = customParser.parse("abc{jay,35,badminton;jin,30,computer}");
        System.out.println(res.get().toString());
    }

    @Data
    @Builder
    public static class CsvExample {
        private String name;
        private Integer age;
        private String hobby;
    }

    @Data
    @Builder
    public static class CustomExample {
        private String className;

        private List<CsvExample> people;
    }

}
