
import java.io.*;
import java.text.*;
import java.util.regex.*;
import java.util.Calendar;

public class DataValidator {
    public static String[] validateAndParse(String input) throws UserDataException {
        String[] data = input.split(" ");

        if(data.length != 6) {
            throw new InvalidQuantityException("Неверное количество введенных данных.");
        }

        // Check for Cyrillic characters and length for name, surname, and patronymic
        Pattern namePattern = Pattern.compile("^[а-яА-ЯёЁ]{1,50}$");
        if (!namePattern.matcher(data[0]).matches() || 
            !namePattern.matcher(data[1]).matches() || 
            !namePattern.matcher(data[2]).matches()) {
            throw new InvalidFormatException("Фамилия, имя или отчество содержат неверные символы или слишком длинные.");
        }

        Pattern datePattern = Pattern.compile("^\d{2}\.\d{2}\.\d{4}$");
        Pattern genderPattern = Pattern.compile("^[fm]$");

        if(!datePattern.matcher(data[3]).matches()) {
            throw new InvalidFormatException("Неверный формат даты.");
        } else {
            // Check if the year is not in the future
            String[] dateParts = data[3].split("\.");
            int year = Integer.parseInt(dateParts[2]);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            if (year > currentYear) {
                throw new InvalidFormatException("Год даты рождения не может быть будущим годом.");
            }
        }

        try {
            Long.parseLong(data[4]);
        } catch(NumberFormatException e) {
            throw new InvalidFormatException("Неверный формат номера телефона.");
        }

        if(!genderPattern.matcher(data[5]).matches()) {
            throw new InvalidFormatException("Неверный формат пола.");
        }

        return data;
    }

    public static void saveToFile(String[] data) throws FileHandlingException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(data[0] + ".txt", true))) {
            writer.write(String.join("", data));
            writer.newLine();
        } catch (IOException e) {
            throw new FileHandlingException("Проблема при записи в файл.");
        }
    }

    public static void main(String[] args) {
        System.out.println("Введите данные: Фамилия Имя Отчество датарождения номертелефона пол");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            String input = reader.readLine();
            String[] parsedData = validateAndParse(input);
            saveToFile(parsedData);
            System.out.println("Данные успешно сохранены!");
        } catch (UserDataException e) {
            System.out.println(e.getMessage());
        } catch (FileHandlingException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Ошибка ввода.");
        }
    }
}
