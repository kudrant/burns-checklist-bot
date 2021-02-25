package com.asfarma.burnschecklistbot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public enum Language {
    RU,
    UZ,
    OZ,
    EN;

    public String getMessageText(Operation operation) {
        return Helper.dialogues.get(operation).get(this.ordinal());
    }

    public List<KeyboardRow> getKeyboard(Operation operation) {
        return Helper.keyboards.get(operation).get(this.ordinal());
    }

    public String getTestQuestion(int questionId) {
        return Helper.testQuestions.get(questionId).get(this.ordinal());
    }


}
