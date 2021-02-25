package com.asfarma.burnschecklistbot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


import static com.asfarma.burnschecklistbot.Operation.*;

/**
 *
 *
 */
public class Bot extends TelegramLongPollingBot
{
    private static final Logger LOGGER = Logger.getLogger( Bot.class.getName() );
    private static final List<KeyboardRow> keyboard = new ArrayList<>();

    public static void main( String[] args )
    {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
            Helper.initDialogues();
            Helper.initTestQuestions();
            Helper.initKeyboards();
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        long chatId = message.getChatId();
        Chat currentChat;
        if(!Helper.isChatExist(chatId)) {
            currentChat = new Chat(chatId, Language.RU, 0);
            Helper.addChatIfAbsent(currentChat);
        }
        String messageText = message.getText();


        int messageId = message.getMessageId();
        Operation operation = getOperationFromReply(chatId, messageText);

        switch (operation) {
            case START:
                initLanguageKeyboard();
                sendMsg(chatId, keyboard, "Тилни танланг | Выберите язык | Tilni tanlang | Select language");
                break;
            case HELP:
                sendMsg(chatId, keyboard, "Чем могу помочь?");
                break;
            case ID:
                sendMsg(chatId, keyboard, message.getChatId().toString());
                break;
            case LANGUAGE_SELECTED:
                sendMsg(chatId,
                        Helper.getChatById(chatId).getLanguage().getKeyboard(operation),
                        Helper.getChatById(chatId).getLanguage().getMessageText(operation));
                break;

            case LANGUAGE_CHANGE:
                initLanguageKeyboard();
                sendMsg(chatId, keyboard, "Выберите язык | Тилни танланг | Select language");
                break;
            case TEST_STARTED:
                Helper.getChatById(chatId).initTest();
                sendMsg(chatId,
                        Helper.getChatById(chatId).getLanguage().getKeyboard(operation),
                        Helper.getChatById(chatId).getLanguage().getTestQuestion(Helper.getChatById(chatId).getTestStep()));
                break;
            case OPTION_ZERO:
                Helper.getChatById(chatId).increaseTestStep();
                if (Helper.getChatById(chatId).testIsOver) {
                    sendMsg(chatId,
                            Helper.getChatById(chatId).getLanguage().getKeyboard(Operation.FINISH_TEST),
                            Helper.getChatById(chatId).getLanguage().getMessageText(Operation.FINISH_TEST));
                }else {
                    sendMsg(chatId,
                            Helper.getChatById(chatId).getLanguage().getKeyboard(TEST_QUESTION),
                            Helper.getChatById(chatId).getLanguage().getTestQuestion(Helper.getChatById(chatId).getTestStep()));
                }
                    break;
            case OPTION_ONE:
                Helper.getChatById(chatId).increaseTestStep();
                Helper.getChatById(chatId).increaseTestScore(1);
                if (Helper.getChatById(chatId).testIsOver) {
                    sendMsg(chatId,
                            Helper.getChatById(chatId).getLanguage().getKeyboard(FINISH_TEST),
                            Helper.getChatById(chatId).getLanguage().getMessageText(FINISH_TEST));
                }else {
                    sendMsg(chatId,
                            Helper.getChatById(chatId).getLanguage().getKeyboard(TEST_QUESTION),
                            Helper.getChatById(chatId).getLanguage().getTestQuestion(Helper.getChatById(chatId).getTestStep()));
                }
                break;
            case OPTION_TWO:
                Helper.getChatById(chatId).increaseTestStep();
                Helper.getChatById(chatId).increaseTestScore(2);
                if (Helper.getChatById(chatId).testIsOver) {
                    sendMsg(chatId,
                            Helper.getChatById(chatId).getLanguage().getKeyboard(FINISH_TEST),
                            Helper.getChatById(chatId).getLanguage().getMessageText(FINISH_TEST));
                }else {
                    sendMsg(chatId,
                            Helper.getChatById(chatId).getLanguage().getKeyboard(TEST_QUESTION),
                            Helper.getChatById(chatId).getLanguage().getTestQuestion(Helper.getChatById(chatId).getTestStep()));
                }
                break;
            case OPTION_THREE:
                Helper.getChatById(chatId).increaseTestStep();
                Helper.getChatById(chatId).increaseTestScore(3);
                if (Helper.getChatById(chatId).testIsOver) {
                    sendMsg(chatId,
                            Helper.getChatById(chatId).getLanguage().getKeyboard(FINISH_TEST),
                            Helper.getChatById(chatId).getLanguage().getMessageText(FINISH_TEST));
                }else {
                    sendMsg(chatId,
                            Helper.getChatById(chatId).getLanguage().getKeyboard(TEST_QUESTION),
                            Helper.getChatById(chatId).getLanguage().getTestQuestion(Helper.getChatById(chatId).getTestStep()));
                }
                break;
            case OPTION_FOUR:
                Helper.getChatById(chatId).increaseTestStep();
                Helper.getChatById(chatId).increaseTestScore(4);
                if (Helper.getChatById(chatId).testIsOver) {
                    sendMsg(chatId,
                            Helper.getChatById(chatId).getLanguage().getKeyboard(FINISH_TEST),
                            Helper.getChatById(chatId).getLanguage().getMessageText(FINISH_TEST));
                }else {
                    sendMsg(chatId,
                            Helper.getChatById(chatId).getLanguage().getKeyboard(TEST_QUESTION),
                            Helper.getChatById(chatId).getLanguage().getTestQuestion(Helper.getChatById(chatId).getTestStep()));
                }
                break;

            case TEST_FINISHED:
                sendMsg(chatId,
                        Helper.getChatById(chatId).getLanguage().getKeyboard(operation),
                        Helper.getChatById(chatId).getLanguage().getMessageText(YOUR_SCORE) +
                                Helper.getChatById(chatId).getTestScore() + "." +
                                Helper.getChatById(chatId).getLanguage().getMessageText(getDepressionLevel(chatId)));
                break;
            case RESULTS_SHOWED: //Drugs prescribing
                Operation prescribingOperation = PRESCRIBING_NO_DEPRESSION;
                switch (getDepressionLevel(chatId)) {
                    case NO_DEPRESSION: prescribingOperation = PRESCRIBING_NO_DEPRESSION; break;
                    case UNHAPPY: prescribingOperation = PRESCRIBING_UNHAPPY; break;
                    case MILD_DEPRESSION: prescribingOperation = PRESCRIBING_MILD_DEPRESSION; break;
                    case MODERATE_DEPRESSION: prescribingOperation = PRESCRIBING_MODERATE_DEPRESSION; break;
                    case SEVERE_DEPRESSION: prescribingOperation = PRESCRIBING_SEVERE_DEPRESSION; break;
                    case EXTREME_DEPRESSION: prescribingOperation = PRESCRIBING_EXTREME_DEPRESSION; break;
                }

                if (prescribingOperation == PRESCRIBING_NO_DEPRESSION || prescribingOperation == PRESCRIBING_UNHAPPY) {
                    sendMsg(chatId,
                            Helper.getChatById(chatId).getLanguage().getKeyboard(SEND_FILES),
                            Helper.getChatById(chatId).getLanguage().getMessageText(prescribingOperation));
                } else {
                    sendMsg(chatId,
                            Helper.getChatById(chatId).getLanguage().getKeyboard(operation),
                            Helper.getChatById(chatId).getLanguage().getMessageText(RESULTS_SHOWED));
                    sendMsg(chatId,
                            Helper.getChatById(chatId).getLanguage().getKeyboard(operation),
                            Helper.getChatById(chatId).getLanguage().getMessageText(prescribingOperation));
                }
                break;

            case SEND_FILES:
                sendFile(chatId,
                        Helper.getChatById(chatId).getLanguage().getKeyboard(operation),
                        Helper.getChatById(chatId).getLanguage().getMessageText(operation),
                        new File("DorepamRU.pdf"));
                sendFile(chatId,
                        Helper.getChatById(chatId).getLanguage().getKeyboard(operation),
                        Helper.getChatById(chatId).getLanguage().getMessageText(operation),
                        new File("DorepamUZ.pdf"));
                break;


            default:
                sendMsg(chatId, "Reply to Message " + messageText, messageId, true);
                break;
        }
    }

