package pratique;

import java.util.Arrays;
import java.util.List;

class EventCategory {
    private String name;

    public EventCategory(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    // Método estático para obter uma lista predefinida de categorias de eventos
    public static List<EventCategory> getDefaultCategories() {
        return Arrays.asList(
                new EventCategory("Festa"),
                new EventCategory("Evento Esportivo"),
                new EventCategory("Show")
                // Adicione mais categorias conforme necessário
        );
    }
}
