package com.asfarma.burnschecklistbot;

public class Chat {
    long id;
    Language language;
    int testStep;
    int testScore = 0;
    boolean testIsOver;

    public Chat(long id, Language language, int testStep) {
        this.id = id;
        this.language = language;
        this.testStep = testStep;
        this.testStep = 0;
        this.testIsOver = false;
    }

    public long getId() {
        return id;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public int getTestStep() {
        return testStep;
    }

    public void setTestStep(int testStep) {
        this.testStep = testStep;
    }

    public int getTestScore() {
        return testScore;
    }

    public void setTestScore(int testScore) {
        this.testScore = testScore;
    }

    public void increaseTestStep() {
        this.testStep++;
        if (this.testStep > 25)
            this.testIsOver = true;
    }

    public void increaseTestScore(int increaseAmount) {
        this.testScore += increaseAmount;
    }

    public void initTest() {
        this.testIsOver = false;
        this.setTestStep(1);
        this.setTestScore(0);
    }
}
