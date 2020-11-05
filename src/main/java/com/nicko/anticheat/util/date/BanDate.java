package com.nicko.anticheat.util.date;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BanDate {

    private final int days;
    private final int seconds;
    private final int milliseconds;
}
