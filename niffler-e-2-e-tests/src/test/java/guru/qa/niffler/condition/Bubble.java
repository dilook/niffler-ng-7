package guru.qa.niffler.condition;

import org.jetbrains.annotations.NotNull;

public record Bubble(Color color, String text) {
    @NotNull
    @Override
    public String toString() {
        return String.format("Bubble{%s, text=%s}", color.rgb, text);
    }
}
