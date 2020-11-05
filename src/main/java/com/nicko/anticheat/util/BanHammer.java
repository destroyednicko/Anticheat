package com.nicko.anticheat.util;

import com.nicko.anticheat.util.date.BanDate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BanHammer {

    private final BanDate banDate;
    private final String command;
}