    public Operation getOperationFromReply(Long chatId, String messageText) {
        String operationSign = messageText.split(" ")[0];
        switch (operationSign) {
            case "/start": return Operation.START;
            case "/help": return Operation.HELP;
            case "/id": return Operation.ID;
            case "\uD83C\uDDF7\uD83C\uDDFA":
                Helper.getChatById(chatId).setLanguage(Language.RU);
                return Operation.LANGUAGE_SELECTED;
            case "\uD83C\uDDFA\uD83C\uDDFF":
                Helper.getChatById(chatId).setLanguage(Language.UZ);
                return Operation.LANGUAGE_SELECTED;
            case "\uD83C\uDDFA\uD83C\uDDFF'":
                Helper.getChatById(chatId).setLanguage(Language.OZ);
                return Operation.LANGUAGE_SELECTED;
            case "\uD83C\uDDEC\uD83C\uDDE7":
                Helper.getChatById(chatId).setLanguage(Language.EN);
                return Operation.LANGUAGE_SELECTED;

            case "\uD83C\uDF10": return Operation.LANGUAGE_CHANGE;
            case "\uD83D\uDD20": return Operation.MENU;
            case "\uD83D\uDFE2": return Operation.TEST_STARTED;
            case "⬜️":           return Operation.OPTION_ZERO;
            case "\uD83D\uDFE8": return Operation.OPTION_ONE;
            case "\uD83D\uDFE7": return Operation.OPTION_TWO;
            case "\uD83D\uDFE5": return Operation.OPTION_THREE;
            case "\uD83D\uDFEB": return Operation.OPTION_FOUR;
            case "\uD83D\uDD35": return Operation.TEST_FINISHED;
            case "\uD83D\uDC8A": return RESULTS_SHOWED;
            case "\uD83D\uDCC4": return Operation.SEND_FILES;

            default: return Operation.UNKNOWN;
        }
    }

