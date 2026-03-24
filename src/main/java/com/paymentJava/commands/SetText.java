package com.paymentJava.commands;

import java.util.HashMap;
import java.util.Map;

interface HelloActions {
  String execute(String argument);
}

public class SetText implements Command {
  Map<String, HelloActions> actions = new HashMap<>();

  private void init() {
    actions.put("upper", new upperCaseAction());
    actions.put("lower", new lowerCaseAction());
  }

  public void execute(ParseInput args) {
    try {
      init();

      HelloActions action = actions.get(args.flag);
      if (action != null) {
        String response = action.execute(args.argument);
        System.out.println(response);

      } else {
        System.out.println("Flag não encontrada!");
      }

    } catch (IllegalArgumentException e) {
      System.out.println("Erro: " + e.getMessage());
    }

  }
}

class upperCaseAction implements HelloActions {
  public String execute(String argument) {
    return argument.toUpperCase();
  }
}

class lowerCaseAction implements HelloActions {
  public String execute(String argument) {
    return argument.toLowerCase();
  }
}
