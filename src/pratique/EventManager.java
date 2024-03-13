package pratique;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventManager {
    private List<Event> events;
    private List<User> users;
    private Scanner scanner;

    public EventManager() {
        events = new ArrayList<>();
        users = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        EventManager eventManager = new EventManager();

        // Carregar eventos do arquivo na inicialização
        eventManager.loadEventsFromFile();

        // Main menu loop
        boolean running = true;
        while (running) {
            System.out.println("\nMenu:");
            System.out.println("1. Registrar Usuario");
            System.out.println("2. Criar Evento");
            System.out.println("3. Listar Eventos");
            System.out.println("4. Participe do Evento");
            System.out.println("5. Cancelar participação do Evento");
            System.out.println("6. Classifique eventos por data e hora");
            System.out.println("7. Mostrar eventos atuais");
            System.out.println("8. Mostrar eventos anteriores");
            System.out.println("9. Salvar eventos em arquivo");
            System.out.println("0. Sair");
            System.out.println("Digite sua escolha:");

            int choice = eventManager.scanner.nextInt();
            eventManager.scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    eventManager.registerUser();
                    break;
                case 2:
                    eventManager.createEvent();
                    break;
                case 3:
                    eventManager.listEvents();
                    break;
                case 4:
                    eventManager.joinEvent();
                    break;
                case 5:
                    eventManager.cancelEvent();
                    break;
                case 6:
                    eventManager.sortEventsByDateTime();
                    System.out.println("Eventos classificados por data e hora.");
                    break;
                case 7:
                    eventManager.showCurrentEvents();
                    break;
                case 8:
                    eventManager.showPastEvents();
                    break;
                case 9:
                    eventManager.saveEventsToFile();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Escolha inválida. Por favor, tente novamente.");
            }
        }

        // Salvar eventos em arquivo ao sair
        eventManager.saveEventsToFile();
    }

    public void registerUser() {
        System.out.println("Digite seu nome (pelo menos 3 caracteres):");
        String name = scanner.nextLine();
        while (name.length() < 3) {
            System.out.println("O nome deve ter pelo menos 3 caracteres. Por favor, tente novamente:");
            name = scanner.nextLine();
        }
        System.out.println("Digite sua cidade:");
        String city = scanner.nextLine();

        //email
        String emailRegex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        String email;
        Matcher matcher;
        boolean validEmail;
        do {
            System.out.println("Digite seu e-mail:");
            email = scanner.nextLine();
            matcher = pattern.matcher(email);
            validEmail = matcher.matches();
            if (!validEmail) {
                System.out.println("Formato de email inválido. Por favor, tente novamente.");
            }
        } while (!validEmail);
        //registrar
        User user = new User(name, city, email);
        users.add(user);
        System.out.println("Usuário cadastrado com sucesso!");
    }

    public void createEvent() {
        System.out.println("Digite o nome do evento:");
        String name = scanner.nextLine();
        System.out.println("Insira o endereço do evento:");
        String address = scanner.nextLine();
        String category = null;
        Boolean validCategory = false;
        while (!validCategory) {
            //selecionar a categoria
            System.out.println("Selecione a categoria do evento:");
            List<EventCategory> categories = EventCategory.getDefaultCategories();
            for (int i = 0; i < categories.size(); i++) {
                System.out.println((i + 1) + ". " + categories.get(i));
            }
            int categoryIndex = Integer.parseInt(scanner.nextLine());

            if (categoryIndex >= 1 && categoryIndex <= categories.size()) {
                category = categories.get(categoryIndex - 1).toString();
                validCategory = true;
            } else {
                System.out.println("Índice de categoria inválido.");
            }
        }
        //data
        LocalDateTime dateTime = null;
        boolean validDateTime = false;
        while (!validDateTime) {
            try {
                System.out.println("Insira a data e hora do evento (dd/MM/yyyy HH:mm):");
                String dateTimeString = scanner.nextLine();
                dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                validDateTime = true;
            } catch (DateTimeParseException e) {
                System.out.println("Formato de data inválido. Insira a data e a hora no formato dd/MM/yyyy HH:mm");
            }
        }
        //descrição
        System.out.println("Insira a descrição do evento:");
        String description = scanner.nextLine();

        Event event = new Event(name, address, category, dateTime, description);
        events.add(event);
        System.out.println("Evento criado com sucesso!");
    }

    public void listEvents() {
        if (events.isEmpty()) {
            System.out.println("Nenhum evento encontrado.");
            return;
        }

        System.out.println("Eventos:");
        int index = 0;
        for (Event event : events) {
            System.out.println((index++) + ". " + event);
        }
    }

    public void joinEvent() {
        User selectedUser = selectUser();
        if (selectedUser != null) {
            listEvents();
            System.out.println("Insira o índice do evento que você deseja participar:");
            int index = Integer.parseInt(scanner.nextLine());
            if (index >= 0 && index < events.size()) {
                Event selectedEvent = events.get(index);
                selectedEvent.addParticipant(selectedUser);
                System.out.println("Você entrou no evento: " + selectedEvent);
            } else {
                System.out.println("Índice de eventos inválido.");
            }
        }
    }

    private User selectUser() {
        User selectedUser = null;
        // Solicitar que o usuário selecione o usuário
        System.out.println("Insira o índice do usuário:");
        for (int i = 0; i < users.size(); i++) {
            System.out.println((i + 1) + ". " + users.get(i).getName());
        }
        int userIndex = Integer.parseInt(scanner.nextLine()) - 1;
        if (userIndex >= 0 && userIndex < users.size()) {
            selectedUser = users.get(userIndex);
        } else {
            System.out.println("Índice de usuário inválido.");
        }
        return selectedUser;
    }

    public void cancelEvent() {
        User selectedUser = selectUser();
        if (selectedUser != null) {
            // Listar eventos em que o usuário está participando
            System.out.println("Eventos dos quais você está participando atualmente:");
            List<Event> userEvents = new ArrayList<>();
            int i = 0;
            for (Event event : events) {
                if (event.getParticipants().contains(selectedUser)) {
                    System.out.println((i++)+ ". " + event);
                    userEvents.add(event);
                }
            }

            if (userEvents.isEmpty()) {
                System.out.println("Você não está participando de nenhum evento.");
                return;
            }

            // Solicitar ao usuário que selecione o evento para cancelar a participação
            System.out.println("Insira o índice do evento em que deseja cancelar a participação:");
            int index = Integer.parseInt(scanner.nextLine());
            if (index >= 0 && index < userEvents.size()) {
                Event selectedEvent = userEvents.get(index);
                selectedEvent.getParticipants().remove(selectedUser); // Remove o usuário da lista de participantes do evento
                System.out.println("Você cancelou a participação no evento: " + selectedEvent.toString());
            } else {
                System.out.println("Índice de eventos inválido.");
            }
        }
    }

    public void sortEventsByDateTime() {
        Collections.sort(events, Comparator.comparing(Event::getDateTime));
    }

    public void showCurrentEvents() {
        // Check if any event is happening now based on current time
        LocalDateTime now = LocalDateTime.now();
        for (Event event : events) {
            if (event.getDateTime().isBefore(now) && event.getDateTime().plusHours(1).isAfter(now)) {
                System.out.println("Evento acontecendo agora: " + event.toString());
            }
        }
    }

    public void showPastEvents() {
        LocalDateTime now = LocalDateTime.now();
        for (Event event : events) {
            if (event.getDateTime().isBefore(now)) {
                System.out.println("Evento passado: " + event.toString());
            }
        }
    }

    public void saveEventsToFile() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("events.data"))) {
            outputStream.writeObject(events);
            System.out.println("Eventos salvos em arquivo.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadEventsFromFile() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("events.data"))) {
            events = (List<Event>) inputStream.readObject();
            System.out.println("Eventos carregados do arquivo.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