    public Operation getDepressionLevel(Long chatId) {
        int testScore = Helper.getChatById(chatId).getTestScore();
        if (testScore < 6)
            return Operation.NO_DEPRESSION;
        else if (testScore < 11)
            return Operation.UNHAPPY;
        else if (testScore < 26)
            return Operation.MILD_DEPRESSION;
        else if (testScore < 51)
            return Operation.MODERATE_DEPRESSION;
        else if (testScore < 76)
            return Operation.SEVERE_DEPRESSION;
        else return Operation.EXTREME_DEPRESSION;
    }

    public void setButtons(SendMessage sendMessage, List<KeyboardRow> keyboard) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public void setButtons(SendDocument sendDocument, List<KeyboardRow> keyboard) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendDocument.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public void initLanguageKeyboard() {
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("\uD83C\uDDFA\uD83C\uDDFF Ўзбекча"));
        keyboardRow.add(new KeyboardButton("\uD83C\uDDF7\uD83C\uDDFA Русский"));
        keyboard.clear();
        keyboard.add(keyboardRow);
        keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("\uD83C\uDDFA\uD83C\uDDFF' O'zbekcha"));
        keyboardRow.add(new KeyboardButton("\uD83C\uDDEC\uD83C\uDDE7 English"));
        keyboard.add(keyboardRow);
    }

    public synchronized void sendMsg(long chatId, List<KeyboardRow> keyboard, String messageTextToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(messageTextToSend);

        try {
            setButtons(sendMessage, keyboard);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            LOGGER.log(Level.SEVERE, "Exception: ", e.toString());
        }
    }

    public synchronized void sendFile(long chatId, List<KeyboardRow> keyboard, String messageTextToSend, File file) {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId);
        sendDocument.setDocument(file);
        sendDocument.setCaption(messageTextToSend);
        try {
            setButtons(sendDocument, keyboard);
            execute(sendDocument);
        } catch (TelegramApiException e) {
            LOGGER.log(Level.SEVERE, "Exception: ", e.toString());
        }
    }

    public synchronized void sendMsg(long chatId, String messageTextToSend, int messageId, boolean isReply) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(messageTextToSend);
        if (isReply)
            sendMessage.setReplyToMessageId(messageId);
        try {
            setButtons(sendMessage, keyboard);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            LOGGER.log(Level.SEVERE, "Exception: ", e.toString());
        }
    }

    @Override
    public String getBotUsername() {
        return getBotInfo(0);
    }

    @Override
    public String getBotToken() {
        return getBotInfo(1);
    }

    private String getBotInfo(int lineIndex) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("tkn.t"))) {
            String line;
            List<String> fileLines = new ArrayList<>();
            while ((line = bufferedReader.readLine()) != null) {
                fileLines.add(line);
            }
            return fileLines.get(lineIndex);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
