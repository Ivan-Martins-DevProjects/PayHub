package com.paymentJava.commands;

public class ParseInput {
  String cmd;
  String flag;
  String argument;

  public ParseInput(String[] input) {
    this.cmd = input[0];

    if (input.length > 1) {

      String flag = parseFlag(input[1]);
      if (flag == null) {
        throw new IllegalArgumentException("Sintaxe para flag inválido, garanta que utilizou '--' ao digitar a flag");
      } else {
        this.flag = flag;
      }

      if (!onlyLetersAndNumbers(input[2])) {
        throw new IllegalArgumentException("Não utilize caracteres especiais");
      } else {
        this.argument = input[2];
      }
    }
  }

  public String parseFlag(String flag) {
    if (flag.startsWith("--")) {
      return flag.substring(2);
    } else {
      return null;
    }
  }

  public boolean onlyLetersAndNumbers(String text) {
    return text.matches("^[a-zA-Z0-9]+$");
  }
}
