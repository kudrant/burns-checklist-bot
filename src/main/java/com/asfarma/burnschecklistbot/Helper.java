package com.asfarma.burnschecklistbot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Helper {
    static Map<Long, Chat> chatMap = new HashMap<>();
    static Map<Operation, List<String>> dialogues = new HashMap<>();
    static Map<Operation, List<List<KeyboardRow>>> keyboards = new HashMap<>();
    static Map<Integer, List<String>> testQuestions = new HashMap<>();




    static void initDialogues() {
        dialogues.clear();
        try(BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("dialogues.txt"), StandardCharsets.UTF_8))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] dialogueByOperation = line.split("@");
                dialogues.put(Enum.valueOf(Operation.class, dialogueByOperation[0]),
                        Arrays.asList(dialogueByOperation[1], dialogueByOperation[2], dialogueByOperation[3], dialogueByOperation[4]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void initTestQuestions() {
        testQuestions.clear();
        try(BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("questions.txt"), StandardCharsets.UTF_8))) {
            String line;
            int questionNumber = 1;
            while ((line = bufferedReader.readLine()) != null) {
                String[] testByNumber = line.split("@");
                testQuestions.put(questionNumber,
                        Arrays.asList(testByNumber[0], testByNumber[1], testByNumber[2], testByNumber[3]));
                questionNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void initKeyboards() {
        keyboards.clear();
        try(BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("keyboards.txt"), StandardCharsets.UTF_8))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] keyboardByOperation = line.split("@");

                List<List<KeyboardRow>> allLanguagesKeyboards = new ArrayList<>();
                for (int i = 1; i < 5; i++) { // making keyboards for each language
                    List<KeyboardRow> keyboardRowsList = new ArrayList<>();
                    String[] keyboardRows = keyboardByOperation[i].split("#"); //splitting to rows of buttons
                    for (String buttonsRow: keyboardRows
                         ) {
                        String[] buttons = buttonsRow.split("~");
                        KeyboardRow keyboardRow = new KeyboardRow();
                        for (String button: buttons
                             ) {
                            keyboardRow.add(button);
                        }
                        keyboardRowsList.add(keyboardRow);
                    }
                    allLanguagesKeyboards.add(keyboardRowsList);
                }
                keyboards.put(Enum.valueOf(Operation.class, keyboardByOperation[0]), allLanguagesKeyboards);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static Chat getChatById(Long chatId) {
        return chatMap.get(chatId);
    }

    static void addChatIfAbsent(Chat chat) {
        if (!chatMap.containsKey(chat.getId())) {
            chatMap.put(chat.getId(), chat);
        }
    }

    static boolean isChatExist(long chatId) {
        return chatMap.containsKey(chatId);
    }
}
