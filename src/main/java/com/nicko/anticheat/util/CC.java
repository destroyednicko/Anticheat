package com.nicko.anticheat.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CC {
    // Se não quiser fazer o uso de ChatColor, usa isso :3.
    BLACK('0'),
    DARK_BLUE('1'),
    DARK_GREEN('2'),
    DARK_AQUA('3'),
    DARK_RED('4'),
    DARK_PURPLE('5'),
    GOLD('6'),
    GRAY('7'),
    DARK_GRAY('8'),
    BLUE('9'),
    GREEN('a'),
    AQUA('b'),
    RED('c'),
    PINK('d'),
    YELLOW('e'),
    WHITE('f'),
    BOLD('l');

    @Getter
    private final char code;

    @Override
    public String toString() {
        return "§" + this.code;
    }
}
