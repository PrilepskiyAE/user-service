package org.prilepskiy.aston_step_two;

import org.prilepskiy.aston_step_two.controller.UserConsoleController;
import org.prilepskiy.aston_step_two.utils.ConsoleHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        ConsoleHelper consoleHelper = new ConsoleHelper();
        new UserConsoleController(consoleHelper).start();
    }
}
