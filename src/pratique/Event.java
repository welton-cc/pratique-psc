package pratique;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

class Event implements Serializable {
    private String name;
    private String address;
    private String category;
    private LocalDateTime dateTime;
    private String description;
    private List<User> participants;

    public Event(String name, String address, String category, LocalDateTime dateTime, String description) {
        this.name = name;
        this.address = address;
        this.category = category;
        this.dateTime = dateTime;
        this.description = description;
        this.participants = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCategory() {
        return category;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void addParticipant(User user) {
        this.participants.add(user);
    }

    public List<User> getParticipants() {
        return participants;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Evento(");
        sb.append("Nome:").append(name).append(", ");
        sb.append("Endereço:").append(address).append(", ");
        sb.append("Categoria:").append(category).append(", ");
        sb.append("Data e hora:").append(dateTime).append(", ");
        sb.append("Descrição:").append(description).append(", ");
        sb.append("Participantes:");
        if (participants.isEmpty()) {
            sb.append("None)");
        } else {
            sb.append("[");
            for (User participant : participants) {
                sb.append(participant.getName()).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length()).append("]"); // Remove a última vírgula e o espaço em branco
        }
        return sb.toString();
    }
}
